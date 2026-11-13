package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static MecanumConfig drivetrainConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set("FrontLeft");
                c.frontRightName.set("FrontRight");
                c.backLeftName.set("BackLeft");
                c.backRightName.set("BackRight");
                c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);

            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(7.6);
        c.yPodOffset.set(-7.25);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });



    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.21789571611221675);
                Controller secondaryTranslationalForward = Controller.proportional(0.0805066448226991);
                Controller primaryTranslationalLateral = Controller.proportional(0.2902791947795649);
                Controller secondaryTranslationalLateral = Controller.proportional(0.10725040606811301);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016525540155628755));
                c.brake.set(Controller.proportionalFeedforward(0.014046709132284441));

                c.headingFeedback.set(Controller.proportional(3.205466812322602));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05201880814939746, 0.004521283564997829));

                c.linearBrakeCoefficients.set(Matrix.diag(0.1197065939849299, 0.07010116444663189));
                c.quadraticBrakeCoefficients.set(Matrix.diag(-8.466844353762018E-5, 0.0012318076269985769));

                c.maxAchievableForwardVelocity.set(66.15643578453103);
                c.maxAchievableStrafeVelocity.set(55.03019937348296);
                c.naturalForwardDeceleration.set(48.49826987405933);
                c.naturalStrafeDeceleration.set(84.92663066503118);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),
                new Mecanum(h, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }
}