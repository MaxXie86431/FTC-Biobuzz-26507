public class Motor implements Mechanism{
    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 0);
    public Command move(double power) {
        return instant(() -> {
            intakeMotor.setThrottle(power);
        });
    }
}
