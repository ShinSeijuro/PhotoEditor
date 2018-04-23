/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class ImageViewEffectAction extends AbstractImageAction {

    private ImageView imageView;

    public ImageView getImageView() {
        return getImageView();
    }

    private Effect effect;

    public Effect getEffect() {
        return effect;
    }

    public ImageViewEffectAction(BufferedImage originalImage, ImageView imageView) {
        super(originalImage);
        this.imageView = imageView;
        this.effect = imageView.getEffect();

        setName(effect.getClass().getSimpleName().replaceAll("(.)([A-Z])", "$1 $2"));
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        image = SwingFXUtils.fromFXImage(imageView.snapshot(sp, null), null);
        return image;
    }

}
