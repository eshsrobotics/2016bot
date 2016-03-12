package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.RobotDrive;
/**
 *
 */
public class DriveCom extends Command {
	RobotDrive myRobot;
    public DriveCom(RobotDrive myRobot, double time) {
    	this.myRobot = myRobot;
    	setTimeout(time);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	myRobot.tankDrive(-1.0,-1.0); //used to be 0.75,0.75.. changed due to test robot testing but may need to switch back with normal robot
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	myRobot.drive(0,0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
