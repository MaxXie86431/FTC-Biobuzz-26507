package org.firstinspires.ftc.teamcode.teleop;

import dev.nextftc.robot.opmode.NextTeleop;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;

@NextTeleop(name = "Red TeleOp")
public class RedTeleOp extends TeleOp {
    public RedTeleOp(Robot robot) {
        super(robot, Alliance.RED);
    }
}
