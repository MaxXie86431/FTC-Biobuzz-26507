package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;

public class Robot implements NextRobot{
    private  Follower follower;

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();

    public Robot() {}

    public Drive getDrivetrain() {
        return drivetrain;
    }

    public Intake getIntake() {
        return intake;
    }

    public void init() {

    }

    @Nonnull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, intake);
    }
}
