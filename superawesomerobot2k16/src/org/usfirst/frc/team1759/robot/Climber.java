package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import java.math.*;




public class Climber {
	 private CANTalon talonActuator1; //talonActuator2;
	 //private AnalogPotentiometer actuator1Potentiometer, actuator2Potentiometer;
     public Climber(int talonPortActuator1){   //, int talonPortActuator2){ //int actuator1PotentiometerPort, int actuator2PotentiometerPort){
    	 talonActuator1 = new CANTalon(talonPortActuator1);
    	 //talonActuator2 = new CANTalon(talonPortActuator2);
    	 //actuator1Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
    	 //actuator2Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
     }
     
     public void climb(Joystick joystick){ // double max1, double max2, double min1, double min2
    	 //checkError(error val);
    	 if (joystick.getRawButton(7))
    		 move(1.0, true); //method to make actuators go up... max1, max2, 
    	 else if (joystick.getRawButton(8))
    		 move(1.0, false); //method to make actuators go down... min1, min2,
    	 else if (joystick.getRawButton(9)) 
    		 move(0.5, true); //actuators go up at half speed... max1, max2,
    	 else if (joystick.getRawButton(10))
    		 move(0.5, false); //actuators go down at half speed... min1, min2, 
    	 else {
    		 talonActuator1.set(0.0);
    	 	 //talonActuator2.set(0.0);
    	 }
    	 
     }
     
     public void move(double speed, boolean up){ //double val1, double val2, 
    	 if(up){	
    	 	//if ((actuator1Potentiometer.get()<val1)&&(actuator2Potentiometer.get()<val2)){
    	 	talonActuator1.set(speed);
    	 	//talonActuator2.set(speed);
    	 	
    	 }
    	 else {
    		 //if ((actuator1Potentiometer.get()>val1)&&(actuator2Potentiometer.get()>val2)){
     	 	talonActuator1.set(speed*-1.0);
     	 	//talonActuator2.set(speed*-1.0);
     	 	   
    	 }
     }
     
     /*public void checkError(double error){
    	 if ((Math.abs(actuator1Potentiometer.get() - actuator2Potentiometer.get()))>error){
    		 talonActuator1.set(0.0);
    		 talonActuator2.set(0.0);
    	 }
     }*/
}
//need to add in method to make sure potentiometers change the same
