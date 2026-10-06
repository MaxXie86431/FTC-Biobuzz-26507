package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.paths.PathsAndPoses;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

public class Routines {
    private Robot robot;
    private PathsAndPoses paths;
    private AutoCommands commands;

    public Routines(Robot robot, PathsAndPoses paths, AutoCommands commands) {
        this.robot = robot;
        this.paths = paths;
        this.commands = commands;
    }

    public Command test() {
        return sequential(
            deadline(
                commands.runPath(paths.startPath()),
                robot.getIntake().intake()
            ),
            robot.getIntake().stop()
        );
    }
}
