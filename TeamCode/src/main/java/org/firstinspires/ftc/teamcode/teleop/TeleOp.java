package org.firstinspires.ftc.teamcode.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode{
    private final Robot robot;
    private final PoseTracker poseTracker;
    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;

        robot.createFollower();
        poseTracker = new PoseTracker(robot.getFollower());

        Scheduler.reset();
        this.robot.init().schedule();
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
        robot.getDrivetrain().drive(gamepad1);

        // Intake
        gp1.leftTrigger().isOver(0.2).onTrue(robot.getIntake().intake());
        gp1.leftTrigger().isOver(0.2).onFalse(robot.getIntake().stop());
        gp1.leftBumper().onTrue(robot.getIntake().outtake());
        gp1.leftBumper().onFalse(robot.getIntake().stop());

        // Flywheel
        gp1.rightTrigger().isOver(0.2).onTrue(
                parallel(
                        robot.getFlywheel().rollout(),
                        sequential(
                                waitMs(3000),
                                robot.getServo().move()
                        )
                )
        );
        gp1.rightTrigger().isOver(0.2).onFalse(
                parallel(
                        robot.getFlywheel().stop(),
                        robot.getServo().reset()
                )
        );
        gp1.rightBumper().onTrue(
                instant(() -> sequential(
                        robot.getServo().move(),
                        robot.getFlywheel().rollIn()
                ).schedule())
        );
        gp1.rightBumper().onFalse(
                sequential(
                        robot.getFlywheel().stop(),
                        robot.getServo().reset()
                )
        );
        gp1.cross().onTrue(robot.getFlywheel().setRPM(() -> robot.getFlywheel().calculateRPM(poseTracker.distanceToGoal(Alliance.RED))));
        gp1.cross().onFalse(robot.getFlywheel().stop());
        
        gp1.dpadUp().onTrue(robot.getFlywheel().increasePower());
        gp1.dpadDown().onTrue(robot.getFlywheel().decreasePower());
    }

    @Override
    public void periodic() {
        poseTracker.update();
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        telemetry.addData("Flywheel RPM", robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        double distance = poseTracker.distanceToGoal(Alliance.RED);
        telemetry.addData("Distance to Goal", "%.1f in%s", distance, robot.getFlywheel().inShootingRange(distance) ? "" : " (OUT OF RANGE)");
        telemetry.addData("Target RPM", "%.1f", robot.getFlywheel().calculateRPM(distance));
        telemetry.update();
    }

    @Override
    public void end() {
    }
}
