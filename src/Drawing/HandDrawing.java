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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
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

    private final DragContext dragContext = new DragContext();

    public enum Tool {
        PEN,
        ERASER,
        CIRCLE,
        RECTANGLE
    }

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

    public ObservableList<Shape> getPathList() {
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
            dragContext.mouseAnchorX = event.getX();
            dragContext.mouseAnchorY = event.getY();
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
                case RECTANGLE:
                    Rectangle rect = new Rectangle(x, y, 0, 0);
                    rect.setStroke(stroke);
                    rect.setStrokeWidth(strokeWidth);
                    rect.setStrokeLineCap(StrokeLineCap.ROUND);
                    rect.setFill(Color.TRANSPARENT);
                    shapeList.add(rect);
                    break;
                case CIRCLE:

                    Circle circle = new Circle(x, y, 0);
                    circle.setStroke(stroke);
                    circle.setStrokeWidth(strokeWidth);
                    circle.setStrokeLineCap(StrokeLineCap.ROUND);
                    circle.setFill(Color.TRANSPARENT);
//                    circle.setCenterX(rect.getX() + rect.getWidth() / 2);
//                    circle.setCenterY(rect.getY() + rect.getHeight() / 2);
                    shapeList.add(circle);
                    //shapeList.add(rect);
                    //Path circle = new Path();
                    //circle.getElements().clear();
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
            double offsetX = event.getX() - dragContext.mouseAnchorX;
            double offsetY = event.getY() - dragContext.mouseAnchorY;

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
                case RECTANGLE:
                    //Circle circle = (Circle) shapeList.get(shapeList.size() - 1);
                    Rectangle rect = (Rectangle) shapeList.get(shapeList.size() - 1);
                    rect.setWidth(Math.abs(x - rect.getX()));
                    rect.setHeight(Math.abs(y - rect.getY()));
                    break;
                case CIRCLE:
                    Circle circle = (Circle) shapeList.get(shapeList.size() - 1);
                    double startX = circle.getCenterX();
                    double startY = circle.getCenterY();
                    if (x > dragContext.mouseAnchorX) {
                        circle.setCenterX(((x - dragContext.mouseAnchorX) / 2.0) + dragContext.mouseAnchorX);
                    } else if (x < dragContext.mouseAnchorX) {
                        circle.setCenterX((dragContext.mouseAnchorX - x) / 2.0 + x);
                    }
                    if (y > dragContext.mouseAnchorY) {
                        circle.setCenterY((y - dragContext.mouseAnchorY) / 2.0 + dragContext.mouseAnchorY);
                    } else if (y < dragContext.mouseAnchorY) {
                        circle.setCenterY((dragContext.mouseAnchorY - y) / 2.0 + y);
                    }
//                    if (offsetX > 0) {
//                        circle.setRadius(offsetX / 2);
//                    } else {
//                        circle.setCenterX(event.getX());
//                        circle.setRadius((dragContext.mouseAnchorX - circle.getCenterX()) / 2);
//                    }
//                    if (offsetY > 0) {
//                        circle.setRadius(offsetY / 2);
//                    } else {
//                        circle.setCenterX(event.getY());
//                        circle.setRadius((dragContext.mouseAnchorX - circle.getCenterY()) / 2);
//                    }
                    circle.setRadius(Math.abs(dragContext.mouseAnchorX - startX) / 2);
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
