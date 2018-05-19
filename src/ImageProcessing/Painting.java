/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.PointillizeFilter;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Painting extends AbstractImageAction {

    private PointillizeFilter filter;

    public PointillizeFilter getFilter() {
        return filter;
    }

    public void setFilter(PointillizeFilter filter) {
        this.filter = filter;
    }

    public Painting() {
        this.filter = getDefaultFilter();
        setName("Painting Effect");
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = SwingFXUtils.fromFXImage(image, null);
        filter.filter(output, output);
        return SwingFXUtils.toFXImage(output, null);
    }

    public static PointillizeFilter getDefaultFilter() {
        PointillizeFilter filter = new PointillizeFilter();
        filter.setFuzziness(5.0f);
        filter.setRandomness(0.5f);
        filter.setFadeEdges(true);
        return filter;
    }
}
