/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.AbstractImageAction;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class Crop extends AbstractImageAction {

    RubberBandSelection rubberBandSelection;
    Bounds bounds;
    BufferedImage image;
    Rectangle rect;

    public RubberBandSelection getRubberBandSelection() {
        return rubberBandSelection;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public Crop(BufferedImage originalImage, Rectangle rect) {
        super(originalImage);
        this.rect = rect;
        this.originalImage = originalImage;
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        this.image = image;
        BufferedImage dest = image.getSubimage((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        return dest;
    }
}
