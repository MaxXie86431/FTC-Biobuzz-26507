package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveUpPaths;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

public class HiveUpRoutines {
    private Robot robot;
    private HiveUpPaths paths;
    private AutoCommands commands;
    private Alliance alliance;

    public HiveUpRoutines(Robot robot, HiveUpPaths bottomPaths, AutoCommands commands, Alliance alliance) {
        this.robot = robot;
        this.paths = bottomPaths;
        this.commands = commands;
        this.alliance = alliance;
    }

    public Command autoPathSimplified() {
        return sequential(
            commands.runPath(paths.startPath()),
            deadline(waitMs(10000), robot.shoot()),
            robot.stop(),
            commands.runPath(paths.intermediatePath()),
            commands.runPath(paths.endPath())
        );
    }
}
