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
import static org.opencv.imgproc.Imgproc.COLOR_BGR2YCrCb;
import static org.opencv.imgproc.Imgproc.COLOR_YCrCb2BGR;
import static org.opencv.imgproc.Imgproc.cvtColor;
import static org.opencv.imgproc.Imgproc.equalizeHist;

/**
 *
 * @author CMQ
 */
public class AutoBalance extends AbstractImageAction {

    public AutoBalance() {
        setName("Auto Balance");
    }

    @Override
    public Image applyTransform(Image image) {
        Mat imageMat = toMat(image);

        // split alpha
        ArrayList<Mat> bgra = new ArrayList<>();
        split(imageMat, bgra);
        Mat alpha = bgra.get(3);

        // convert to YCrCb
        Mat yCrCbMat = new Mat();
        cvtColor(imageMat, yCrCbMat, COLOR_BGR2YCrCb);

        // split channel
        ArrayList<Mat> yCrCb = new ArrayList<>();
        split(yCrCbMat, yCrCb);

        // equalize channel Y
        Mat channelY = yCrCb.get(0);
        Mat eq = new Mat();
        equalizeHist(channelY, eq);
        yCrCb.set(0, eq);

        // convert back to bgr
        merge(yCrCb, yCrCbMat);
        Mat bgrMat = new Mat();
        cvtColor(yCrCbMat, bgrMat, COLOR_YCrCb2BGR);

        // add back alpha
        split(bgrMat, bgra);
        bgra.add(alpha);
        merge(bgra, imageMat);

        image = toImage(imageMat);
        return image;
    }

}
