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
public class ColdFilter extends ImageColorAction {

    public ColdFilter(BufferedImage originalImage) {
        super(originalImage);
        setName("Cold Filter");
    }

    @Override
    protected Color convertColor(Color color) {
        int red = color.getRed();
        int blue = color.getBlue();

        int temp = (int) ((red + blue) / 4.0d);

        red -= temp;
        if (red < 0) {
            red = 0;
        }
        blue += temp;
        if (blue > 255) {
            blue = 255;
        }

        return new Color(red, color.getGreen(), blue);
    }

}
