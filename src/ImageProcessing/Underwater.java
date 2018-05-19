/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.SwimFilter;
import com.jhlabs.math.ImageFunction2D;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Underwater extends AbstractImageAction {

    private SwimFilter filter;

    public SwimFilter getFilter() {
        return filter;
    }

    public void setFilter(SwimFilter filter) {
        this.filter = filter;
    }

    public Underwater() {
        this.filter = getDefaultFilter();
        setName("Underwater Effect");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

    public static SwimFilter getDefaultFilter() {
        SwimFilter filter = new SwimFilter();
        filter.setScale(30.0f);
        filter.setStretch(1.0f);
        filter.setTurbulence(1.0f);
        filter.setAmount(30.0f);
        filter.setTime(0.0f);
        filter.setEdgeAction(ImageFunction2D.CLAMP);
        return filter;
    }

}
