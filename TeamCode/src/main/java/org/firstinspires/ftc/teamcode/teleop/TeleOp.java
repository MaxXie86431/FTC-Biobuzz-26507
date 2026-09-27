package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.DriveConfig;
import org.firstinspires.ftc.teamcode.robot.Drivetrain;

import dev.nextftc.robot.opmode.NextOpMode;

public abstract class TeleOp extends NextOpMode {
    public enum Alliance {
        RED,
        BLUE
    }

    protected final Robot robot;
    protected final Alliance alliance;
    private final Drivetrain drivetrain;
    private final Command driveCommand;
    private boolean previousBack;

    protected TeleOp(Robot robot, Alliance alliance) {
        super(robot);
        this.robot = robot;
        this.alliance = alliance;
        this.drivetrain = robot.getDrivetrain();

        // This returns before all hardware lookups while DriveConfig.HARDWARE_CONFIRMED is false.
        drivetrain.initialize(hardwareMap);
        driveCommand = drivetrain.infinite(() -> {
            boolean backPressed = gamepad1.back;
            if (backPressed && !previousBack) {
                drivetrain.captureFieldForwardReference();
            }
            previousBack = backPressed;

            double speed = gamepad1.left_bumper ? DriveConfig.SLOW_SPEED : 1.0;
            drivetrain.drive(
                    gamepad1.left_stick_y,
                    gamepad1.left_stick_x,
                    gamepad1.right_stick_x,
                    speed);
        }).setEnd(endCondition -> drivetrain.stop());
    }

    @Override
    public void disabledPeriodic() {
        // Sample status in INIT too; drive powers remain zero until the PLAY command runs.
        drivetrain.periodic();
        publishDriveTelemetry();
    }

    @Override
    public void start() {
        previousBack = gamepad1.back;
        drivetrain.captureFieldForwardReference();
        driveCommand.schedule();
        publishDriveTelemetry();
    }

    @Override
    public void periodic() {
        publishDriveTelemetry();
    }

    @Override
    public void end() {
        drivetrain.stop();
    }

    private void publishDriveTelemetry() {
        telemetry.addData("Alliance", alliance);
        telemetry.addData("Drive config", DriveConfig.HARDWARE_CONFIRMED
                ? "CONFIRMED; hardware gate enabled"
                : "UNCONFIRMED; motor and sensor lookups are disabled");
        telemetry.addData("Drive setup", drivetrain.getSetupStatus());
        telemetry.addData("Pinpoint", drivetrain.getPinpointStatus());
        if (drivetrain.isHeadingValid()) {
            telemetry.addData("Heading", "%.1f deg", drivetrain.getHeadingDegrees());
        } else {
            telemetry.addData("Heading", "unavailable");
        }
        telemetry.addData("Drive mode", drivetrain.getModeStatus());
        telemetry.addLine("Controls: left stick drives field-relative; right stick turns clockwise.");
        telemetry.addLine("Left bumper: slow mode (0.3). Press Back once to set field-forward.");
        telemetry.addLine("Aim the robot toward the desired field-forward direction before PLAY.");
        telemetry.update();
    }
}
