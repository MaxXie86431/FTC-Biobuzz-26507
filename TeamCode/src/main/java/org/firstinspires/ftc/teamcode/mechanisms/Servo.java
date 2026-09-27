public class Servo {
    NextServo servo = new NextServo("testServo");
    public void move(double x) {
        servo.setPosition(x);
    }
}
