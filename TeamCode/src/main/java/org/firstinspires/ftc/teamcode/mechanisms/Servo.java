package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class Servo implements Mechanism{
    NextServo servo = new NextServo("testServo");
    public Command move(double position) {
        return instant(() -> {
            servo.setPosition(position);
        });
    }
}
