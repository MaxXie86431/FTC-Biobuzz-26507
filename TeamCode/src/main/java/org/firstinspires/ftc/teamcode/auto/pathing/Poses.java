package org.firstinspires.ftc.teamcode.auto.pathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class Poses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(56, 8, 90);
    public static Pose bottomLaunchPose = factory.of(56, 80, 90);
    public static Pose topLaunchPose = factory.of(56, 110, 270);
    public static Pose bottomCollect1Pose = factory.of(20, 8, 180);
    public static Pose bottomCollect2Pose = factory.of(8, 8, 180);
    public static Pose topCollect1Pose = factory.of(113, 133, 0);
    public static Pose topCollect2Pose = factory.of(133, 133, 0);
    public static Pose endPose = factory.of(8, 106, 0);
}
