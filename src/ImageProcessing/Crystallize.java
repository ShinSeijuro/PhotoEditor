/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import com.jhlabs.image.CrystallizeFilter;
import java.awt.image.BufferedImage;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class Crystallize extends AbstractImageAction {

    public static CrystallizeFilter getDefaultFilter() {
        CrystallizeFilter filter = new CrystallizeFilter();
        filter.setAmount(30.0f);
        filter.setFadeEdges(false);
        filter.setRandomness(0.5f);
        filter.setScale(30.0f);
        filter.setStretch(1.0f);
        filter.setTurbulence(1.0f);
        return filter;
    }

    private CrystallizeFilter filter;

    public Crystallize() {
        this.filter = getDefaultFilter();
        setName("Crystallize Effect");
    }

    public CrystallizeFilter getFilter() {
        return filter;
    }

    public void setFilter(CrystallizeFilter filter) {
        this.filter = filter;
    }

    @Override
    public Image applyTransform(Image image) {
        BufferedImage output = fromFXImage(image, null);
        filter.filter(output, output);
        return toFXImage(output, null);
    }

}
