package org.firstinspires.ftc.teamcode.auto.pathing;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.Alliance;

public class Paths {
    private PoseFactory allianceFactory;
    private Pose startPose;
    private Pose bottomLaunchPose;
    private Pose topLaunchPose;
    private Pose bottomCollect1Pose;
    private Pose bottomCollect2Pose;
    private Pose topCollect1Pose;
    private Pose topCollect2Pose;
    private Pose endPose;

    public Paths(Alliance alliance){
        if (alliance == Alliance.BLUE) {
            allianceFactory = Poses.factory.mirrorX(72).mirrorY(72);
        }
        else {
            allianceFactory = Poses.factory;
        }
        initPoses();
    }

    private Pose assign(Pose pose) {
        return allianceFactory.of(pose.x(), pose.y(), Math.toDegrees(pose.heading()));
    }
    private void initPoses() {
        startPose = assign(Poses.startPose);
        bottomLaunchPose = assign(Poses.bottomLaunchPose);
        topLaunchPose = assign(Poses.topLaunchPose);
        bottomCollect1Pose = assign(Poses.bottomCollect1Pose);
        bottomCollect2Pose = assign(Poses.bottomCollect2Pose);
        topCollect1Pose = assign(Poses.topCollect1Pose);
        topCollect2Pose = assign(Poses.topCollect2Pose);
        endPose = assign(Poses.endPose);


    }

    /*
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(56, 8, 90);
    public static Pose bottomLaunchPose = factory.of(56, 80, 90);
    public static Pose topLaunchPose = factory.of(56, 110, 270);
    public static Pose bottomCollect1Pose = factory.of(20, 8, 180);
    public static Pose bottomCollect2Pose = factory.of(8, 8, 180);
    public static Pose topCollect1Pose = factory.of(113, 133, 0);
    public static Pose topCollect2Pose = factory.of(133, 133, 0);
    public static Pose endPose = factory.of(8, 106, 0);
    */

    public Path startShootPath(boolean isBottom){
        if (isBottom) {
            return line(startPose, bottomLaunchPose).linear(startPose, bottomLaunchPose);
        }
        return line(startPose, topLaunchPose).linear(startPose, topLaunchPose);
    }

    public Path shootCollectPath(boolean isBottom){
        if (isBottom) {
            return line(bottomLaunchPose, bottomCollect1Pose).linear(bottomLaunchPose, bottomCollect1Pose);
        }
        return line(topLaunchPose, topCollect1Pose).linear(topLaunchPose, topCollect1Pose);
    }

    public Path collect1and2Path(boolean isBottom) {
        if (isBottom) {
            return line(bottomCollect1Pose, bottomCollect2Pose).linear(bottomCollect1Pose, bottomCollect2Pose);
        }
        return line(topCollect1Pose, topCollect2Pose).linear(topCollect1Pose, topCollect2Pose);
    }

    public Path collectShootPath(boolean isBottom){
        if (isBottom) {
            return line(bottomCollect2Pose, topLaunchPose).linear(bottomCollect2Pose, topLaunchPose);
        }
        return line(topCollect2Pose, bottomLaunchPose).linear(topCollect2Pose, bottomLaunchPose);
    }

    public Path shootLoadingPath(boolean isBottom){
        if (isBottom) {
            return line(bottomLaunchPose, endPose).linear(bottomLaunchPose, endPose);
        }
        return line(topLaunchPose, endPose).linear(topLaunchPose, endPose);
    }

    public Pose getStartPose() {
        return startPose;
    }
}
