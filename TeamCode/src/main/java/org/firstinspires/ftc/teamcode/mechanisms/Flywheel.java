package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

public class Flywheel implements Mechanism {

    public final NextMotor flywheel = new NextMotor(RobotController.expansionHub(), 1);
    private double power = 0.7;

    public double getPower() {
        return power;
    }

    public Command rollout() {
        return infinite(() ->
                flywheel.setThrottle(power)   // read every loop
        );
    }

    public Command increasePower() {
        return instant(() -> power = power + 0.1);
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
}