/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.MotionBlurFilter;
import java.awt.image.BufferedImage;
import static java.lang.Math.toRadians;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class MotionBlurAction extends AbstractImageAction {

    private double angle;
    private double radius;

    public MotionBlurAction(double angle, double radius) {
        this.angle = angle;
        this.radius = radius;
        setName("Motion Blur");
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        MotionBlurFilter filter = new MotionBlurFilter();
        filter.setAngle((float) toRadians(angle));
        filter.setDistance((float) radius);
        filter.setWrapEdges(false);
        filter.filter(output, output);
        return toFXImage(output, null);
    }
}
