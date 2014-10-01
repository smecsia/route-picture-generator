package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;
import jodd.io.FileUtil;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static java.util.Arrays.asList;

/**
 * @author smecsia
 */
public class GeneratorTest {

    @Test
    public void testGenerate() throws Exception {
        File file = new Generator(300, 250, 3000).generate(
                new File("/home/smecsia/projects/route-picture-generator/src/main/resources/trip.jpg"),
                new ArrayList<TripPoint>(
                        asList(
                                new TripPoint(0.0, 0.0, "London"),
                                new TripPoint(50.0, 30.0, "St.Petersburg"),
                                new TripPoint(60.0, 90.0, "Moscow"),
                                new TripPoint(40.0, 80.0, "Tokyo"),
                                new TripPoint(50.0, 100.0, "Another place")
                        )
                )
        );
        FileUtil.copy(file, new File(System.getProperty("user.home") + "/trip.png"));
    }
}
