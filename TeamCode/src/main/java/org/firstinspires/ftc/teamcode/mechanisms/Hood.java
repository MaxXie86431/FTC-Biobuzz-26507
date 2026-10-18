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
    public final NextMotor flywheel =
            new NextMotor(RobotController.expansionHub(), 1, Degrees.of(360.0 / 560));

    public static double servoLaunchPosition = 0.5;
    public static double servoStep = 0.05;
    NextServo launcherServo = new NextServo(RobotController.expansionHub(), 0, 0.01);
    private double power = 0.7;

    public Flywheel() {
        flywheel.getVelocityConstants()
                .withP(0.1)
                .withI(0.0)
                .withD(0.0);
    }

    public double getPower() {
        return power;
    }

    public double getServoAngle() {
        return servoLaunchPosition;
    }

    public Command rollout() {
        return infinite(() -> flywheel.setThrottle(power))
                .setEnd(condition -> flywheel.setThrottle(0.0));
    }

    public Command increasePower() {
        return instant(() -> power = Math.min(1.0, power + 0.01));
    }

    public Command decreasePower() {
        return instant(() -> power = Math.max(0.0, power - 0.01));
    }

    public Command rollIn() {
        return infinite(() -> flywheel.setThrottle(-1))
                .setEnd(condition -> flywheel.setThrottle(0.0));
    }

    public Command stop() {
        return setThrottle(0.0);
    }

    public Command setThrottle(double throttle) {
        return instant(() -> flywheel.setThrottle(throttle));
    }

    public Command setVelocity(double rpm) {
        return instant(() -> flywheel.setVelocitySetpoint(RotationsPerMinute.of(rpm)));
    }

    // distances (inches) our shooting data covers
    public static final double MIN_DISTANCE = 21.5;
    public static final double MAX_DISTANCE = 44;

    public double calculateRPM(double distance) {
        // outside the tested range, use the closest tested distance instead of guessing
        distance = Math.max(MIN_DISTANCE, Math.min(MAX_DISTANCE, distance));
        return (0.761321 * distance + 107.87642);
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