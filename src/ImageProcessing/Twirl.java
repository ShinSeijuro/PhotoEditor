/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.TwirlFilter;
import com.jhlabs.math.ImageFunction2D;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Twirl extends AbstractImageAction {

    private final TwirlFilter filter;

    public TwirlFilter getFilter() {
        return filter;
    }

    public Twirl(Image originalImage) {
        super(originalImage);
        this.filter = getDefaultFilter(originalImage);
        setName("Twirl Effect");
    }

    @Override
    protected Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

    public static TwirlFilter getDefaultFilter(Image image) {
        TwirlFilter filter = new TwirlFilter();
        float width = (float) image.getWidth();
        float height = (float) image.getHeight();
        filter.setCentreX(0.5f);
        filter.setCentreY(0.5f);
        filter.setAngle(-3.0f);
        filter.setRadius(width > height ? height : width);
        filter.setEdgeAction(ImageFunction2D.CLAMP);
        return filter;
    }
}
