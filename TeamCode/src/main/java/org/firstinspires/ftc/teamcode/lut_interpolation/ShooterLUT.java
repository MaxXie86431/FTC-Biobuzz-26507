package org.firstinspires.ftc.teamcode.lut_interpolation;
public class ShooterLUT {

    private static final double[] DISTANCES = {};
    private static final double[] ANGLES = {};
    private static final double[] RPMS = {};

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
