package com.triptomap.libs.picture;

import com.triptomap.libs.picture.math.CatmullRom;
import com.triptomap.libs.picture.math.Point2D;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author smecsia
 */
public class RouteDemo extends PApplet {

    PImage img, globe;
    PFont font;

    class Vertex {
        int x, y;
        String city;

        Vertex(int x, int y, String city) {
            this.x = x;
            this.y = y;
            this.city = city;
        }

        int getX() {
            return x;
        }

        int getY() {
            return y;
        }
    }

    class BezierCurve {
        float x1, y1, x2, y2;
        float ax1, ay1;
    }


    enum Transport {
        CAR, TRAIN, PLANE, BUS
    }

    List<Vertex> vertexes = new ArrayList<Vertex>();

    public void setup() {
        resize(800, 600);
        size(getWidth(), getHeight());
        globe = loadImage("globe.jpg");
        img = loadImage("trip.jpg");
        noStroke();
        smooth();
        float aspect = ((float) height) / ((float) img.height);
        float gAspect = ((float) height) / ((float) globe.height)  * 2.2f;
        img.resize((int) (img.width * aspect), (int) (img.height * aspect));
        globe.resize((int) (globe.width * gAspect), (int) (globe.height * gAspect));

        noLoop();
        font = createFont("Verdana Bold", 22);
    }

    public void draw() {
        tint(255, 255);
        image(img, width / 2 - img.width / 2, height / 2 - img.height / 2);
        tint(175, 70);
        image(globe, width / 2 - globe.width / 2, height / 2 - globe.height / 2);
        tint(255, 255);

        if (vertexes.size() > 0) {

            noFill();
            setRouteStroke(10.0f);
            beginShape();

            if (vertexes.size() >= 2) {
                final Vertex last = vertexes.get(vertexes.size() - 1);
                final Vertex first = vertexes.get(0);
                CatmullRom catmullRom = new CatmullRom();
                catmullRom.addPoint(first.getX(), first.getY());
                for (Vertex vertex : vertexes) {
                    catmullRom.addPoint(vertex.x, vertex.y);
                }
                catmullRom.addPoint(last.getX(), last.getY());
                List<Point2D> points = catmullRom.getInterpolated(15);
                for (int i = 1; i < points.size(); i++) {
                    line((float) points.get(i - 1).getX(), (float) points.get(i - 1).getY(),
                            (float) points.get(i).getX(), (float) points.get(i).getY());
                    if (points.get(i).isCenter()) {
                        transport(randomTransport(), points.get(i).getX(), points.get(i).getY());
                    }
                }
            }
            endShape();

            noStroke();

            for (Vertex vertex : vertexes) {
                noStroke();
                fill(255, 255, 230);
                ellipse(vertex.x, vertex.y, 15, 15);

                int cityX = vertex.x + 22;
                int cityY = vertex.y - 12;

                if (cityX + textWidth(vertex.city) > getWidth()) {
                    cityX = (int) (getWidth() - textWidth(vertex.city) - 10);
                }
                cityText(vertex.city, cityX, cityY, 3);

                noFill();
                stroke(255, 255, 230);
                strokeWeight(4);
                ellipse(vertex.x, vertex.y, 24, 24);

            }


        }
        save(System.getProperty("user.home") + "/map.png");
    }

    private void setRouteStroke(float width) {
        float[] dashes = {6.0f, 20.0f};
        stroke(255, 255, 230, 180);
        BasicStroke pen = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                4.0f, dashes, 0.0f);
        Graphics2D g2 = ((PGraphicsJava2D) g).g2;
        g2.setStroke(pen);
    }

    Transport randomTransport() {
        return Transport.values()[((int) random(Transport.values().length - 1))];
    }

    void transport(Transport transport, int x, int y) {
        PImage tImg = loadImage("icon-" + transport.name().toLowerCase() + ".png");
        tImg.resize(50, 50);
        image(tImg, x - 25, y - 25);
    }

    void cityText(String message, int x, int y, int shadowSize) {
        textFont(font);
        for (int i = 1; i <= shadowSize; ++i) {
            fill(0, 0, 0, 2);
            for (int j = 1; j <= shadowSize; ++j) {
                // y
                text(message, x - i, y);
                text(message, x - j, y);
                text(message, x, y);
                text(message, x + i, y);
                text(message, x + j, y);
                // y - i
                text(message, x - i, y - i);
                text(message, x - j, y - i);
                text(message, x, y - i);
                text(message, x + i, y - i);
                text(message, x + j, y - i);
                // y - j
                text(message, x - i, y - j);
                text(message, x - j, y - j);
                text(message, x, y - j);
                text(message, x + i, y - j);
                text(message, x + j, y - j);
                // y + i
                text(message, x - i, y + i);
                text(message, x - j, y + i);
                text(message, x, y + i);
                text(message, x + i, y + i);
                text(message, x + j, y + i);
                // y + j
                text(message, x - i, y + j);
                text(message, x - j, y + j);
                text(message, x, y + j);
                text(message, x + i, y + j);
                text(message, x + j, y + j);
            }

            fill(0, 0, 0, 120);
            text(message, x - i, y - i);
            text(message, x - i, y + i);
            text(message, x + i, y - i);
            text(message, x + i, y + i);
        }
        fill(248, 178, 78, 200);
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
        vertexes.add(new Vertex(mouseX, mouseY, randomCity()));
        clear();
        redraw();

    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", RouteDemo.class.getName()});
    }
}
