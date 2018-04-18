/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;

/**
 *
 * @author CMQ
 */
public class Crop extends AbstractImageAction {

    private int X, Y, WIDTH, HEIGHT;

    public int getX() {
        return X;
    }

    public void setX(int X) {
        this.X = X;
    }

    public int getY() {
        return Y;
    }

    public void setY(int Y) {
        this.Y = Y;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public Crop(BufferedImage originalImage, int x, int y, int width, int height) {
        super(originalImage);
        this.X = x;
        this.Y = y;
        this.WIDTH = width;
        this.HEIGHT = height;
        setName("Crop");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        image = image.getSubimage(X, Y, WIDTH, HEIGHT);
        return image;
    }

}
