package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author smecsia
 */
public class Generator {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private double width;
    private double height;
    private int waitTimeout;
    private double borderSize = 0.80;
    private int padding = 20;

    public Generator(double width, double height, int waitTimeout) {
        this.width = width;
        this.height = height;
        this.waitTimeout = waitTimeout;
    }

    public File generate(File picture, final List<TripPoint> places) throws IOException, InterruptedException {
        final ViewPort viewPort = new ViewPort(borderSize, padding, width, height);
        viewPort.convertToFlat(places);
        viewPort.adjustToViewPort(places);
        final RouteDrawer applet = new RouteDrawer((int) width, (int) height, picture.getPath(), places);
        applet.init();
        JFrame window = new JFrame();
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.add(applet);
        window.setSize((int) width, (int) height);
        window.setContentPane(contentPane);
        window.setVisible(true);
        contentPane.setBackground(new Color(0, 167, 191));
        int timeout = 0;
        while (!applet.isPainted() && timeout < waitTimeout) {
            timeout += 30;
            sleep(30);
        }
        final File outputFile = applet.getOutputFile();
        window.dispose();
        return outputFile;
    }

    public static BufferedImage transformImage(BufferedImage image, AffineTransform transform) throws Exception {

        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BICUBIC);

        BufferedImage destinationImage = op.createCompatibleDestImage(image, (image.getType() == BufferedImage.TYPE_BYTE_GRAY) ? image.getColorModel() : null);
        Graphics2D g = destinationImage.createGraphics();
        g.setBackground(Color.WHITE);
        g.clearRect(0, 0, destinationImage.getWidth(), destinationImage.getHeight());
        destinationImage = op.filter(image, destinationImage);
        return destinationImage;
    }

    public double getBorderSize() {
        return borderSize;
    }

    public void setBorderSize(double borderSize) {
        this.borderSize = borderSize;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }
}
