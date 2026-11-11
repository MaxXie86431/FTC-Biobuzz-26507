package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static dev.nextftc.units.Units.Amperes;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

// Collects the dataset for ShooterLUT and ShooterLinearInverse.
// Set an RPM and hood position, shoot, then mark the shot made or missed. Every shot is
// saved on the Control Hub in /sdcard/FIRST/shooter_data.csv, and the made shots are
// written as ready-to-paste Java arrays in /sdcard/FIRST/shooter_arrays.txt.
// The model "angle" is the hood servo position (0-1).
//
// Controls (gamepad 1):
//   left stick / right stick  drive
//   left trigger              intake
//   right trigger (hold)      shoot (same reverse, spin-up and feed sequence as robot.shoot())
//   dpad up / down            target RPM +/- rpmStep
//   dpad right / left         hood position +/- hoodStep
//   cross                     record last shot as MADE
//   circle                    record last shot as MISSED
//   square                    delete the last recorded shot
//   triangle                  switch alliance goal (red <-> blue)
//
// Distance is from odometry to the closest goal of the selected alliance.
@Configurable
@NextTeleop(name = "Shooter Data Collection")
public class ShooterDataCollection extends NextOpMode {
    public static double targetRpm = 2700;
    public static double rpmStep = 10;
    public static double hoodStep = 0.02;

    private static final File DATA_FILE = new File(AppUtil.ROOT_FOLDER, "shooter_data.csv");
    private static final File ARRAYS_FILE = new File(AppUtil.ROOT_FOLDER, "shooter_arrays.txt");
    private static final String HEADER = "result,distance_in,distance_source,hood_position,"
            + "target_rpm,avg_rpm,min_rpm,avg_rpm_motor2,avg_power,avg_current_a,battery_v,"
            + "x,y,heading_deg";

    private final Robot robot;
    private final Follower follower;
    private final PoseTracker poseTracker;
    private boolean initialized;

    private Alliance alliance = Alliance.RED;

    // the shot in progress (right trigger held)
    private boolean shooting;
    private long shotStartMs;
    private Shot current;

    // the last finished shot, waiting to be marked made/missed
    private Shot lastShot;
    private String status = "Hold right trigger to shoot";

    private static class Shot {
        double distance;
        String distanceSource;
        double hoodPosition;
        double targetRpm;
        double x, y, headingDeg;

        // sampled once feeding starts, so spin-up doesn't drag the averages down
        int samples;
        double rpmSum, rpm2Sum, powerSum, currentSum, batterySum;
        double minRpm = Double.POSITIVE_INFINITY;

        String toCsv(boolean made) {
            return String.format(Locale.US,
                    "%s,%.2f,%s,%.3f,%.1f,%.1f,%.1f,%.1f,%.3f,%.2f,%.2f,%.2f,%.2f,%.1f",
                    made ? "MADE" : "MISSED", distance, distanceSource, hoodPosition, targetRpm,
                    rpmSum / samples, minRpm, rpm2Sum / samples, powerSum / samples,
                    currentSum / samples, batterySum / samples, x, y, headingDeg);
        }
    }

    public ShooterDataCollection(Robot robot) {
        super(robot);
        this.robot = robot;

        robot.createFollower();
        follower = robot.getFollower();
        poseTracker = new PoseTracker(follower);

        Scheduler.reset();
    }

    @Override
    public void disabledPeriodic() {
        if (!initialized) {
            robot.init().schedule();
            Scheduler.execute();
            initialized = true;
        }
    }

    @Override
    public void start() {
        follower.setPose(PoseTracker.getLastPose());
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad gp1 = new CommandGamepad(gamepad1);

        robot.getDrivetrain().frontLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().frontRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().drive(gamepad1).schedule();

        // Intake
        gp1.leftTrigger().isOver(0.2).onTrue(parallel(
                robot.getIntake().intake(),
                robot.getIntermediate().forward()
        ));
        gp1.leftTrigger().isOver(0.2).onFalse(parallel(
                robot.getIntake().stop(),
                robot.getIntermediate().stop()
        ));

        // Shoot at the chosen RPM, holding the hood at the chosen position
        gp1.rightTrigger().isOver(0.2).onTrue(parallel(
                robot.shootAtRPM(() -> targetRpm),
                loop(lazy(() -> robot.getHood().setPosition(robot.getHood().getServoAngle())))
        ));
        gp1.rightTrigger().isOver(0.2).onFalse(robot.stop());

        gp1.dpadUp().onTrue(instant(() -> targetRpm += rpmStep));
        gp1.dpadDown().onTrue(instant(() -> targetRpm = Math.max(0.0, targetRpm - rpmStep)));
        gp1.dpadRight().onTrue(lazy(() ->
                robot.getHood().setPosition(robot.getHood().getServoAngle() + hoodStep)));
        gp1.dpadLeft().onTrue(lazy(() ->
                robot.getHood().setPosition(robot.getHood().getServoAngle() - hoodStep)));

        gp1.cross().onTrue(instant(() -> record(true)));
        gp1.circle().onTrue(instant(() -> record(false)));
        gp1.square().onTrue(instant(this::deleteLastRecord));
        gp1.triangle().onTrue(instant(() ->
                alliance = alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED));
    }

    private double distance() {
        return poseTracker.distanceToGoal(alliance);
    }

    private double measuredRpm(NextMotor motor) {
        return motor.getEncoderVelocity().into(RotationsPerMinute);
    }

    // in RPM mode the PID output can go past 1 (e.g. 0.1 * 300 RPM error = 30); the hub clips it to [-1, 1]
    private double appliedPower(NextMotor motor) {
        return Math.max(-1.0, Math.min(1.0, motor.getThrottle()));
    }

    private double batteryVoltage() {
        double lowest = Double.POSITIVE_INFINITY;
        for (VoltageSensor sensor : hardwareMap.voltageSensor) {
            double voltage = sensor.getVoltage();
            if (voltage > 0) {
                lowest = Math.min(lowest, voltage);
            }
        }
        return Double.isInfinite(lowest) ? 0.0 : lowest;
    }

    // tracks the shot while the right trigger is held, and keeps it once the trigger is released
    private void trackShot() {
        boolean triggerHeld = gamepad1.right_trigger > 0.2;
        long now = System.currentTimeMillis();

        if (triggerHeld && !shooting) {
            shooting = true;
            shotStartMs = now;
            current = new Shot();
            current.distance = distance();
            current.distanceSource = "ODOMETRY";   // kept so the CSV columns don't change
            current.hoodPosition = robot.getHood().getServoAngle();
            current.targetRpm = targetRpm;
            current.x = poseTracker.getX();
            current.y = poseTracker.getY();
            current.headingDeg = Math.toDegrees(poseTracker.getHeading());
        }
        if (!shooting) {
            return;
        }

        if (now - shotStartMs >= Robot.charge + Robot.servoWait) {
            NextMotor motor = robot.getFlywheel().flywheel;
            double rpm = measuredRpm(motor);
            current.samples++;
            current.rpmSum += rpm;
            current.minRpm = Math.min(current.minRpm, rpm);
            current.rpm2Sum += measuredRpm(robot.getFlywheel().flywheel2);
            current.powerSum += appliedPower(motor);
            current.currentSum += motor.getCurrent().into(Amperes);
            current.batterySum += batteryVoltage();
        }

        if (!triggerHeld) {
            shooting = false;
            if (current.samples > 0) {
                lastShot = current;
                status = "Shot done: cross = MADE, circle = MISSED";
            } else {
                status = "Released before feeding started, shot not kept";
            }
            current = null;
        }
    }

    private void record(boolean made) {
        if (lastShot == null) {
            status = "No shot to record, take a shot first";
            return;
        }
        try {
            boolean newFile = !DATA_FILE.exists();
            try (FileWriter writer = new FileWriter(DATA_FILE, true)) {
                if (newFile) {
                    writer.write(HEADER + "\n");
                }
                writer.write(lastShot.toCsv(made) + "\n");
            }
            lastShot = null;
            writeArrays();
            status = "Saved as " + (made ? "MADE" : "MISSED");
        } catch (IOException e) {
            status = "SAVE FAILED: " + e.getMessage();
        }
    }

    private void deleteLastRecord() {
        try {
            List<String> lines = readDataLines();
            if (lines.isEmpty()) {
                status = "Nothing to delete";
                return;
            }
            lines.remove(lines.size() - 1);
            try (FileWriter writer = new FileWriter(DATA_FILE, false)) {
                writer.write(HEADER + "\n");
                for (String line : lines) {
                    writer.write(line + "\n");
                }
            }
            writeArrays();
            status = "Deleted last shot";
        } catch (IOException e) {
            status = "DELETE FAILED: " + e.getMessage();
        }
    }

    // data rows only (no header)
    private List<String> readDataLines() throws IOException {
        List<String> lines = new ArrayList<>();
        if (!DATA_FILE.exists()) {
            return lines;
        }
        // BufferedReader instead of java.nio.file.Files, which the Control Hub's Android 7 doesn't have
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && !line.startsWith("result")) {
                    lines.add(line);
                }
            }
        }
        return lines;
    }

    // made shots sorted by distance, as Java arrays for ShooterLUT / ShooterLinearInverse
    private void writeArrays() throws IOException {
        List<String[]> made = new ArrayList<>();
        for (String line : readDataLines()) {
            String[] columns = line.split(",");
            if (columns[0].equals("MADE")) {
                made.add(columns);
            }
        }
        made.sort(Comparator.comparingDouble(columns -> Double.parseDouble(columns[1])));

        StringBuilder distances = new StringBuilder();
        StringBuilder angles = new StringBuilder();
        StringBuilder rpms = new StringBuilder();
        for (int i = 0; i < made.size(); i++) {
            String separator = i == 0 ? "" : ", ";
            distances.append(separator).append(made.get(i)[1]);
            angles.append(separator).append(made.get(i)[3]);
            rpms.append(separator).append(made.get(i)[4]);
        }

        try (FileWriter writer = new FileWriter(ARRAYS_FILE, false)) {
            writer.write("// " + made.size() + " made shots, sorted by distance (inches).\n");
            writer.write("// ShooterLUT needs strictly increasing distances: keep one row per distance.\n");
            writer.write("private static final double[] DISTANCES = {" + distances + "};\n");
            writer.write("private static final double[] ANGLES = {" + angles + "};\n");
            writer.write("private static final double[] RPMS = {" + rpms + "};\n");
        }
    }

    @Override
    public void periodic() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
        trackShot();

        NextMotor motor = robot.getFlywheel().flywheel;
        telemetry.addData("Status", status);
        telemetry.addData("Distance to closest goal", "%.1f in", distance());
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Hood position (angle)", "%.3f", robot.getHood().getServoAngle());
        telemetry.addData("Target RPM", "%.0f", targetRpm);
        telemetry.addData("Flywheel RPM", "%.0f  (motor 2: %.0f)",
                measuredRpm(motor), measuredRpm(robot.getFlywheel().flywheel2));
        telemetry.addData("Flywheel power", "%.2f", appliedPower(motor));
        telemetry.addData("Flywheel current", "%.2f A", motor.getCurrent().into(Amperes));
        telemetry.addData("Battery", "%.2f V", batteryVoltage());
        if (lastShot != null) {
            telemetry.addData("Last shot", "avg %.0f RPM, min %.0f RPM, power %.2f",
                    lastShot.rpmSum / lastShot.samples, lastShot.minRpm,
                    lastShot.powerSum / lastShot.samples);
        }
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°",
                poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        telemetry.addData("Saving to", DATA_FILE.getPath());
        telemetry.update();
    }

    @Override
    public void end() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
    }
}
