package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.drive.DriveCommands;

public class Drivetrain implements Mechanism{
    public Drivetrain(){
        frontRight.setDirection(NextMotor.Direction.REVERSE);
        backRight.setDirection(NextMotor.Direction.REVERSE);
    }

    public final NextMotor frontLeft = new NextMotor(RobotController.controlHub(), 0);
    public final NextMotor frontRight = new NextMotor(RobotController.expansionHub(), 1);
    public final NextMotor backLeft = new NextMotor(RobotController.controlHub(), 0);
    public final NextMotor backRight =  new NextMotor(RobotController.expansionHub(), 1);

    public void drive(Gamepad gamepad1) {
        DriveCommands.mecanumDrive(
            drivetrain.frontLeft,
            drivetrain.frontRight,
            drivetrain.backLeft,
            drivetrain.backRight,
            gamepad1
        )
    }
}
