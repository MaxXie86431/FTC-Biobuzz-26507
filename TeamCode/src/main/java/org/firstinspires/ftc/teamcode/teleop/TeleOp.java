package org.firstinspires.ftc.teamcode.teleop;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

import com.pedropathing.ivy.Scheduler;

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

        // Roller 2
        gp1.circle().onTrue(robot.getIntermediate().rollup());
        gp1.circle().onFalse(robot.getIntermediate().stop());

        gp1.triangle().onTrue(robot.getIntermediate().rolldown());
        gp1.triangle().onFalse(robot.getIntermediate().stop());
    }

    @Override
    public void end() {
    }
}
