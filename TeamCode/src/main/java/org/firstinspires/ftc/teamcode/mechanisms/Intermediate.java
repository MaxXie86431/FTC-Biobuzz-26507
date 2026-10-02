package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

public class Intermediate implements Mechanism {

    public final NextMotor intermediateMotor = new NextMotor(RobotController.controlHub(), 2);
    public Command rollup() {
        return infinite(() ->
                intermediateMotor.setThrottle(-1)
        );
    }

    public Command rolldown() {
        return infinite(() ->
                intermediateMotor.setThrottle(1)
        );
    }

    public Command stop() {
        return instant(() ->
                intermediateMotor.setThrottle(0.0)
        );
    }
}
