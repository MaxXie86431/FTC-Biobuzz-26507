package org.firstinspires.ftc.teamcode.auto.pathing.poses;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class HiveUpPoses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(59.5, 8, 270);
    public static Pose intermediatePose = factory.of(30, 30, 270);
    public static Pose endPose = factory.of(12, 90, 270);
}
