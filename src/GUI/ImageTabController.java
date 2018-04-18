/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import History.History;
import Transformation.Crop;
import Transformation.RubberBandSelection;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class ImageTabController extends Tab implements Initializable {

    RubberBandSelection rubberBandSelection;
    final DragContext dragContext = new DragContext();

    Rectangle rect = new Rectangle();

    public Pane getPane() {
        return pane;
    }
    private History history;

    public History getHistory() {
        return history;
    }

    public ImageView getImageView() {
        return imageView;
    }

    //public Pane
    public BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(imageView.getImage(), null);
    }

    public void setBufferedImage(BufferedImage image) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

    public Rectangle getRect() {
        return rect;
    }

    @FXML
    public void onMouseDragEnter(MouseEvent event) {
        //rubberBandSelection = new RubberBandSelection(imageView);
    }

    @FXML
    public void onMousePressed(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            return;
        }

        // remove old rect
        rect.setX(0);
        rect.setY(0);
        rect.setWidth(0);
        rect.setHeight(0);

        pane.getChildren().remove(rect);
        // prepare new drag operation
        dragContext.mouseAnchorX = event.getX();
        dragContext.mouseAnchorY = event.getY();

        rect.setX(dragContext.mouseAnchorX);
        rect.setY(dragContext.mouseAnchorY);
        rect.setWidth(0);
        rect.setHeight(0);

        pane.getChildren().add(rect);
    }

    @FXML
    public void onMouseDragged(MouseEvent event) {
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

    @FXML
    public void onMouseReleased(MouseEvent event) {
        if (event.isSecondaryButtonDown()) {
            return;
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        history = new History();

        rect = new Rectangle(0, 0, 0, 0);
        rect.setStroke(Color.BLUE);
        rect.setStrokeWidth(1);
        rect.setStrokeLineCap(StrokeLineCap.ROUND);
        rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));
    }

    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Pane pane;

    private final class DragContext {

        public double mouseAnchorX;
        public double mouseAnchorY;

    }
}
