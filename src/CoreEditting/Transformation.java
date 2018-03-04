/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CoreEditting;

import java.awt.Canvas;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

/**
 *
 * @author Admin
 */
public class Transformation {

    public static BufferedImage Rotate(BufferedImage bufferedImage, int radians) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radians, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        bufferedImage = op.filter(bufferedImage, null);
        return bufferedImage;
    }

    public void changeCanvas(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        int newCanvasWidth = canvasHeight;
        int newCanvasHeight = canvasWidth;
        //canvas.updateSize(newCanvasWidth, newCanvasHeight);
    }
}
