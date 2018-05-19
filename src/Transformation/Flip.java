/*
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import Action.INameable;
import Action.ImageSnapshotAction;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author CMQ
 */
public class Flip extends ImageSnapshotAction {

    public enum Orientation implements INameable {
        Horizontal {
            @Override
            public void setName(String newName) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() {
                return "horizontal";
            }
        },
        Vertical {
            @Override
            public void setName(String newName) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public String getName() {
                return "vertical";
            }

        }
    }

    private Orientation orientation;

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        setName("Flip " + orientation.getName());
    }

    public Flip(ImageView node, Orientation orientation) {
        super(node);
        this.orientation = orientation;
        setName("Flip " + orientation.getName());
    }

    @Override
    public Image applyTransform(Image image) {
        Image output = null;
        switch (orientation) {
            case Horizontal:
                getNode().setScaleX(-1.0);
                output = takeSnapShot();
                getNode().setScaleX(1.0);
                break;
            case Vertical:
                getNode().setScaleY(-1.0);
                output = takeSnapShot();
                getNode().setScaleY(1.0);
                break;
        }

        return output;
    }

}
