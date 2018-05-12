/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Action.AbstractImageAction;
import Adjustment.GrayScale;
import ImageProcessing.Utils;
import Preset.*;
import java.awt.image.BufferedImage;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class PresetPreview {

    private final ObjectProperty<Image> warmFilter = new SimpleObjectProperty<>(null);

    public Image getWarmFilter() {
        return warmFilter.get();
    }

    public void setWarmFilter(Image value) {
        warmFilter.set(value);
    }

    public ObjectProperty warmFilterProperty() {
        return warmFilter;
    }
    private final ObjectProperty<Image> coldFilter = new SimpleObjectProperty<>(null);

    public Image getColdFilter() {
        return coldFilter.get();
    }

    public void setColdFilter(Image value) {
        coldFilter.set(value);
    }

    public ObjectProperty coldFilterProperty() {
        return coldFilter;
    }

    private final ObjectProperty<Image> blackAndWhite = new SimpleObjectProperty<>(null);

    public Image getBlackAndWhite() {
        return blackAndWhite.get();
    }

    public void setBlackAndWhite(Image value) {
        blackAndWhite.set(value);
    }

    public ObjectProperty blackAndWhiteProperty() {
        return blackAndWhite;
    }

    private BufferedImage thumbnail;

    public BufferedImage getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(BufferedImage originalImage) {
        if (originalImage == null) {
            this.thumbnail = null;
            setWarmFilter(null);
            setColdFilter(null);
            setBlackAndWhite(null);
            return;
        }

        this.thumbnail = Utils.getThumbnail(originalImage, 100, 100);
        setWarmFilter(getImageFromAction(new WarmFilter(thumbnail)));
        setColdFilter(getImageFromAction(new ColdFilter(thumbnail)));
        setBlackAndWhite(getImageFromAction(new GrayScale(thumbnail)));
    }

    public PresetPreview() {
    }

    private Image getImageFromAction(AbstractImageAction action) {
        return SwingFXUtils.toFXImage(action.applyTransform(), null);
    }
}
