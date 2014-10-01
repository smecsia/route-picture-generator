package com.triptomap.libs.picture.util;

import java.io.File;
import java.io.IOException;

/**
 * @author Ilya Sadykov (mailto: smecsia@yandex-team.ru)
 */
public class FileUtil {

    public static final String TRIPTOMAP = "triptomap-";

    public static File createTempFile(String suffix) throws IOException {
        final File tempFile = jodd.io.FileUtil.createTempFile(TRIPTOMAP, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }
}
