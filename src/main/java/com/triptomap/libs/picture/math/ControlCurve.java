package com.triptomap.libs.picture.math;


import java.util.ArrayList;
import java.util.List;

public class ControlCurve {

    protected List<Point2D> pts = new ArrayList<Point2D>();
    protected int selection = -1;

    /**
     * add a control point, return index of new control point
     */
    public int addPoint(int x, int y) {
        pts.add(new Point2D(x, y));
        return selection = pts.size() - 1;
    }
}
