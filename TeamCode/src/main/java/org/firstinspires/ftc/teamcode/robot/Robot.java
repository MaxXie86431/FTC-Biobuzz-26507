package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;
import java.util.Set;
import java.util.function.DoubleSupplier;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Flywheel;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Intermediate;
import org.firstinspires.ftc.teamcode.mechanisms.Blocker;
import org.firstinspires.ftc.teamcode.auto.pathing.Poses;
import org.firstinspires.ftc.teamcode.pedro.Constants;

public class Robot implements NextRobot {
    private  Follower follower;

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intermediate intermediate = new Intermediate();
    private final Intake intake = new Intake();
    private final Flywheel flywheel = new Flywheel();
    private final Blocker blocker = new Blocker();
    public Robot() {}

    public void createFollower() {
        if (follower == null) {
            follower = Constants.create(RobotController.hardwareMap());
        }
    }
    public Follower getFollower() {
        return follower;
    }

    public Drivetrain getDrivetrain() {
        return drivetrain;
    }

    public Intake getIntake() {
        return intake;
    }

    public Intermediate getIntermediate() {
        return intermediate;
    }

    public Blocker getBlocker() {
        return blocker;
    }

    public Flywheel getFlywheel(){ return flywheel; }

    public Command intake() {
        return parallel(
                intake.intake(),
                intermediate.forward()
        );
    }

    public Command outtake() {
        return parallel(
                intake.outtake(),
                intermediate.reverse()
        );
    }

    public Command stopIntake() {
        return parallel(
                intake.stop(),
                intermediate.stop()
        );
    }

    public Command shoot() {
        return parallel(
                flywheel.rollout(),
                sequential(
                        intermediate.stop(),
                        waitMs(2000),
                        blocker.move(),
                        loop(intermediate.forward())
                )
        );
    }

    public Command setRPM(DoubleSupplier rpm) {
        return parallel(
                loop(lazy(() -> flywheel.setVelocity(rpm.getAsDouble())).requiring(flywheel)),
                sequential(
                        intermediate.stop(),
                        waitMs(3000),
                        blocker.move(),
                        loop(intermediate.forward())
                )
        );
    }

    public Command stop() {
        return parallel(
                flywheel.stop(),
                intermediate.stop(),
                blocker.close()
        );
    }

    public Command init() {
        return blocker.close();
    }


    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, intermediate, intake, flywheel, blocker);
    }
}
