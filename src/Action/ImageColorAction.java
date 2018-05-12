/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import ImageProcessing.Utils;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Admin
 */
public abstract class ImageColorAction extends AbstractImageAction {

    public ImageColorAction(BufferedImage originalImage) {
        super(originalImage);
    }

    @Override
    protected final BufferedImage applyTransform(BufferedImage image) {
        BufferedImage newImage = Utils.deepCopy(image);

        for (int i = 0, width = newImage.getWidth(); i < width; i++) {
            for (int j = 0, height = newImage.getHeight(); j < height; j++) {
                int argb = newImage.getRGB(i, j);

                int alpha = 0xFF & (argb >> 24);
                int red = 0xFF & (argb >> 16);
                int green = 0xFF & (argb >> 8);
                int blue = 0xFF & (argb >> 0);

                Color color = new Color(red, green, blue, alpha);
                color = convertColor(color);

                newImage.setRGB(i, j, color.getRGB());
            }
        }

        return newImage;
    }

    protected abstract Color convertColor(Color color);
}
