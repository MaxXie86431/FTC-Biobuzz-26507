package org.firstinspires.ftc.teamcode.auto.pathing.paths;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.auto.pathing.poses.HiveDownPoses;
import org.firstinspires.ftc.teamcode.utils.Alliance;

public class HiveDownPaths {
    private PoseFactory allianceFactory;
    private Pose startPose;
    private Pose launchPose;
    private Pose endPose;

    public HiveDownPaths(Alliance alliance){
        if (alliance == Alliance.BLUE) {
            allianceFactory = HiveDownPoses.factory.mirrorX(72).mirrorY(72);
        }
        else {
            allianceFactory = HiveDownPoses.factory;
        }
        initPoses();
    }

    private Pose assign(Pose pose) {
        return allianceFactory.of(pose.x(), pose.y(), Math.toDegrees(pose.heading()));
    }
    private void initPoses() {
        startPose = assign(HiveDownPoses.startPose);
        launchPose = assign(HiveDownPoses.launchPose);
        endPose = assign(HiveDownPoses.endPose);
    }

    public Path startPath(){
        return line(startPose, launchPose).linear(startPose, launchPose);
    }
    public Path endPath(){
        return line(launchPose, endPose).linear(launchPose, endPose);
    }

    public Pose getStartPose() {
        return startPose;
    }
}
