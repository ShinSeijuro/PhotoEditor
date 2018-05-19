/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.SwimFilter;
import static com.jhlabs.math.ImageFunction2D.CLAMP;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Underwater extends AbstractImageAction {

    public static SwimFilter getDefaultFilter() {
        SwimFilter filter = new SwimFilter();
        filter.setScale(30.0f);
        filter.setStretch(1.0f);
        filter.setTurbulence(1.0f);
        filter.setAmount(30.0f);
        filter.setTime(0.0f);
        filter.setEdgeAction(CLAMP);
        return filter;
    }

    private SwimFilter filter;

    public Underwater() {
        this.filter = getDefaultFilter();
        setName("Underwater Effect");
    }

    public SwimFilter getFilter() {
        return filter;
    }

    public void setFilter(SwimFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
