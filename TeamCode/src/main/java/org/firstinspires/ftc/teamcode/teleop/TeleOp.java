package org.firstinspires.ftc.teamcode.teleop;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.robot.Robot;

@NextTeleop(name = "Teleop")
public class TeleOp extends NextOpMode{
    private final Robot robot;

    public TeleOp(Robot robot) {
        super(robot)
        this.robot = robot;

        Scheduler.reset();
        this.robot.init.schedule();
    }

    @Override
    public void start() {
        CommandGamepad gp1 = new CommandGamepad(gamepad1);
        CommandGamepad gp2 = new CommandGamepad(gamepad2);
        robot.getDrivetrain().drive(gamepad1);
    }

    @Override
    public void end() {
    }
}
