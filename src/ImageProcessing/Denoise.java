/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

/**
 *
 * @author Admin
 */
public class Denoise extends AbstractImageAction {

    public Denoise(BufferedImage originalImage) {
        super(originalImage);
        setName("Denoise");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        Mat rgb = Utils.toMat(image);
        Mat gray = new Mat(rgb.size(), CvType.CV_8U);
        Imgproc.cvtColor(rgb, gray, Imgproc.COLOR_BGR2GRAY);
        Mat mask = new Mat(rgb.size(), CvType.CV_8U);
        Imgproc.threshold(gray, mask, 70, 255, Imgproc.THRESH_BINARY_INV);
        Mat dn = new Mat(rgb.size(), CvType.CV_8UC3);
        Photo.inpaint(rgb, mask, dn, 20, Photo.INPAINT_TELEA);

        BufferedImage newImage = Utils.toBufferedImage(dn);
        if (newImage != null) {
            return newImage;
        }
        return super.getOriginalImage();
    }

}
