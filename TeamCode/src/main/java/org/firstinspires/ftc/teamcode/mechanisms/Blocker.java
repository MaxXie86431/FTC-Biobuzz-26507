package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Blocker implements Mechanism {
    public static double servoPosition = 0.5;
    public static double servoReset = 0;
    NextServo servo = new NextServo(RobotController.expansionHub(), 1, 0.01, NextMotor.Direction.REVERSE);
    private boolean open;

    public Command move() {
        return instant(() -> setOpen(true));
    }

    public Command close() {
        return instant(() -> setOpen(false));
    }

    public Command toggle() {
        return instant(() -> setOpen(!open));
    }

    private void setOpen(boolean open) {
        servo.setPosition(open ? servoPosition : servoReset);
        servo.enable();
        this.open = open;
    }
}
