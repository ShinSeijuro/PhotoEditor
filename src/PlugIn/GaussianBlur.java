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
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GaussianBlur {

    public static BufferedImage img;

    public static void main(String args[]) {
        readImage();
        blur(10, 49); //Giá trị đầu từ 1 đến 10 hoặc muốn thì đến 20 cũng đc // Giá trị thứ 2 là 49 thì chuẩn nhất
        writeImage();
    }

    public static void readImage() {
        GaussianBlur.img = null;
        try {
            img = ImageIO.read(new File("D:/test.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void blur(double sigma, int kernelsize) {
        double[] kernel = createKernel(sigma, kernelsize);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                double overflow = 0;
                int counter = 0;
                int kernelhalf = (kernelsize - 1) / 2;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int k = i - kernelhalf; k < i + kernelhalf; k++) {
                    for (int l = j - kernelhalf; l < j + kernelhalf; l++) {
                        if (k < 0 || k >= img.getWidth() || l < 0 || l >= img.getHeight()) {
                            counter++;
                            overflow += kernel[counter];
                            continue;
                        }

                        Color c = new Color(img.getRGB(k, l));
                        red += c.getRed() * kernel[counter];
                        green += c.getGreen() * kernel[counter];
                        blue += c.getBlue() * kernel[counter];
                        counter++;
                    }
                    counter++;
                }

                if (overflow > 0) {
                    red = 0;
                    green = 0;
                    blue = 0;
                    counter = 0;
                    for (int k = i - kernelhalf; k < i + kernelhalf; k++) {
                        for (int l = j - kernelhalf; l < j + kernelhalf; l++) {
                            if (k < 0 || k >= img.getWidth() || l < 0 || l >= img.getHeight()) {
                                counter++;
                                continue;
                            }

                            Color c = new Color(img.getRGB(k, l));
                            red += c.getRed() * kernel[counter] * (1 / (1 - overflow));
                            green += c.getGreen() * kernel[counter] * (1 / (1 - overflow));
                            blue += c.getBlue() * kernel[counter] * (1 / (1 - overflow));
                            counter++;
                        }
                        counter++;
                    }
                }

                GaussianBlur.img.setRGB(i, j, new Color((int) red % 255, (int) green % 255, (int) blue % 255).getRGB());
            }
        }
    }

    public static double[] createKernel(double sigma, int kernelsize) {
        double[] kernel = new double[kernelsize * kernelsize];
        for (int i = 0; i < kernelsize; i++) {
            double x = i - (kernelsize - 1) / 2;
            for (int j = 0; j < kernelsize; j++) {
                double y = j - (kernelsize - 1) / 2;
                kernel[j + i * kernelsize] = 1 / (2 * Math.PI * sigma * sigma) * Math.exp(-(x * x + y * y) / (2 * sigma * sigma));
            }
        }
        float sum = 0;
        for (int i = 0; i < kernelsize; i++) {
            for (int j = 0; j < kernelsize; j++) {
                sum += kernel[j + i * kernelsize];
            }
        }
        for (int i = 0; i < kernelsize; i++) {
            for (int j = 0; j < kernelsize; j++) {
                kernel[j + i * kernelsize] /= sum;
            }
        }
        return kernel;
    }

    public static void lineareAbbildung(double a, double b) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgb = (int) (a * GaussianBlur.img.getRGB(x, y) + b);
                GaussianBlur.img.setRGB(x, y, rgb);
            }
        }
    }

    public static void blackAndWhite() {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgb = GaussianBlur.img.getRGB(x, y);
                Color c = new Color(rgb);
                int grey = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
                Color c2 = new Color(grey, grey, grey);
                GaussianBlur.img.setRGB(x, y, c2.getRGB());
            }
        }
    }

    public static void improveGrey() {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                int rgb = GaussianBlur.img.getRGB(x, y);
                if (rgb > max) {
                    max = rgb;
                }
                if (rgb < min) {
                    min = rgb;
                }
            }
        }
        lineareAbbildung(255 / (max - min), - 255 * min / (max - min));
    }

    public static void writeImage() {
        File output = new File("D:/Blur10v49.png");
        try {
            ImageIO.write(img, "png", output);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
