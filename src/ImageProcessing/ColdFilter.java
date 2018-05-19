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
public class ColdFilter extends ImageBlendAction {

    public ColdFilter() {
        super(Color.web("#006dff"), 0.25);
        setName("Cold Filter");
    }

}
