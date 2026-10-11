package org.firstinspires.ftc.teamcode.auto.pathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

@Configurable
public class Poses {
    public static PoseFactory factory = PoseFactory.degrees();
    public static Pose startPose = factory.of(0,0,0);
    public static Pose leavePose = factory.of(0,50,0);
}
