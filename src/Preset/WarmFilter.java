/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Preset;

import Action.ImageColorAction;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Admin
 */
public class WarmFilter extends ImageColorAction {

    public WarmFilter(Image originalImage) {
        super(originalImage);
        setName("Warm Filter");
    }

    @Override
    protected Color convertColor(Color color) {
        double red = color.getRed();
        double blue = color.getBlue();

        double temp = (red + blue) / 4.0;

        red += temp;
        if (red > 1.0) {
            red = 1.0;
        }
        blue -= temp;
        if (blue < 0.0) {
            blue = 0.0;
        }

        return new Color(red, color.getGreen(), blue, color.getOpacity());
    }

}
