package org.firstinspires.ftc.teamcode.teleop;

import static dev.nextftc.units.Units.RotationsPerMinute;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.robot.Robot;

@NextTeleop(name = "TeleOp")
public class TeleOp extends NextOpMode{
    private final Robot robot;
    public TeleOp(Robot robot) {
        super(robot);
        this.robot = robot;

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
        gp1.rightBumper().onTrue(robot.getFlywheel().rollin());
        gp1.rightBumper().onFalse(robot.getFlywheel().stop());
        gp1.cross().onTrue(robot.getFlywheel().setRPM());
        gp1.cross().onFalse(robot.getFlywheel().stop());
        
        gp1.dpadUp().onTrue(robot.getFlywheel().increasePower());
        gp1.dpadDown().onTrue(robot.getFlywheel().decreasePower());
        // Roller 2
        gp1.circle().onTrue(robot.getIntermediate().rollup());
        gp1.circle().onFalse(robot.getIntermediate().stop());
        gp1.triangle().onTrue(robot.getIntermediate().rolldown());
        gp1.triangle().onFalse(robot.getIntermediate().stop());
    }

    @Override
    public void periodic() {
        telemetry.addData("Launcher Motor Power", robot.getFlywheel().getPower());
        telemetry.update();
    }

    @Override
    public void end() {
    }
}
