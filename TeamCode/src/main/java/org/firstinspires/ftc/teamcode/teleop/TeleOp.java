package org.firstinspires.ftc.teamcode.teleop;

import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.robot.Robot;
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
        robot.getDrivetrain().drive(gamepad1);

        // Intake
        gp1.leftTrigger().isOver(0.2).onTrue(robot.getIntake().intake());
        gp1.leftTrigger().isOver(0.2).onFalse(robot.getIntake().stop());
        gp1.leftBumper().onTrue(robot.getIntake().outtake());
        gp1.leftBumper().onFalse(robot.getIntake().stop());

        // Flywheel
        gp1.rightTrigger().isOver(0.2).onTrue(robot.getFlywheel().rollout());
        gp1.rightTrigger().isOver(0.2).onFalse(robot.getFlywheel().stop());
        gp1.rightBumper().onTrue(robot.getFlywheel().rollIn());
        gp1.rightBumper().onFalse(robot.getFlywheel().stop());
        gp1.cross().onTrue(robot.getFlywheel().setRPM());
        gp1.cross().onFalse(robot.getFlywheel().stop());
        
        gp1.dpadUp().onTrue(robot.getFlywheel().increasePower());
        gp1.dpadDown().onTrue(robot.getFlywheel().decreasePower());
    }

    @Override
    public void periodic() {
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        telemetry.addData("Flywheel RPM", robot.getFlywheel().flywheel.getEncoderVelocity().into(RotationsPerMinute));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", poseTracker.getX(), poseTracker.getY(), Math.toDegrees(poseTracker.getHeading()));
        telemetry.update();
    }

    @Override
    public void end() {
    }
}
