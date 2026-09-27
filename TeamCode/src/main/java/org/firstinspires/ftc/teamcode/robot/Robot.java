package org.firstinspires.ftc.teamcode.robot;

import java.util.Collections;
import java.util.Set;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

/** Hardware-free root container for the robot's mechanisms. */
public final class Robot implements NextRobot {
    private final Drivetrain drivetrain = new Drivetrain();

    @Override
    public Set<Mechanism> getMechanisms() {
        return Collections.<Mechanism>singleton(drivetrain);
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }
}
