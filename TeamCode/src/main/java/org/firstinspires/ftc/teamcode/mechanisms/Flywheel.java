package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.repeat;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;
import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

public class Flywheel implements Mechanism {
    private final Intermediate intermediate;

    // REV HD Hex 20:1 -> 28 counts per motor rev * 20 = 560 counts per output rev
    public final NextMotor flywheel =
            new NextMotor(RobotController.expansionHub(), 1, Degrees.of(360.0 / 560));
    private double power = 0.7;

    public Flywheel(Intermediate intermediate) {
        this.intermediate = intermediate;

        flywheel.getVelocityConstants()
            .withP(0.1)
            .withI(0.0)
            .withD(0.0);
    }

    public double getPower() {
        return power;
    }
    public Command rollout() {
        return infinite(() -> {
            flywheel.setThrottle(power);   // read every loop
            intermediate.forward();
        }).requiring(this, intermediate)
                .setEnd(condition -> {
                    flywheel.setThrottle(0.0);
                    intermediate.stop();
                });
    }

    public Command increasePower() {
        return instant(() -> power = Math.min(1.0, power + 0.01));
    }

    public Command decreasePower() {
        return instant(() -> power = Math.max(0.0, power - 0.01));
    }

    public Command rollIn() {
        return infinite(() -> {
            flywheel.setThrottle(-1);
            intermediate.reverse();
        }).requiring(this, intermediate);
    }

    public Command stop() {
        return instant(() -> {
            flywheel.setThrottle(0.0);
            intermediate.stop();
        }).requiring(this, intermediate);
    }

    public Command setRPM(double rpm) {
        return infinite(() -> {
            flywheel.setVelocitySetpoint(RotationsPerMinute.of(rpm));
            intermediate.forward();
        }).requiring(this, intermediate);
    }

    public Command shoot(int balls, int intermediateDelay) {
        return sequential(
            deadline(
                sequential(
                    // Let the flywheel charge up before feeding the first ball.
                    waitMs(4000),
                    repeat(
                        sequential(
                            instant(intermediate::forward).requiring(intermediate),
                            waitMs(intermediateDelay),
                            instant(intermediate::stop).requiring(intermediate)
                        ), balls
                    )
                ),
                // Keep the flywheel spinning until every ball has been fed.
                infinite(() -> flywheel.setThrottle(power)).requiring(this)
            ),
            this.stop()
        );
    }
}

