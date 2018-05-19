/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class OilPainting extends AbstractImageAction {

    private final OilFilter filter;

    public OilFilter getFilter() {
        return filter;
    }

    public OilPainting(Image originalImage) {
        super(originalImage);
        this.filter = getDefaultFilter();
        setName("Oil Painting Effect");
    }

    @Override
    protected Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

    public static OilFilter getDefaultFilter() {
        OilFilter filter = new OilFilter();
        filter.setLevels(3);
        filter.setRange(7);
        return filter;
    }
}
