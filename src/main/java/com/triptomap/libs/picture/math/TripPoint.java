package com.triptomap.libs.picture.math;

/**
 * @author smecsia
 */
public class TripPoint {
    double x, y;
    String name;
    Transport transport;

    public static enum Transport {
        CAR, TRAIN, PLANE, BUS
    }

    public TripPoint() {
    }

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

    public double getLongitude() {
        return y;
    }

    public double getLatitude() {
        return x;
    }

    public void setLatitude(double lat) {
        this.x = lat;
    }

    public void setLongitude(double lon) {
        this.y = lon;
    }

    public TripPoint withLatitude(double lat) {
        setLatitude(lat);
        return this;
    }

    public TripPoint withLongitude(double lon) {
        setLongitude(lon);
        return this;
    }

    public TripPoint withTransport(Transport transport) {
        setTransport(transport);
        return this;
    }

    public TripPoint withName(String name) {
        setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }
}
