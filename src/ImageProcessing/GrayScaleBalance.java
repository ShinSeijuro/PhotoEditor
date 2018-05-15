/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.util.ArrayList;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author CMQ
 */
public class GrayScaleBalance extends AbstractImageAction {

    public GrayScaleBalance(Image originalImage) {
        super(originalImage);
        setName("BW Contrast");
    }

    @Override
    protected Image applyTransform(Image image) {
        Mat imageMat = Utils.toMat(image);

        // split alpha
        ArrayList<Mat> bgra = new ArrayList<>();
        Core.split(imageMat, bgra);
        Mat alpha = bgra.get(3);

        // convert to grayscale
        Mat grayMat = new Mat();
        Imgproc.cvtColor(imageMat, grayMat, Imgproc.COLOR_BGRA2GRAY);

        // histogram equalisation
        Mat histeqMat = new Mat();
        Imgproc.equalizeHist(grayMat, histeqMat);

        // convert back to bgr
        Mat bgrMat = new Mat();
        Imgproc.cvtColor(histeqMat, bgrMat, Imgproc.COLOR_GRAY2BGR);

        // add back alpha
        Core.split(bgrMat, bgra);
        bgra.add(alpha);
        Core.merge(bgra, imageMat);

        image = Utils.toImage(imageMat);
        return image;
    }
}
