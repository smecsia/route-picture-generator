package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.TripPoint;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * @author smecsia
 */
public class Generator {

    private double width;
    private double height;
    private int waitTimeout;

    public Generator(double width, double height, int waitTimeout) {
        this.width = width;
        this.height = height;
        this.waitTimeout = waitTimeout;
    }

    public File generate(final File picture, final List<TripPoint> places) throws IOException, InterruptedException {
        new ViewPort(0.75, 15).adjustToViewPort(width, height, places);
        final RouteDemo applet = new RouteDemo((int) width, (int) height, picture.getPath(), places);
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
}
