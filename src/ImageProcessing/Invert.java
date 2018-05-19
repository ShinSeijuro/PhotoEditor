/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.ImageColorAction;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class Invert extends ImageColorAction {

    public Invert(Image originalImage) {
        super(originalImage);
    }

    @Override
    protected Color convertColor(Color color) {
        return color.invert();
    }

}
