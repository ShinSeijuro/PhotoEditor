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
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class GrayScale extends AbstractImageAction {

    public GrayScale(Image originalImage) {
        super(originalImage);

        setName("Black&White");
    }

    @Override
    protected Image applyTransform(Image image) {
        BufferedImage newImage = SwingFXUtils.fromFXImage(image, null);
        ColorConvertOp op = new ColorConvertOp(
                ColorSpace.getInstance(ColorSpace.CS_GRAY),
                null);

        newImage = op.filter(newImage, null);
        return SwingFXUtils.toFXImage(newImage, null);
    }

}
