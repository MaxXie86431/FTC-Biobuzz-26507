package org.firstinspires.ftc.teamcode.auto.pathing.poses;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class HiveUpPoses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(56, 8, 270);
    public static Pose launchPose = factory.of(56, 23.58, 270);   // 25 in from the near goal (58, 48.5)
    public static Pose intermediatePose = factory.of(24, 30, 270);
    public static Pose endPose = factory.of(12, 106, 270);
}
