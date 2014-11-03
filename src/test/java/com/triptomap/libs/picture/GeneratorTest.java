package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;
import jodd.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static com.triptomap.libs.picture.math.TripPoint.Transport.*;
import static java.util.Arrays.asList;

/**
 * @author smecsia
 */
public class GeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        final String imagePath = getClass().getClassLoader().getResource("trip-cover.jpg").getPath();
        File file = new Generator(300, 250, 3000).generate(
                new File(imagePath),
                new ArrayList<>(
                        asList(
                                new TripPoint(61.039, 30.134, "London"),
                                new TripPoint(35.86, 139.64, "St.Petersburg").withTransport(PLANE),
                                new TripPoint(39.904, 116.40, "Moscow").withTransport(TRAIN),
                                new TripPoint(40.71, -74.006, "Tokyo").withTransport(CAR),
                                new TripPoint(50.71, -74.006, "Somewhere").withTransport(BOAT),
                                new TripPoint(42.5, 1.5218, "Another place").withTransport(BUS),
                                new TripPoint(32.019, 25.268, "Placesss...").withTransport(WALK)
                        )
                )
        );
        FileUtil.copy(file, new File(System.getProperty("user.home") + "/trip.png"));
    }
}
