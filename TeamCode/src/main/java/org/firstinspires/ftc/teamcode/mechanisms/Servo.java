package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Servo implements Mechanism {
    public static double servoPosition = 0.5;
    public static double servoReset = 0;
    NextServo servo = new NextServo(RobotController.expansionHub(), 1, 0.01, NextMotor.Direction.REVERSE);
    public Command move() {
        return instant(() -> {
            servo.setPosition(servoPosition);
        });
    }

    public Command reset(){
        return instant(() -> {
            servo.setPosition(servoReset);
        });
    }
}
