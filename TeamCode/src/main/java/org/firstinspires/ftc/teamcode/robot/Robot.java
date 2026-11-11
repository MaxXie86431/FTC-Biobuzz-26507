package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;
import java.util.Set;
import java.util.function.DoubleSupplier;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

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
import org.firstinspires.ftc.teamcode.mechanisms.Hood;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Intermediate;
import org.firstinspires.ftc.teamcode.mechanisms.Blocker;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Configurable
public class Robot implements NextRobot {
    private  Follower follower;

    private final Drivetrain drivetrain = new Drivetrain();
    private final Intermediate intermediate = new Intermediate();
    private final Intake intake = new Intake();
    private final Flywheel flywheel = new Flywheel();
    private final Blocker blocker = new Blocker();
    private final Hood hood = new Hood();
    public static int charge = 2000;
    public static double servoWait = 100;
    public static double reverseTime = 200;
    public Robot() {}

    public void createFollower() {
        follower = Constants.create(RobotController.hardwareMap());
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

    public Hood getHood(){ return hood; }

    // spin-up wait while the intermediate backs the balls away, then open the blocker and feed
    private Command feed() {
        return sequential(
                parallel(
                        waitMs(charge),
                        sequential(
                                intermediate.reverse(),
                                waitMs(reverseTime),
                                intermediate.stop()
                        )
                ),
                blocker.move(),
                waitMs(servoWait),
                parallel(intake.intake(), intermediate.forward())
        );
    }

    public Command shoot() {
        return parallel(flywheel.rollout(), feed());
    }

    // same feed sequence as shoot(), but holds the flywheel at an RPM instead of a fixed power
    public Command shootAtRPM(DoubleSupplier rpm) {
        return parallel(
                loop(lazy(() -> flywheel.setVelocity(rpm.getAsDouble())).requiring(flywheel)),
                feed()
        );
    }

    public Command stop() {
        return parallel(
                flywheel.stop(),
                intake.stop(),
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
