public class Motor implements Mechanism {

    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 0);

    public Command intake() {
        return instant(() ->
                intakeMotor.setThrottle(1.0)
        );
    }

    public Command outtake() {
        return instant(() ->
                intakeMotor.setThrottle(-1.0)
        );
    }

    public Command stopIntake() {
        return instant(() ->
                intakeMotor.setThrottle(0.0)
        );
    }
}