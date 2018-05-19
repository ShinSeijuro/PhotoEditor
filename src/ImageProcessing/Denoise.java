/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import static ImageProcessing.Utils.toBufferedImage;
import static ImageProcessing.Utils.toMat;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;
import static org.opencv.core.CvType.CV_8U;
import static org.opencv.core.CvType.CV_8UC3;
import org.opencv.core.Mat;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY_INV;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.threshold;
import static org.opencv.photo.Photo.INPAINT_TELEA;
import static org.opencv.photo.Photo.inpaint;

/**
 *
 * @author Admin
 */
public class Denoise extends AbstractImageAction {

    public Denoise() {
        setName("Denoise");
    }

    @Override
    public Image applyTransform(Image image) {
        Mat rgb = toMat(fromFXImage(image, null));
        Mat gray = new Mat(rgb.size(), CV_8U);
        cvtColor(rgb, gray, COLOR_BGR2GRAY);
        Mat mask = new Mat(rgb.size(), CV_8U);
        threshold(gray, mask, 70, 255, THRESH_BINARY_INV);
        Mat dn = new Mat(rgb.size(), CV_8UC3);
        inpaint(rgb, mask, dn, 20, INPAINT_TELEA);

        Image newImage = toFXImage(toBufferedImage(dn), null);
        return newImage;
    }

}
