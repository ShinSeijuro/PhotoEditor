/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Action.AbstractImageAction;
import ImageProcessing.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/**
 *
 * @author CMQ
 */
public class PresetPreview {

    private final ObjectProperty<Image> autoBalance = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> warmFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> coldFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> greenFilter = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> underwater = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> crystallize = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> twirl = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> painting = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> oilPainting = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> blackAndWhite = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> bwContrast = new SimpleObjectProperty<>();
    private final ObjectProperty<Image> invert = new SimpleObjectProperty<>();
    private Image thumbnail;

    public PresetPreview() {
    }

    public Image getAutoBalance() {
        return autoBalance.get();
    }

    public void setAutoBalance(Image value) {
        autoBalance.set(value);
    }

    public ObjectProperty autoBalanceProperty() {
        return autoBalance;
    }

    public Image getWarmFilter() {
        return warmFilter.get();
    }

    public void setWarmFilter(Image value) {
        warmFilter.set(value);
    }

    public ObjectProperty warmFilterProperty() {
        return warmFilter;
    }

    public Image getColdFilter() {
        return coldFilter.get();
    }

    public void setColdFilter(Image value) {
        coldFilter.set(value);
    }

    public ObjectProperty coldFilterProperty() {
        return coldFilter;
    }

    public Image getGreenFilter() {
        return greenFilter.get();
    }

    public void setGreenFilter(Image value) {
        greenFilter.set(value);
    }

    public ObjectProperty greenFilterProperty() {
        return greenFilter;
    }

    public Image getUnderwater() {
        return underwater.get();
    }

    public void setUnderwater(Image value) {
        underwater.set(value);
    }

    public ObjectProperty underwaterProperty() {
        return underwater;
    }

    public Image getCrystallize() {
        return crystallize.get();
    }

    public void setCrystallize(Image value) {
        crystallize.set(value);
    }

    public ObjectProperty crystallizeProperty() {
        return crystallize;
    }

    public Image getTwirl() {
        return twirl.get();
    }

    public void setTwirl(Image value) {
        twirl.set(value);
    }

    public ObjectProperty twirlProperty() {
        return twirl;
    }

    public Image getPainting() {
        return painting.get();
    }

    public void setPainting(Image value) {
        painting.set(value);
    }

    public ObjectProperty paintingProperty() {
        return painting;
    }

    public Image getOilPainting() {
        return oilPainting.get();
    }

    public void setOilPainting(Image value) {
        oilPainting.set(value);
    }

    public ObjectProperty oilPaintingProperty() {
        return oilPainting;
    }

    public Image getBlackAndWhite() {
        return blackAndWhite.get();
    }

    public void setBlackAndWhite(Image value) {
        blackAndWhite.set(value);
    }

    public ObjectProperty blackAndWhiteProperty() {
        return blackAndWhite;
    }

    public Image getBwContrast() {
        return bwContrast.get();
    }

    public void setBwContrast(Image value) {
        bwContrast.set(value);
    }

    public ObjectProperty bwContrastProperty() {
        return bwContrast;
    }

    public Image getInvert() {
        return invert.get();
    }

    public void setInvert(Image value) {
        invert.set(value);
    }

    public ObjectProperty invertProperty() {
        return invert;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image originalImage) {
        if (originalImage == null) {
            this.thumbnail = null;
            setAutoBalance(null);
            setWarmFilter(null);
            setColdFilter(null);
            setUnderwater(null);
            setCrystallize(null);
            setTwirl(null);
            setPainting(null);
            setOilPainting(null);
            setBlackAndWhite(null);
            setBwContrast(null);
            setInvert(null);
            return;
        }

        this.thumbnail = Utils.getThumbnail(originalImage, 100, -1);
        setAutoBalance(getImageFromAction(new AutoBalance()));
        setWarmFilter(getImageFromAction(new WarmFilter()));
        setColdFilter(getImageFromAction(new ColdFilter()));
        setGreenFilter(getImageFromAction(new GreenFilter()));
        setUnderwater(getImageFromAction(new Underwater()));
        setCrystallize(getImageFromAction(new Crystallize()));
        setTwirl(getImageFromAction(new Twirl()));
        setPainting(getImageFromAction(new Painting()));
        setOilPainting(getImageFromAction(new OilPainting()));
        setBlackAndWhite(getImageFromAction(new GrayScale()));
        setBwContrast(getImageFromAction(new GrayScaleBalance()));
        setInvert(getImageFromAction(new Invert()));
    }

    private Image getImageFromAction(AbstractImageAction action) {
        return action.applyTransform(thumbnail);
    }
}
