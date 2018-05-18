/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Drawing;

import Action.ImageSnapshotAction;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 *
 * @author Admin
 */
public class HandDrawing {

    public enum Tool {
        PEN,
        ERASER,
        LINE,
        ELLIPSE,
        RECTANGLE
    }

    private final DragContext dragContext = new DragContext();

    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        if (this.group != null) {
            shapeList.clear();
            removeEventHandler();
        }

        this.group = group;

        if (group != null) {
            addEventHandler();
        }
    }

    public void setGroup(Group group, Image image) {
        setGroup(group);
        maxX = image.getWidth();
        maxY = image.getHeight();
    }

    private final ObjectProperty<Paint> stroke = new SimpleObjectProperty<>(Color.BLACK);

    public Paint getStroke() {
        return stroke.get();
    }

    public void setStroke(Paint value) {
        stroke.set(value);
    }

    public ObjectProperty strokeProperty() {
        return stroke;
    }

    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(10.0);

    public double getStrokeWidth() {
        return strokeWidth.get();
    }

    public void setStrokeWidth(double value) {
        double offset = value - getStrokeWidth();
        this.maxX += offset;
        this.maxY += offset;

        strokeWidth.set(value);
    }

    public DoubleProperty strokeWidthProperty() {
        return strokeWidth;
    }

    private final ObservableList<Shape> shapeList;

    public ObservableList<Shape> getShapeList() {
        return shapeList;
    }

    private final ObjectProperty<Tool> tool = new SimpleObjectProperty<>(Tool.PEN);

    public Tool getTool() {
        return tool.get();
    }

    public void setTool(Tool value) {
        tool.set(value);
    }

    public ObjectProperty toolProperty() {
        return tool;
    }

    private final Path eraserPath = new Path();

    private double maxX;

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    private double maxY;

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public HandDrawing() {
        this.shapeList = FXCollections.observableArrayList();

        eraserPath.setStroke(Color.RED);
        eraserPath.setStrokeLineCap(StrokeLineCap.ROUND);
    }

    private void erase() {
        if (getTool() == Tool.ERASER
                && eraserPath != null
                && shapeList.size() > 0) {
            for (int i = shapeList.size() - 1; i >= 0; i--) {
                Shape shape = shapeList.get(i);
                Bounds bound = Shape.intersect(eraserPath, shape).getBoundsInLocal();
                if (bound.getWidth() != -1 || bound.getHeight() != -1) {
                    shapeList.remove(i);
                }
            }
        }
    }

    private void addEventHandler() {
        if (group != null) {
            shapeList.addListener(shapeListChangeListener);
            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        }
    }

    private void removeEventHandler() {
        if (group != null) {
            shapeList.removeListener(shapeListChangeListener);
            group.removeEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.removeEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.removeEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
        }
    }

    private final ListChangeListener<Shape> shapeListChangeListener
            = new ListChangeListener<Shape>() {
        @Override
        public void onChanged(ListChangeListener.Change<? extends Shape> c) {
            while (c.next()) {
                if (c.wasAdded()) {
                    group.getChildren().addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {
                    group.getChildren().removeAll(c.getRemoved());
                }
            }
        }
    };

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

            double x = event.getX();
            double y = event.getY();

            switch (getTool()) {
                case PEN:
                    Path path = new Path();
                    path.setStroke(getStroke());
                    path.setStrokeWidth(getStrokeWidth());
                    path.setStrokeLineCap(StrokeLineCap.ROUND);
                    path.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    path.getElements().clear();
                    path.getElements().add(new MoveTo(x, y));
                    path.getElements().add(new LineTo(x, y));
                    shapeList.add(path);

                    break;
                case ERASER:
                    eraserPath.setStrokeWidth(getStrokeWidth());
                    eraserPath.getElements().add(new MoveTo(x, y));
                    eraserPath.getElements().add(new LineTo(x, y));
                    getGroup().getChildren().add(eraserPath);
                    break;
                case LINE:
                    Line line = new Line(x, y, x, y);
                    line.setStroke(getStroke());
                    line.setStrokeWidth(getStrokeWidth());
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    line.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    shapeList.add(line);
                    break;
                case RECTANGLE:
                    dragContext.mouseAnchorX = event.getX();
                    dragContext.mouseAnchorY = event.getY();

                    Rectangle rect = new Rectangle(x, y, 0, 0);
                    rect.setStroke(getStroke());
                    rect.setStrokeWidth(getStrokeWidth());
                    rect.setStrokeLineCap(StrokeLineCap.ROUND);
                    rect.setFill(Color.TRANSPARENT);
                    shapeList.add(rect);
                    break;
                case ELLIPSE:
                    dragContext.mouseAnchorX = event.getX();
                    dragContext.mouseAnchorY = event.getY();

                    Ellipse ellipse = new Ellipse(x, y, 0, 0);
                    ellipse.setStroke(getStroke());
                    ellipse.setStrokeWidth(getStrokeWidth());
                    ellipse.setStrokeLineCap(StrokeLineCap.ROUND);
                    ellipse.setFill(Color.TRANSPARENT);
                    shapeList.add(ellipse);
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

            switch (getTool()) {
                case PEN:
                    ((Path) shapeList.get(shapeList.size() - 1)).getElements().add(new LineTo(x, y));
                    break;
                case ERASER:
                    eraserPath.getElements().add(new LineTo(x, y));
                    break;
                case LINE:
                    Line line = (Line) shapeList.get(shapeList.size() - 1);
                    line.setEndX(x);
                    line.setEndY(y);
                    break;
                case RECTANGLE:
                    Rectangle rect = (Rectangle) shapeList.get(shapeList.size() - 1);

                    if (x >= dragContext.mouseAnchorX) {
                        rect.setWidth(x - rect.getX());
                    } else {
                        rect.setX(x);
                        rect.setWidth(dragContext.mouseAnchorX - x);
                    }

                    if (y >= dragContext.mouseAnchorY) {
                        rect.setHeight(y - rect.getY());
                    } else {
                        rect.setY(y);
                        rect.setHeight(dragContext.mouseAnchorY - y);
                    }

                    break;
                case ELLIPSE:
                    Ellipse ellipse = (Ellipse) shapeList.get(shapeList.size() - 1);

                    if (x >= dragContext.mouseAnchorX) {
                        ellipse.setCenterX(
                                ((x - dragContext.mouseAnchorX) / 2.0)
                                + dragContext.mouseAnchorX);
                    } else {
                        ellipse.setCenterX(
                                (dragContext.mouseAnchorX - x) / 2.0
                                + x);
                    }

                    if (y >= dragContext.mouseAnchorY) {
                        ellipse.setCenterY(
                                (y - dragContext.mouseAnchorY) / 2.0
                                + dragContext.mouseAnchorY);
                    } else {
                        ellipse.setCenterY(
                                (dragContext.mouseAnchorY - y) / 2.0
                                + y);
                    }

                    ellipse.setRadiusX(Math.abs(x - dragContext.mouseAnchorX) / 2.0);
                    ellipse.setRadiusY(Math.abs(y - dragContext.mouseAnchorY) / 2.0);
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

            switch (getTool()) {
                case ERASER:
                    erase();
                    eraserPath.getElements().clear();
                    getGroup().getChildren().remove(eraserPath);
                    break;
            }
        }
    };
}
