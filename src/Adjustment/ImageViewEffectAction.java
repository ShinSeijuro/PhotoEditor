/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adjustment;

import Action.ImageSnapshotAction;
import java.awt.image.BufferedImage;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;

/**
 *
 * @author CMQ
 */
public class ImageViewEffectAction extends ImageSnapshotAction {

    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public ImageViewEffectAction(BufferedImage originalImage, ImageView node) {
        super(originalImage, node);
        this.effect = node.getEffect();

        setName(effect.getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"));
    }

}
