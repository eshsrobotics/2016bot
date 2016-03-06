package org.usfirst.frc.team1759.robot;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

//Thank you to the morotorq (1515) team for letting us modify the code and use it

public class PapasVision {
	/*
	 * final static double HORIZ_FOV_DEG = 59.253; final static double
	 * HORIZ_FOV_RAD = Math.toRadians(HORIZ_FOV_DEG); final static double
	 * VERT_FOV_DEG = 44.44; final static double VERT_FOV_RAD =
	 * Math.toRadians(VERT_FOV_DEG); final static double REAL_TAPE_HEIGHT = 14;
	 * // inches of real tape height final static double IMG_HEIGHT = 480; //
	 * pixels of image resolution final static double IMG_WIDTH = 640; // pixels
	 * of image resolution final static double CAM_EL_DEG = 30.0; final static
	 * double CAM_EL_RAD = Math.toRadians(CAM_EL_DEG);
	 */

	final static double HORIZ_FOV_DEG = 59.253;
	//final static double HORIZ_FOV_DEG = 59.703;
	final static double HORIZ_FOV_RAD = Math.toRadians(HORIZ_FOV_DEG);
	final static double VERT_FOV_DEG = 44.44;
	//final static double VERT_FOV_DEG = 33.583;
	final static double VERT_FOV_RAD = Math.toRadians(VERT_FOV_DEG);
	final static double REAL_TAPE_HEIGHT = 14; // inches of real tape height
	final static double IMG_HEIGHT = 480; // pixels of image resolution
	final static double IMG_WIDTH = 640; // pixels of image resolution
	final static double CAM_EL_DEG = 15.0;
	final static double CAM_EL_RAD = Math.toRadians(CAM_EL_DEG);

	VideoCapture camera;

	double distToGoalInch;
	double azimuthGoalDeg;
	double elevationGoalDeg;
	Boolean solutionFound;
	long processingTimeMs;

	public PapasVision() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// System.load("/usr/local/lib/libopencv_java310.so");
		System.out.println("Welcome to OpenCV " + Core.VERSION);
		camera = new VideoCapture(0);
		//camera.open(1);
		camera.set(Videoio.CAP_PROP_EXPOSURE, -9);
		camera.set(Videoio.CAP_PROP_BRIGHTNESS, 30);
		camera.set(Videoio.CAP_PROP_IOS_DEVICE_WHITEBALANCE, 4745);
		camera.set(Videoio.CAP_PROP_SATURATION, 200);
		camera.set(Videoio.CAP_PROP_CONTRAST, 10);
		// Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
		// System.out.println("OpenCV Mat: " + m);
		// Mat mr1 = m.row(1);
		// mr1.setTo(new Scalar(1));
		// Mat mc5 = m.col(5);
		// mc5.setTo(new Scalar(5));
		// System.out.println("OpenCV Mat data - Version 2:\n" + m.dump());
	}

	public void findGoal(int pictureFile, boolean useCam) {
		long time = System.currentTimeMillis();
		solutionFound = false;
		Mat frame = new Mat();
		Mat output = new Mat();

		if (useCam == false) {
			frame = Imgcodecs.imread(pictureFile + ".png");
		} else {
			camera.read(frame);
			Imgcodecs.imwrite(pictureFile + ".png", frame);
		}
		
		convertImage(frame, output);
		Imgcodecs.imwrite(pictureFile + "_converted.png", output);

		cancelColorsTape(output, output);
		Imgcodecs.imwrite(pictureFile + "_cancelcolors.png", output);

		Imgproc.erode(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		Imgproc.dilate(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		Imgcodecs.imwrite(pictureFile + "_cancelcolors_morphfilt.png", output);

		List<MatOfPoint> contours = findContours(output);

		Mat frameContours = new Mat();
		frameContours = frame.clone();
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(frameContours, contours, i, new Scalar(0, 0, 255));
		}
		Imgcodecs.imwrite(pictureFile + "_frameContours.png", frameContours);

		contours = filterContours(contours);

		Mat frameFiltContours = new Mat();
		frameFiltContours = frame.clone();
		for (int i = 0; i < contours.size(); i++) {
			Imgproc.drawContours(frameFiltContours, contours, i, new Scalar(0, 0, 255));
		}
		Imgcodecs.imwrite(pictureFile + "_frameFiltContours.png", frameFiltContours);

		if (contours.size() > 0) {
			MatOfPoint goalContour = findGoalContour(contours);
			Rect goalRect = Imgproc.boundingRect(goalContour);
			MatOfPoint2f points2f = approxPoly(goalContour);

			Point[] bottomPts = findBottomPts(points2f.toArray(), goalRect);
			Point[] topPts = findTopPts(points2f.toArray(), goalRect);
			// Point[] bottomPts = findBottomPts(points2f.toArray());
			// Point[] topPts = findTopPts(points2f.toArray());

			Mat framePoints = new Mat();
			framePoints = frame.clone();
			Imgproc.circle(framePoints, topPts[0], 5, new Scalar(0, 255, 0));
			Imgproc.circle(framePoints, topPts[1], 5, new Scalar(0, 255, 0));
			Imgproc.circle(framePoints, bottomPts[0], 5, new Scalar(0, 0, 255));
			Imgproc.circle(framePoints, bottomPts[1], 5, new Scalar(0, 0, 255));
			Imgcodecs.imwrite(pictureFile + "_framePoints.png", framePoints);

			System.out.println("Solution found");

			// double distToGoal = findDistToGoal(goalRect.width, 31);
			distToGoalInch = findDistToGoal(topPts, bottomPts);
			System.out.println("Distance to goal: " + distToGoalInch + " inches");

			azimuthGoalDeg = findAzimuthGoal(topPts, bottomPts);
			System.out.println("Goal azimuth: " + azimuthGoalDeg + " degrees");
			
			
			if(distToGoalInch > 180)
			{
				System.out.println("Sorry integrity check failed");
				System.out.println("PictureFile number: " + pictureFile);
			}
			else{
				solutionFound = true;
			}
			/*
			 * if(isFacingForward(bottomPts)) { System.out.println(
			 * "facing forward"); } else { System.out.println("isFacingLeft: " +
			 * isFacingLeft(bottomPts)); System.out.println("lat angle: " +
			 * findLateralAngle(bottomPts)); }
			 * 
			 * if(isOnMidline(goalRect)) { System.out.println("on midline"); }
			 * else { System.out.println("isOnRight: " + isOnRight(goalRect));
			 * System.out.println("distFromMidline: " +
			 * distFromMidline(bottomPts, topPts, distToGoalInch)); }
			 */
		} else {
			System.out.println("Solution not found");
		}
		processingTimeMs = System.currentTimeMillis() - time;
		System.out.println("Processing time: " + processingTimeMs + " ms");
	}

	public static void convertImage(Mat input, Mat output) {
		//Imgproc.cvtColor(input, output, Imgproc.COLOR_RGB2GRAY);
		
		Imgproc.blur(input, output, new Size(5,5));
		//Imgproc.cvtColor(output, output, Imgproc.COLOR_RGB2GRAY);
		//Imgproc.cvtColor(output, output, Imgproc.COLOR_BGR2HSV);

		//Imgproc.erode(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		//Imgproc.dilate(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));

		//Imgproc.dilate(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		//Imgproc.erode(output, output, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5)));
		 
		//Imgproc.blur(output, output, new Size(5,5));
		 
	}

	// scalar params: H(0-180), S(0-255), V(0-255)
	public static void cancelColorsTape(Mat input, Mat output) {
		/*Imgproc.threshold(input, output, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
		Core.inRange(input, new Scalar(25, 0, 220), new Scalar(130, 80, 255), output);
		
		Core.inRange(input, new Scalar(65, 90, 0), new Scalar(85, 115, 35), output);
		Scaler value order is (Blue, Green, Red)*/
		Core.inRange(input, new Scalar(30, 60, 0), new Scalar(115, 145, 65), output);		
	}

	public static List<MatOfPoint> findContours(Mat image) {
		List<MatOfPoint> contours = new ArrayList<>();
		Imgproc.findContours(image, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		return contours;
	}

	public static List<MatOfPoint> filterContours(List<MatOfPoint> contours) {
		List<MatOfPoint> newContours = new ArrayList<MatOfPoint>();
		List<MatOfPoint> convexHulls = new ArrayList<MatOfPoint>();

		for (int i = 0; i < contours.size(); i++) {
			MatOfInt convexHullMatOfInt = new MatOfInt();
			ArrayList convexHullPointArrayList = new ArrayList<Point>();
			MatOfPoint convexHullMatOfPoint = new MatOfPoint();
			// ArrayList convexHullMatOfPointArrayList = new
			// ArrayList<MatOfPoint>();

			Imgproc.convexHull(contours.get(i), convexHullMatOfInt);

			for (int j = 0; j < convexHullMatOfInt.toList().size(); j++) {
				convexHullPointArrayList.add(contours.get(i).toList().get(convexHullMatOfInt.toList().get(j)));
			}
			convexHullMatOfPoint.fromList(convexHullPointArrayList);
			// convexHullMatOfPointArrayList.add(convexHullMatOfPoint);

			double contourArea = Imgproc.contourArea(contours.get(i));
			double convexHullArea = Imgproc.contourArea(convexHullMatOfPoint);
			double contourToConvexHullRatio = contourArea / convexHullArea;

			Rect rect = Imgproc.boundingRect(contours.get(i));

			if (contourToConvexHullRatio < 0.7 && rect.width > 40 && rect.height > 40) {
				newContours.add(convexHullMatOfPoint);
				// newContours.add(contours.get(i));
			}

			convexHullMatOfInt = null;
			convexHullPointArrayList = null;
			convexHullMatOfPoint = null;
		}
		return newContours;

		/*
		 * List<MatOfPoint> newContours = new ArrayList<MatOfPoint>(); for(int i
		 * = 0; i < contours.size(); i++) { Rect rect =
		 * Imgproc.boundingRect(contours.get(i)); if(rect.width > 80 &&
		 * rect.width < 200 && rect.height > 30 && rect.height < 100 && rect.y <
		 * 400) { MatOfPoint2f point2f = approxPoly(contours.get(i)); //
		 * if(point2f.toList().size() >= 7 && point2f.toList().size() <= 9) {
		 * newContours.add(contours.get(i)); //} } } return newContours;
		 */
	}

	public static MatOfPoint findGoalContour(List<MatOfPoint> contours) {
		List<Rect> rects = new ArrayList<Rect>();
		rects.add(Imgproc.boundingRect(contours.get(0)));
		int lrgstRectIndx = 0;
		for (int i = 1; i < contours.size(); i++) {
			Rect rect = Imgproc.boundingRect(contours.get(i));
			rects.add(rect);
			if (rect.width > rects.get(lrgstRectIndx).width) {
				lrgstRectIndx = i;
			}
		}
		return contours.get(lrgstRectIndx);
	}

	// find approximate point vertices of contoured goal tape
	public static MatOfPoint2f approxPoly(MatOfPoint contour) {
		MatOfPoint2f point2f = new MatOfPoint2f();
		List<Point> points = contour.toList();
		point2f.fromList(points);
		Imgproc.approxPolyDP(point2f, point2f, 5.0, true); // third parameter:
															// smaller->more
															// points
		return point2f;
	}

	// finds bottom vertices of goal tape, left to right
	public static Point[] findBottomPts(Point[] points, Rect rect) {
		Point rectBottomRight = new Point();
		rectBottomRight = rect.br().clone();
		Point rectBottomLeft = new Point(rect.br().x - (rect.width - 1), rect.br().y);
		Point bottomRight = new Point();
		Point bottomLeft = new Point();

		double lowestDist = 0;
		for (int i = 0; i < points.length; i++) {
			double dist = Math.sqrt((points[i].x - rectBottomLeft.x) * (points[i].x - rectBottomLeft.x)
					+ (points[i].y - rectBottomLeft.y) * (points[i].y - rectBottomLeft.y));

			if (i == 0) {
				bottomLeft = points[i];
				lowestDist = dist;
			} else if (dist < lowestDist) {
				bottomLeft = points[i];
				lowestDist = dist;
			}
		}

		for (int i = 0; i < points.length; i++) {
			double dist = Math.sqrt((points[i].x - rectBottomRight.x) * (points[i].x - rectBottomRight.x)
					+ (points[i].y - rectBottomRight.y) * (points[i].y - rectBottomRight.y));

			if (i == 0) {
				bottomRight = points[i];
				lowestDist = dist;
			} else if (dist < lowestDist) {
				bottomRight = points[i];
				lowestDist = dist;
			}
		}

		Point[] bottomPts = { bottomLeft, bottomRight };
		return bottomPts;
	}

	// finds top vertices of goal tape, left to right
	public static Point[] findTopPts(Point[] points, Rect rect) {
		Point rectTopRight = new Point(rect.tl().x + (rect.width - 1), rect.tl().y);
		Point rectTopLeft = new Point();
		rectTopLeft = rect.tl().clone();
		Point topRight = new Point();
		Point topLeft = new Point();

		double lowestDist = 0;
		for (int i = 0; i < points.length; i++) {
			double dist = Math.sqrt((points[i].x - rectTopLeft.x) * (points[i].x - rectTopLeft.x)
					+ (points[i].y - rectTopLeft.y) * (points[i].y - rectTopLeft.y));

			if (i == 0) {
				topLeft = points[i];
				lowestDist = dist;
			} else if (dist < lowestDist) {
				topLeft = points[i];
				lowestDist = dist;
			}
		}

		for (int i = 0; i < points.length; i++) {
			double dist = Math.sqrt((points[i].x - rectTopRight.x) * (points[i].x - rectTopRight.x)
					+ (points[i].y - rectTopRight.y) * (points[i].y - rectTopRight.y));

			if (i == 0) {
				topRight = points[i];
				lowestDist = dist;
			} else if (dist < lowestDist) {
				topRight = points[i];
				lowestDist = dist;
			}
		}

		Point[] topPts = { topLeft, topRight };
		return topPts;
	}

	/*
	 * //finds bottom vertices of goal tape public static Point[]
	 * findBottomPts(Point[] points) { Point highestY = points[0]; Point
	 * secondHighestY = points[1]; for(int i = 2; i < points.length; i++) {
	 * if(points[i].y > highestY.y) { highestY = points[i]; } else
	 * if(points[i].y > secondHighestY.y) { secondHighestY = points[i]; } }
	 * Point[] highestYCoords = {highestY, secondHighestY}; return
	 * highestYCoords; }
	 * 
	 * public static Point[] findTopPts(Point[] points) { Point lowestY =
	 * points[0]; Point secondLowestY = points[1]; for(int i = 2; i <
	 * points.length; i++) { if(points[i].y < lowestY.y) { lowestY = points[i];
	 * } else if(points[i].y < secondLowestY.y) { secondLowestY = points[i]; } }
	 * Point[] lowestYCoords = {lowestY, secondLowestY}; return lowestYCoords; }
	 */

	public static double pointDist(Point[] points) {
		double dist = Math.sqrt((points[0].x - points[1].x) * (points[0].x - points[1].x)
				+ (points[0].y - points[1].y) * (points[0].y - points[1].y));
		return dist;
	}

	public static double findDistToGoal(Point topPoints[], Point bottomPoints[]) {
		double topMidPointY = (topPoints[0].y + topPoints[1].y) / 2.0;
		double bottomMidPointY = (bottomPoints[0].y + bottomPoints[1].y) / 2.0;
		double degPerPixelVert = VERT_FOV_DEG / IMG_HEIGHT;
		double theta_b = degPerPixelVert * (((IMG_HEIGHT - 1) / 2.0) - bottomMidPointY);
		double theta_t = degPerPixelVert * (((IMG_HEIGHT - 1) / 2.0) - topMidPointY);
		double theta_w = CAM_EL_DEG + theta_t;
		double theta_rb = 90.0 - theta_w;
		double theta_hg = theta_t - theta_b;
		double theta_cb = CAM_EL_DEG + theta_b;
		double rb = (REAL_TAPE_HEIGHT * Math.sin(Math.toRadians(theta_rb))) / Math.sin(Math.toRadians(theta_hg));
		double distance = rb * (Math.cos(Math.toRadians(theta_cb)));
		return distance;

		/*
		 * double topMidPointY = (topPoints[0].y + topPoints[1].y) / 2.0; double
		 * bottomMidPointY = (bottomPoints[0].y + bottomPoints[1].y) / 2.0;
		 * double goalCenterHeight = Math.abs(topMidPointY - bottomMidPointY);
		 * double distance = (REAL_TAPE_HEIGHT *
		 * ((IMG_HEIGHT/2)/(goalCenterHeight))) / Math.tan(VERT_FOV_RAD/2.0);
		 * //Dylan's first dist formula return distance;
		 */
	}

	/*
	 * public static double findDistToGoal(double tapeWidth, double camAngle) {
	 * camAngle = Math.toRadians(camAngle); double tapeHeight = tapeWidth * 0.7;
	 * //ratio from height to width is 14/20 (0.7) double distance =
	 * (REAL_TAPE_HEIGHT * ((IMG_HEIGHT/2)/(tapeHeight))) /
	 * Math.tan(VERT_FOV/2.0); //Dylan's first dist formula //double distance =
	 * (REAL_TAPE_HEIGHT * ((((VERT_FOV/2 + camAngle)/VERT_FOV) *
	 * IMG_HEIGHT)/tapeHeight)) / Math.tan(VERT_FOV/2 + camAngle); //our dist
	 * formula double distError = Math.log10(distance/61.223) /
	 * Math.log10(1.0056); //power function of error of first distance formula
	 * return distError; }
	 */

	public static double findAzimuthGoal(Point topPoints[], Point bottomPoints[]) {
		double topMidPointX = (topPoints[0].x + topPoints[1].x) / 2.0;
		double bottomMidPointX = (bottomPoints[0].x + bottomPoints[1].x) / 2.0;
		double goalCenterX = (topMidPointX + bottomMidPointX) / 2.0;
		double degPerPixelHoriz = HORIZ_FOV_DEG / IMG_WIDTH;
		double imageCenterX = (IMG_WIDTH - 1) / 2.0;
		double azimuthGoalDeg = (goalCenterX - imageCenterX) * degPerPixelHoriz;

		return azimuthGoalDeg;
	}

	// facing forward relative to closest goal
	public static boolean isFacingForward(Point[] points) {
		return Math.abs(points[0].y - points[1].y) < 5;
	}

	// if true then robot turns right to face goal straight on
	public static boolean isFacingLeft(Point[] points) {
		if (points[0].y > points[1].y) {
			return points[0].x < points[1].x;
		}
		return points[0].x > points[1].x;
	}

	// finds angle at which camera is facing the goal
	public static double findLateralAngle(Point[] points) {
		double angle = Math.toDegrees(Math.asin(Math.abs(points[0].y - points[1].y) / pointDist(points)));
		return angle;
	}

	// midline of the closest goal
	public static boolean isOnMidline(Rect rect) {
		return Math.abs(IMG_WIDTH / 2 - (rect.x + rect.width / 2)) < 10;
	}

	// if true then robot is on right of midline
	public static boolean isOnRight(Rect rect) {
		return (rect.x + rect.width / 2) < IMG_WIDTH / 2;
	}

	public static double distFromMidline(Point[] bottomPts, Point[] topPts, double dist) {
		Point bottomLeft = bottomPts[0];
		if (bottomPts[1].x < bottomPts[0].x) {
			bottomLeft = bottomPts[1];
		}
		Point topLeft = topPts[0];
		if (topPts[1].x < topPts[0].x) {
			topLeft = topPts[1];
		}
		return dist * (Math.abs(bottomLeft.x - topLeft.x) / Math.abs(bottomLeft.y - topLeft.y));
	}
	
	public Boolean getSolutionFound()
	{
		return solutionFound;
	}
	
	public double getAzimuthGoalDeg()
	{
		return azimuthGoalDeg;
	}

	public double getDistToGoalInch()
	{
		return distToGoalInch;
	}
}