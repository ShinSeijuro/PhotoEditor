/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlugIn;

/**
 *
 * @author Yuuki
 */
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class CaptureScreen {

    public static void main(String[] args) throws Exception {

        try {
            System.out.println("...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.exit(1);
        }

        String outFileName = "screen.jpg";

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        Robot robot = new Robot();

        BufferedImage image = robot.createScreenCapture(screenRect);

        ImageIO.write(image, "jpg", new File(outFileName));
        System.out.println("Da luu vao file \"" + outFileName + "\".");
    }
}
