/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author CMQ
 */
public class Utils {

    public static Mat toMat(BufferedImage image) {
        BufferedImage convertImg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        convertImg.getGraphics().drawImage(image, 0, 0, null);
        byte[] pixels = ((DataBufferByte) convertImg.getRaster().getDataBuffer()).getData();

        // Create a Matrix the same size of image
        Mat matImage = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        matImage.put(0, 0, pixels);

        return matImage;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}