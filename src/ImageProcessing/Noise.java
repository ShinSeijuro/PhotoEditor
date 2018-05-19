/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.NoiseFilter;
import static com.jhlabs.image.NoiseFilter.GAUSSIAN;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Noise extends AbstractImageAction {

    public static NoiseFilter getDefaultFilter() {
        NoiseFilter filter = new NoiseFilter();
        filter.setDistribution(GAUSSIAN);
        filter.setMonochrome(true);
        filter.setDensity(1.0f);
        filter.setAmount(10);
        return filter;
    }

    private NoiseFilter filter;

    public Noise() {
        this.filter = getDefaultFilter();
        setName("Add Noise");
    }

    public NoiseFilter getFilter() {
        return filter;
    }

    public void setFilter(NoiseFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }
}
