package com.triptomap.libs.picture.math;

/**
 * @author smecsia
 */
public class TripPoint {
    double x, y;
    String name;

    public TripPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public TripPoint(double x, double y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getYI() {
        return (int) y;
    }

    public int getXI() {
        return (int) x;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
