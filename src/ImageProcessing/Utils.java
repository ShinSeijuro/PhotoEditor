/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

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

    public static BufferedImage toBufferedImage(Mat matrix) {

        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[] = mob.toArray();

        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new ByteArrayInputStream(ba));
        } catch (IOException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bi;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage getThumbnail(BufferedImage image, int width, int height) {
        return image;
        //java.awt.Image scaledImage = image.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        //return toBufferedImage(scaledImage);
    }

    public static BufferedImage toBufferedImage(java.awt.Image image) {
        BufferedImage output = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = output.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return output;
    }
}
