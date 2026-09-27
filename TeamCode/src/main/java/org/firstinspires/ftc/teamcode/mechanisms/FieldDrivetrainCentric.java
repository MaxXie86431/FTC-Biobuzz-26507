package org.firstinspires.ftc.teamcode.mechanisms;
/**
import com.qualcomm.robotcore.hardware.Gamepad;

import com.pedropathing.follower.Follower;
import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;

public class FieldDrivetrainCentric implements Mechanism{
    private final Follower follower;

    public final NextMotor frontLeft = new NextMotor(RobotController.controlHub(), 0);
    public final NextMotor frontRight = new NextMotor(RobotController.expansionHub(), 1);
    public final NextMotor backLeft = new NextMotor(RobotController.controlHub(), 0);
    public final NextMotor backRight =  new NextMotor(RobotController.expansionHub(), 1);
    public final heading = () -> follower.getPose().getHeading().schedule();
    public final Supplier<Double> heading;

    public FieldDrivetrainCentric(Follower follower){
        this.follower = follower;
        frontRight.setDirection(NextMotor.Direction.REVERSE);
        backRight.setDirection(NextMotor.Direction.REVERSE);
        heading = () -> follower.pose().heading();
    }

    public void drive(Gamepad gamepad1) {
        DriveCommands.mecanumDrive(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gamepad1,
            heading
        );
    }
}
*/
public FieldDrivetrainCentric() {}