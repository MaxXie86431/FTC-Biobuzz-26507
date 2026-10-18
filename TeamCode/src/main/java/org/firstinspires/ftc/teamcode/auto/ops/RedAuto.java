package org.firstinspires.ftc.teamcode.auto.ops;

import static com.pedropathing.ivy.Scheduler.schedule;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.auto.pathing.Paths;
import org.firstinspires.ftc.teamcode.auto.pathing.Poses;
import org.firstinspires.ftc.teamcode.auto.routines.Routines;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.AutoCommands;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name="Red Autonomous")
public class RedAuto extends NextOpMode {
    private final Robot robot;
    private final Follower follower;
    private final Routines routines;
    private final Paths paths;
    private final Alliance alliance = Alliance.RED;
    private boolean initialized;

    public RedAuto(Robot robot) {
        super(robot);
        this.robot = robot;
        Scheduler.reset();

        robot.createFollower();
        follower = robot.getFollower();
        paths = new Paths(alliance);
        follower.setPose(Poses.startPose);
        routines = new Routines(robot, paths, new AutoCommands(follower));

    }

    @Override
    public void disabledPeriodic() {
        if (!initialized) {
            robot.init().schedule();
            Scheduler.execute();
            initialized = true;
        }
    }

    @Override
    public void start() {
        schedule(routines.autoPath(true));
    }

    @Override
    public void periodic() {
        follower.update();
    }

    @Override
    public void end() {
    }

}
