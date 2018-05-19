/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.BoxBlurFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class BoxBlurAction extends AbstractImageAction {

    private double width;

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    private double height;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    private int iteration;

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public BoxBlurAction(double width, double height, int iteration) {
        this.width = width;
        this.height = height;
        this.iteration = iteration;
        setName("Box Blur");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        BoxBlurFilter filter = new BoxBlurFilter();
        filter.setHRadius((int) width);
        filter.setVRadius((int) height);
        filter.setIterations(iteration);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

}
