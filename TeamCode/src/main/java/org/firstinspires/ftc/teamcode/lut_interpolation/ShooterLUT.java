package org.firstinspires.ftc.teamcode.lut_interpolation;
public class ShooterLUT {

    // From shooter_data.csv (made shots after the two-motor fix). Angle = hood servo position.
    // Each distance averages the made shots taken there. Left out: 24.7 in (0.24 @ 3030) and
    // 36.6 in (0.54 @ 3110), which disagree with the better-supported neighbours on either side,
    // and the low-hood alternative at 49.3 in (0.18 @ 3430).
    private static final double[] DISTANCES = {14.58, 19.81, 22.30, 26.55, 28.62, 38.58, 42.30, 49.27};
    private static final double[] ANGLES = {0.03, 0.16, 0.13, 0.17, 0.32, 0.28, 0.38, 0.56};
    private static final double[] RPMS = {3130, 2525, 2633, 2663, 3150, 2740, 3010, 3010};

    public static class ShotSettings {
        public final double angle;
        public final double rpm;

        public ShotSettings(double angle, double rpm) {
            this.angle = angle;
            this.rpm = rpm;
        }
    }

    public static ShotSettings calculate(double distance) {
        int n = DISTANCES.length;

        if (n == 0 || ANGLES.length != n || RPMS.length != n) {
            throw new IllegalStateException("Invalid LUT data");
        }

        if (n == 1) {
            return new ShotSettings(ANGLES[0], RPMS[0]);
        }

        if (distance <= DISTANCES[0]) {
            return new ShotSettings(ANGLES[0], RPMS[0]);
        }

        if (distance >= DISTANCES[n - 1]) {
            return new ShotSettings(ANGLES[n - 1], RPMS[n - 1]);
        }

        for (int i = 0; i < n - 1; i++) {
            double d0 = DISTANCES[i];
            double d1 = DISTANCES[i + 1];

            if (d1 <= d0) {
                throw new IllegalStateException(
                        "Distances must be strictly increasing"
                );
            }

            if (distance >= d0 && distance <= d1) {
                double t = (distance - d0) / (d1 - d0);

                double angle = ANGLES[i]
                        + t * (ANGLES[i + 1] - ANGLES[i]);

                double rpm = RPMS[i]
                        + t * (RPMS[i + 1] - RPMS[i]);

                return new ShotSettings(angle, rpm);
            }
        }

        throw new IllegalStateException("Interpolation failed");
    }
}
