/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transformation;

import java.awt.image.BufferedImage;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class RubberBandSelection {

    final DragContext dragContext = new DragContext();
    Rectangle rect = new Rectangle();

    ImageView imageView;

    public Bounds getBounds() {
        return rect.getBoundsInParent();
    }

    public RubberBandSelection(ImageView group) {

        this.imageView = group;
        rect = new Rectangle(0, 0, 0, 0);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

        group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            // remove old rect
            rect.setX(0);
            rect.setY(0);
            rect.setWidth(0);
            rect.setHeight(0);

            //imageView.remove(rect);
            // prepare new drag operation
            dragContext.mouseAnchorX = event.getX();
            dragContext.mouseAnchorY = event.getY();

            rect.setX(dragContext.mouseAnchorX);
            rect.setY(dragContext.mouseAnchorY);
            rect.setWidth(0);
            rect.setHeight(0);

            //imageView.getChildren().add(rect);
        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            double offsetX = event.getX() - dragContext.mouseAnchorX;
            double offsetY = event.getY() - dragContext.mouseAnchorY;

            if (offsetX > 0) {
                rect.setWidth(offsetX);
            } else {
                rect.setX(event.getX());
                rect.setWidth(dragContext.mouseAnchorX - rect.getX());
            }

            if (offsetY > 0) {
                rect.setHeight(offsetY);
            } else {
                rect.setY(event.getY());
                rect.setHeight(dragContext.mouseAnchorY - rect.getY());
            }
        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            // remove rectangle
            // note: we want to keep the ruuberband selection for the cropping => code is just commented out
            /*
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);
             */
        }
    };

    private static final class DragContext {

        public double mouseAnchorX;
        public double mouseAnchorY;

    }
}
