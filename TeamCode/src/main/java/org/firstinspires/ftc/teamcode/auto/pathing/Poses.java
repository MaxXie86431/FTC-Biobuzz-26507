package org.firstinspires.ftc.teamcode.auto.pathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class Poses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(56, 8, 270);
    public static Pose bottomLaunchPose = factory.of(56, 30, 270);
    public static Pose topLaunchPose = factory.of(56, 115, 90);
    public static Pose bottomCollect1Pose = factory.of(20, 8, 180);
    public static Pose bottomCollect2Pose = factory.of(8, 8, 180);
    public static Pose topCollect1Pose = factory.of(113, 133, 0);
    public static Pose topCollect2Pose = factory.of(133, 133, 0);
    public static Pose bottomCollectToTopLaunchTransition = factory.of(56, 8, 0);
    public static Pose topCollectToBottomLaunchTransition = factory.of(56, 133, 0);
    public static Pose bottomLaunchToEndPoseTransition = factory.of(8, 30, 0);
    public static Pose endPose = factory.of(8, 106, 0);
}
