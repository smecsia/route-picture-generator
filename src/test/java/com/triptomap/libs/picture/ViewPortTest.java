package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Arrays.asList;

/**
 * @author smecsia
 */
public class ViewPortTest {

    @Test
    public void testConvertToFlat() throws Exception {
        final ViewPort viewPort = new ViewPort(0.85, 15, 300, 250);
        final ArrayList<TripPoint> places = new ArrayList<>(
                asList(
                        new TripPoint(61.039, 30.134, "London"),
                        new TripPoint(35.86, 139.64, "St.Petersburg"),
                        new TripPoint(39.904, 116.40, "Moscow"),
                        new TripPoint(40.71, -74.006, "Tokyo"),
                        new TripPoint(42.5, 1.5218, "Another place"),
                        new TripPoint(32.019, 25.268, "Placesss...")
                )
        );
        viewPort.convertToFlat(places);
        for (TripPoint place : places) {
            System.out.println("Place: " + place.getX() + ";" + place.getY() + " : " + place.getName());
        }
        viewPort.adjustToViewPort(places);
        for (TripPoint place : places) {
            System.out.println("Place: " + place.getX() + ";" + place.getY() + " : " + place.getName());
        }
    }
}
