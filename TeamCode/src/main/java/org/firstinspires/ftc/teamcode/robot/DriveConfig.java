package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Provisional drive hardware map. Verify every entry on the Robot Controller before enabling it.
 */
public final class DriveConfig {
    /** Change only after the Driver Station configuration and physical motor directions are checked. */
    public static final boolean HARDWARE_CONFIRMED = false;

    // Historical names and directions copied from the older op-robot repository; unverified here.
    public static final String FRONT_LEFT_NAME = "Top-Left-Motor";
    public static final String FRONT_RIGHT_NAME = "Top-Right-Motor";
    public static final String BACK_LEFT_NAME = "Bottom-Left-Motor";
    public static final String BACK_RIGHT_NAME = "Bottom-Right-Motor";
    public static final String PINPOINT_NAME = "pinpoint"; // Historical name; verify before enabling.

    public static final DcMotorSimple.Direction FRONT_LEFT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction FRONT_RIGHT_DIRECTION = DcMotorSimple.Direction.REVERSE;
    public static final DcMotorSimple.Direction BACK_LEFT_DIRECTION = DcMotorSimple.Direction.FORWARD;
    public static final DcMotorSimple.Direction BACK_RIGHT_DIRECTION = DcMotorSimple.Direction.REVERSE;

    public static final double SLOW_SPEED = 0.3;

    private DriveConfig() {
    }
}
