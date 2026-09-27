package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.PoseFactory;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

@Autonomous(name = "Blue Forward Test")
public class Blue extends OpMode {

    private Follower follower;

    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(0, 0, 0);

    private final Pose endPose = p.of(24, 0, 0);

    private Path forwardPath() {
        return line(startPose, endPose)
                .linear(startPose, endPose);
    }

    @Override
    public void init() {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);

        follower.setPose(startPose);
        follower.update();
    }

    @Override
    public void start() {
        schedule(
                follow(follower, forwardPath())
        );
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.update();
    }
}