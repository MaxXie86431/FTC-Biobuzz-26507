public class Servo implements Mechanism{
    NextServo servo = new NextServo("testServo");
    public Command move(double position) {
        return instant(() -> {
            servo.setPosition(position);
        });
    }
}
