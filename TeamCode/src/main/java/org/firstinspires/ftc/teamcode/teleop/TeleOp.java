package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.robot.Robot;
import dev.nextftc.robot.opmode.NextOpMode;

public abstract class TeleOp extends NextOpMode {
    public enum Alliance {
        RED,
        BLUE
    }

    protected final Robot robot;
    protected final Alliance alliance;

    protected TeleOp(Robot robot, Alliance alliance) {
        super(robot);
        this.robot = robot;
        this.alliance = alliance;
    }
}
