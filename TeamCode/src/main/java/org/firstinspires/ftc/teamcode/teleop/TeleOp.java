package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
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
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

@Configurable
@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode{
    private final Robot robot;
    private final PoseTracker poseTracker;
    private Follower follower;
    private boolean initialized;
    public static int launcherRPM = 130;

    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;

        robot.createFollower();
        follower = robot.getFollower();
        follower.setPose(new Pose(56, 8, Math.toRadians(90)));
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
        Trigger.Companion.getDefaultEventLoop().clear();

        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);

        robot.getDrivetrain().frontLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().frontRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().backRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        robot.getDrivetrain().drive(gamepad1).schedule();

        // Intake
        gp1.leftTrigger().isOver(0.2).onTrue(robot.intake());
        gp1.leftTrigger().isOver(0.2).onFalse(robot.stopIntake());
        gp1.leftBumper().onTrue(robot.outtake());
        gp1.leftBumper().onFalse(robot.stopIntake());

        // Flywheel
        gp1.rightTrigger().isOver(0.2).onTrue(robot.setRPM(() -> launcherRPM));
        gp1.rightTrigger().isOver(0.2).onFalse(robot.stop());
        gp1.rightBumper().onTrue(parallel(
                robot.getBlocker().move(),
                robot.getFlywheel().rollIn(),
                loop(robot.getIntermediate().reverse())
        ));
        gp1.rightBumper().onFalse(robot.stop());
        gp1.cross().onTrue(robot.shoot());
        gp1.cross().onFalse(robot.stop());

        //gp1.circle().onTrue(robot.getHood().moveServoUp());
        //gp1.square().onTrue(robot.getHood().moveServoDown());

        gp1.dpadLeft().onTrue(robot.getFlywheel().increasePower());
        gp1.dpadRight().onTrue(robot.getFlywheel().decreasePower());
    }

    @Override
    public void periodic() {
        poseTracker.update();
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        //telemetry.addData("Servo Launcher Angle", robot.getFlywheel().getServoAngle());
        telemetry.addData("Flywheel RPM", robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        double distance = poseTracker.distanceToGoal(Alliance.RED);
        telemetry.addData("Distance from Goal: ", distance);
        telemetry.addData("Target RPM", "%.1f", robot.getFlywheel().calculateRPM(distance));
        telemetry.update();
    }

    @Override
    public void end() {
    }
}
