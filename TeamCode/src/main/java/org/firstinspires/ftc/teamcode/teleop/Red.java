package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.robot.Robot;
import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "DriverControlledRed")
public class Red extends TeleOp {

    public Red(Robot robot) {
        super(robot, Alliance.RED);
    }
}
