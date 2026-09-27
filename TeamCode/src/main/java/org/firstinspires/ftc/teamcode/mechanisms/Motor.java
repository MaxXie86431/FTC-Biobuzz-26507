package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Volts;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Motor implements Mechanism{
    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 0);
    NextMotor liftMotor = new NextMotor(RobotController.controlHub(), 1);
    public Command move(double power) {
        return instant(() -> {
            intakeMotor.setThrottle(power);
        });
    }
    public Command lift(double voltage) {
        return instant(() -> {
            liftMotor.setVoltage(Volts.of(voltage));
        });
    }
}
