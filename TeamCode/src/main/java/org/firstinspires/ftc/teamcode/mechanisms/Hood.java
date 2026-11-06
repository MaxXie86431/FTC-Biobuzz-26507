package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

@Configurable
public class Hood implements Mechanism {

    public static double servoLaunchPosition = 0.5;
    NextServo launcherServo = new NextServo(RobotController.controlHub(), 4, 0.01);
    private double power = 0.7;


    public double getServoAngle() {
        return servoLaunchPosition;
    }


    public Command moveServoUp() {
        return instant(() -> {
            servoLaunchPosition = Math.min(1.0, servoLaunchPosition + 0.05);
            launcherServo.setPosition(servoLaunchPosition);
        });
    }

    public Command moveServoDown() {
        return instant(() -> {
            servoLaunchPosition = Math.max(0.0, servoLaunchPosition - 0.05);
            launcherServo.setPosition(servoLaunchPosition);
        });
    }


}