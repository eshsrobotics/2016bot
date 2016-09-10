package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;

//import edu.wpi.first.wpilibj.AnalogPotentiometer;

import edu.wpi.first.wpilibj.Ultrasonic;

import java.lang.Thread;

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
	Joystick shootStick;
	CANTalon canTalon0;
	CANTalon canTalon1;
	CANTalon canTalon2;
	CANTalon canTalon3;
	CANTalon canTalon4;
	CANTalon canTalon6;
	CANTalon canTalon7;
	Solenoid solenoidFire;

	CommandGroup autoCom;
	boolean camOn;

	Ultrasonic ultrasanic;
	// CameraServer server;

	// AnalogPotentiometer pot; //for testing purposes
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		// enable the camera
		/*
		 * server = CameraServer.getInstance(); server.setQuality(100);
		 * server.startAutomaticCapture("cam0");
		 */

		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		solenoidFire = new Solenoid(1);

		canTalon0 = new CANTalon(0);//not used
		canTalon1 = new CANTalon(1);//not used
		canTalon2 = new CANTalon(2);//Back left
		canTalon3 = new CANTalon(3);//Front Right
		canTalon4 = new CANTalon(4);//Back right
		canTalon6 = new CANTalon(6);//Cannon Y
		canTalon7 = new CANTalon(7);//Front left
		
		// output for talons was reversed so this inverts them
		canTalon0.setInverted(false);
		canTalon1.setInverted(false);
		canTalon2.setInverted(false);
		canTalon3.setInverted(false);
		canTalon4.setInverted(false);
		canTalon7.setInverted(false);

		camOn = false;

		// front left, back left, front right, back right
		myRobot = new RobotDrive(canTalon7, canTalon2, canTalon1, canTalon3);

		// load talon port (cantalon), lower shoot talon port(cantalon), upper
		// shoot talon port(cantalon)
		leftStick = new Joystick(0);
		rightStick = new Joystick(2);
		

		// talonPortActuator1 (cantalon) ,talonPortActuator2
		// (cantalon),lowerlimitswitch1port, lowerlimitswitch2port,
		// upperlimitswitch1port, upperlimitswitch2port

		autoCom = new AutoCom(myRobot, launcher);

		// pot = new AnalogPotentiometer(3); //for testing purposes
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	public void autonomousInit() {
		autoSelected = (String) chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		autoCom.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		myRobot.tankDrive(leftStick, rightStick);// two joystick tank drive
		//myRobot.setMaxOutput(0.5);
		canTalon6.set((leftStick.getThrottle()-.5)/-5);
		//cannon angle 
		boolean fire;
		fire = leftStick.getTrigger();
		//gets trigger state
		if(fire=true){
			solenoidFire.set(true);
			//trigger solenoid
		}
		solenoidFire.set(false);
		//reset solenoid
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {
		
	}

}

