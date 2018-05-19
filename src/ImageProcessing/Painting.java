/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.PointillizeFilter;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Painting extends AbstractImageAction {

    public static PointillizeFilter getDefaultFilter() {
        PointillizeFilter filter = new PointillizeFilter();
        filter.setFuzziness(5.0f);
        filter.setRandomness(0.5f);
        filter.setFadeEdges(true);
        return filter;
    }

    private PointillizeFilter filter;

    public Painting() {
        this.filter = getDefaultFilter();
        setName("Painting Effect");
    }

    public PointillizeFilter getFilter() {
        return filter;
    }

    public void setFilter(PointillizeFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
