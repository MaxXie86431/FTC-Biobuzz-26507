package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import java.util.function.DoubleSupplier;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;
import static dev.nextftc.units.Units.Volts;

@Configurable
public class Flywheel implements Mechanism {
    private static final double VOLTAGE = 12.0;

    public static double kS = 0.1;
    public static double kV = 0.0002;
    public static double kA = 0;
    public static double kP = 0.001;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public final NextMotor flywheel =
            new NextMotor(RobotController.expansionHub(), 1, Degrees.of(360.0 / 28.0));
    public final NextMotor flywheel2 =
            new NextMotor(RobotController.expansionHub(), 3, Degrees.of(360.0 / 28.0));
    private double power = 0.7;

    public Flywheel() {
        flywheel.setDirection(NextMotor.Direction.REVERSE);
        flywheel.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        flywheel2.setDirection(NextMotor.Direction.REVERSE);
        flywheel2.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
        applyPIDGains();
    }

    @Override
    public void periodic() {
        applyPIDGains();
    }

    private void applyPIDGains() {
        flywheel.getVelocityConstants()
                .withS(kS)
                .withV(kV)
                .withA(kA)
                .withP(kP)
                .withI(kI)
                .withD(kD);
        flywheel2.getVelocityConstants()
                .withS(kS)
                .withV(kV)
                .withA(kA)
                .withP(kP)
                .withI(kI)
                .withD(kD);
    }

    public double getPower() {
        return power;
    }

    public double getVoltage() {
        return power*VOLTAGE;
    }

    public Command rollOut() {
        return infinite(() -> {
                    flywheel.setVoltage(Volts.of(power * VOLTAGE));
                    flywheel2.setVoltage(Volts.of(power * VOLTAGE));
                })
                .setEnd(condition -> {
                    flywheel.setVoltage(Volts.of(0.0));
                    flywheel2.setVoltage(Volts.of(0.0));
                });
    }

    public Command rollIn() {
        return infinite(() -> {
            flywheel.setVoltage(Volts.of(-power * VOLTAGE));
            flywheel2.setVoltage(Volts.of(-power * VOLTAGE));
        })
                .setEnd(condition -> {
                    flywheel.setVoltage(Volts.of(0.0));
                    flywheel2.setVoltage(Volts.of(0.0));
                });
    }

    public Command increasePower() {
        return Commands.instant(() -> power = Math.min(1.0, power + 0.01));
    }

    public Command decreasePower() {
        return Commands.instant(() -> power = Math.max(0.0, power - 0.01));
    }

    public Command setVoltage(double volts) {
        return infinite(() -> {
            flywheel.setVoltage(Volts.of(volts));
            flywheel2.setVoltage(Volts.of(volts));
        }).setEnd(condition -> {
            flywheel.setVoltage(Volts.of(0.0));
            flywheel2.setVoltage(Volts.of(0.0));
        });
    }

    public Command setRPM(DoubleSupplier rpm) {
        return infinite(() -> {
            double targetRPM = rpm.getAsDouble();
            flywheel.setVelocitySetpoint(RotationsPerMinute.of(targetRPM));
            flywheel2.setVelocitySetpoint(RotationsPerMinute.of(targetRPM));
        }).setEnd(condition -> {
            flywheel.setVoltage(Volts.of(0.0));
            flywheel2.setVoltage(Volts.of(0.0));
        });
    }
}

