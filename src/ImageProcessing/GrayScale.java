/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import static java.awt.color.ColorSpace.CS_GRAY;
import static java.awt.color.ColorSpace.getInstance;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class GrayScale extends AbstractImageAction {

    public GrayScale() {
        setName("Black&White");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage newImage = fromFXImage(image, null);
        ColorConvertOp op = new ColorConvertOp(
                getInstance(CS_GRAY),
                null);

        newImage = op.filter(newImage, null);
        return toFXImage(newImage, null);
    }

}
