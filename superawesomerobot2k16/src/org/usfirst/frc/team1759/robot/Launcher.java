package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
public class Launcher {
	private Talon loadTalon, lowerShootTalon, upperShootTalon; 
	public Launcher(int loadTalonPort, int lowerShootTalonPort, int upperShootTalonPort){
		loadTalon = new Talon(loadTalonPort);
		lowerShootTalon = new Talon(lowerShootTalonPort);
		upperShootTalon = new Talon(upperShootTalonPort);
	}
	public void load(Joystick joystick, double loadingSpeed){ //have not tested this method yet (as of 2/13)
		if (joystick.getRawButton(5)) //runs loading talon forwards with button 5
		    loadTalon.set(loadingSpeed);
		else if (joystick.getRawButton(3)) //runs loading talon backwards with button 3
			loadTalon.set((loadingSpeed*-1.0));
		else
			loadTalon.set(0.0);
		//System.out.print("load talon val -- " + (loadTalon.get())); //prints loading talon value for testing purposes
	}
	public void shoot(Joystick leftStick, Joystick rightStick){ 
		lowerShootTalon.set(((leftStick.getThrottle()/2.0)+0.5)*-1.0);//this makes so values between -1 and 1 become values between 0 and 1 (for lower shooting wheel)
		//System.out.println("lower talon val (ls): " + ((leftStick.getThrottle()/2.0)+0.5)*-1.0); //prints lower talon value for testing purposes
		
		upperShootTalon.set(((rightStick.getThrottle()/2.0)+0.5)); //makes it so upper shooting wheel spins at values between 0 and -1
		//System.out.println("upper talon val (rs): " + ((rightStick.getThrottle()/2.0)+0.5)); //prints upper talon value for
	}

}
