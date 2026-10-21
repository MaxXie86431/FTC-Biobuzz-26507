package org.firstinspires.ftc.teamcode.utils;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class PoseTracker {

    private static Pose lastPose = new Pose(0, 0, 0);
    private final Follower follower;
    public PoseTracker(Follower follower) {
        this.follower = follower;
    }

    public static void saveLastPose(Pose pose) {
        lastPose = pose;
    }

    // Keep the latest measured pose across Auto and TeleOp runs.
    public static Pose getLastPose() {
        return lastPose;
    }

    public void update() {
        follower.localizer.update();
    }

    public Pose getPosition() {
        return follower.pose();
    }

    public double getX() {
        return follower.pose().x();
    }

    public double getY() {
        return follower.pose().y();
    }

    public double getHeading() {
        return follower.pose().heading();
    }

    // goal centers (inches): red goals at (58, 48.5) and (58, 93), blue goals at (83.5, 48.5) and (83.5, 93)
    private static final double RED_GOAL_X = 58;
    private static final double BLUE_GOAL_X = 83.5;
    private static final double NEAR_GOAL_Y = 48.5;
    private static final double FAR_GOAL_Y = 93;

    // shortest distance from the robot to the closest goal of the given alliance
    public double distanceToGoal(Alliance alliance) {
        double goalX;
        if (alliance == Alliance.RED) {
            goalX = RED_GOAL_X;
        } else {
            goalX = BLUE_GOAL_X;
        }
        double distanceToNear = distance(getX(), getY(), goalX, NEAR_GOAL_Y);
        double distanceToFar = distance(getX(), getY(), goalX, FAR_GOAL_Y);
        return Math.min(distanceToNear, distanceToFar);
    }
    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
