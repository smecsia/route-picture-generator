package com.triptomap.libs.picture.math;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Bspline extends ControlCurve {

    public static final int STEP_SIZE_QUOTIENT = 300;

    // the basis function for a cubic B spline
    float b(int i, float t) {
        switch (i) {
            case -2:
                return (((-t + 3) * t - 3) * t + 1) / 6;
            case -1:
                return (((3 * t - 6) * t) * t + 4) / 6;
            case 0:
                return (((-3 * t + 3) * t + 3) * t + 1) / 6;
            case 1:
                return (t * t * t) / 6;
        }
        return 0; //we only get here if an invalid i is specified
    }

    //evaluate a point on the B spline
    Point2D p(int i, float t) {
        float px = 0;
        float py = 0;
        for (int j = -2; j <= 1; j++) {
            px += b(j, t) * pts.get(i + j).getX();
            py += b(j, t) * pts.get(i + j).getY();
        }
        return new Point2D(Math.round(px), Math.round(py));
    }

    public List<Point2D> getInterpolated(int stepSize) {
        List<Point2D> result = new ArrayList<Point2D>();
        Point2D q = p(2, 0);
        result.add(new Point2D(q.x, q.y));
        for (int i = 2; i < pts.size() - 1; i++) {
            Point2D prev = pts.get(i - 1);
            Point2D curr = pts.get(i);
            final double steps = stepSize * dist(prev, curr) / STEP_SIZE_QUOTIENT;
            final int halfSteps = (int) (steps / 2.0);
            for (int j = 1; j <= steps; j++) {
                q = p(i, j / (float) steps);
                final Point2D point = new Point2D(q.x, q.y);
                if (j == halfSteps) {
                    point.setCenter(true);
                }
                if (j < 2 || j == steps) {
                    point.setControl(true);
                }
                result.add(point);
            }
        }
        return result;
    }

    private double dist(Point2D prev, Point2D curr) {
        return sqrt(pow(curr.getX() - prev.getX(), 2) + pow(curr.getY() - prev.getY(), 2));
    }

}
