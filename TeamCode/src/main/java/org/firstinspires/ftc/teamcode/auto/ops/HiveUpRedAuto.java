package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveUpPaths;
import org.firstinspires.ftc.teamcode.auto.routines.HiveUpRoutines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Hive Up Red Auto")
public class HiveUpRedAuto extends NextOpMode {
    private final Follower follower;
    private final HiveUpRoutines routines;
    private final HiveUpPaths paths;
    private final Alliance alliance = Alliance.RED;
    private boolean initialized;
    private final Robot robot;


    public HiveUpRedAuto(Robot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset();

        robot.createFollower();
        follower = robot.getFollower();
        paths = new HiveUpPaths(alliance);
        routines = new HiveUpRoutines(robot, paths, new AutoCommands(follower));
    }

    @Override
    public void start() {
        follower.setPose(paths.getStartPose());
        PoseTracker.saveLastPose(follower.pose());
        schedule(routines.autoPathSimplified());
    }

    @Override
    public void periodic() {
        follower.update();

        Pose pose = follower.pose();
        PoseTracker.saveLastPose(pose);
        telemetry.addData("Heading", "%.1f°", Math.toDegrees(pose.heading()));
        telemetry.addData("Pose", "x: %.1f, y: %.1f, h: %.1f°", pose.x(), pose.y(), Math.toDegrees(pose.heading()));
        telemetry.update();
    }

    @Override
    public void disabledPeriodic() {
        if (!initialized) {
            robot.init().schedule();
            Scheduler.execute();
            initialized = true;
        }
    }

    @Override
    public void end() {
        follower.localizer.update();
        PoseTracker.saveLastPose(follower.pose());
    }

}
