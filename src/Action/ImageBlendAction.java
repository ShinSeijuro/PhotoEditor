/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class ImageBlendAction extends ImageColorAction {

    private Color blendColor;
    private double blendRatio;
    private boolean useOpacity;

    public ImageBlendAction(Color blendColor, double blendRatio, boolean useOpacity) {
        this.blendColor = blendColor;
        this.blendRatio = blendRatio;
        this.useOpacity = useOpacity;
        setName("Photo Filter");
    }

    public ImageBlendAction(Color blendColor, double blendRatio) {
        this(blendColor, blendRatio, false);
    }

    public Color getBlendColor() {
        return blendColor;
    }

    public double getBlendRatio() {
        return blendRatio;
    }

    public boolean isUseOpacity() {
        return useOpacity;
    }

    public void setBlendColor(Color blendColor) {
        this.blendColor = blendColor;
    }

    public void setBlendRatio(double blendRatio) {
        this.blendRatio = blendRatio;
    }

    public void setUseOpacity(boolean useOpacity) {
        this.useOpacity = useOpacity;
    }

    @Override
    protected Color convertColor(Color color) {
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
