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

    }

    public NextMotor frontLeft = new NextMotor(RobotController.controlHub(), 1);
    public NextMotor frontRight = new NextMotor(RobotController.expansionHub(), 0);
    public NextMotor backLeft = new NextMotor(RobotController.controlHub(), 0);
    public NextMotor backRight =  new NextMotor(RobotController.expansionHub(), 2);
    public Command drive(Gamepad gamepad1) {
        return DriveCommands.mecanumDrive(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gamepad1
        );
    }
}
