/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adjustment;

import Action.AbstractImageAction;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

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
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                null);

        image = op.filter(image, null);
        return image;
    }

}
