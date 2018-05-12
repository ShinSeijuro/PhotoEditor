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

    private boolean invalid;

    public boolean isInvalid() {
        return invalid;
    }

    public Crop(Image originalImage, Rectangle rect) {
        super(originalImage);
        this.rect = rect;
        this.invalid = !validateRect();

        setName("Crop");
    }

    private boolean validateRect() {
        double imageWidth = getOriginalImage().getWidth();
        double imageHeight = getOriginalImage().getHeight();

        if (!rect.intersects(0, 0, imageWidth, imageHeight)) {
            return false;
        }

        if (rect.getX() < 0) {
            rect.setWidth(rect.getWidth() + rect.getX());
            if (rect.getWidth() <= 0) {
                return false;
            }
            rect.setX(0);
        }

        if (rect.getX() + rect.getWidth() > imageWidth) {
            rect.setWidth(imageWidth - rect.getX());

            if (rect.getWidth() <= 0) {
                return false;
            }
        }

        if (rect.getY() < 0) {
            rect.setHeight(rect.getHeight() + rect.getY());
            if (rect.getHeight() <= 0) {
                return false;
            }
            rect.setY(0);
        }

        if (rect.getY() + rect.getHeight() > imageHeight) {
            rect.setHeight(imageHeight - rect.getY());

            if (rect.getHeight() <= 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected Image applyTransform(Image image) {
        if (isInvalid()) {
            return getOriginalImage();
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
