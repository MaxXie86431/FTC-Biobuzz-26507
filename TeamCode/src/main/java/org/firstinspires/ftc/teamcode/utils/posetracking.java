import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

public class PoseTracker {

    private final Follower follower;

    private final PoseFactory p = PoseFactory.degrees();
    private final Pose startPose = p.of(0, 0, 0);

    public PoseTracker(Follower follower) {
        this.follower = follower;
        follower.setPose(startPose);
    }

    public void update() {
        follower.update();
    }

    public Pose getPosition() {
        return follower.pose();
    }

    public double getX() {
        return follower.pose().x();
    }
    
    public double getY() {
        return follower.pose().y();
    }

    public double getHeading() {
        return follower.pose().heading();
    }
}