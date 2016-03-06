package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
public class Launcher {
	
	private CANTalon loadTalon, lowerShootTalon, upperShootTalon;
	//private Victor spinTalon; //for using 393 motor
	private Servo turnServo;
	private double papasVisionDistance;
	private double papasVisionAngle;
	public Launcher(int loadTalonPort, int lowerShootTalonPort, int upperShootTalonPort){
		loadTalon = new CANTalon(loadTalonPort);
		lowerShootTalon = new CANTalon(lowerShootTalonPort);
		upperShootTalon = new CANTalon(upperShootTalonPort);
		//spinTalon = new Victor(2); //for usng 393 motors
		turnServo = new Servo(3); //for using servo turning
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
	}
	
	public void manualShoot(Joystick joyStick){
		if (joyStick.getRawButton(11)){ //shooting from the base of the tower
			shoot(1.0, 1.0);
			System.out.println("shoot full speed (bottom of tower)");
		}
		else if(joyStick.getRawButton(12)){ //shooting from green line
			shoot(1.0, 0.75);
			System.out.println("shoot lower speed (green line)");
		}
		else {
			shoot(0.0, 0.0);
		}
	}
	
	public void shoot(double lowerWheel, double upperWheel){
		lowerShootTalon.set((Math.abs(lowerWheel))*-1.0); //makes sure lower wheel value is always negative
		upperShootTalon.set(upperWheel);
	}
	
	//method created to get upper and lower bound values of the servo when rotating shooter is attached to robot
	//with this method we can find the exact degree range the servo has when shooter mounted between the arms
	public void testTurn(Joystick joyStick){
		double deg = turnServo.getAngle();
		if ((joyStick.getPOV()==(0))&&(deg<=120)){ //right on shooter, up on pov, positive servo angle val
			deg += 1.0;
		}
		else if (joyStick.getPOV()==(180)&& (deg>=0)){
			deg -= 1.0;
		}
		turnServo.setAngle(deg);
		System.out.println("Servo angle is: " + turnServo.getAngle());
	}
	
	
	public void turn(Joystick joyStick, double max, double min){
		//write code here for servo
		//need test code to see on robot how many degrees of servo we have to work with
		//also code with button to add a certain number of degrees with the push of a button
		double deg = turnServo.getAngle();
		if ((joyStick.getPOV()==(90)) && (deg <= max)){
			//spinTalon.set(1.0); //for using 393 motor
			deg += 5.0;
			System.out.println("turn shooter right");
		}
		else if ((joyStick.getPOV()==(270)) && (deg >= min)){
			//spinTalon.set(-1.0); //for using 393 motor
			deg -= 5.0;
			System.out.println("turn shooter left");
		}
		turnServo.setAngle(deg);
		System.out.println("Servo angle is: " + turnServo.getAngle());
		//else{
			//spinTalon.set(0.0); //for using 393 motor
		//}
	}
	
	
}
