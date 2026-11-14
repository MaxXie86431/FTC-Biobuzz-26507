package org.firstinspires.ftc.teamcode.auto.pathing.paths;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.auto.pathing.poses.HiveDownPoses;
import org.firstinspires.ftc.teamcode.utils.Alliance;
import org.firstinspires.ftc.teamcode.utils.PoseTracker;

public class HiveDownPaths {
    private PoseFactory allianceFactory;
    private Pose startPose;
    private Pose launchPose;
    private Pose intermediatePose;
    private Pose endPose;

    public HiveDownPaths(Alliance alliance){
        if (alliance == Alliance.BLUE) {
            allianceFactory = HiveDownPoses.factory.mirrorX(70.75).mirrorY(70.75);
        }
        else {
            allianceFactory = HiveDownPoses.factory;
        }
        initPoses(alliance);
    }

    private Pose assign(Pose pose) {
        return allianceFactory.of(pose.x(), pose.y(), Math.toDegrees(pose.heading()));
    }
    private void initPoses(Alliance alliance) {
        startPose = assign(HiveDownPoses.startPose);
        launchPose = PoseTracker.closerToGoal(startPose, alliance, 2.0);
        intermediatePose = assign(HiveDownPoses.intermediatePose);
        endPose = assign(HiveDownPoses.endPose);
    }

    public Path launchPath() {
        return line(startPose, launchPose).linear(startPose, launchPose);
    }

    public Path startPath() {
        return line(launchPose, intermediatePose).linear(launchPose, intermediatePose);
    }

    public Path endPath() {
        return line(intermediatePose, endPose).linear(intermediatePose, endPose);
    }

    public Pose getStartPose() {
        return startPose;
    }
}
