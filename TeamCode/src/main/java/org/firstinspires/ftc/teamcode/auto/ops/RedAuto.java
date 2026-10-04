package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;

import org.firstinspires.ftc.teamcode.auto.paths.PathsAndPoses;
import org.firstinspires.ftc.teamcode.auto.routines.Routines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

import java.nio.file.Path;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Red Autonomous")
public class RedAuto extends NextOpMode {
    private Robot robot;
    private Routines routines;
    private AutoCommands commands;
    private PathsAndPoses paths;

    public RedAuto(Robot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset();

        paths = new PathsAndPoses(Alliance.RED);
        commands = new AutoCommands(robot.getFollower());
        routines = new Routines(robot, paths, commands);
    }

    @Override
    public void start() {
        robot.getFollower().setPose(paths.startPose);
        schedule(routines.test());
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        Scheduler.execute();
    }

}
