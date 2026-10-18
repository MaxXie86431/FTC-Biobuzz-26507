package org.firstinspires.ftc.teamcode.auto.pathing.poses;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class HiveDownPoses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(56, 133, 90);
    public static Pose launchPose = factory.of(56, 115, 90);
    public static Pose endPose = factory.of(12, 106, 90);
}
