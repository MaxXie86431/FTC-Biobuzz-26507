package org.firstinspires.ftc.teamcode.auto.pathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class Poses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(55,8,0);
    public static Pose leavePose = factory.of(55,58,0);
}
