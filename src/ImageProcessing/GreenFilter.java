/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class GreenFilter extends ImageBlendAction {

    public GreenFilter(Image originalImage) {
        super(originalImage, Color.web("#19c919"), 0.25);
        setName("Green Filter");
    }
}
