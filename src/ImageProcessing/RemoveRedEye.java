/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import static GUI.WorkspaceController.bufferedImageToMat;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
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
public class RemoveRedEye extends AbstractImageAction {

    public RemoveRedEye(BufferedImage originalImage) {
        super(originalImage);
        this.setName("Remove Red Eye");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("\nRunning FaceDetector");
        CascadeClassifier faceDetector = new CascadeClassifier("E:\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_default.xml");
        CascadeClassifier eyeDetector = new CascadeClassifier("E:\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_eye.xml");
        if (faceDetector.empty()) {
            System.out.println("Error!");
        } else {
            Mat img = bufferedImageToMat(image);
            MatOfRect faceDetections = new MatOfRect();
            faceDetector.detectMultiScale(img, faceDetections);
            System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
            // Draw a bounding box around each face.
            for (Rect rect : faceDetections.toArray()) {
                //This line for draw a circle for face regconization
                //Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
                MatOfRect eyeDetections = new MatOfRect();
                Mat eyeMat = new Mat(img, rect);
                eyeDetector.detectMultiScale(eyeMat, eyeDetections);
                for (Rect rect_2 : eyeDetections.toArray()) {
                    Point leftTop = new Point(rect_2.x, rect_2.y);
                    Point rightBot = new Point(rect_2.x + rect_2.width, rect_2.y + rect_2.height);
                    image = fixRedEye(leftTop, rightBot, image);
                }
            }
        }
        return image;
    }

    public BufferedImage fixRedEye(Point leftTop, Point rightBot, BufferedImage image) {
        // Obtain PixelReader
        //Image img = getCurrentController().getImageView().getImage();
        Image img = SwingFXUtils.toFXImage(image, null);
        PixelReader pixelReader = img.getPixelReader();
        // Create WritableImage
        WritableImage wImage = new WritableImage((int) img.getWidth(), (int) img.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        //MatOfRect eyeDetection = eyeDetect();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                // Color values of the pixel from 0-1
                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();

                // Color values from 0 to 255
                int red = (int) (r * 255);
                int green = (int) (g * 255);
                int blue = (int) (b * 255);
                //If this is a pixel inside the left eye area
                if ((x >= leftTop.x + 128) && (x < rightBot.x + 128) && (y >= leftTop.y + 220) && (y < rightBot.y + 220)) {
                    // The red pixels had a red component > 80 and green < 60
                    if ((red > 80) && (green < 60)) {
                        red = (green + blue) / 2;		// Decreases the red to the average of the green and blue
                        color = Color.rgb(red, green, blue);  // Create new color for this pixel that is not so red
                    }
                }

                pixelWriter.setColor(x, y, color);
            }
        }
        return SwingFXUtils.fromFXImage(wImage, null);
    }

}
