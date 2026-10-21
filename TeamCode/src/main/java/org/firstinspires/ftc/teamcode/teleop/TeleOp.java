package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;

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
    public static int padding = 16;
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

        // Intake
        gp1.leftTrigger().isOver(0.2).onTrue(parallel(
                robot.getIntake().intake(),
                robot.getIntermediate().forward()
        ));
        gp1.leftTrigger().isOver(0.2).onFalse(parallel(
                robot.getIntake().stop(),
                robot.getIntermediate().stop()
        ));
        gp1.leftBumper().onTrue(parallel(
                robot.getIntake().outtake(),
                robot.getIntermediate().reverse()
        ));
        gp1.leftBumper().onFalse(parallel(
                robot.getIntake().stop(),
                robot.getIntermediate().stop()
        ));

        // Flywheel
        gp1.rightTrigger().isOver(0.2).onTrue(robot.setRPM(() -> robot.getFlywheel().calculateRPM(poseTracker.distanceToGoal(alliance))+padding));
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
        PoseTracker.saveLastPose(poseTracker.getPosition());
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        //telemetry.addData("Servo Launcher Angle", robot.getFlywheel().getServoAngle());
        telemetry.addData("Flywheel RPM", robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        double distance = poseTracker.distanceToGoal(alliance);
        telemetry.addData("Distance from Goal: ", distance);
        telemetry.addData("Target RPM", "%.1f", robot.getFlywheel().calculateRPM(distance));
        telemetry.update();
    }

    @Override
    public void end() {
        poseTracker.update();
        PoseTracker.saveLastPose(poseTracker.getPosition());
    }
}
