package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
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
                robot.getIntake().intake()
            ),
            robot.getIntake().stop()
        );
    }*/

    public Command autoPath(boolean isBottom) {
        return sequential(
            commands.runPath(paths.startShootPath(isBottom)),
            robot.getFlywheel().shoot(4, 1000),
            commands.runPath(paths.shootCollectPath(isBottom)),
            robot.getIntake().intake(),
            waitMs(100),
            commands.runPath(paths.collect1and2Path(isBottom)),
            robot.getIntake().stop(),
            commands.runPath(paths.collectShootPath(isBottom)),
            robot.getFlywheel().shoot(4, 1000),
            commands.runPath(paths.shootLoadingPath(isBottom))
        );
    }
}
