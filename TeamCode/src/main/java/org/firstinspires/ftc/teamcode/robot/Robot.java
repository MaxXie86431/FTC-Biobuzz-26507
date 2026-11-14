package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;
import java.util.Set;
import java.util.function.DoubleSupplier;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.onInterrupt;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.groups.Groups.loop;
import static com.pedropathing.ivy.commands.Commands.lazy;

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
    public static double servoWait = 500;
    public static double reverseTime = 300;

    public Robot() {}
    // ==================== Follower ====================
    public void createFollower() {
        follower = Constants.create(RobotController.hardwareMap());
    }

    public Follower getFollower() {
        return follower;
    }

    // ==================== Getters ====================
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

    // ==================== Multi-Mechanism Actions ====================
    public Command intake() {
        return parallel(
                intake.inward(),
                intermediate.inward()
        );
    }

    public Command outTake() {
        return parallel(
                intake.outward(),
                intermediate.outward()
        );
    }

    public Command legacyManualShot() {
        return parallel(
                flywheel.rollOut(),
                sequential(
                        parallel(
                                waitMs(charge),
                                intermediate.outward().raceWith(waitMs(reverseTime))
                        ),
                        blocker.open(),
                        waitMs(servoWait),
                        parallel(intake.inward(), intermediate.inward())
                )
        ).with(onInterrupt(() -> blocker.close().start()));
    }

    public Command manualShot() {
        return parallel(
                loop(
                        lazy(() -> hood.setPosition(Hood.servoLaunchPosition)).requiring(hood)
                ),
                flywheel.setRPM(() -> Flywheel.manualRPM + Flywheel.padding),
                sequential(
                        parallel(
                                waitMs(charge),
                                intermediate.outward().raceWith(waitMs(reverseTime))
                        ),
                        blocker.open(),
                        waitMs(servoWait),
                        parallel(intake.inward(), intermediate.inward())
                )
        ).with(onInterrupt(() -> blocker.close().start()));
    }

    public Command cornerShot() {
        return parallel(
                loop(
                        lazy(() -> hood.setPosition(Hood.cornerLaunchPosition)).requiring(hood)
                ),
                flywheel.setRPM(() -> Flywheel.cornerRPM + Flywheel.padding),
                sequential(
                        parallel(
                                waitMs(charge),
                                intermediate.outward().raceWith(waitMs(reverseTime))
                        ),
                        blocker.open(),
                        waitMs(servoWait),
                        parallel(intake.inward(), intermediate.inward())
                )
        ).with(onInterrupt(() -> blocker.close().start()));
    }

    public Command calculatedShot(DoubleSupplier rpm, DoubleSupplier servolaunchPos) {
        return parallel(
                loop(
                        lazy(() -> hood.setPosition(servolaunchPos.getAsDouble())).requiring(hood)
                ),
                flywheel.setRPM(rpm),
                sequential(
                        parallel(
                                waitMs(charge),
                                intermediate.outward().raceWith(waitMs(reverseTime))
                        ),
                        blocker.open(),
                        waitMs(servoWait),
                        parallel(intake.inward(), intermediate.inward())
                )
        ).with(onInterrupt(() -> blocker.close().start()));
    }

    public Command eject() {
        return sequential(
                blocker.open(),
                waitMs(servoWait),
                parallel(flywheel.rollIn(), intermediate.outward())
        ).with(onInterrupt(() -> blocker.close().start()));
    }

    // ==================== Calculations ====================
    public double[] calculateLaunch(double distance) {
        // scaled up to correct rpm
        double servoLaunchPos = 0.48;
        // 20 * (-47.02952 + 53.45873 * Math.log(distance))
        return new double[] {Flywheel.manualRPM, servoLaunchPos};
    }

    // ==================== Initialization ====================
    public Command init() {
        return parallel(
               hood.setPosition(Hood.servoLaunchPosition),
               blocker.close()
        );
    }

    // ==================== Returning Mechanisms ====================
    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(drivetrain, intermediate, intake, flywheel, blocker, hood);
    }
}
