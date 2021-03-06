/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.TwirlFilter;
import static com.jhlabs.math.ImageFunction2D.CLAMP;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Twirl extends AbstractImageAction {

    public static TwirlFilter getDefaultFilter(Image image) {
        TwirlFilter filter = new TwirlFilter();
        float width = (float) image.getWidth();
        float height = (float) image.getHeight();
        filter.setCentreX(0.5f);
        filter.setCentreY(0.5f);
        filter.setAngle(-3.0f);
        filter.setRadius(width > height ? height : width);
        filter.setEdgeAction(CLAMP);
        return filter;
    }

    private TwirlFilter filter;

    public Twirl() {
        setName("Twirl Effect");
    }

    public TwirlFilter getFilter() {
        return filter;
    }

    public void setFilter(TwirlFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        if (filter == null) {
            filter = getDefaultFilter(image);
        }

        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
