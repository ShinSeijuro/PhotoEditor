/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Drawing;

import Action.AbstractImageAction;
import java.awt.image.BufferedImage;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;

/**
 *
 * @author Admin
 */
public class HandDrawing extends AbstractImageAction {

    public enum Tool {
        PEN,
        ERASER
    }

    private Group group;

    public Group getGroup() {
        return group;
    }

    private Paint stroke;

    public Paint getStroke() {
        return stroke;
    }

    public void setStroke(Paint stroke) {
        this.stroke = stroke;
    }

    private double strokeWidth;

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    private final ObservableList<Path> pathList;

    public ObservableList<Path> getPathList() {
        return pathList;
    }

    private Tool tool;

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    private Path eraserPath;

    private double maxX;
    private double maxY;

    public HandDrawing(BufferedImage originalImage, Group group) {
        super(originalImage);
        setName("Hand Drawing");
        this.pathList = FXCollections.observableArrayList();
        this.group = group;
        this.tool = Tool.PEN;
        this.stroke = Color.BLACK;
        this.strokeWidth = 1.0;
        maxX = originalImage.getWidth();
        maxY = originalImage.getHeight();
        addEventHandler();

        this.pathList.addListener(new ListChangeListener<Path>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Path> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        group.getChildren().addAll(c.getAddedSubList());
                    } else if (c.wasRemoved()) {
                        group.getChildren().removeAll(c.getRemoved());
                    }
                }
            }
        });
    }

    @Override
    protected BufferedImage applyTransform(BufferedImage image) {
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        image = SwingFXUtils.fromFXImage(group.snapshot(sp, null), null);
        return image;
    }

    public void finish() {
        removeEventHandler();
    }

    private void erase() {
        if (tool == Tool.ERASER
                && eraserPath != null
                && pathList.size() > 0) {
            for (int i = pathList.size() - 1; i >= 0; i--) {
                Path path = pathList.get(i);
                Bounds bound = Shape.intersect(eraserPath, path).getBoundsInLocal();
                if (bound.getWidth() != -1 || bound.getHeight() != -1) {
                    pathList.remove(i);
                }
            }
        }
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

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        finish();
    }

    private final EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            Path path = new Path();
            path.getElements().clear();
            path.getElements().add(new MoveTo(event.getX(), event.getY()));

            switch (tool) {
                case PEN:
                    pathList.add(path);
                    break;
                case ERASER:
                    eraserPath = path;
                    break;
            }
        }
    };

    private final EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }

            double x = event.getX();
            double y = event.getY();

            if (x < 0) {
                x = 0;
            } else if (x > maxX) {
                x = maxX;
            }

            if (y < 0) {
                y = 0;
            } else if (y > maxY) {
                y = maxY;
            }

            switch (tool) {
                case PEN:
                    pathList.get(pathList.size() - 1).getElements().add(new LineTo(x, y));
                    break;
                case ERASER:
                    eraserPath.getElements().add(new LineTo(x, y));
                    erase();
                    break;
            }
        }
    };

    private final EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if (event.isSecondaryButtonDown()) {
                return;
            }
        }
    };
}
