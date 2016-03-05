package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
public class Launcher {
	
	private CANTalon loadTalon, lowerShootTalon, upperShootTalon;
	private Victor spinTalon;
	public Launcher(int loadTalonPort, int lowerShootTalonPort, int upperShootTalonPort){
		loadTalon = new CANTalon(loadTalonPort);
		lowerShootTalon = new CANTalon(lowerShootTalonPort);
		upperShootTalon = new CANTalon(upperShootTalonPort);
		spinTalon = new Victor(2);
	}
	public void load(Joystick joystick, double loadingSpeed, double halfSpeed){
		if (joystick.getRawButton(5)){ //runs loading talon forwards with button 5
		    loadTalon.set(loadingSpeed);
			System.out.println("load in full speed");
		}
		else if (joystick.getRawButton(6)){ //runs loading talon backwards with button 6
			loadTalon.set((loadingSpeed*-1.0));
			System.out.println("load out full speed");
		}
		else if (joystick.getRawButton(3)){ //runs loading talon forwards with button 3 at half speed
		    loadTalon.set(halfSpeed);
		    System.out.println("load in half speed");
		}
		else if (joystick.getRawButton(4)){ //runs loading talon backwards with button 4 at half speed
			loadTalon.set((halfSpeed*-1.0));
			System.out.println("load out half speed");
		}
		else{
			loadTalon.set(0.0);
			System.out.println();
		}
		//System.out.print("load talon val -- " + (loadTalon.get())); //prints loading talon value for testing purposes
	}
	
	
	public void shoot(Joystick joyStick){ 
		//code below makes shooters change values based on joystick throttles, used for testing new shooting values
/*		lowerShootTalon.set(((leftStick.getThrottle()/2.0)+0.5)*-1.0);//this makes so values between -1 and 1 become values between 0 and 1 (for lower shooting wheel)
		System.out.println("lower shooting val (ls): " + (lowerShootTalon.get())); //prints lower talon value for testing purposes
		
		upperShootTalon.set(((rightStick.getThrottle()/2.0)+0.5)); //makes it so upper shooting wheel spins at values between 0 and -1
		System.out.println("upper shooting val (rs): " + (upperShootTalon.get())); //prints upper talon value for*/
			
		//shooting from base of tower
		if (joyStick.getRawButton(11)){
			lowerShootTalon.set(-1.0);
			upperShootTalon.set(1.0);
			System.out.println("shoot full speed (bottom of tower)");
		}
		//shooting from 11 feet line (green line) (still not for sure)
		else if (joyStick.getRawButton(12)){
			lowerShootTalon.set(-1.0);
			upperShootTalon.set(.75);
			System.out.println("shoot lower speed (green line)");
		}
		else{
			lowerShootTalon.set(0.0);
			upperShootTalon.set(0.0);
		}

	}
	
	public void turn(Joystick joyStick){
		if (joyStick.getPOV()==(90)){
			spinTalon.set(1.0);
			System.out.println("turn shooter right");
		}
		else if (joyStick.getPOV()==(270)){
			spinTalon.set(-1.0);
			System.out.println("turn shooter left");
		}
		else{
			spinTalon.set(0.0);
		}
	}

}
