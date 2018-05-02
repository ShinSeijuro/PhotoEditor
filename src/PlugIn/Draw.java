/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PlugIn;

import Action.AbstractImageAction;
import GUI.Selection;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Admin
 */
public class Draw extends AbstractImageAction {

    private Group group;
    double initX;
    double initY;
    Line line;
    private final Draw.DragContext dragContext = new Draw.DragContext();

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }
            initX = event.getSceneX();
            initY = event.getSceneY();
            event.consume();
        }
    };
    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }
            line = new Line(initX, initY, event.getSceneX(), event.getSceneY());
            line.setFill(null);
            line.setStroke(Color.RED);
            line.setStrokeWidth(2);
            group.getChildren().add(line);
            initX = event.getSceneX();
            initY = event.getSceneY();
        }
    };
    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

        }
    };

    public Draw(BufferedImage originalImage, Group group) {
        super(originalImage);
        this.group = group;
        addEventHandler();
    }

    private void addEventHandler() {
        group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        image = SwingFXUtils.fromFXImage(group.snapshot(sp, null), null);
        return image;
    }

    private static final class DragContext {

        public double mouseAnchorX;
        public double mouseAnchorY;

    }
}
