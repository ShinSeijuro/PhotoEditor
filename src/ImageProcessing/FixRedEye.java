/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import static GUI.PhotoEditor.getStartupPath;
import static ImageProcessing.Utils.toMat;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

/**
 *
 * @author Admin
 */
public class FixRedEye extends AbstractImageAction {

    private static final CascadeClassifier EYE_DETECTOR
            = new CascadeClassifier(getStartupPath() + "\\lib\\haarcascade\\haarcascade_eye.xml");

    private static final boolean SUPPORTED = !EYE_DETECTOR.empty();

    public static boolean isSupported() {
        return SUPPORTED;
    }

    public FixRedEye() {
        setName("Fix Red Eye");
    }

    @Override
    public Image applyTransform(Image image) {
        if (!SUPPORTED) {
            return image;
        }

        Mat matImage = toMat(image);
        if (matImage == null) {
            return image;
        }

        WritableImage fixedImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelReader pixelReader = fixedImage.getPixelReader();
        PixelWriter pixelWriter = fixedImage.getPixelWriter();

        MatOfRect eyeDetections = new MatOfRect();
        EYE_DETECTOR.detectMultiScale(matImage, eyeDetections);

        for (Rect eyeRect : eyeDetections.toArray()) {
            int width = eyeRect.x + eyeRect.width;
            int height = eyeRect.y + eyeRect.height;
            for (int x = eyeRect.x; x <= width; x++) {
                for (int y = eyeRect.y; y <= height; y++) {
                    Color color = pixelReader.getColor(x, y);

                    // Color values from 0 to 255
                    double red = color.getRed();
                    double green = color.getGreen();
                    double blue = color.getBlue();

                    if ((red > (80.0 / 255.0)) && (green < (60.0 / 255.0))) {
                        // Decreases the red to the average of the green and blue
                        red = (green + blue) / 2.0;
                        // Create new color for this pixel that is not so red
                        color = new Color(red, green, blue, color.getOpacity());
                        pixelWriter.setColor(x, y, color);
                    }
                }
            }
        }

        return fixedImage;
    }
}
