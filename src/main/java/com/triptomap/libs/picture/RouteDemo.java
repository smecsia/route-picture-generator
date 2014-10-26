package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.CatmullRom;
import com.triptomap.libs.picture.math.Point2D;
import com.triptomap.libs.picture.math.TripPoint;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.triptomap.libs.picture.util.FileUtil.createTempFile;

/**
 * @author smecsia
 */
public class RouteDemo extends PApplet {

    PImage img, globe, fade;
    PFont font;
    private int width = 300;
    private int height = 200;
    private String filename = "trip.jpg";
    private File outputFile;
    private volatile boolean painted = false;

    List<TripPoint> vertexes = new ArrayList<TripPoint>();

    public RouteDemo() {
    }

    public RouteDemo(int width, int height, String filePath, List<TripPoint> vertexes) throws IOException {
        this.width = width;
        this.height = height;
        this.vertexes = vertexes;
        this.filename = filePath;
        outputFile = createTempFile("route.png");
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setup() {
        resize(width, height);
        size(getWidth(), getHeight());
        globe = loadImage("globe.png");
        img = loadImage(filename);
        fade = loadImage("fade-effect.png");
        noStroke();
        smooth();
        float aspect = ((float) height) / ((float) img.height);
        float fadeAspect = ((float) height) / ((float) fade.height);
        float gAspect = ((float) height) / ((float) globe.height) * 1.2f;
        img.resize((int) (img.width * aspect), (int) (img.height * aspect));
        globe.resize((int) (globe.width * gAspect), (int) (globe.height * gAspect));
        fade.resize((int) (fade.width * fadeAspect), (int) (fade.height * fadeAspect));

        noLoop();
        font = createFont("Verdana Bold", 10);
    }

    public boolean isPainted() {
        return painted;
    }

    public void draw() {
        tint(255, 255);
        image(img, width / 2 - img.width / 2, height / 2 - img.height / 2);
        tint(240);
        image(fade, width / 2 - fade.width / 2, height / 2 - fade.height / 2);
        tint(190);
        image(globe, width / 2 - globe.width / 2, height / 2 - globe.height / 2);
        tint(255, 255);

        if (vertexes.size() > 0) {

            noFill();
            setRouteStroke(2.5f);
            beginShape();

            if (vertexes.size() >= 2) {
                final TripPoint last = vertexes.get(vertexes.size() - 1);
                final TripPoint first = vertexes.get(0);
                CatmullRom catmullRom = new CatmullRom();
                catmullRom.addPoint(first.getXI(), first.getYI());
                for (TripPoint vertex : vertexes) {
                    catmullRom.addPoint(vertex.getXI(), vertex.getYI());
                }
                catmullRom.addPoint(last.getXI(), last.getYI());
                List<Point2D> points = catmullRom.getInterpolated(45);
                for (int i = 1; i < points.size(); i++) {
                    if (i > 2 && i < points.size() - 1)
                        line((float) points.get(i - 1).getX(), (float) points.get(i - 1).getY(),
                                (float) points.get(i).getX(), (float) points.get(i).getY());
                }
                fill(255);
                int transportIdx = 0;
                for (int i = 1; i < points.size(); i++) {
                    if (points.get(i).isCenter()) {
                        transport(vertexes.get(++transportIdx).getTransport(), points.get(i).getX(), points.get(i).getY());
                    }
                }
            }
            endShape();

            noStroke();

            Set<TripPoint> drawnPoints = new HashSet<TripPoint>();
            for (TripPoint vertex : vertexes) {
                noStroke();
                boolean found = false;
                for (TripPoint drawn : drawnPoints) {
                    if (dist(drawn.getXI(), drawn.getYI(), vertex.getXI(), vertex.getYI()) < 10) {
                        found = true;
                    }
                }
                if (found) {
                    continue;
                }
                fill(255, 255, 230);
                ellipse(vertex.getXI(), vertex.getYI(), 6, 6);

                int cityX = vertex.getXI() + 10;
                int cityY = vertex.getYI() - 6;

                if (cityX + textWidth(vertex.getName()) > getWidth()) {
                    cityX = (int) (getWidth() - textWidth(vertex.getName()) - 10);
                }
                cityText(vertex.getName(), cityX, cityY, 3);

                noFill();
                stroke(255, 255, 230);
                strokeWeight(2);
                ellipse(vertex.getXI(), vertex.getYI(), 10, 10);

                drawnPoints.add(vertex);
            }
        }
        save(outputFile.getPath());
        painted = true;
    }

    private void setRouteStroke(float width) {
        float[] dashes = {3.0f, 10.0f};
        stroke(255, 255, 230, 255);
        BasicStroke pen = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                4.0f, dashes, 0.0f);
        Graphics2D g2 = ((PGraphicsJava2D) g).g2;
        g2.setStroke(pen);
    }

    void transport(TripPoint.Transport transport, int x, int y) {
        PImage tImg = loadImage("icon-" + transport.name().toLowerCase() + ".png");
        tImg.resize(20, 20);
        image(tImg, x - 10, y - 10);
    }

    void cityText(String message, int x, int y, int shadowSize) {
        textFont(font);
        fill(255, 255, 255, 255);
        text(message, x, y);
    }

    private String randomCity() {
        final String[] cities = {
                "London", "St.Petersrburg", "Moscow",
                "Tokyo", "Paris", "New-York", "Los-Angeles"
        };
        return cities[((int) random(cities.length))];
    }

    public void mousePressed() {
        vertexes.add(new TripPoint(mouseX, mouseY, randomCity()));
        clear();
        redraw();
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", RouteDemo.class.getName()});
    }
}
