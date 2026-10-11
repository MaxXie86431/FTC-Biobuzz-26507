package org.firstinspires.ftc.teamcode.mechanisms;

import com.bylazar.configurables.annotations.Configurable;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

@Configurable
public class Intermediate implements Mechanism {
    private final NextMotor intermediateMotor =
            new NextMotor(RobotController.controlHub(), 2);

    public static double intermediateValue = 1.0;

    // Commands calling these methods must require the shared Intermediate mechanism.
    public void forward() {
        setThrottle(-intermediateValue);
    }

    public void reverse() {
        setThrottle(intermediateValue);
    }

    private void setThrottle(double throttle) {
        intermediateMotor.setThrottle(throttle);
    }

    public void stop() {
        setThrottle(0.0);
    }
}
