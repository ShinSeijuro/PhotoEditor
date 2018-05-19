/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.GaussianFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class GaussianBlurAction extends AbstractImageAction {

    private final double radius;

    public double getRadius() {
        return radius;
    }

    public GaussianBlurAction(Image originalImage, double radius) {
        super(originalImage);
        this.radius = radius;
        setName("Gaussian Blur");
    }

    @Override
    protected Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        new GaussianFilter((float) radius).filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

}
