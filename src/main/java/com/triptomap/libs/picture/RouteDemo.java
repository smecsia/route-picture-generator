package com.triptomap.libs.picture;

import com.triptomap.libs.picture.algo.Bezier;
import org.apache.commons.collections4.Transformer;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphicsJava2D;
import processing.core.PImage;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.collect;

/**
 * @author smecsia
 */
public class RouteDemo extends PApplet {

    PImage img;
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

    enum Transport {
        CAR, TRAIN, PLANE, BUS
    }

    List<Vertex> vertexes = new ArrayList<Vertex>();

    public void setup() {
        resize(800, 600);
        size(getWidth(), getHeight());
        img = loadImage("trip.jpg");
        noStroke();
        smooth();
        img.resize(getWidth(), getHeight());
        noLoop();
        font = createFont("Verdana Bold", 22);
    }

    public void draw() {
        image(img, 0, 0);

        if (vertexes.size() > 0) {

            float[] dashes = {6.0f, 20.0f};
            stroke(255, 255, 230, 180);
            BasicStroke pen = new BasicStroke(10.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER,
                    4.0f, dashes, 0.0f);
            Graphics2D g2 = ((PGraphicsJava2D) g).g2;
            g2.setStroke(pen);


            Bezier bezier = new Bezier(collect(vertexes, new Transformer<Vertex, Point2D>() {
                @Override
                public Point2D transform(Vertex input) {
                    return new Point2D.Double(input.x, input.y);
                }
            }).toArray(new Point2D[vertexes.size()]));

            final Vertex last = vertexes.get(vertexes.size() - 1);
            final Vertex first = vertexes.get(0);
            noFill();
            beginShape();
//            for (Point2D vertex : bezier.getPoints()) {
//                vertex((float) vertex.getX(), (float) vertex.getY());
//            }
            curveVertex(first.x, first.y);
            for (Vertex vertex : vertexes) {
                curveVertex(vertex.x, vertex.y);
            }
            curveVertex(last.x, last.y);
            endShape();
            noStroke();

            for (Vertex vertex : vertexes) {
                noStroke();
                fill(255, 255, 230);
                ellipse(vertex.x, vertex.y, 15, 15);

                strokeText(vertex.city, vertex.x + 22, vertex.y - 12, 3);

                noFill();
                stroke(255, 255, 230);
                strokeWeight(4);
                ellipse(vertex.x, vertex.y, 24, 24);

            }

            for (int i = 1; i < vertexes.size(); i++) {
                transport(randomTransport(), vertexes.get(i - 1).x, vertexes.get(i - 1).y,
                        vertexes.get(i).x, vertexes.get(i).y);
            }
        }
        save(System.getProperty("user.home") + "/map.png");
    }

    Transport randomTransport() {
        return Transport.values()[((int) random(Transport.values().length - 1))];
    }

    void transport(Transport transport, int x1, int y1, int x2, int y2) {
        PImage tImg = loadImage("icon-" + transport.name().toLowerCase() + ".png");
        tImg.resize(50, 50);
        image(tImg, (x1 + x2) / 2 - 25, (y1 + y2) / 2 - 25);
    }

    void strokeText(String message, int x, int y, int shadowSize) {
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
