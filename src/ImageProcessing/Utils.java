/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import java.awt.Graphics;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import static javafx.embed.swing.SwingFXUtils.toFXImage;
import javafx.scene.image.Image;
import static javafx.scene.image.PixelFormat.getByteBgraInstance;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import static javax.imageio.ImageIO.read;
import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.core.CvType.CV_8UC4;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import static org.opencv.imgcodecs.Imgcodecs.imencode;

/**
 *
 * @author CMQ
 */
public class Utils {

    public static Mat toMat(BufferedImage image) {
        BufferedImage convertImg = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_3BYTE_BGR);
        convertImg.getGraphics().drawImage(image, 0, 0, null);
        byte[] pixels = ((DataBufferByte) convertImg.getRaster().getDataBuffer()).getData();

        // Create a Matrix the same size of image
        Mat matImage = new Mat(image.getHeight(), image.getWidth(), CV_8UC3);
        // Fill Matrix with image values
        matImage.put(0, 0, pixels);

        return matImage;
    }

    public static Mat toMat(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];

        PixelReader reader = image.getPixelReader();
        WritablePixelFormat<ByteBuffer> format = getByteBgraInstance();
        reader.getPixels(0, 0, width, height, format, buffer, 0, width * 4);

        Mat mat = new Mat(height, width, CV_8UC4);
        mat.put(0, 0, buffer);
        return mat;
    }

    public static BufferedImage toBufferedImage(Mat matrix) {
        MatOfByte mob = new MatOfByte();
        imencode(matrix.channels() > 3 ? ".png" : ".bmp", matrix, mob);
        byte ba[] = mob.toArray();

        BufferedImage bi = null;
        try {
            bi = read(new ByteArrayInputStream(ba));
        } catch (IOException ex) {
            getLogger(Utils.class.getName()).log(SEVERE, null, ex);
        }
        return bi;
    }

    public static Image toImage(Mat matrix) {
        MatOfByte byteMat = new MatOfByte();
        imencode(matrix.channels() > 3 ? ".png" : ".bmp", matrix, byteMat);
        return new Image(new ByteArrayInputStream(byteMat.toArray()));
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static BufferedImage getThumbnail(BufferedImage image, int width, int height) {
        java.awt.Image scaledImage = image.getScaledInstance(width, height, SCALE_SMOOTH);
        return toBufferedImage(scaledImage);
    }

    public static Image getThumbnail(Image image, int width, int height) {
        return toFXImage(getThumbnail(fromFXImage(image, null), width, height),
                null);
    }

    public static BufferedImage toBufferedImage(java.awt.Image image) {
        BufferedImage output = new BufferedImage(image.getWidth(null), image.getHeight(null), TYPE_INT_ARGB);
        Graphics graphics = output.getGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return output;
    }
}
