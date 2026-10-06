package org.firstinspires.ftc.teamcode.auto.paths;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.api.Paths;

import org.firstinspires.ftc.teamcode.utils.Alliance;

@Configurable
public class PathsAndPoses {
    PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = PoseFactory.degrees().of(56, 8, 90);
    public static Pose endPose = PoseFactory.degrees().of(56, 80, 90);


    public PathsAndPoses(Alliance alliance) {
        if (alliance == Alliance.BLUE) {
            factory.mirrorX(72.0).mirrorY(72.0);
        }
    }


    public Path startPath() {
        return Paths.line(startPose, endPose).linear(startPose, endPose);
    }
}
