/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 *
 * @author Yuuki
 */
public class Sharpen extends AbstractImageAction {

    public Sharpen(BufferedImage originalImage) {
        super(originalImage);
        this.setName("Sharpen");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        float[] sharpenKernel = {
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0};
        BufferedImageOp sharpen = new ConvolveOp(new Kernel(3, 3, sharpenKernel));
        image = sharpen.filter(image, new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
        return image;
    }

}
