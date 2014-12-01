package com.triptomap.libs.picture.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.jpeg.JpegDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Ilya Sadykov
 */
public class ImageUtil {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);


    // Inner class containing image information
    public static class ImageInformation {
        public final int orientation;
        public final int width;
        public final int height;
        public float scale;

        public ImageInformation(int orientation, int width, int height) {
            this.orientation = orientation;
            this.width = width;
            this.height = height;
        }

        public float getScale() {
            return scale;
        }

        public void setScale(float scale) {
            this.scale = scale;
        }

        public String toString() {
            return String.format("%dx%d,%d", this.width, this.height, this.orientation);
        }
    }

    public static ImageInformation readImageInformation(File imageFile)
            throws IOException, MetadataException, ImageProcessingException, SAXException {
        Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
        JpegDirectory jpegDirectory = (JpegDirectory) metadata.getDirectory(JpegDirectory.class);

        int orientation = 1;
        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (MetadataException me) {
            logger.warn("Could not get orientation", me);
        }
        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();

        return new ImageInformation(orientation, width, height);
    }

    public static File adjustOrientation(File imageFile) throws Exception {
        final ImageInformation info = readImageInformation(imageFile);
        final AffineTransform transform = getExifTransformation(info);
        final BufferedImage image = ImageIO.read(imageFile);
        final BufferedImage result = transformImage(image, transform);
        File outputFile = File.createTempFile("generated", "image");
        outputFile.deleteOnExit();
        ImageIO.write(result, "jpg", outputFile);
        return outputFile;
    }

    // Look at http://chunter.tistory.com/143 for information
    public static AffineTransform getExifTransformation(ImageInformation info) {

        AffineTransform t = new AffineTransform();

        switch (info.orientation) {
            case 1:
                break;
            case 2: // Flip X
                t.scale(-1.0, 1.0);
                t.translate(-info.width, 0);
                break;
            case 3: // PI rotation
                t.translate(info.width, info.height);
                t.rotate(Math.PI);
                break;
            case 4: // Flip Y
                t.scale(1.0, -1.0);
                t.translate(0, -info.height);
                break;
            case 5: // - PI/2 and Flip X
                t.rotate(-Math.PI / 2);
                t.scale(-1.0, 1.0);
                break;
            case 6: // -PI/2 and -width
                t.translate(info.height, 0);
                t.rotate(Math.PI / 2);
                break;
            case 7: // PI/2 and Flip
                t.scale(-1.0, 1.0);
                t.translate(-info.height, 0);
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
            case 8: // PI / 2
                t.translate(0, info.width);
                t.rotate(3 * Math.PI / 2);
                break;
        }
        return t;
    }

    public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception {

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage destinationImage = op.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
        Graphics2D g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(image, destinationImage);
        ;
        return destinationImage;
    }
}
