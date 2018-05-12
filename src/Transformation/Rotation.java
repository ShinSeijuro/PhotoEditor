/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.ImageSnapshotAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author CMQ
 */
public class Rotation extends ImageSnapshotAction {

    private double angle;

    public double getAngle() {
        return angle;
    }

    public Rotation(Image originalImage, ImageView node, double angle) {
        super(originalImage, node);
        this.angle = angle;

        setName("Rotate " + (angle < 0 ? "left" : "right") + " " + Math.abs(angle) + "°");
    }

    @Override
    public Image applyTransform(Image image) {
        getNode().setRotate(angle);
        Image output = takeSnapShot();
        getNode().setRotate(0.0);
        return output;
    }

}
