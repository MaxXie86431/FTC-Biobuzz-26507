package org.firstinspires.ftc.teamcode.teleop;

import dev.nextftc.robot.opmode.NextTeleop;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;

@NextTeleop(name = "Blue TeleOp")
public class BlueTeleOp extends TeleOp {
    public BlueTeleOp(Robot robot) {
        super(robot, Alliance.BLUE);
    }
}
