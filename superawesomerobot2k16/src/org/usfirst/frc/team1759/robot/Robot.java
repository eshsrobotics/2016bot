
package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
	
    RobotDrive myRobot;
    Launcher launcher;
    Joystick leftStick;
    Joystick rightStick;
    CANTalon canTalon0;
    CANTalon canTalon1;
    CANTalon canTalon2;
    CANTalon canTalon3;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        canTalon0 = new CANTalon(0); //output for this talon is reversed, wire to motor backwards
    	canTalon1 = new CANTalon(1); //output for this talon is reversed, wire to motor backwards
    	canTalon2 = new CANTalon(2);
    	canTalon3 = new CANTalon(3);
    	
    	canTalon0.setInverted(true);
    	canTalon1.setInverted(true);
    	//fl,bl,fr,br
        myRobot = new RobotDrive(canTalon0, canTalon1, canTalon2, canTalon3);
        launcher = new Launcher(0,1);
        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	myRobot.tankDrive(leftStick, rightStick); //left side is inverted, goes backwards when y is pushed forwards
    	launcher.shoot(leftStick, rightStick);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

/* stuff to work on:
 * - simple camera code (for michael)
 * - loader code
 * - shooting code (2 speeds... or more)
 * - shifting code if necessary
 * 
 */














