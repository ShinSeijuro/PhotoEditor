/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Preset;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Admin
 */
public class WarmFilter extends ImageBlendAction {

    public WarmFilter(Image originalImage) {
        super(originalImage, Color.web("#ec8a00"), 0.25);
        setName("Warm Filter");
    }
}
