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

    public void setNode(Node node) {
        this.node = node;
    }

    public ImageSnapshotAction() {
        setName("Image Effect");
    }

    public ImageSnapshotAction(Node node) {
        this.node = node;
        setName("Image Effect");
    }

    @Override
    public Image applyTransform(Image image) {
        return takeSnapShot();
    }

    protected final Image takeSnapShot() {
        if (node == null) {
            throw new NullPointerException("Node is null.");
        }

        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        return node.snapshot(sp, null);
    }

}
