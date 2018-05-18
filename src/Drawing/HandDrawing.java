/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Drawing;

import Action.ImageSnapshotAction;
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
public class HandDrawing extends ImageSnapshotAction {

    public enum Tool {
        PEN,
        ERASER,
        LINE,
        ELLIPSE,
        RECTANGLE
    }

    private final DragContext dragContext = new DragContext();

    public Group getGroup() {
        return (Group) getNode();
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
        if (this.strokeWidth < strokeWidth) {
            this.maxX -= strokeWidth - this.strokeWidth;
            this.maxY -= strokeWidth - this.strokeWidth;
        } else if (this.strokeWidth > strokeWidth) {
            this.maxX += strokeWidth - this.strokeWidth;
            this.maxY = strokeWidth - this.strokeWidth;
        }

        this.strokeWidth = strokeWidth;
    }

    private final ObservableList<Shape> shapeList;

    public ObservableList<Shape> getShapeList() {
        return shapeList;
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

    public HandDrawing(Image originalImage, Group node) {
        super(originalImage, node);
        setName("Hand Drawing");
        this.shapeList = FXCollections.observableArrayList();
        this.tool = Tool.PEN;
        this.stroke = Color.BLACK;
        this.strokeWidth = 10.0;
        maxX = originalImage.getWidth();
        maxY = originalImage.getHeight();
        addEventHandler();

        this.shapeList.addListener(new ListChangeListener<Shape>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Shape> c) {
                while (c.next()) {
                    if (c.wasAdded()) {
                        node.getChildren().addAll(c.getAddedSubList());
                    } else if (c.wasRemoved()) {
                        node.getChildren().removeAll(c.getRemoved());
                    }
                }
            }
        });
    }

    public void finish() {
        removeEventHandler();
    }

    private void erase() {
        if (tool == Tool.ERASER
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
        Group group = getGroup();
        group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
        group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);
    }

    private void removeEventHandler() {
        Group group = getGroup();
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

            double x = event.getX();
            double y = event.getY();

            switch (tool) {
                case PEN:
                case ERASER:
                    Path path = new Path();
                    path.setStroke(stroke);
                    path.setStrokeWidth(strokeWidth);
                    path.setStrokeLineCap(StrokeLineCap.ROUND);
                    path.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    path.getElements().clear();
                    path.getElements().add(new MoveTo(x, y));
                    path.getElements().add(new LineTo(x, y));

                    if (tool == Tool.PEN) {
                        shapeList.add(path);
                    } else {
                        eraserPath = path;
                    }

                    break;
                case LINE:
                    Line line = new Line(x, y, x, y);
                    line.setStroke(stroke);
                    line.setStrokeWidth(strokeWidth);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    line.setStrokeLineJoin(StrokeLineJoin.ROUND);
                    shapeList.add(line);
                    break;
                case RECTANGLE:
                    dragContext.mouseAnchorX = event.getX();
                    dragContext.mouseAnchorY = event.getY();

                    Rectangle rect = new Rectangle(x, y, 0, 0);
                    rect.setStroke(stroke);
                    rect.setStrokeWidth(strokeWidth);
                    rect.setStrokeLineCap(StrokeLineCap.ROUND);
                    rect.setFill(Color.TRANSPARENT);
                    shapeList.add(rect);
                    break;
                case ELLIPSE:
                    dragContext.mouseAnchorX = event.getX();
                    dragContext.mouseAnchorY = event.getY();

                    Ellipse ellipse = new Ellipse(x, y, 0, 0);
                    ellipse.setStroke(stroke);
                    ellipse.setStrokeWidth(strokeWidth);
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

            switch (tool) {
                case PEN:
                    ((Path) shapeList.get(shapeList.size() - 1)).getElements().add(new LineTo(x, y));
                    break;
                case ERASER:
                    eraserPath.getElements().add(new LineTo(x, y));
                    erase();
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
        }
    };
}
