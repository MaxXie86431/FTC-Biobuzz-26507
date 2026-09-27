package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.robot.Robot;
import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "DriverControlledBlue")
public class Blue extends TeleOp {

    public Blue(Robot robot) {
        super(robot, Alliance.BLUE);
    }
}
