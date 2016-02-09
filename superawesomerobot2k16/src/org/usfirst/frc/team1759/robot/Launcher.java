package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
public class Launcher {
	private Talon shootTalon1, shootTalon2; //loadTalon
	public Launcher(int shootTalonPort1, int shootTalonPort2){ //int loadTalon
		//loadTalon = new Talon(loadTalonPort);
		shootTalon1 = new Talon(shootTalonPort1);
		shootTalon2 = new Talon(shootTalonPort2);
	}
	public void load(){//not in use yet
	//look at arm code on git for reference	
	}
	public void shoot(Joystick leftStick, Joystick rightStick){ //returning values between -0.5 and 0.5
		shootTalon1.set((leftStick.getThrottle()/2.0)+0.5);//this makes so values between -1 and 1 become values between 0 and 1
		System.out.println("talon1 val (ls): " + (leftStick.getThrottle()/2.0)+0.5);//check to actually make sure dividing by 0.5
		
		shootTalon2.set((rightStick.getThrottle()/2.0)+0.5); //returning values between -0.5 and 0.5
		System.out.println("talon2 val (rs): " + (rightStick.getThrottle()/2.0)+0.5); //did not print, need to figure out how to print to see throttle values
	}

}
