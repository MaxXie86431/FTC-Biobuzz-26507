package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;
import com.pedropathing.ivy.Command;

public class Drivetrain implements Mechanism{
    public Drivetrain(){
        frontRight.setDirection(NextMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.BRAKE);
    }

    public final NextMotor frontLeft = new NextMotor(RobotController.controlHub(), 1);
    public final NextMotor frontRight = new NextMotor(RobotController.expansionHub(), 0);
    public final NextMotor backLeft = new NextMotor(RobotController.controlHub(), 0);
    public final NextMotor backRight =  new NextMotor(RobotController.expansionHub(), 2);
    public void drive(Gamepad gamepad1) {
        DriveCommands.mecanumDrive(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gamepad1
        ).schedule();
    }
}
