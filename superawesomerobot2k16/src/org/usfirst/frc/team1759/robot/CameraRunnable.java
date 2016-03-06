/**
 * 
 */
package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.robot.PapasVision;

/**
 * @author Ari Berkowicz
 *
 * The purpose of this class is to implement the thread run function for our camera.
 * It will let us check the camera asynchronously without bothering the main thread, which allows the driver to drive
 * while the camera does its thing.
 * 
 * Ari's initial test sees that the OpenCV code takes awhile to run. 
 *
 */
public class CameraRunnable implements Runnable {
	
	//When set to true the camerathread will kill itself.
	public static boolean killCameraThread = false;
	
	// This variable gives us one place to tweak the amount of time the camera thread
	// will sleep before taking up CPU again.  It also helps for the end of the program:
	// after the main thread sets our death flag above, it only needs to wait for about
	// this many milliseconds for *us* to die before it does itself.
	public static int sleepTimeMillisecond = 1;

	double distToGoalInch;
	double azimuthGoalDeg;
	Boolean solutionFound;
	
	//private PapasVision papasVision;
	
	/* 
	 * This is what the thread is actually running.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		PapasVision papasVision = new PapasVision();
		int i = 1;
		while(killCameraThread == false)
		{
			try 
			{
				java.lang.Thread.sleep(sleepTimeMillisecond);
				System.out.println("Hi I am in the camera thread, about to find a goal.");
				
				papasVision.findGoal(i, true);
				solutionFound = papasVision.getSolutionFound();
				if (solutionFound)
				{
					azimuthGoalDeg = papasVision.getAzimuthGoalDeg();
					distToGoalInch = papasVision.getDistToGoalInch();
				}
				i++;
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// As soon as control makes it here, R.I.P.
		System.out.println(Thread.currentThread().getName() + ": I have just been informed that is is my time to die.");
	}
}