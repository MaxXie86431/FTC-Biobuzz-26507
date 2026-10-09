package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;
import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

public class Flywheel implements Mechanism {

    // REV HD Hex 20:1 -> 28 counts per motor rev * 20 = 560 counts per output rev
    public final NextMotor flywheel =
            new NextMotor(RobotController.expansionHub(), 1, Degrees.of(360.0 / 560));
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
    public Command rollout() {
        return infinite(() ->
                flywheel.setThrottle(power)   // read every loop
        );
    }

    public Command increasePower() {
        return instant(() -> power = Math.min(1.0, power + 0.1));
    }

    public Command decreasePower() {
        return instant(() -> power = Math.max(0.0, power - 0.1));
    }

    public Command rollin() {
        return infinite(() ->
                flywheel.setThrottle(-1)
        );
    }

    public Command stop() {
        return instant(() ->
                flywheel.setThrottle(0.0)
        );
    }

    public Command setRPM() {
        return instant(() -> 
                flywheel.setVelocitySetpoint(RotationsPerMinute.of(250))
        );
    }
}