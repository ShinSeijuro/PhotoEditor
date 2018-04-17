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
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 *
 * @author Yuuki
 */
public class BoxBlur extends AbstractImageAction {

    public BoxBlur(BufferedImage originalImage) {
        super(originalImage);
        this.setName("Box Blur");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        float alpha = 9;
        float[] blurKernel = {
            (float) (1 / alpha), (float) (1 / alpha), (float) (1 / alpha),
            (float) (1 / alpha), (float) (1 / alpha), (float) (1 / alpha),
            (float) (1 / alpha), (float) (1 / alpha), (float) (1 / alpha)};
        BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
        image = blur.filter(image, new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
        return image;
    }

}
