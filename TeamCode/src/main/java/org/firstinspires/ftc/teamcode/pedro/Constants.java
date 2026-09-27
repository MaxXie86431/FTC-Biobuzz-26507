package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Robot-specific Pedro Pathing setup.
 *
 * <p>The hardware configuration, localizer, and tuned follower constants have not been
 * established for this robot yet. Keep this factory fail-closed until those values are
 * measured and documented; a placeholder follower could command unsafe motion.</p>
 */
public final class Constants {

    private Constants() {
    }

    public static Follower create(HardwareMap hardwareMap) {
        throw new IllegalStateException(
                "Pedro Pathing is not configured: confirm Robot Controller device names, "
                        + "select and configure the localizer, and measure robot-specific "
                        + "follower constants before enabling autonomous motion."
        );
    }
}
