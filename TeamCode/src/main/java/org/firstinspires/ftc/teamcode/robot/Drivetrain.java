package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ivy.Command;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import dev.nextftc.robot.Mechanism;

/** Mecanum drive mechanism with an explicit hardware-confirmation gate. */
public final class Drivetrain implements Mechanism {
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backLeft;
    private DcMotorEx backRight;
    private GoBildaPinpointDriver pinpoint;

    private boolean hardwareInitialized;
    private boolean headingValid;
    private boolean fieldReferenceValid;
    private boolean motorFaulted;
    private double headingRadians = Double.NaN;
    private double fieldForwardHeadingRadians = Double.NaN;
    private String setupStatus = "Hardware setup has not been checked.";
    private String pinpointStatus = "Not sampled";
    private String runtimeStatus = "Waiting for PLAY.";

    /** The root robot is hardware-free; NextFTC's automatic default command does no work. */
    @Override
    public Command getDefaultCommand() {
        return Command.NOOP;
    }

    /**
     * Looks up and configures hardware only after the source-level confirmation gate is enabled.
     * Call from the OpMode constructor, after NextFTC has supplied its hardware map.
     */
    public boolean initialize(HardwareMap hardwareMap) {
        if (!DriveConfig.HARDWARE_CONFIRMED) {
            setupStatus = "Setup required: verify device names and wheel directions before enabling drive.";
            return false;
        }
        if (hardwareInitialized) {
            return true;
        }
        if (hardwareMap == null) {
            setupStatus = "Setup failed: FTC hardware map is unavailable.";
            return false;
        }

        try {
            frontLeft = hardwareMap.get(DcMotorEx.class, DriveConfig.FRONT_LEFT_NAME);
            frontRight = hardwareMap.get(DcMotorEx.class, DriveConfig.FRONT_RIGHT_NAME);
            backLeft = hardwareMap.get(DcMotorEx.class, DriveConfig.BACK_LEFT_NAME);
            backRight = hardwareMap.get(DcMotorEx.class, DriveConfig.BACK_RIGHT_NAME);

            requireSuccessfulStop("zero the drive motors");
            configureMotor(frontLeft, DriveConfig.FRONT_LEFT_DIRECTION);
            configureMotor(frontRight, DriveConfig.FRONT_RIGHT_DIRECTION);
            configureMotor(backLeft, DriveConfig.BACK_LEFT_DIRECTION);
            configureMotor(backRight, DriveConfig.BACK_RIGHT_DIRECTION);

            pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, DriveConfig.PINPOINT_NAME);
            hardwareInitialized = true;
            setupStatus = "Drive hardware initialized from the confirmed configuration.";
            runtimeStatus = "Waiting for a READY Pinpoint heading.";
            return true;
        } catch (RuntimeException error) {
            RuntimeException stopError = zeroAvailableMotors();
            setupStatus = "Setup failed: " + describe(error);
            if (stopError != null) {
                setupStatus += "; motor zeroing also failed: " + describe(stopError);
            }
            clearHardwareReferences();
            return false;
        }
    }

    private static void configureMotor(
            DcMotorEx motor,
            com.qualcomm.robotcore.hardware.DcMotorSimple.Direction direction
    ) {
        motor.setDirection(direction);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void requireSuccessfulStop(String operation) {
        RuntimeException error = zeroAvailableMotors();
        if (error != null) {
            throw new IllegalStateException("Could not " + operation + ": " + describe(error), error);
        }
    }

    private void clearHardwareReferences() {
        frontLeft = null;
        frontRight = null;
        backLeft = null;
        backRight = null;
        pinpoint = null;
        hardwareInitialized = false;
        headingValid = false;
        fieldReferenceValid = false;
    }

    @Override
    public void periodic() {
        if (!DriveConfig.HARDWARE_CONFIRMED || !hardwareInitialized) {
            return;
        }
        refreshHeading();
    }

    /** Refreshes Pinpoint without resetting its pose, pods, offsets, or IMU calibration. */
    public boolean refreshHeading() {
        if (!DriveConfig.HARDWARE_CONFIRMED || !hardwareInitialized) {
            return false;
        }

        try {
            pinpoint.update();
            GoBildaPinpointDriver.DeviceStatus status = pinpoint.getDeviceStatus();
            pinpointStatus = status == null ? "UNKNOWN" : status.name();
            double measuredHeading = pinpoint.getHeading(AngleUnit.RADIANS);

            if (status == GoBildaPinpointDriver.DeviceStatus.READY && isFinite(measuredHeading)) {
                headingRadians = measuredHeading;
                headingValid = true;
                return true;
            }

            headingRadians = Double.NaN;
            headingValid = false;
            fieldReferenceValid = false;
            runtimeStatus = "Stopped: Pinpoint is not READY or its heading is invalid.";
            stopMotors();
            return false;
        } catch (RuntimeException error) {
            pinpointStatus = "ERROR: " + describe(error);
            headingRadians = Double.NaN;
            headingValid = false;
            fieldReferenceValid = false;
            runtimeStatus = "Stopped: Pinpoint read failed.";
            stopMotors();
            return false;
        }
    }

    /** Captures the current sensor heading as the driver's chosen field-forward direction. */
    public boolean captureFieldForwardReference() {
        if (!DriveConfig.HARDWARE_CONFIRMED || !hardwareInitialized || !refreshHeading()) {
            fieldReferenceValid = false;
            return false;
        }
        fieldForwardHeadingRadians = headingRadians;
        fieldReferenceValid = true;
        runtimeStatus = "Field-forward reference is set.";
        return true;
    }

    /** Applies field-centric powers from raw gamepad axes, if every safety gate is ready. */
    public void drive(double leftStickY, double leftStickX, double rightStickX, double speedScalar) {
        if (!DriveConfig.HARDWARE_CONFIRMED || !hardwareInitialized) {
            return;
        }
        if (motorFaulted) {
            stopMotors();
            return;
        }
        if (!headingValid || !fieldReferenceValid) {
            stopMotors();
            return;
        }

        double relativeHeading = headingRadians - fieldForwardHeadingRadians;
        if (!isFinite(relativeHeading)) {
            fieldReferenceValid = false;
            runtimeStatus = "Stopped: heading difference is invalid; press Back after recovery.";
            stopMotors();
            return;
        }

        FieldCentricDrive.WheelPowers powers = FieldCentricDrive.calculate(
                leftStickY,
                leftStickX,
                rightStickX,
                relativeHeading,
                speedScalar);
        try {
            frontLeft.setPower(powers.frontLeft);
            frontRight.setPower(powers.frontRight);
            backLeft.setPower(powers.backLeft);
            backRight.setPower(powers.backRight);
        } catch (RuntimeException error) {
            motorFaulted = true;
            fieldReferenceValid = false;
            runtimeStatus = "Stopped: motor write failed; restart this OpMode after checking hardware.";
            stopMotors();
        }
    }

    /** Immediately requests zero power from all configured drive motors. */
    public void stop() {
        if (!DriveConfig.HARDWARE_CONFIRMED || !hardwareInitialized) {
            return;
        }
        stopMotors();
    }

    private void stopMotors() {
        RuntimeException error = zeroAvailableMotors();
        if (error != null) {
            motorFaulted = true;
            runtimeStatus = "Motor zeroing failed: " + describe(error);
        }
    }

    private RuntimeException zeroAvailableMotors() {
        RuntimeException firstError = null;
        firstError = zeroMotor(frontLeft, firstError);
        firstError = zeroMotor(frontRight, firstError);
        firstError = zeroMotor(backLeft, firstError);
        firstError = zeroMotor(backRight, firstError);
        return firstError;
    }

    private static RuntimeException zeroMotor(DcMotorEx motor, RuntimeException firstError) {
        if (motor == null) {
            return firstError;
        }
        try {
            motor.setPower(0.0);
        } catch (RuntimeException error) {
            return firstError == null ? error : firstError;
        }
        return firstError;
    }

    public String getSetupStatus() {
        return setupStatus;
    }

    public String getPinpointStatus() {
        return pinpointStatus;
    }

    public boolean isHeadingValid() {
        return headingValid;
    }

    public double getHeadingDegrees() {
        return Math.toDegrees(headingRadians);
    }

    public String getModeStatus() {
        if (!DriveConfig.HARDWARE_CONFIRMED) {
            return "Disabled: hardware setup is unconfirmed.";
        }
        if (!hardwareInitialized) {
            return "Disabled: drive hardware is not initialized.";
        }
        if (motorFaulted) {
            return "Stopped: motor fault; restart after checking hardware.";
        }
        if (!headingValid) {
            return "Stopped: wait for Pinpoint READY, then press Back.";
        }
        if (!fieldReferenceValid) {
            return "Stopped: press Back once to set field-forward.";
        }
        return runtimeStatus;
    }

    private static boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    private static String describe(RuntimeException error) {
        String message = error.getMessage();
        return message == null || message.trim().isEmpty()
                ? error.getClass().getSimpleName()
                : message;
    }
}
