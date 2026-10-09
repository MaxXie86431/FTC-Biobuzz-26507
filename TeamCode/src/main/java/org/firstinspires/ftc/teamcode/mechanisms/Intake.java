package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

@Configurable
public class Intake implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 3);
    NextMotor intermediateMotor = new NextMotor(RobotController.controlHub(), 2);

    public static double intakeValue = 0.61;
    public static double intermediateValue = 1.0;

    public Command intake() {
        return instant(() -> {
            intakeMotor.setThrottle(-intakeValue);
            intermediateMotor.setThrottle(-intermediateValue);
        });
    }

    public Command outtake() {
        return instant(() -> {
            intakeMotor.setThrottle(intakeValue);
            intermediateMotor.setThrottle(intermediateValue);
        });
    }

    public Command stop() {
        return instant(() -> {
            intakeMotor.setThrottle(0.0);
            intermediateMotor.setThrottle(0.0);
        });
    }
}