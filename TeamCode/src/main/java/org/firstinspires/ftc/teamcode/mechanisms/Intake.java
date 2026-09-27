package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

public class Intake implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 3);

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