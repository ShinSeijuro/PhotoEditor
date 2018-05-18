/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Adjustment;

import Action.ImageSnapshotAction;
import javafx.scene.image.Image;
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

    public ImageView getImageView() {
        return (ImageView) getNode();
    }

    public ImageViewEffectAction(Image originalImage, ImageView node) {
        super(originalImage, node);
        this.effect = node.getEffect();
        setActionName();
    }

    public ImageViewEffectAction(Image originalImage, ImageView node, Effect effect) {
        super(originalImage, node);
        this.effect = effect;
        node.setEffect(effect);
        setActionName();
    }

    @Override
    protected Image applyTransform(Image image) {
        Image output = takeSnapShot();
        getImageView().setEffect(null);
        return output;
    }

    private void setActionName() {
        if (effect != null) {
            setName(effect.getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"));
        } else {
            setName("Image Effect");
        }
    }
}
