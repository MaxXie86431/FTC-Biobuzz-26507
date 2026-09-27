package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;

public class Robot implements NextRobot{
    private  Follower follower;
    private final Drivetrain drivetrain = new Drivetrain();

    public Robot() {}

    public Drive getDrivetrain() {
        return drivetrain;
    }

    public void init() {

    }

    @Nonnull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain);
    }
}
