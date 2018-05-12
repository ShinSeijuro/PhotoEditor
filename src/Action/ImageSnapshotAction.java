/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Action;

import javafx.scene.image.Image;
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

    public ImageSnapshotAction(Image originalImage, Node node) {
        super(originalImage);
        this.node = node;
    }

    @Override
    protected Image applyTransform(Image image) {
        return takeSnapShot();
    }

    protected final Image takeSnapShot() {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        return node.snapshot(sp, null);
    }

}
