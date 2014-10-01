package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;

import java.util.List;

/**
 * @author smecsia
 */
public class ViewPort {
    TripPoint leftTop = new TripPoint(Double.MAX_VALUE, Double.MAX_VALUE);
    TripPoint rightBottom = new TripPoint(Double.MIN_VALUE, Double.MIN_VALUE);
    private int padding;
    private double borderSize;

    public ViewPort(double borderSize, int padding) {
        this.borderSize = borderSize;
        this.padding = padding;
    }

    public void adjustToViewPort(double width, double height, List<TripPoint> places) {
        for (TripPoint place : places) {
            if (place.getY() < leftTop.getY() && place.getX() < leftTop.getX()) {
                leftTop = place;
            }
            if (place.getY() > rightBottom.getY() && place.getX() > rightBottom.getX()) {
                rightBottom = place;
            }
        }
        double scaleX = width / (rightBottom.getX() - leftTop.getX());
        double scaleY = height / (rightBottom.getY() - leftTop.getY());
        for (TripPoint place : places) {
            place.setX((place.getX() - leftTop.getX()) * scaleX * borderSize + padding);
            place.setY((place.getY() - leftTop.getY()) * scaleY * borderSize + padding);
        }
    }

    public TripPoint getLeftTop() {
        return leftTop;
    }

    public TripPoint getRightBottom() {
        return rightBottom;
    }
}
