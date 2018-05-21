/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author CMQ
 */
public class ImageViewEffectAction extends ImageSnapshotAction {

    private Effect effect;

    public ImageViewEffectAction() {
        super();
    }

    public ImageViewEffectAction(ImageView node) {
        super(node);
        if (node.getEffect() != null) {
            setActionName(node.getEffect());
        }
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
        setActionName(effect);
    }

    public ImageView getImageView() {
        return (ImageView) getNode();
    }

    public void setImageView(ImageView imageView) {
        setNode(imageView);
        if (effect == null && imageView.getEffect() != null) {
            setEffect(imageView.getEffect());
        }
    }

    @Override
    public Image applyTransform(Image image) {
        Effect originalEffect = getImageView().getEffect();
        if (effect != null) {
            getImageView().setEffect(effect);
        }
        Image output = takeSnapShot();
        getImageView().setEffect(originalEffect);

        return output;
    }

    private void setActionName(Effect effect) {
        if (effect != null) {
            setName(effect.getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"));
        } else {
            setName("Image Effect");
        }
    }
}
