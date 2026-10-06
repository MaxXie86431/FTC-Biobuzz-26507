package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.auto.paths.PathsAndPoses;
import org.firstinspires.ftc.teamcode.auto.routines.Routines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Red Autonomous")
public class RedAuto extends NextOpMode {
    private final Follower follower;
    private final Routines routines;
    private final PathsAndPoses paths;

    public RedAuto(Robot robot) {
        super(robot);
        Scheduler.reset();

        robot.createFollower();
        follower = robot.getFollower();
        paths = new PathsAndPoses(Alliance.RED);
        routines = new Routines(robot, paths, new AutoCommands(follower));
    }

    @Override
    public void start() {
        follower.setPose(PathsAndPoses.startPose);
        schedule(routines.test());
    }

    @Override
    public void periodic() {
        follower.update();
    }

    @Override
    public void end() {
    }

}
