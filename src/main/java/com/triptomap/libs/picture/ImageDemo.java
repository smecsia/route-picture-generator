package com.triptomap.libs.picture;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * @author smecsia
 */
public class ImageDemo extends PApplet {

    PImage img;
    int pointillize = 10;

    public void setup() {
        setSize(500, 500);
        size(getWidth(), getHeight());
        img = loadImage("sunflower.jpg");
        img.resize(getWidth(), getHeight());
        background(0);
        smooth();
    }

    public void draw() {
        // Pick a random point
        int x = (int) random(img.width);
        int y = (int) random(img.height);
        int loc = x + y * img.width;

        // Look up the RGB color in the source image
        loadPixels();
        float r = red(img.pixels[loc]);
        float g = green(img.pixels[loc]);
        float b = blue(img.pixels[loc]);
        noStroke();

        // Draw an ellipse at that location with that color
        fill(r, g, b, 100);
        ellipse(x, y, pointillize, pointillize);
    }

    public static void main(String args[]) {
        PApplet.main(new String[]{"--present", ImageDemo.class.getName()});
    }
}
