package org.firstinspires.ftc.teamcode.utils;

import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.auto.paths.PathsAndPoses;

public class AutoCommands {
    private final Follower follower;

    public AutoCommands(Follower follower) {
        this.follower = follower;
    }
    public Command runPath(Path path) {
        return follow(follower, path);
    }
}
