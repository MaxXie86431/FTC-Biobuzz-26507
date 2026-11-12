package org.firstinspires.ftc.teamcode.auto.pathing.poses;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class HiveDownPoses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(59.5, 133.5, 90);
    public static Pose launchPose = factory.of(59.5, 117.92, 90);
    public static Pose endPose = factory.of(12, 120, 90);
}
