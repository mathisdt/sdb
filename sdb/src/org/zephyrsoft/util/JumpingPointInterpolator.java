package org.zephyrsoft.util;

import java.awt.*;
import org.pushingpixels.trident.interpolator.*;

/**
 * ATTENTION: this is unfinished work in progress!
 */
public class JumpingPointInterpolator<P extends Point> implements PropertyInterpolator<Point> {
	
	private static int diffPixels = 3;
	
	private Point lastPoint = null;
	
	public Point interpolate(Point from, Point to, float timelinePosition) {
		int x = from.x + (int) (timelinePosition * (to.x - from.x));
		int y = from.y + (int) (timelinePosition * (to.y - from.y));
//		System.out.println("interpolate: from=(" + from.x + "/" + from.y + ") to=(" + to.x + "/" + to.y + ") timelinePosition=" + timelinePosition + " result=(" + x + "/" + y + ")");
		Point toReturn = new Point(x, y);
		if (lastPoint == null) {
			lastPoint = toReturn;
		} else {
			// Differenz in Y-Richtung muss mindestens "diffPixels" Pixel sein
			if (Math.abs(lastPoint.y - toReturn.y) < diffPixels && timelinePosition!=0.0 && timelinePosition!=1.0) {
				toReturn = lastPoint;
				System.out.println("returned last point");
			} else {
				lastPoint = toReturn;
				System.out.println("returned new point");
			}
		}
		return toReturn;
	}

	public Class getBasePropertyClass() {
		return Point.class;
	}
}
