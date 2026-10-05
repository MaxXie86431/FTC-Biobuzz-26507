package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.auto.paths.PathsAndPoses;
import org.firstinspires.ftc.teamcode.auto.routines.Routines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Red Autonomous")
public class RedAuto extends NextOpMode {
    private Robot robot;
    private Routines routines;
    private AutoCommands commands;
    private PathsAndPoses paths;
    private boolean logFirstHeading;

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
        logFirstHeading = true;
        schedule(routines.test());
    }

    @Override
    public void periodic() {
        robot.getFollower().update();

        double targetHeading = Math.toDegrees(paths.startPose.heading());
        double measuredHeading = Math.toDegrees(robot.getFollower().pose().heading());
        double headingError = Math.IEEEremainder(targetHeading - measuredHeading, 360.0);
        telemetry.addData("Target heading (deg)", "%.1f", targetHeading);
        telemetry.addData("Measured heading (deg)", "%.1f", measuredHeading);
        telemetry.addData("Heading error (deg)", "%.1f", headingError);
        telemetry.update();

        if (logFirstHeading) {
            RobotLog.ii("RedAuto", "First sensor update: target=%.1f deg, measured=%.1f deg, error=%.1f deg",
                    targetHeading, measuredHeading, headingError);
            logFirstHeading = false;
        }
    }

    @Override
    public void end() {
        Telemetry.log("Auto Finished");
        Telemetry.update();
    }

}
