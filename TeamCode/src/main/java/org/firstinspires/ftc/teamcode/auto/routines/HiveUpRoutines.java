package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.deadline;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveUpPaths;

import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

public class HiveUpRoutines {
    private Robot robot;
    private HiveUpPaths paths;
    private AutoCommands commands;

    public HiveUpRoutines(Robot robot, HiveUpPaths bottomPaths, AutoCommands commands) {
        this.robot = robot;
        this.paths = bottomPaths;
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

    /*
    public Command autoPath(boolean isBottom) {
        if (isBottom) {
            return sequential(
                commands.runPath(paths.startShootPath(isBottom)),
                robot.getFlywheel().shoot(4, 1000),
                commands.runPath(paths.shootCollectPath(isBottom)),
                robot.getIntake().intake(),
                waitMs(100),
                commands.runPath(paths.collect1and2Path(isBottom)),
                robot.getIntake().stop(),
                commands.runPath(paths.collectShootTransPath(isBottom)),
                commands.runPath(paths.collectShootPath(isBottom)),
                robot.getFlywheel().shoot(4, 1000),
                commands.runPath(paths.bottomShootLoadingTransPath()),
                commands.runPath(paths.shootLoadingPath(isBottom))
            );
        } else {
            return sequential(
                commands.runPath(paths.startShootPath(isBottom)),
                robot.getFlywheel().shoot(4, 1000),
                commands.runPath(paths.shootCollectPath(isBottom)),
                robot.getIntake().intake(),
                waitMs(100),
                commands.runPath(paths.collect1and2Path(isBottom)),
                robot.getIntake().stop(),
                commands.runPath(paths.collectShootTransPath(isBottom)),
                commands.runPath(paths.collectShootPath(isBottom)),
                robot.getFlywheel().shoot(4, 1000),
                commands.runPath(paths.shootLoadingPath(isBottom))
            );
        }
    }
     */

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
