package org.firstinspires.ftc.teamcode.mechanisms;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

public class Motor implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 2);

    public Command intake() {
        return infinite(() ->
                intakeMotor.setThrottle(1.0)
        );
    }

    public Command outtake() {
        return infinite(() ->
                intakeMotor.setThrottle(-1.0)
        );
    }

    public Command stop() {
        return instant(() ->
                intakeMotor.setThrottle(0.0)
        );
    }
}