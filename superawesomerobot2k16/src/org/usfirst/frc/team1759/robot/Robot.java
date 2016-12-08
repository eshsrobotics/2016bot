package org.usfirst.frc.team1759.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
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
	Climber climber;
	Joystick leftStick;
	Joystick rightStick;
	Joystick shootStick;
	CANTalon canTalon0;
	CANTalon canTalon1;
	CANTalon canTalon2;
	CANTalon canTalon3;
	CANTalon canTalon4;
	CANTalon canTalon7;

	Thread cameraThread;
	CameraRunnable runnable;

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
		runnable = new CameraRunnable();
		cameraThread = new Thread(runnable);
		cameraThread.setName("robot camera thread");
		cameraThread.start();

		chooser = new SendableChooser();
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);

		canTalon0 = new CANTalon(0);//not used
		canTalon1 = new CANTalon(1);
		canTalon2 = new CANTalon(2);//Back left
		canTalon3 = new CANTalon(3);//Front Right
		canTalon4 = new CANTalon(4);//Back right
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
		launcher = new Launcher(8, 5, 6);
		leftStick = new Joystick(0);
		rightStick = new Joystick(2);
		shootStick = new Joystick(1);

		// talonPortActuator1 (cantalon) ,talonPortActuator2
		// (cantalon),lowerlimitswitch1port, lowerlimitswitch2port,
		// upperlimitswitch1port, upperlimitswitch2port
		climber = new Climber(7, 4, 0, 1);

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
		// system
		launcher.manualShoot(shootStick); // uses throttle on both joysticks to
		// control shooting system
		launcher.load(shootStick, 1.0, 0.5); // uses buttons 5 and 3 on right
		// joystick to run loading motor
		launcher.manualTurn(shootStick);
		climber.climb(shootStick);
		if (shootStick.getThrottle() == 1.0) {
			if (camOn == true) {
				try {
					CameraRunnable.killCameraThread = true;
					Thread.currentThread().join(CameraRunnable.sleepTimeMillisecond);
				} catch (Throwable t) {
					System.out.println("Camera thread throwable caught");
				}
				System.out.println("Camera turned off");
				camOn = false;
			}
		} 
		else if (shootStick.getThrottle() == -1.0) {
			if (camOn == false) {
				CameraRunnable.killCameraThread = false;
				runnable.run();
				System.out.println("Camera turned on");
				camOn = true;
			}
			launcher.autoTurn(runnable.PapasAngleDegrees, runnable.PapasDistanceInches, 3.0);
			System.out.println("Camera running");
		}
		// System.out.println("POT " + (pot.get())); //for4 testing purposes
		// System.out.println(ultrasanic.getRangeInches());
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

	protected void finalize() throws Throwable {
		try {
			CameraRunnable.killCameraThread = true;
			Thread.currentThread().join(CameraRunnable.sleepTimeMillisecond + 1000);
		} catch (Throwable t) {
			throw t;
		} finally {
			super.finalize();
		}
	}

}

/*
 * TO DO WEDNESDAY: - fix driver station - camera shit - set up potentiometer
 * stuff - test continuous servo - create methods to use feedback from papas
 * vision
 */

/*
 * TO DO AT COMP: put on potentiometers put on camera remount electronics boards
 *
 */
