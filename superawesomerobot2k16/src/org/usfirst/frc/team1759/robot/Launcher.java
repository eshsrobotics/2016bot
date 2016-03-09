package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Servo;
public class Launcher {
	
	private CANTalon loadTalon, lowerShootTalon, upperShootTalon;
	//private Victor spinTalon; //for using 393 motor
	private Servo turnServo;
	private double error; //error value
	private double min; //hard coded shooter min and max values
	private double max; 
	public Launcher(int loadTalonPort, int lowerShootTalonPort, int upperShootTalonPort){
		loadTalon = new CANTalon(loadTalonPort);
		lowerShootTalon = new CANTalon(lowerShootTalonPort);
		upperShootTalon = new CANTalon(upperShootTalonPort);
		//spinTalon = new Victor(2); //for usng 393 motors
		turnServo = new Servo(2); //for using servo turning
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
			deg += 5.0;
		}
		else if (joyStick.getPOV()==(180)&& (deg>=0)){
			deg -= 5.0;
		}
		turnServo.setAngle(deg);
		System.out.println("Servo angle is: " + turnServo.getAngle());
	}
	
	public void turn(double angle){
		if (angle > max){
			System.out.println("Desired target too far right of shooter range. Turn robot right.");
		}
		else if (angle < min){
			System.out.println("Desired target too far left of shooter range. Turn robot left.");
		}
		else {
			turnServo.setAngle(angle);
		}
		System.out.println("Servo angle is: " + turnServo.getAngle());
	}
	
	public void manualTurn(Joystick joyStick){
		double deg = turnServo.getAngle();
		if (joyStick.getPOV()==(90)){ //turns shooter right
			deg += 5.0;
			turn(deg);
		}
		else if (joyStick.getPOV()==(270)){ //turns shooter left
			deg -= 5.0;
			turn(deg);
		}
	}
	
	//papas vision angle: RIGHT NEGATIVE, LEFT POSITIVE
	public void autoTurn(double papasVisionAngle, double error){ //start error value at about 3 degrees
		double ang = turnServo.getAngle();
		//if papas vision angle is left of tower, need to move servo right, if papas vision angle is right of tower, need to move servo left
		if (papasVisionAngle > (0 + error)){ //left of tower, need to move servo right
			turn(ang + papasVisionAngle);
		}
		else if (papasVisionAngle <(0 - error)){//right of tower, need to move servo left
			turn(ang - papasVisionAngle);
		}
	}
	
}
