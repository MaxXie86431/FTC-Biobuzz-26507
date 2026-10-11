package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

@Configurable
public class Intake implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 3);
    private final Intermediate intermediate;

    public static double intakeValue = 0.61;

    public Intake(Intermediate intermediate) {
        this.intermediate = intermediate;
    }

    public Command intake() {
        return instant(() -> {
            intakeMotor.setThrottle(-intakeValue);
            intermediate.forward();
        }).requiring(this, intermediate);
    }

    public Command outtake() {
        return instant(() -> {
            intakeMotor.setThrottle(intakeValue);
            intermediate.reverse();
        }).requiring(this, intermediate);
    }

    public Command stop() {
        return instant(() -> {
            intakeMotor.setThrottle(0.0);
            intermediate.stop();
        }).requiring(this, intermediate);
    }
}
