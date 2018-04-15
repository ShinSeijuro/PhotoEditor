/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.AbstractImageAction;
import Action.INameable;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author CMQ
 */
public class Flip extends AbstractImageAction {

    public enum Orientation implements INameable {
        Horizontal {
            @Override
            public void setName(String newName) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() {
                return "horizontal";
            }
        },
        Vertical {
            @Override
            public void setName(String newName) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() {
                return "vertical";
            }

        }
    }

    private Orientation orientation;

    public Orientation getOrientation() {
        return orientation;
    }

    public Flip(BufferedImage originalImage, Orientation orientation) {
        super(originalImage);
        this.orientation = orientation;

        setName("Flip " + orientation.getName());
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        switch (orientation) {
            case Horizontal:
                AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
                tx.translate(-image.getWidth(), 0);
                AffineTransformOp op = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;
            case Vertical:
                tx = AffineTransform.getScaleInstance(1, -1);
                tx.translate(0, -image.getHeight());
                op = new AffineTransformOp(tx,
                        AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                image = op.filter(image, null);
                break;
        }

        return image;
    }

}
