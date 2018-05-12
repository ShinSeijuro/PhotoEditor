/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Preset;

import Action.ImageColorAction;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Admin
 */
public class WarmFilter extends ImageColorAction {

    public WarmFilter(BufferedImage originalImage) {
        super(originalImage);
        setName("Warm Filter");
    }

    @Override
    protected Color convertColor(Color color) {
        int red = color.getRed();
        int blue = color.getBlue();

        int temp = (int) ((red + blue) / 4.0d);

        red += temp;
        if (red > 255) {
            red = 255;
        }
        blue -= temp;
        if (blue < 0) {
            blue = 0;
        }

        return new Color(red, color.getGreen(), blue);
    }

}
