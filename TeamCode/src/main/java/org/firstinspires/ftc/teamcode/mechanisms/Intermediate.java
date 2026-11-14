package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Intermediate implements Mechanism {
    private final NextMotor intermediateMotor =
            new NextMotor(RobotController.controlHub(), 2);

    public static double intermediateValue = 1.0;

    public Command inward() {
        return Command.build()
                .requiring(this)
                .setStart(() -> intermediateMotor.setThrottle(-intermediateValue))
                .setEnd(condition -> intermediateMotor.setThrottle(0.0));
    }

    public Command outward() {
        return Command.build()
                .requiring(this)
                .setStart(() -> intermediateMotor.setThrottle(intermediateValue))
                .setEnd(condition -> intermediateMotor.setThrottle(0.0));
    }

}
