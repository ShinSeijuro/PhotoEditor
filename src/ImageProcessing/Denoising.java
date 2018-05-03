/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

/**
 *
 * @author Admin
 */
public class Denoising extends AbstractImageAction {

    public Denoising(BufferedImage originalImage) {
        super(originalImage);
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
        try {
            return Utils.mat2image(dn);
        } catch (IOException ex) {
            Logger.getLogger(Denoising.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }

}
