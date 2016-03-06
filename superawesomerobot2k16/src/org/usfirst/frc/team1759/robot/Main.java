package org.usfirst.frc.team1759.robot;
import java.lang.Thread;

/**
 * Test driver for the camera thread.
 * 
 * This file can be deleted when we integrate with the driver station.
 * 
 * */
public class Main {
	static Thread cameraThread;
	public static void main(String[] args)
	{
		// Hello.  My name is main thread.
		Thread.currentThread().setName("Main thread");
		
		
		int runningTimeMiliseconds = 10000;
		// TODO Auto-generated method stub
    	CameraRunnable runnable = new CameraRunnable();
    	cameraThread = new Thread(runnable);
    	cameraThread.setName("robot camera thread");
    	cameraThread.start();
    	
    	
    	
    	try {
    		System.out.println(Thread.currentThread().getName() + ": About to go to bed.");
			Thread.currentThread().sleep(runningTimeMiliseconds);
			
			// Die, camerathread.  Die!
			System.out.println(Thread.currentThread().getName() + ": Okay, quitting time.");
			CameraRunnable.killCameraThread = true;
    		Thread.currentThread().join(CameraRunnable.sleepTimeMillisecond + 100000);
    		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
}