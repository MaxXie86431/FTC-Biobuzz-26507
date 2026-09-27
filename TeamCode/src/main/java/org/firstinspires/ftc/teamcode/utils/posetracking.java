import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class PoseExample implements Mechanism {
    private final Follower follower;
    private final Servo servo;
    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(0, 0, 0);

    public PoseExample(Follower follower, Servo servo) {
        this.follower = follower;
        this.servo = servo;
        follower.setPose(startPose);
    }

    public Command example() {
        return instant(() -> {
            Pose pose = follower.pose();

            if (pose.x() > 10) {
                servo.move(0.5).schedule();
            }
        })
    }
}