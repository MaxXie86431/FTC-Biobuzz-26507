package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;
import java.util.Set;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import static com.pedropathing.ivy.commands.Commands.instant;

import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;

public class Robot implements NextRobot {
    private  Follower follower;

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intake intake = new Intake();

    public Robot() {}

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public Intake getIntake() {
        return intake;
    }

    public Command init() {
        return instant(() -> {});
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, intake);
    }
}
