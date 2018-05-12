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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private final ObjectProperty<Image> invert = new SimpleObjectProperty<>(null);

    public Image getInvert() {
        return invert.get();
    }

    public void setInvert(Image value) {
        invert.set(value);
    }

    public ObjectProperty invertProperty() {
        return invert;
    }

    private Image thumbnail;

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image originalImage) {
        if (originalImage == null) {
            this.thumbnail = null;
            setWarmFilter(null);
            setColdFilter(null);
            setBlackAndWhite(null);
            return;
        }

        this.thumbnail = Utils.getThumbnail(originalImage, 100, -1);
        setWarmFilter(getImageFromAction(new WarmFilter(thumbnail)));
        setColdFilter(getImageFromAction(new ColdFilter(thumbnail)));
        setBlackAndWhite(getImageFromAction(new GrayScale(thumbnail)));
        setInvert(getImageFromAction(new Invert(thumbnail)));
    }

    public PresetPreview() {
    }

    private Image getImageFromAction(AbstractImageAction action) {
        return action.applyTransform();
    }
}
