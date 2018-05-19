/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.AbstractImageAction;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class Crop extends AbstractImageAction {

    private Rectangle rect;

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public Crop(Rectangle rect) {
        super();
        this.rect = rect;

        setName("Crop");
    }

    public boolean validateRect(Image originalImage) {
        if (rect == null) {
            return false;
        }

        double imageWidth = originalImage.getWidth();
        double imageHeight = originalImage.getHeight();

        if (!rect.intersects(0, 0, imageWidth, imageHeight)) {
            return false;
        }

        if (rect.getX() < 0) {
            rect.setWidth(rect.getWidth() + rect.getX());
            if (rect.getWidth() <= 1) {
                return false;
            }
            rect.setX(0);
        }

        if (rect.getX() + rect.getWidth() > imageWidth) {
            rect.setWidth(imageWidth - rect.getX());

            if (rect.getWidth() <= 1) {
                return false;
            }
        }

        if (rect.getY() < 0) {
            rect.setHeight(rect.getHeight() + rect.getY());
            if (rect.getHeight() <= 1) {
                return false;
            }
            rect.setY(0);
        }

        if (rect.getY() + rect.getHeight() > imageHeight) {
            rect.setHeight(imageHeight - rect.getY());

            if (rect.getHeight() <= 1) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Image applyTransform(Image image) {
        if (!validateRect(image)) {
            return image;
        }

        WritableImage output = new WritableImage(
                image.getPixelReader(),
                (int) rect.getX(),
                (int) rect.getY(),
                (int) rect.getWidth(),
                (int) rect.getHeight());

        return output;
    }
}
