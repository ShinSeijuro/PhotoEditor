/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.Rectangle;
import Drawing.DragContext;

/**
 *
 * @author Admin
 */
public class Selection {

    private final DragContext dragContext = new DragContext();

    public DragContext getDragContext() {
        return dragContext;
    }

    private Rectangle rect;

    public Rectangle getRect() {
        return rect;
    }

    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        boolean oldState = isDisabled();
        setDisabled(true);
        this.group = group;
        setDisabled(oldState);
    }

    public Bounds getBounds() {
        return rect.getBoundsInParent();
    }

    private boolean disabled;

    public final boolean isDisabled() {
        return disabled;
    }

    public final void setDisabled(boolean disabled) {
        this.disabled = disabled;

        if (group != null) {
            if (disabled) {
                removeEventHandler();
                removeRect();
            } else {
                addEventHandler();
            }
        }
    }

    public Selection() {
        rect = new Rectangle(0, 0, 0, 0);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

        disabled = false;
    }

    private void removeRect() {
        group.getChildren().remove(rect);
    }

    private void addEventHandler() {
        group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    private void removeEventHandler() {
        group.removeEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        group.removeEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        group.removeEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    public final boolean isNothing() {
        return rect == null
                || rect.getWidth() == 0
                || rect.getHeight() == 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        removeEventHandler();
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            // remove old rect
            removeRect();

            // prepare new drag operation
            dragContext.mouseAnchorX = event.getX();
            dragContext.mouseAnchorY = event.getY();

            rect.setX(dragContext.mouseAnchorX);
            rect.setY(dragContext.mouseAnchorY);
            rect.setWidth(0);
            rect.setHeight(0);

            group.getChildren().add(rect);

        }
    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            double offsetX = event.getX() - dragContext.mouseAnchorX;
            double offsetY = event.getY() - dragContext.mouseAnchorY;

            if (offsetX >= 0) {
                rect.setWidth(offsetX);
            } else {
                rect.setX(event.getX());
                rect.setWidth(dragContext.mouseAnchorX - rect.getX());
            }

            if (offsetY >= 0) {
                rect.setHeight(offsetY);
            } else {
                rect.setY(event.getY());
                rect.setHeight(dragContext.mouseAnchorY - rect.getY());
            }
        }
    };

    private final EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            if (isNothing()) {
                removeRect();
            }
        }
    };
}
