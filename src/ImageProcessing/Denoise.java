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
import static org.opencv.core.CvType.CV_8UC3;
import org.opencv.core.Mat;

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
        Mat dest = new Mat(rgb.size(), CV_8UC3);

        org.opencv.photo.Photo.fastNlMeansDenoisingColored(rgb, dest, 15, 10, 7, 21);
        Image newImage = toFXImage(toBufferedImage(dest), null);
        return newImage;
    }

}
