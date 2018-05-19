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
public class AutoBalance extends AbstractImageAction {

    public AutoBalance() {
        setName("Auto Balance");
    }

    @Override
    public Image applyTransform(Image image) {
        Mat imageMat = Utils.toMat(image);

        // split alpha
        ArrayList<Mat> bgra = new ArrayList<>();
        Core.split(imageMat, bgra);
        Mat alpha = bgra.get(3);

        // convert to YCrCb
        Mat yCrCbMat = new Mat();
        Imgproc.cvtColor(imageMat, yCrCbMat, Imgproc.COLOR_BGR2YCrCb);

        // split channel
        ArrayList<Mat> yCrCb = new ArrayList<>();
        Core.split(yCrCbMat, yCrCb);

        // equalize channel Y
        Mat channelY = yCrCb.get(0);
        Mat eq = new Mat();
        Imgproc.equalizeHist(channelY, eq);
        yCrCb.set(0, eq);

        // convert back to bgr
        Core.merge(yCrCb, yCrCbMat);
        Mat bgrMat = new Mat();
        Imgproc.cvtColor(yCrCbMat, bgrMat, Imgproc.COLOR_YCrCb2BGR);

        // add back alpha
        Core.split(bgrMat, bgra);
        bgra.add(alpha);
        Core.merge(bgra, imageMat);

        image = Utils.toImage(imageMat);
        return image;
    }

}
