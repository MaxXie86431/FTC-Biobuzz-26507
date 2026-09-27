package org.firstinspires.ftc.teamcode.robot;

/** Pure field-centric mecanum math; this class does not access FTC hardware. */
public final class FieldCentricDrive {
    private static final double TWO_PI = 2.0 * Math.PI;
    private static final WheelPowers ZERO = new WheelPowers(0.0, 0.0, 0.0, 0.0);

    private FieldCentricDrive() {
    }

    /**
     * Converts gamepad axes and robot heading into wheel powers.
     *
     * <p>Conventions: forward is {@code -leftStickY}, right is {@code leftStickX}, and
     * clockwise turn is {@code rightStickX}. Heading is radians, positive counterclockwise
     * from the driver's chosen field-forward direction. A speed scalar is clamped to [0, 1]
     * and applied after all four wheels have been normalized together.</p>
     */
    public static WheelPowers calculate(
            double leftStickY,
            double leftStickX,
            double rightStickX,
            double headingRadians,
            double speedScalar
    ) {
        if (!isFinite(leftStickY)
                || !isFinite(leftStickX)
                || !isFinite(rightStickX)
                || !isFinite(headingRadians)
                || !isFinite(speedScalar)) {
            return ZERO;
        }

        double speed = Math.max(0.0, Math.min(1.0, speedScalar));
        if (speed == 0.0) {
            return ZERO;
        }

        double fieldForward = -leftStickY;
        double fieldRight = leftStickX;
        double clockwiseTurn = rightStickX;

        // Scaling all inputs together preserves their ratios. It keeps unusual finite values
        // bounded; the later wheel normalization removes this common scale when needed.
        double largestInput = Math.max(
                Math.max(Math.abs(fieldForward), Math.abs(fieldRight)),
                Math.abs(clockwiseTurn));
        if (largestInput > 1.0) {
            fieldForward /= largestInput;
            fieldRight /= largestInput;
            clockwiseTurn /= largestInput;
        }

        double wrappedHeading = headingRadians % TWO_PI;
        double cosHeading = Math.cos(wrappedHeading);
        double sinHeading = Math.sin(wrappedHeading);

        double rightRobot = fieldRight * cosHeading + fieldForward * sinHeading;
        double forwardRobot = fieldForward * cosHeading - fieldRight * sinHeading;

        double frontLeft = forwardRobot + rightRobot + clockwiseTurn;
        double frontRight = forwardRobot - rightRobot - clockwiseTurn;
        double backLeft = forwardRobot - rightRobot + clockwiseTurn;
        double backRight = forwardRobot + rightRobot - clockwiseTurn;

        double largestWheel = Math.max(
                Math.max(Math.abs(frontLeft), Math.abs(frontRight)),
                Math.max(Math.abs(backLeft), Math.abs(backRight)));
        if (largestWheel > 1.0) {
            frontLeft /= largestWheel;
            frontRight /= largestWheel;
            backLeft /= largestWheel;
            backRight /= largestWheel;
        }

        // Keep this after normalization so slow mode caps the final motor powers.
        return new WheelPowers(
                frontLeft * speed,
                frontRight * speed,
                backLeft * speed,
                backRight * speed);
    }

    private static boolean isFinite(double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    /** Immutable powers for the four named mecanum wheels. */
    public static final class WheelPowers {
        public final double frontLeft;
        public final double frontRight;
        public final double backLeft;
        public final double backRight;

        private WheelPowers(double frontLeft, double frontRight, double backLeft, double backRight) {
            this.frontLeft = frontLeft;
            this.frontRight = frontRight;
            this.backLeft = backLeft;
            this.backRight = backRight;
        }
    }
}
