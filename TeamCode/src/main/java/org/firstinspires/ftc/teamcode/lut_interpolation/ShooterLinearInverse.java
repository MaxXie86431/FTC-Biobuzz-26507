package org.firstinspires.ftc.teamcode.lut_interpolation;
public final class ShooterLinearInverse {

    // From shooter_data.csv: every made shot after the two-motor fix. Angle = hood servo position.
    private static final double[] DISTANCES = {14.57, 14.59, 19.80, 19.82, 22.28, 22.30, 22.33, 24.70, 26.53,
            26.54, 26.57, 28.62, 36.64, 38.58, 42.29, 42.29, 42.32, 49.26, 49.27};
    private static final double[] ANGLES = {0.06, 0.00, 0.16, 0.16, 0.14, 0.12, 0.12, 0.24, 0.12,
            0.16, 0.22, 0.32, 0.54, 0.28, 0.38, 0.38, 0.38, 0.18, 0.56};
    private static final double[] RPMS = {3100, 3160, 2550, 2500, 2590, 2630, 2680, 3030, 2680,
            2720, 2590, 3150, 3110, 2740, 3010, 3010, 3010, 3430, 3010};

    // limits = the range our data covers, so the model never asks for settings we haven't tested
    private static final double MIN_ANGLE = 0.0;
    private static final double MAX_ANGLE = 0.56;
    private static final double MIN_RPM = 2500;
    private static final double MAX_RPM = 3430;

    private static final double PREFERRED_ANGLE = 0.3;
    // hood angle is a servo position (0-1), so step in hundredths
    private static final double ANGLE_STEP = 0.01;

    private final double wAngle;
    private final double wRpm;
    private final double bias;

    public static final class ShotSettings {
        public final double angle;
        public final double rpm;
        public final double predictedDistance;

        public ShotSettings(double angle, double rpm, double predictedDistance) {
            this.angle = angle;
            this.rpm = rpm;
            this.predictedDistance = predictedDistance;
        }
    }

    public ShooterLinearInverse() {
        int n = DISTANCES.length;
        if (n < 3 || ANGLES.length != n || RPMS.length != n) {
            throw new IllegalStateException("Provide at least 3 matched calibration rows");
        }

        double meanAngle = 0.0;
        double meanRpm = 0.0;
        double meanDistance = 0.0;
        for (int i = 0; i < n; i++) {
            if (!Double.isFinite(DISTANCES[i]) || !Double.isFinite(ANGLES[i])
                    || !Double.isFinite(RPMS[i])) {
                throw new IllegalStateException("Calibration data must be finite");
            }
            meanAngle += ANGLES[i];
            meanRpm += RPMS[i];
            meanDistance += DISTANCES[i];
        }
        meanAngle /= n;
        meanRpm /= n;
        meanDistance /= n;

        double aa = 0.0;
        double ar = 0.0;
        double rr = 0.0;
        double ad = 0.0;
        double rd = 0.0;
        for (int i = 0; i < n; i++) {
            double a = ANGLES[i] - meanAngle;
            double r = RPMS[i] - meanRpm;
            double d = DISTANCES[i] - meanDistance;
            aa += a * a;
            ar += a * r;
            rr += r * r;
            ad += a * d;
            rd += r * d;
        }

        double determinant = aa * rr - ar * ar;
        if (aa <= 0.0 || rr <= 0.0 || determinant <= 1e-10 * aa * rr) {
            throw new IllegalStateException(
                    "Angle and RPM must vary independently across calibration shots");
        }
        wAngle = (ad * rr - rd * ar) / determinant;
        wRpm = (rd * aa - ad * ar) / determinant;
        bias = meanDistance - wAngle * meanAngle - wRpm * meanRpm;
    }

    public double predictDistance(double angle, double rpm) {
        return wAngle * angle + wRpm * rpm + bias;
    }

    public ShotSettings calculate(double targetDistance) {
        if (!Double.isFinite(targetDistance)
                || !Double.isFinite(MIN_ANGLE) || !Double.isFinite(MAX_ANGLE)
                || !Double.isFinite(MIN_RPM) || !Double.isFinite(MAX_RPM)
                || !Double.isFinite(PREFERRED_ANGLE)
                || MIN_ANGLE > MAX_ANGLE || MIN_RPM > MAX_RPM
                || !Double.isFinite(ANGLE_STEP) || ANGLE_STEP <= 0.0) {
            throw new IllegalStateException("Set valid angle/RPM limits and preferred angle");
        }
        if (Math.abs(wRpm) < 1e-12) {
            throw new IllegalStateException("Model cannot solve reliably for RPM");
        }

        ShotSettings best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        int steps = (int) Math.ceil((MAX_ANGLE - MIN_ANGLE) / ANGLE_STEP);
        for (int i = 0; i <= steps; i++) {
            double angle = Math.min(MAX_ANGLE, MIN_ANGLE + i * ANGLE_STEP);
            double rpm = (targetDistance - bias - wAngle * angle) / wRpm;
            if (!Double.isFinite(rpm) || rpm < MIN_RPM || rpm > MAX_RPM) {
                continue;
            }
            double score = Math.abs(angle - PREFERRED_ANGLE);
            if (score < bestScore) {
                bestScore = score;
                best = new ShotSettings(angle, rpm, predictDistance(angle, rpm));
            }
        }
        if (best == null) {
            throw new IllegalArgumentException("No feasible shot for this distance");
        }
        return best;
    }

    public double getAngleCoefficient() {
        return wAngle;
    }

    public double getRpmCoefficient() {
        return wRpm;
    }

    public double getBias() {
        return bias;
    }
}
