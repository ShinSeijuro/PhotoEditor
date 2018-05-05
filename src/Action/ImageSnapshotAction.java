/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.paint.Color;

/**
 *
 * @author CMQ
 */
public class ImageSnapshotAction extends AbstractImageAction {

    private Node node;

    public Node getNode() {
        return node;
    }

    public ImageSnapshotAction(BufferedImage originalImage, Node node) {
        super(originalImage);
        this.node = node;
    }

    @Override
    protected final BufferedImage applyTransform(BufferedImage image) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        image = SwingFXUtils.fromFXImage(node.snapshot(sp, null), null);
        return image;
    }

}
