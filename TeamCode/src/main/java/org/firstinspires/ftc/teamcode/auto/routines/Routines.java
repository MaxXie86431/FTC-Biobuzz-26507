package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.repeat;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.pathing.Paths;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

public class Routines {
    private Robot robot;
    private Paths paths;
    private AutoCommands commands;

    public Routines(Robot robot, Paths paths, AutoCommands commands) {
        this.robot = robot;
        this.paths = paths;
        this.commands = commands;
    }

    /*public Command test() {
        return sequential(
            deadline(
                commands.runPath(paths.startPath()),
                robot.intake()
            ),
            robot.stopIntake()
        );
    }*/

    public Command autoPath(boolean isBottom) {
        if (isBottom) {
            return sequential(
                commands.runPath(paths.startShootPath(isBottom)),
                robot.shoot(),
                commands.runPath(paths.shootCollectPath(isBottom)),
                robot.getIntake().intake(),
                waitMs(100),
                commands.runPath(paths.collect1and2Path(isBottom)),
                robot.getIntake().stop(),
                commands.runPath(paths.collectShootTransPath(isBottom)),
                commands.runPath(paths.collectShootPath(isBottom)),
                robot.shoot(),
                commands.runPath(paths.bottomShootLoadingTransPath()),
                commands.runPath(paths.shootLoadingPath(isBottom))
            );
        } else {
            return sequential(
                commands.runPath(paths.startShootPath(isBottom)),
                robot.shoot(),
                commands.runPath(paths.shootCollectPath(isBottom)),
                robot.getIntake().intake(),
                waitMs(100),
                commands.runPath(paths.collect1and2Path(isBottom)),
                robot.getIntake().stop(),
                commands.runPath(paths.collectShootTransPath(isBottom)),
                commands.runPath(paths.collectShootPath(isBottom)),
                robot.shoot(),
                commands.runPath(paths.shootLoadingPath(isBottom))
            );
        }

    }
}
