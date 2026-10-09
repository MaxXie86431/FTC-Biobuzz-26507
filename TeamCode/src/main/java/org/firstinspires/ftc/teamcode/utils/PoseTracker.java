package org.firstinspires.ftc.teamcode.utils;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
//dimension for the center of red bucket facing us (orientation: left, blue): 54.25 inches up from bottom edge, 58.5 inches right from left edge
public class PoseTracker {

    private final Follower follower;

    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(0, 0, 0);

    public PoseTracker(Follower follower) {
        this.follower = follower;
        follower.setPose(startPose);
    }

    public void update() {
        // only update odometry; follower.update() would stop the drive motors when no path is running
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

    // shortest distance from the robot to the closest bucket of the given alliance
    public double distanceToBucket(Alliance alliance) {
        // red buckets are on the left (x = 58.5), blue buckets are mirrored to the right (x = 144 - 58.5)
        double bucketX;
        if (alliance == Alliance.RED) {
            bucketX = 58.5;
        } else {
            bucketX = 144 - 58.5;
        }
        double nearBucketY = 54.25;
        double farBucketY = 144 - 54.25;
        double distanceToNear = distance(getX(), getY(), bucketX, nearBucketY);
        double distanceToFar = distance(getX(), getY(), bucketX, farBucketY);
        return Math.min(distanceToNear, distanceToFar);
    }
    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
