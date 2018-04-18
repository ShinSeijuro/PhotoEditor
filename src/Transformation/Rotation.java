/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.AbstractImageAction;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author CMQ
 */
public class Rotation extends AbstractImageAction {

    private double radian;

    public double getRadian() {
        return radian;
    }

    public Rotation(BufferedImage originalImage, double radian) {
        super(originalImage);
        this.radian = radian;

        setName("Rotate " + (radian < 0 ? "left" : "right") + " " + Math.abs(Math.toDegrees(radian)) + "°");
    }

    @Override
    public BufferedImage applyTransform(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radian, (double) image.getWidth() / 2.0, (double) image.getHeight() / 2.0);
        double offset
                = (radian >= 0
                        ? (image.getWidth() - image.getHeight())
                        : (image.getHeight() - image.getWidth()))
                / 2;
        transform.translate(offset, offset);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage newImage = null;
        switch (getAngle()) {
            case -90:
            case 90:
                newImage = new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
                break;
            case -180:
            case 180:
                newImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
                break;
        }

        op.filter(image, newImage);
        return newImage;
    }

    public int getAngle() {
        return (int) Math.toDegrees(radian);
    }

}
