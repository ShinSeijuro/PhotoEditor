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
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author Yuuki
 */
public class Sharpen extends AbstractImageAction {

    public Sharpen() {
        setName("Sharpen");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        float[] sharpenKernel = {
            0, -1, 0,
            -1, 5, -1,
            0, -1, 0};
        BufferedImageOp sharpen = new ConvolveOp(new Kernel(3, 3, sharpenKernel));
        output = sharpen.filter(output, new BufferedImage(output.getWidth(), output.getHeight(), output.getType()));
        return toFXImage(output, null);
    }

}
