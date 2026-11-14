package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveDownPaths;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

public class HiveDownRoutines {
    private Robot robot;
    private HiveDownPaths paths;
    private AutoCommands commands;
    private Alliance alliance;
    private PoseTracker poseTracker;

    public HiveDownRoutines(Robot robot, HiveDownPaths paths, AutoCommands commands, Alliance alliance) {
        this.robot = robot;
        this.paths = paths;
        this.commands = commands;
        this.alliance = alliance;
        this.poseTracker = new PoseTracker(robot.getFollower());
    }

    public Command autoPathSimplified() {
        return sequential(
            commands.runPath(paths.launchPath()),
            waitMs(15000),
            robot.manualShot().raceWith(waitMs(10000)),
            commands.runPath(paths.startPath()),
            commands.runPath(paths.endPath())
        );
    }
}
