package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

@Configurable
public class Intake implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 3);
    public static double intakeValue=0.61;

    public Command intake() {
        return infinite(() ->
                intakeMotor.setThrottle(-intakeValue)
        );
    }

    public Command outtake() {
        return infinite(() ->
                intakeMotor.setThrottle(intakeValue)
        );
    }

    public Command stop() {
        return instant(() ->
                intakeMotor.setThrottle(0.0)
        );
    }
}