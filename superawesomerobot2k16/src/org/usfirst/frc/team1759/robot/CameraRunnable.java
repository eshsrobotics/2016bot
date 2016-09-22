package org.usfirst.frc.team1759.robot;

import org.usfirst.frc.team1759.robot.PapasVision;

/**
 * @author Ari Berkowicz
 *
 *         The purpose of this class is to implement the thread run function for
 *         our camera. It will let us check the camera asynchronously without
 *         bothering the main thread, which allows the driver to drive while the
 *         camera does its thing.
 * 
 *         Ari's initial test sees that the OpenCV code takes awhile to run.
 *
 */
public class CameraRunnable implements Runnable {
	/**
	 * Any target goal that is greater than this distance will be rejected by
	 * findgoal().
	 */
	
	public static double goalRejectionThresholdInches = 180;
	// When set to true the camerathread will kill itself.
	public static boolean killCameraThread = false;

	// This variable gives us one place to tweak the amount of time the camera
	// thread
	// will sleep before taking up CPU again. It also helps for the end of the
	// program:
	// after the main thread sets our death flag above, it only needs to wait
	// for about
	// this many milliseconds for *us* to die before it does itself.
	public static int sleepTimeMillisecond = 1;

	/***
	 * If PapasSolutionFound == true, then this will be the distance, in inches
	 * to the target. If PapasSolutionFound == false, this will be set to a
	 * negative number.
	 */
	public static double PapasDistanceInches;

	/***
	 * If PapasSolutionFound == true, then this will be the angle that the bot
	 * needs to turn in order to align with the center of the target.
	 */
	public static double PapasAngleDegrees;

	/***
	 * Set to true if we acquired a target at the appropriate distance. Set to
	 * false if no target was acquired (which is going to be fairly common.)
	 */
	public static Boolean PapasSolutionFound;

	// private PapasVision papasVision;

	/*
	 * This is what the thread is actually running.
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		int i = 1;
		PapasVision papasVision = new PapasVision(goalRejectionThresholdInches, false);
		while (killCameraThread == false) {
			try {
				java.lang.Thread.sleep(sleepTimeMillisecond);
				//System.out.println("Hi I am in the camera thread, about to find a goal.");

				papasVision.findGoal(i, true);
				PapasSolutionFound = papasVision.getSolutionFound();
				if (PapasSolutionFound) {
					PapasAngleDegrees = papasVision.getAzimuthGoalDeg();
					PapasDistanceInches = papasVision.getDistToGoalInch();
				}
				i++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// As soon as control makes it here, R.I.P.
		System.out.println(Thread.currentThread().getName() + ": I have just been informed that is is my time to die.");
	}
	
	public void die()
	{
		killCameraThread = true;
	}
}