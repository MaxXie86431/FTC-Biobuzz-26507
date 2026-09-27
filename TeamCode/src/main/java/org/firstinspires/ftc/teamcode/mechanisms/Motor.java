package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Motor implements Mechanism{
    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 0);
    NextMotor liftMotor = new NextMotor(RobotController.controlHub(), 1);
    public Command spin(double power) {
        return instant(() -> {
            intakeMotor.setThrottle(power);
        });
    }
    public Command setVolt(double voltage) {
        return instant(() -> {
            liftMotor.setVoltage(Volts.of(voltage));
        });
    }
}
