package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;

import java.util.List;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;

/**
 * @author smecsia
 */
public class ViewPort {
    TripPoint leftTop = new TripPoint(Double.MAX_VALUE, Double.MAX_VALUE);
    TripPoint rightBottom = new TripPoint(Double.MIN_VALUE, Double.MIN_VALUE);
    private int padding;
    private double borderSize;
    private double width;
    private double height;

    public ViewPort(double borderSize, int padding, double width, double height) {
        this.borderSize = borderSize;
        this.padding = padding;
        this.width = width;
        this.height = height;
    }

    public void adjustToViewPort(List<TripPoint> places) {
        for (TripPoint place : places) {
            if (place.getY() < leftTop.getY()) {
                leftTop.setY(place.getY());
            }
            if (place.getX() < leftTop.getX()) {
                leftTop.setX(place.getX());
            }
            if (place.getY() > rightBottom.getY()) {
                rightBottom.setY(place.getY());
            }
            if (place.getX() > rightBottom.getX()) {
                rightBottom.setX(place.getX());
            }
        }
        double scaleX = width / (rightBottom.getX() - leftTop.getX());
        double scaleY = height / (rightBottom.getY() - leftTop.getY());
        for (TripPoint place : places) {
            place.setX((place.getX() - leftTop.getX()) * scaleX * borderSize + padding);
            place.setY((place.getY() - leftTop.getY()) * scaleY * borderSize + padding);
            if (isInfinite(scaleX) || isNaN(scaleX)) {
                place.setX(width / 2.0);
            }
            if (isInfinite(scaleY) || isNaN(scaleY)) {
                place.setY(height / 2.0);
            }
        }
    }

    public void convertToFlat(List<TripPoint> places) {
        for (TripPoint place : places) {
            double latRads = Math.toRadians(place.getLatitude());
            double pixZoom = (1 << 6) * 256;
            double x = (Math.floor(((180.0 + place.getLongitude()) / 360.0) * pixZoom));
            double y = (Math.floor((1 - Math.log(Math.tan(latRads) + 1 / Math.cos(latRads)) / Math.PI) / 2 * pixZoom));
            //Add in offsets
//            x -= 1948;
//            y -= 5268;
            place.setX(x);
            place.setY(y);
        }
    }


    public TripPoint getLeftTop() {
        return leftTop;
    }

    public TripPoint getRightBottom() {
        return rightBottom;
    }
}
