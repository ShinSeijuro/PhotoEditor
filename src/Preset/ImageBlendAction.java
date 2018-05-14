/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Preset;

import Action.ImageColorAction;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class ImageBlendAction extends ImageColorAction {

    private Color blendColor;

    public Color getBlendColor() {
        return blendColor;
    }

    private double blendRatio;

    public double getBlendRatio() {
        return blendRatio;
    }

    private boolean useOpacity;

    public boolean isUseOpacity() {
        return useOpacity;
    }

    public ImageBlendAction(Image originalImage, Color blendColor, double blendRatio, boolean useOpacity) {
        super(originalImage);
        this.blendColor = blendColor;
        this.blendRatio = blendRatio;
        this.useOpacity = useOpacity;
        setName("Photo Filter");
    }

    public ImageBlendAction(Image originalImage, Color blendColor, double blendRatio) {
        this(originalImage, blendColor, blendRatio, false);
    }

    @Override
    protected final Color convertColor(Color color) {
        double keepRatio = 1.0 - blendRatio;

        double red = (color.getRed() * keepRatio + blendColor.getRed() * blendRatio);
        double green = (color.getGreen() * keepRatio + blendColor.getGreen() * blendRatio);
        double blue = (color.getBlue() * keepRatio + blendColor.getBlue() * blendRatio);
        double opacity = color.getOpacity();
        if (useOpacity) {
            opacity = (opacity * keepRatio + blendColor.getOpacity() * blendRatio);
        }

        return new Color(red, green, blue, opacity);
    }

}
