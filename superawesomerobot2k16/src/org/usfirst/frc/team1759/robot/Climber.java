package org.usfirst.frc.team1759.robot;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;


public class Climber {
	 private CANTalon talonActuator1, talonActuator2;
	 private AnalogPotentiometer actuator1Potentiometer, actuator2Potentiometer;
     public Climber(int talonPortActuator1, int talonPortActuator2, int actuator1PotentiometerPort, int actuator2PotentiometerPort){
    	 talonActuator1 = new CANTalon(talonPortActuator1);
    	 talonActuator2 = new CANTalon(talonPortActuator2);
    	 actuator1Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
    	 actuator2Potentiometer = new AnalogPotentiometer(actuator1PotentiometerPort);
     }
     
     public void climb(Joystick joystick){
    	 if (joystick.getRawButton(6))
    		 up(); //method to make actuators go up
    	 else if (joystick.getRawButton(4))
    		 down(); //method to make actuators go down
    	 else {
    		 talonActuator1.set(0.0);
    	 	 talonActuator2.set(0.0);
    	 }
    	 
     }
     
     public void up(){
    	 
    	 
     }
     
     public void down(){
    	 
    	 
     }
}
