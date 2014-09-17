package com.triptomap.libs.picture.math;

/**
 * @author smecsia
 */
public class Point2D {

    int x, y;

    boolean isCenter = false;
    boolean isControl = false;

    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2D point2D = (Point2D) o;

        if (x != point2D.x) return false;
        if (y != point2D.y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public void setCenter(boolean center) {
        isCenter = center;
    }

    public boolean isControl() {
        return isControl;
    }

    public void setControl(boolean control) {
        isControl = control;
    }
}
