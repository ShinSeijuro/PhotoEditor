/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.MotionBlurFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class MotionBlurAction extends AbstractImageAction {

    private double angle;

    public double getAngle() {
        return angle;
    }

    private double radius;

    public double getRadius() {
        return radius;
    }

    public MotionBlurAction(Image originalImage, double angle, double radius) {
        super(originalImage);
        this.angle = angle;
        this.radius = radius;
        setName("Motion Blur");
    }

    @Override
    protected Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        MotionBlurFilter filter = new MotionBlurFilter();
        filter.setAngle((float) Math.toRadians(angle));
        filter.setDistance((float) radius);
        filter.setWrapEdges(false);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

}
