/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.BoxBlurFilter;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class BoxBlurAction extends AbstractImageAction {

    private double width;
    private double height;
    private int iteration;
    public BoxBlurAction(double width, double height, int iteration) {
        this.width = width;
        this.height = height;
        this.iteration = iteration;
        setName("Box Blur");
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }


    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }


    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        BoxBlurFilter filter = new BoxBlurFilter();
        filter.setHRadius((int) width);
        filter.setVRadius((int) height);
        filter.setIterations(iteration);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
