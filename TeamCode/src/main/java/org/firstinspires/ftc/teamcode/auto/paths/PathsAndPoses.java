package org.firstinspires.ftc.teamcode.auto.paths;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.api.Paths;

import org.firstinspires.ftc.teamcode.utils.Alliance;

@Configurable
public class PathsAndPoses {
    private final PoseFactory poseFactory = PoseFactory.degrees();
    public PathsAndPoses(Alliance alliance) {
        if (alliance == Alliance.BLUE) {
            poseFactory.mirrorX(72.0);
            poseFactory.mirrorY(72.0);
        }

    }


    public Pose startPose = poseFactory.of(56,8,90);
    public Pose endPose = poseFactory.of(56, 20, 90);

    public Path startPath() {
        return Paths.line(startPose, endPose).constant(startPose);
    }
}
