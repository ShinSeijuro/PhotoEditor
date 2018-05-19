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
import static java.awt.Toolkit.getDefaultToolkit;
import java.awt.image.*;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;

public class ScreenCapture {

    public static Dimension getScreenSize() {
        Toolkit toolkit = getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        return screenSize;
    }

    public static BufferedImage Capture() {
        Rectangle screenRect = new Rectangle(getScreenSize());
        return Capture(screenRect);
    }

    public static BufferedImage Capture(Rectangle rect) {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            getLogger(ScreenCapture.class.getName()).log(SEVERE, null, ex);
        }

        if (robot == null) {
            return null;
        }

        BufferedImage image = robot.createScreenCapture(rect);
        return image;
    }
}
