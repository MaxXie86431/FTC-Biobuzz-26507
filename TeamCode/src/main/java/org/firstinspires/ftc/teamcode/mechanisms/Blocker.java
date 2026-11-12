package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Blocker implements Mechanism {
    public static double upPos = 0.8;
    public static double downPos = 0.55;
    NextServo servo = new NextServo(RobotController.expansionHub(), 1, 0.01, NextMotor.Direction.REVERSE);
    private boolean opened = false;

    public Command open() {
        return instant(() -> {
            servo.setPosition(upPos);
            opened = true;
        });
    }

    public Command close() {
        return instant(() -> {
            servo.setPosition(downPos);
            opened = false;
        });
    }

    public Command toggle() {
        return instant(() -> {
            opened = !opened;
            servo.setPosition(opened ? upPos : downPos);
        });
    }

    public Command closeOnEnd() {
        return Commands.onInterrupt(() -> {
            servo.setPosition(downPos);
            opened = false;
        }).requiring(this);
    }
}
