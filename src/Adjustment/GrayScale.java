/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adjustment;

import Action.AbstractImageAction;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author CMQ
 */
public class GrayScale extends AbstractImageAction {

    public GrayScale(BufferedImage originalImage) {
        super(originalImage);

        setName("Black&White");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        BufferedImage output = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics graphics = output.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return output;
    }

}
