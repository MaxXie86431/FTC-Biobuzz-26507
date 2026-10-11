package org.firstinspires.ftc.teamcode.auto.pathing;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.utils.Alliance;

public class Paths {
    private PoseFactory allianceFactory;
    private Pose startPose;
    private Pose leavePose;

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
        leavePose = assign(Poses.leavePose);
    }
    public Path startPath(){
        return line(startPose,leavePose).tangent();
    }

    public Pose getStartPose() {
        return startPose;
    }
}
