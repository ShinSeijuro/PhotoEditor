/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import GUI.PhotoEditor;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Admin
 */
public class FixRedEye extends AbstractImageAction {

    private static final CascadeClassifier eyeDetector
            = new CascadeClassifier(PhotoEditor.getStartupPath() + "\\lib\\haarcascade\\haarcascade_eye.xml");

    private static final boolean supported = !eyeDetector.empty();

    public static boolean isSupported() {
        return supported;
    }

    public FixRedEye(BufferedImage originalImage) {
        super(originalImage);
        this.setName("Fix Red Eye");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        if (!supported) {
            return super.getOriginalImage();
        }

        Mat matImage = Utils.toMat(image);
        if (matImage == null) {
            return super.getOriginalImage();
        }

        BufferedImage fixedImage = Utils.deepCopy(image);

        MatOfRect eyeDetections = new MatOfRect();
        eyeDetector.detectMultiScale(matImage, eyeDetections);

        for (Rect eyeRect : eyeDetections.toArray()) {
            int width = eyeRect.x + eyeRect.width;
            int height = eyeRect.y + eyeRect.height;
            for (int x = eyeRect.x; x <= width; x++) {
                for (int y = eyeRect.y; y <= height; y++) {
                    Color color = new Color(fixedImage.getRGB(x, y));

                    // Color values from 0 to 255
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    if ((red > 80) && (green < 60)) {
                        // Decreases the red to the average of the green and blue
                        red = (green + blue) / 2;
                        // Create new color for this pixel that is not so red
                        color = new Color(red, green, blue);
                    }

                    fixedImage.setRGB(x, y, color.getRGB());
                }
            }
        }

        return fixedImage;
    }
}
