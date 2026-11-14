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

    public static double servoLaunchPosition = 0.48;
    public static double cornerLaunchPosition = 0.46;
    NextServo launcherServo = new NextServo(RobotController.controlHub(), 4);

    public double getServoAngle() {
        return servoLaunchPosition;
    }


    public Command moveServoDown() {
        return instant(() -> {
            servoLaunchPosition = Math.min(1.0, servoLaunchPosition + 0.02);
            launcherServo.setPosition(servoLaunchPosition);
        });
    }

    public Command moveServoUp() {
        return instant(() -> {
            servoLaunchPosition = Math.max(0.0, servoLaunchPosition - 0.02);
            launcherServo.setPosition(servoLaunchPosition);
        });
    }

    public Command setPosition(double position) {
        return instant(() -> {
            servoLaunchPosition = Math.max(0.0, Math.min(1.0, position));
            launcherServo.setPosition(servoLaunchPosition);
        });
    }
}