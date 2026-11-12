package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.ivy.Command;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.RobotController;

@Configurable
public class Intake implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 3);
    public static double intakeValue = 0.6;
    public static double outtakeValue = -0.3;

    public Command inward() {
        return Command.build()
                .requiring(this)
                .setStart(() -> intakeMotor.setThrottle(intakeValue))
                .setEnd(condition -> intakeMotor.setThrottle(0.0));
    }

    public Command outward() {
        return Command.build()
                .requiring(this)
                .setStart(() -> intakeMotor.setThrottle(outtakeValue))
                .setEnd(condition -> intakeMotor.setThrottle(0.0));
    }

}
