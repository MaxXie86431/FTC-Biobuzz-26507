package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.lut_interpolation.ShooterLUT;
import org.firstinspires.ftc.teamcode.lut_interpolation.ShooterLinearInverse;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

// Tests ShooterLUT and ShooterLinearInverse: shows what each model outputs for the
// current distance, and shoots with the selected model's RPM and hood angle.
// The model "angle" is sent straight to the hood servo as a position (0-1).
//
// Controls (gamepad 1):
//   left stick / right stick  drive
//   left trigger              intake
//   right trigger (hold)      shoot with the selected model
//   cross                     switch model (LUT <-> Linear Inverse)
//   square                    switch distance source (odometry <-> manual)
//   dpad up / down            manual distance +/- distanceStep
//   triangle                  switch alliance goal (red <-> blue)
//   circle                    toggle blocker
@Configurable
@NextTeleop(name = "Shooter Model Test")
public class ShooterModelTest extends NextOpMode {
    public static double manualDistance = 30;
    public static double distanceStep = 1;

    private final Robot robot;
    private final Follower follower;
    private final PoseTracker poseTracker;
    private boolean initialized;

    private Alliance alliance = Alliance.RED;
    private boolean useLinearInverse = false;
    private boolean useManualDistance = false;

    // the linear inverse model fits its data when created, so it can fail before we ever call it
    private ShooterLinearInverse linearInverse;
    private String linearInverseSetupError;

    // latest model outputs, updated every loop; null means that model failed (see the error string)
    private ShooterLUT.ShotSettings lutShot;
    private String lutError;
    private ShooterLinearInverse.ShotSettings inverseShot;
    private String inverseError;

    public ShooterModelTest(Robot robot) {
        super(robot);
        this.robot = robot;

        robot.createFollower();
        follower = robot.getFollower();
        poseTracker = new PoseTracker(follower);

        try {
            linearInverse = new ShooterLinearInverse();
        } catch (RuntimeException e) {
            linearInverseSetupError = e.getMessage();
        }

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

        // Shoot with the selected model; RPM and hood follow the model every loop
        gp1.rightTrigger().isOver(0.2).onTrue(parallel(
                robot.shootAtRPM(this::targetRpm),   // same shot sequence as Data Collection
                loop(lazy(() -> robot.getHood().setPosition(targetAngle())))
        ));
        gp1.rightTrigger().isOver(0.2).onFalse(robot.stop());

        gp1.cross().onTrue(instant(() -> useLinearInverse = !useLinearInverse));
        gp1.square().onTrue(instant(() -> useManualDistance = !useManualDistance));
        gp1.dpadUp().onTrue(instant(() -> manualDistance += distanceStep));
        gp1.dpadDown().onTrue(instant(() -> manualDistance -= distanceStep));
        gp1.triangle().onTrue(instant(() ->
                alliance = alliance == Alliance.RED ? Alliance.BLUE : Alliance.RED));
        gp1.circle().onTrue(robot.getBlocker().toggle());
    }

    private double testDistance() {
        return useManualDistance ? manualDistance : poseTracker.distanceToGoal(alliance);
    }

    private void updateModels(double distance) {
        try {
            lutShot = ShooterLUT.calculate(distance);
            lutError = null;
        } catch (RuntimeException e) {
            lutShot = null;
            lutError = e.getMessage();
        }

        if (linearInverse == null) {
            inverseShot = null;
            inverseError = linearInverseSetupError;
            return;
        }
        try {
            inverseShot = linearInverse.calculate(distance);
            inverseError = null;
        } catch (RuntimeException e) {
            inverseShot = null;
            inverseError = e.getMessage();
        }
    }

    // 0 RPM if the selected model failed, so a bad model never spins the flywheel
    private double targetRpm() {
        if (useLinearInverse) {
            return inverseShot != null ? inverseShot.rpm : 0.0;
        }
        return lutShot != null ? lutShot.rpm : 0.0;
    }

    // keep the hood where it is if the selected model failed
    private double targetAngle() {
        if (useLinearInverse) {
            return inverseShot != null ? inverseShot.angle : robot.getHood().getServoAngle();
        }
        return lutShot != null ? lutShot.angle : robot.getHood().getServoAngle();
    }

    @Override
    public void periodic() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());

        double distance = testDistance();
        updateModels(distance);

        telemetry.addData("Model", useLinearInverse ? "LINEAR INVERSE" : "LUT");
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Distance source", useManualDistance ? "MANUAL" : "ODOMETRY");
        telemetry.addData("Test distance", "%.1f in", distance);
        telemetry.addData("Odometry distance", "%.1f in", poseTracker.distanceToGoal(alliance));

        if (lutShot != null) {
            telemetry.addData("LUT", "angle %.3f, rpm %.1f", lutShot.angle, lutShot.rpm);
        } else {
            telemetry.addData("LUT", "ERROR: %s", lutError);
        }
        if (inverseShot != null) {
            telemetry.addData("Linear Inverse", "angle %.3f, rpm %.1f, predicts %.1f in",
                    inverseShot.angle, inverseShot.rpm, inverseShot.predictedDistance);
        } else {
            telemetry.addData("Linear Inverse", "ERROR: %s", inverseError);
        }

        telemetry.addData("Target RPM", "%.1f", targetRpm());
        telemetry.addData("Flywheel RPM", "%.1f",
                robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Flywheel 2 RPM", "%.1f",
                robot.getFlywheel().flywheel2.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Hood position", "%.3f", robot.getHood().getServoAngle());
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°",
                poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        telemetry.update();
    }

    @Override
    public void end() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
    }
}
