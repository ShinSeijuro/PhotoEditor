/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.OilFilter;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class OilPainting extends AbstractImageAction {

    public static OilFilter getDefaultFilter() {
        OilFilter filter = new OilFilter();
        filter.setLevels(3);
        filter.setRange(7);
        return filter;
    }

    private OilFilter filter;

    public OilPainting() {
        this.filter = getDefaultFilter();
        setName("Oil Painting Effect");
    }

    public OilFilter getFilter() {
        return filter;
    }

    public void setFilter(OilFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
