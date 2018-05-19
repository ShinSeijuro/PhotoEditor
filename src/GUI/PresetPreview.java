/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import ImageProcessing.Invert;
import ImageProcessing.GreenFilter;
import ImageProcessing.WarmFilter;
import ImageProcessing.ColdFilter;
import Action.AbstractImageAction;
import Adjustment.GrayScale;
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

    public Image getAutoBalance() {
        return autoBalance.get();
    }

    public void setAutoBalance(Image value) {
        autoBalance.set(value);
    }

    public ObjectProperty autoBalanceProperty() {
        return autoBalance;
    }

    private final ObjectProperty<Image> warmFilter = new SimpleObjectProperty<>();

    public Image getWarmFilter() {
        return warmFilter.get();
    }

    public void setWarmFilter(Image value) {
        warmFilter.set(value);
    }

    public ObjectProperty warmFilterProperty() {
        return warmFilter;
    }
    private final ObjectProperty<Image> coldFilter = new SimpleObjectProperty<>();

    public Image getColdFilter() {
        return coldFilter.get();
    }

    public void setColdFilter(Image value) {
        coldFilter.set(value);
    }

    public ObjectProperty coldFilterProperty() {
        return coldFilter;
    }

    private final ObjectProperty<Image> greenFilter = new SimpleObjectProperty<>();

    public Image getGreenFilter() {
        return greenFilter.get();
    }

    public void setGreenFilter(Image value) {
        greenFilter.set(value);
    }

    public ObjectProperty greenFilterProperty() {
        return greenFilter;
    }

    private final ObjectProperty<Image> underwater = new SimpleObjectProperty<>();

    public Image getUnderwater() {
        return underwater.get();
    }

    public void setUnderwater(Image value) {
        underwater.set(value);
    }

    public ObjectProperty underwaterProperty() {
        return underwater;
    }

    private final ObjectProperty<Image> crystallize = new SimpleObjectProperty<>();

    public Image getCrystallize() {
        return crystallize.get();
    }

    public void setCrystallize(Image value) {
        crystallize.set(value);
    }

    public ObjectProperty crystallizeProperty() {
        return crystallize;
    }

    private final ObjectProperty<Image> twirl = new SimpleObjectProperty<>();

    public Image getTwirl() {
        return twirl.get();
    }

    public void setTwirl(Image value) {
        twirl.set(value);
    }

    public ObjectProperty twirlProperty() {
        return twirl;
    }

    private final ObjectProperty<Image> painting = new SimpleObjectProperty<>();

    public Image getPainting() {
        return painting.get();
    }

    public void setPainting(Image value) {
        painting.set(value);
    }

    public ObjectProperty paintingProperty() {
        return painting;
    }

    private final ObjectProperty<Image> oilPainting = new SimpleObjectProperty<>();

    public Image getOilPainting() {
        return oilPainting.get();
    }

    public void setOilPainting(Image value) {
        oilPainting.set(value);
    }

    public ObjectProperty oilPaintingProperty() {
        return oilPainting;
    }

    private final ObjectProperty<Image> blackAndWhite = new SimpleObjectProperty<>();

    public Image getBlackAndWhite() {
        return blackAndWhite.get();
    }

    public void setBlackAndWhite(Image value) {
        blackAndWhite.set(value);
    }

    public ObjectProperty blackAndWhiteProperty() {
        return blackAndWhite;
    }
    private final ObjectProperty<Image> bwContrast = new SimpleObjectProperty<>();

    public Image getBwContrast() {
        return bwContrast.get();
    }

    public void setBwContrast(Image value) {
        bwContrast.set(value);
    }

    public ObjectProperty bwContrastProperty() {
        return bwContrast;
    }

    private final ObjectProperty<Image> invert = new SimpleObjectProperty<>();

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
        setAutoBalance(getImageFromAction(new AutoBalance(thumbnail)));
        setWarmFilter(getImageFromAction(new WarmFilter(thumbnail)));
        setColdFilter(getImageFromAction(new ColdFilter(thumbnail)));
        setGreenFilter(getImageFromAction(new GreenFilter(thumbnail)));
        setUnderwater(getImageFromAction(new Underwater(thumbnail)));
        setCrystallize(getImageFromAction(new Crystallize(thumbnail)));
        setTwirl(getImageFromAction(new Twirl(thumbnail)));
        setPainting(getImageFromAction(new Painting(thumbnail)));
        setOilPainting(getImageFromAction(new OilPainting(thumbnail)));
        setBlackAndWhite(getImageFromAction(new GrayScale(thumbnail)));
        setBwContrast(getImageFromAction(new GrayScaleBalance(thumbnail)));
        setInvert(getImageFromAction(new Invert(thumbnail)));
    }

    public PresetPreview() {
    }

    private Image getImageFromAction(AbstractImageAction action) {
        return action.applyTransform();
    }
}
