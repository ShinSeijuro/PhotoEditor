/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.ImageBlendAction;
import static javafx.scene.paint.Color.web;

/**
 *
 * @author CMQ
 */
public class GreenFilter extends ImageBlendAction {

    public GreenFilter() {
        super(web("#19c919"), 0.25);
        setName("Green Filter");
    }
}
