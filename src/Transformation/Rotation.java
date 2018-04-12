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

    public void setRadian(double radian) {
        this.radian = radian;
    }

    public Rotation(BufferedImage originalImage, double radian) {
        super(originalImage);
        this.radian = radian;
    }

    @Override
    public BufferedImage applyTransform(BufferedImage image) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radian, image.getWidth() / 2, image.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);

        return image;
    }

    public int getAngle() {
        return (int) Math.toDegrees(radian);
    }

}
