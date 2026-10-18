package org.firstinspires.ftc.teamcode.auto.routines;

import static com.pedropathing.ivy.commands.Commands.waitMs;

import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.auto.pathing.paths.HiveDownPaths;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

public class HiveDownRoutines {
    private Robot robot;
    private HiveDownPaths paths;
    private AutoCommands commands;

    public HiveDownRoutines(Robot robot, HiveDownPaths paths, AutoCommands commands) {
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

    /*
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
     */

    public Command autoPathSimplified() {
        return sequential(
            waitMs(15000),
            commands.runPath(paths.startPath()),
            robot.shoot(),
            commands.runPath(paths.endPath())
        );
    }
}
