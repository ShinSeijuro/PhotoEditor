/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import javafx.scene.paint.Color;

/**
 *
 * @author Admin
 */
public class WarmFilter extends ImageBlendAction {

    public WarmFilter() {
        super(Color.web("#ec8a00"), 0.25);
        setName("Warm Filter");
    }
}
