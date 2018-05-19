/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import static ImageProcessing.Utils.toImage;
import static ImageProcessing.Utils.toMat;
import java.util.ArrayList;
import javafx.scene.image.Image;
import static org.opencv.core.Core.merge;
import static org.opencv.core.Core.split;
import org.opencv.core.Mat;
import static org.opencv.imgproc.Imgproc.COLOR_BGRA2GRAY;
import static org.opencv.imgproc.Imgproc.COLOR_GRAY2BGR;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.equalizeHist;

/**
 *
 * @author CMQ
 */
public class GrayScaleBalance extends AbstractImageAction {

    public GrayScaleBalance() {
        setName("BW Contrast");
    }

    @Override
    public Image applyTransform(Image image) {
        Mat imageMat = toMat(image);

        // split alpha
        ArrayList<Mat> bgra = new ArrayList<>();
        split(imageMat, bgra);
        Mat alpha = bgra.get(3);

        // convert to grayscale
        Mat grayMat = new Mat();
        cvtColor(imageMat, grayMat, COLOR_BGRA2GRAY);

        // histogram equalisation
        Mat histeqMat = new Mat();
        equalizeHist(grayMat, histeqMat);

        // convert back to bgr
        Mat bgrMat = new Mat();
        cvtColor(histeqMat, bgrMat, COLOR_GRAY2BGR);

        // add back alpha
        split(bgrMat, bgra);
        bgra.add(alpha);
        merge(bgra, imageMat);

        image = toImage(imageMat);
        return image;
    }
}
