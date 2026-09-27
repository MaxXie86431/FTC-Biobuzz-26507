package org.firstinspires.ftc.teamcode.robot;

/** Dependency-free executable tests for the field-centric wheel math. */
public final class FieldCentricDriveTest {
    private static int assertions;

    private FieldCentricDriveTest() {
    }

    public static void main(String[] args) {
        testZeroAndForwardAtZeroHeading();
        testStrafeDirectionsAtZeroHeading();
        testFieldForwardAtQuarterTurns();
        testFieldForwardAtHalfTurn();
        testDiagonalDirection();
        testRotationDoesNotDependOnHeading();
        testMixedNormalizationAndSlowCap();
        testZeroAndClampedSlowScalars();
        testHeadingWraps();
        testNonFiniteInputsReturnZero();
        System.out.println("FieldCentricDrive: passed " + assertions + " assertions.");
    }

    private static void testZeroAndForwardAtZeroHeading() {
        expectWheels(0.0, 0.0, 0.0, 0.0,
                FieldCentricDrive.calculate(0.0, 0.0, 0.0, 0.0, 1.0));

        // Pushing the left stick away from the driver means the robot moves forward.
        expectWheels(1.0, 1.0, 1.0, 1.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, 0.0, 1.0));
        expectWheels(-1.0, -1.0, -1.0, -1.0,
                FieldCentricDrive.calculate(1.0, 0.0, 0.0, 0.0, 1.0));
    }

    private static void testStrafeDirectionsAtZeroHeading() {
        // Strafing right drives front-left/back-right forward and the other diagonal backward.
        expectWheels(1.0, -1.0, -1.0, 1.0,
                FieldCentricDrive.calculate(0.0, 1.0, 0.0, 0.0, 1.0));
        expectWheels(-1.0, 1.0, 1.0, -1.0,
                FieldCentricDrive.calculate(0.0, -1.0, 0.0, 0.0, 1.0));
    }

    private static void testFieldForwardAtQuarterTurns() {
        // With the robot turned 90 degrees counterclockwise, field-forward is robot-right.
        expectWheels(1.0, -1.0, -1.0, 1.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, Math.PI / 2.0, 1.0));
        // At -90 degrees, field-forward is robot-left.
        expectWheels(-1.0, 1.0, 1.0, -1.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, -Math.PI / 2.0, 1.0));
    }

    private static void testFieldForwardAtHalfTurn() {
        expectWheels(-1.0, -1.0, -1.0, -1.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, Math.PI, 1.0));
    }

    private static void testDiagonalDirection() {
        // Forward plus right leaves only the front-left and back-right wheels driving forward.
        expectWheels(1.0, 0.0, 0.0, 1.0,
                FieldCentricDrive.calculate(-1.0, 1.0, 0.0, 0.0, 1.0));
    }

    private static void testRotationDoesNotDependOnHeading() {
        FieldCentricDrive.WheelPowers expected =
                FieldCentricDrive.calculate(0.0, 0.0, 1.0, 0.0, 1.0);
        expectWheels(1.0, -1.0, 1.0, -1.0, expected);
        expectSameWheels(expected,
                FieldCentricDrive.calculate(0.0, 0.0, 1.0, Math.PI / 2.0, 1.0));
        expectSameWheels(expected,
                FieldCentricDrive.calculate(0.0, 0.0, 1.0, -2.4, 1.0));
    }

    private static void testMixedNormalizationAndSlowCap() {
        FieldCentricDrive.WheelPowers fullSpeed =
                FieldCentricDrive.calculate(-1.0, 1.0, 1.0, 0.0, 1.0);
        expectWheels(1.0, -1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, fullSpeed);

        FieldCentricDrive.WheelPowers slow =
                FieldCentricDrive.calculate(-1.0, 1.0, 1.0, 0.0, 0.3);
        expectWheels(0.3, -0.1, 0.1, 0.1, slow);
        expectAtMost(0.3, slow);
    }

    private static void testZeroAndClampedSlowScalars() {
        expectWheels(0.0, 0.0, 0.0, 0.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, 0.0, 0.0));
        expectWheels(0.0, 0.0, 0.0, 0.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, 0.0, -0.5));
        expectWheels(1.0, 1.0, 1.0, 1.0,
                FieldCentricDrive.calculate(-1.0, 0.0, 0.0, 0.0, 1.5));
    }

    private static void testHeadingWraps() {
        FieldCentricDrive.WheelPowers expected =
                FieldCentricDrive.calculate(-0.7, 0.2, -0.3, Math.PI / 3.0, 0.8);
        expectSameWheels(expected,
                FieldCentricDrive.calculate(-0.7, 0.2, -0.3, Math.PI / 3.0 + 2.0 * Math.PI, 0.8));
        expectSameWheels(expected,
                FieldCentricDrive.calculate(-0.7, 0.2, -0.3, Math.PI / 3.0 - 4.0 * Math.PI, 0.8));
    }

    private static void testNonFiniteInputsReturnZero() {
        double[] nonFiniteValues = {
                Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
        };
        for (double invalid : nonFiniteValues) {
            expectZero(FieldCentricDrive.calculate(invalid, 0.0, 0.0, 0.0, 1.0));
            expectZero(FieldCentricDrive.calculate(0.0, invalid, 0.0, 0.0, 1.0));
            expectZero(FieldCentricDrive.calculate(0.0, 0.0, invalid, 0.0, 1.0));
            expectZero(FieldCentricDrive.calculate(0.0, 0.0, 0.0, invalid, 1.0));
            expectZero(FieldCentricDrive.calculate(0.0, 0.0, 0.0, 0.0, invalid));
        }
    }

    private static void expectAtMost(double cap, FieldCentricDrive.WheelPowers actual) {
        expect(Math.abs(actual.frontLeft) <= cap + 1e-12, "front-left exceeded cap");
        expect(Math.abs(actual.frontRight) <= cap + 1e-12, "front-right exceeded cap");
        expect(Math.abs(actual.backLeft) <= cap + 1e-12, "back-left exceeded cap");
        expect(Math.abs(actual.backRight) <= cap + 1e-12, "back-right exceeded cap");
    }

    private static void expectZero(FieldCentricDrive.WheelPowers actual) {
        expectWheels(0.0, 0.0, 0.0, 0.0, actual);
    }

    private static void expectSameWheels(
            FieldCentricDrive.WheelPowers expected,
            FieldCentricDrive.WheelPowers actual
    ) {
        expectWheels(expected.frontLeft, expected.frontRight, expected.backLeft,
                expected.backRight, actual);
    }

    private static void expectWheels(
            double frontLeft,
            double frontRight,
            double backLeft,
            double backRight,
            FieldCentricDrive.WheelPowers actual
    ) {
        expectNear(frontLeft, actual.frontLeft, "front-left");
        expectNear(frontRight, actual.frontRight, "front-right");
        expectNear(backLeft, actual.backLeft, "back-left");
        expectNear(backRight, actual.backRight, "back-right");
    }

    private static void expectNear(double expected, double actual, String wheel) {
        expect(Math.abs(expected - actual) <= 1e-9,
                wheel + " expected " + expected + " but was " + actual);
    }

    private static void expect(boolean condition, String message) {
        assertions++;
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
