public class Motor implements Mechanism{
    NextMotor intakeMotor = new NextMotor(RobotController.controlHub(), 0);
    NextMotor liftMotor = new NextMotor(RobotController.controlHub(), 1);
    public Command move(double power) {
        return instant(() -> {
            intakeMotor.setThrottle(power);
        });
    }
    public Command lift(double voltage) {
        return instant(() -> {
            liftMotor.setVoltage(Volts.of(voltage));
        });
    }
}
