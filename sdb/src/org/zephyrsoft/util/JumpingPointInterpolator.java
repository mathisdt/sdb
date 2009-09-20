package org.zephyrsoft.util;

import java.awt.*;
import org.pushingpixels.trident.interpolator.*;

public class JumpingPointInterpolator implements PropertyInterpolator<Point> {
	
	// MDT Intervallgröße abhängig von der zu überbrückenden Stecke wählen
	
	public Point interpolate(Point from, Point to, float timelinePosition) {
		int x = from.x + (int) (timelinePosition * (to.x - from.x));
		int y = from.y + (int) (timelinePosition * (to.y - from.y));
//		System.out.println("interpolate: from=(" + from.x + "/" + from.y + ") to=(" + to.x + "/" + to.y + ") timelinePosition=" + timelinePosition + " result=(" + x + "/" + y + ")");
		return new Point(x, y);
	}

	public Class getBasePropertyClass() {
		return Point.class;
	}
}
