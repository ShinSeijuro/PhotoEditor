/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ImageProcessing;

import Action.AbstractImageAction;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Yuuki
 */
public class GaussianBlur extends AbstractImageAction {

    private double sigma;

    private int kernelsize;

    public int getKernelsize() {
        return kernelsize;
    }

    public void setKernelsize(int kernelsize) {
        this.kernelsize = kernelsize;
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
    }

    public GaussianBlur(BufferedImage originalImage, double sigma, int kernelsize) {
        super(originalImage);
        this.sigma = sigma;
        this.kernelsize = kernelsize;
        this.setName("Gaussian Blur");
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        double[] kernel = createKernel(sigma, kernelsize);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                double overflow = 0;
                int counter = 0;
                int kernelhalf = (kernelsize - 1) / 2;
                double red = 0;
                double green = 0;
                double blue = 0;
                for (int k = i - kernelhalf; k < i + kernelhalf; k++) {
                    for (int l = j - kernelhalf; l < j + kernelhalf; l++) {
                        if (k < 0 || k >= image.getWidth() || l < 0 || l >= image.getHeight()) {
                            counter++;
                            overflow += kernel[counter];
                            continue;
                        }

                        Color c = new Color(image.getRGB(k, l));
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
                            if (k < 0 || k >= image.getWidth() || l < 0 || l >= image.getHeight()) {
                                counter++;
                                continue;
                            }

                            Color c = new Color(image.getRGB(k, l));
                            red += c.getRed() * kernel[counter] * (1 / (1 - overflow));
                            green += c.getGreen() * kernel[counter] * (1 / (1 - overflow));
                            blue += c.getBlue() * kernel[counter] * (1 / (1 - overflow));
                            counter++;
                        }
                        counter++;
                    }
                }

                image.setRGB(i, j, new Color((int) red % 255, (int) green % 255, (int) blue % 255).getRGB());
            }
        }
        return image;
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

}
