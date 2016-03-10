package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import java.math.*;
import edu.wpi.first.wpilibj.DigitalInput;

public class Climber {
	 private CANTalon talonActuator1, talonActuator2;
	 private DigitalInput lowerLimit1, lowerLimit2;
	 //private AnalogPotentiometer actuator1Potentiometer, actuator2Potentiometer;
     public Climber(int talonPortActuator1, int talonPortActuator2, int lls1port, int lls2port){
    	 talonActuator1 = new CANTalon(talonPortActuator1);
    	 talonActuator2 = new CANTalon(talonPortActuator2);
    	 lowerLimit1 = new DigitalInput(lls1port);
    	 lowerLimit2 = new DigitalInput(lls2port);
    	 //actuator1Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
    	 //actuator2Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
     }
     
     public void climb(Joystick joystick){ // double max1, double max2, double min1, double min2
    	 //checkError(error val);
    	 if (joystick.getRawButton(7)){
    		 move(1.0, true); //method to make actuators go up if button 7 is hit... max1, max2, 
    	 	 System.out.println("up full speed");
    	 }
    	 else if (joystick.getRawButton(8)){
    		 move(1.0, false); //method to make actuators go down if button 8 is hit... min1, min2,
    		 System.out.println("down full speed");
    	 }
    	 else if (joystick.getRawButton(9)){ 
    		 move(0.5, true); //actuators go up at half speed if button 9 is hit... max1, max2,
    		 System.out.println("up half speed");
    	}
    	 else if (joystick.getRawButton(10)){
    		 move(0.5, false); //actuators go down at half speed if button 10 is hit... min1, min2, 
    		 System.out.println("down half speed");
    	 }
    	else {
    		 talonActuator1.set(0.0);
    	 	 //talonActuator2.set(0.0);
    	 }
    	 
     }
     
     public void move(double speed, boolean up){ //double val1, double val2, 
    	 if(up){	
    	 	//if ((actuator1Potentiometer.get()<val1)&&(actuator2Potentiometer.get()<val2)){
    	 	talonActuator1.set(speed);
    	 	talonActuator2.set(speed);
    	 }
    	 else {
    		//if ((actuator1Potentiometer.get()>val1)&&(actuator2Potentiometer.get()>val2)){
     	 	if(lowerLimit1.get() || lowerLimit2.get()){ //stops actuators when either lower limit switch is hit
     	 		talonActuator1.set(0.0);
     	 		talonActuator2.set(0.0);
     	 	}  
     	 	else {
     	 		talonActuator1.set(speed*-1.0);
     	 		talonActuator2.set(speed*-1.0);
     	 	}
    	 }
     }
     
     /*public void testPot(){
    	 System.out.println("actuator one val: " + actuator1Potentiometer.get());
    	 System.out.println("actuator two val: " + actuator2Potentiometer.get());
     }*/
     
     //for reimplementation of potentiometers if necessary
     /*public void checkError(double error){
    	 if ((Math.abs(actuator1Potentiometer.get() - actuator2Potentiometer.get()))>error){
    		 talonActuator1.set(0.0);
    		 talonActuator2.set(0.0);
    	 }
     }*/
}
