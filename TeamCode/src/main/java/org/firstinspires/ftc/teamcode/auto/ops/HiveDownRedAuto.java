package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveDownPaths;
import org.firstinspires.ftc.teamcode.auto.routines.HiveDownRoutines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Hive Down Red Auto")
public class HiveDownRedAuto extends NextOpMode {
    private final Follower follower;
    private final HiveDownRoutines routines;
    private final HiveDownPaths paths;
    private final Alliance alliance = Alliance.RED;

    // The Pinpoint keeps its pose between runs and recalibrates its gyro when the follower is created,
    // so the start pose is set again once that calibration has had time to finish.
    private static final long CALIBRATION_MS = 500;
    private final long initTimeMs;
    private boolean startPoseReapplied = false;

    public HiveDownRedAuto(Robot robot) {
        super(robot);
        Scheduler.reset();

        robot.createFollower();
        follower = robot.getFollower();
        paths = new HiveDownPaths(alliance);
        resetToStartPose();   // follower holds the start pose from init, not just after start
        initTimeMs = System.currentTimeMillis();
        routines = new HiveDownRoutines(robot, paths, new AutoCommands(follower));
    }

    // Start every run fresh: clear whatever pose the previous run left on the Pinpoint.
    private void resetToStartPose() {
        follower.setPose(paths.getStartPose());
    }

    @Override
    public void start() {
        resetToStartPose();
        //schedule(routines.test());
        schedule(routines.autoPathSimplified());
    }

    @Override
    public void periodic() {
        follower.update();

        Pose pose = follower.pose();
        telemetry.addData("Heading", "%.1f°", Math.toDegrees(pose.heading()));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", pose.x(), pose.y(), Math.toDegrees(pose.heading()));
        telemetry.update();
    }

    @Override
    public void disabledPeriodic() {
        if (!startPoseReapplied && System.currentTimeMillis() - initTimeMs > CALIBRATION_MS) {
            resetToStartPose();
            startPoseReapplied = true;
        }

        // Show the heading during init so it can be checked before the robot moves.
        follower.localizer.update();
        telemetry.addData("Heading", "%.1f°", Math.toDegrees(follower.pose().heading()));
        telemetry.update();
    }

    @Override
    public void end() {
    }

}
