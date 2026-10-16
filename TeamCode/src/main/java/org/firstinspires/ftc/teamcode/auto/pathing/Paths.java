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
    private Pose bottomCollectToTopLaunchTransition;
    private Pose topCollectToBottomLaunchTransition;
    private Pose bottomLaunchToEndPoseTransition;

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
        bottomCollectToTopLaunchTransition = assign(Poses.bottomCollectToTopLaunchTransition);
        topCollectToBottomLaunchTransition = assign(Poses.topCollectToBottomLaunchTransition);
        bottomLaunchToEndPoseTransition = assign(Poses.bottomLaunchToEndPoseTransition);


    }

    /*
    public static Pose bottomCollectToTopLaunchTransition = factory.of(56, 8, 0);
    public static Pose topCollectToBottomLaunchTransition = factory.of(56, 133, 0);
    public static Pose bottomLaunchToEndPoseTransition = factory.of(8, 80, 0);
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
    public Path collectShootTransPath(boolean isBottom){
        if (isBottom) {
            return line(bottomCollect2Pose, bottomCollectToTopLaunchTransition).linear(bottomCollect2Pose, bottomCollectToTopLaunchTransition);
        }
        return line(topCollect2Pose, topCollectToBottomLaunchTransition).linear(topCollect2Pose, topCollectToBottomLaunchTransition);
    }
    public Path collectShootPath(boolean isBottom){
        if (isBottom) {
            return line(bottomCollectToTopLaunchTransition, topLaunchPose).linear(bottomCollectToTopLaunchTransition, topLaunchPose);
        }
        return line(topCollectToBottomLaunchTransition, bottomLaunchPose).linear(topCollectToBottomLaunchTransition, bottomLaunchPose);
    }

    public Path bottomShootLoadingTransPath() {
        return line(bottomLaunchPose, bottomLaunchToEndPoseTransition).linear(bottomLaunchPose, bottomLaunchToEndPoseTransition);
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
