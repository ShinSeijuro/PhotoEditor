/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.NoiseFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Noise extends AbstractImageAction {

    private NoiseFilter filter;

    public NoiseFilter getFilter() {
        return filter;
    }

    public void setFilter(NoiseFilter filter) {
        this.filter = filter;
    }

    public Noise() {
        this.filter = getDefaultFilter();
        setName("Add Noise");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

    public static NoiseFilter getDefaultFilter() {
        NoiseFilter filter = new NoiseFilter();
        filter.setDistribution(NoiseFilter.GAUSSIAN);
        filter.setMonochrome(true);
        filter.setDensity(1.0f);
        filter.setAmount(10);
        return filter;
    }

}
