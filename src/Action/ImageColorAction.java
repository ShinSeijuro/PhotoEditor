/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import ImageProcessing.Utils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 *
 * @author Admin
 */
public abstract class ImageColorAction extends AbstractImageAction {

    public ImageColorAction(Image originalImage) {
        super(originalImage);
    }

    @Override
    protected final Image applyTransform(Image image) {
        WritableImage newImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = newImage.getPixelWriter();
        PixelReader pixelReader = newImage.getPixelReader();

        for (int i = 0, width = (int) newImage.getRequestedWidth(); i < width; i++) {
            for (int j = 0, height = (int) newImage.getHeight(); j < height; j++) {
                Color color = pixelReader.getColor(i, j);
                color = convertColor(color);
                pixelWriter.setColor(i, j, color);
            }
        }

        return newImage;
    }

    protected abstract Color convertColor(Color color);
}
