package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.lut_interpolation.ShooterLUT;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

@Configurable
public abstract class TeleOp extends NextOpMode{
    private final Robot robot;
    private final Alliance alliance;
    private final PoseTracker poseTracker;
    private Follower follower;
    private boolean initialized;
    // driver trim added on top of the LUT RPM (dpad up/down)
    public static int padding = 0;
    public static int paddingStep = 5;
    protected TeleOp(Robot robot, Alliance alliance) {
        super(robot);
        this.robot = robot;
        this.alliance = alliance;

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
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        robot.getDrivetrain().frontLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().frontRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().drive(gamepad1).schedule();

        // ==================== Actual Control Inputs ====================

        // Intake
        gp1.leftTrigger().isOver(0.2).whileTrue(robot.intake());
        gp1.leftBumper().whileTrue(robot.outTake());

        // Regression
        gp1.rightTrigger().isOver(0.2).whileTrue(robot.calculatedShot(
                () -> robot.getFlywheel().calculateRPM(poseTracker.distanceToGoal(alliance))
        ));
        gp1.rightBumper().whileTrue(robot.eject());

        // Manual Shooting
        gp1.a().whileTrue(robot.manualShot());

        // Blocker
        gp1.b().onTrue(robot.getBlocker().toggle());

        // Hood Adjustment
        gp1.y().onTrue(robot.getHood().moveServoDown());
        gp1.x().onTrue(robot.getHood().moveServoUp());

        // Padding Adjustments
        gp1.dpadUp().onTrue(instant(() -> padding += paddingStep));
        gp1.dpadDown().onTrue(instant(() -> padding -= paddingStep));
        gp1.dpadLeft().onTrue(robot.getFlywheel().decreasePower());
        gp1.dpadRight().onTrue(robot.getFlywheel().increasePower());
    }

    @Override
    public void periodic() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        //telemetry.addData("Servo Launcher Angle", robot.getFlywheel().getServoAngle());
        telemetry.addData("Flywheel RPM", robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        double distance = poseTracker.distanceToGoal(alliance);
        ShooterLUT.ShotSettings shot = ShooterLUT.calculate(distance);
        telemetry.addData("Distance from Goal", "%.1f in", distance);
        telemetry.addData("RPM Padding", padding);
        telemetry.addData("Target RPM", "%.1f", robot.getFlywheel().calculateRPM(distance));
        telemetry.addData("Servo Position", robot.getHood().getServoAngle());
        telemetry.update();
    }

    @Override
    public void end() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
    }
}
