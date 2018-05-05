/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Drawing.HandDrawing;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class ImageTabController extends Tab implements Initializable {

    public ImageView getImageView() {
        return imageView;
    }

    public Group getGroupImage() {
        return groupImage;
    }

    public BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(imageView.getImage(), null);
    }

    public void setBufferedImage(BufferedImage image) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

    private Selection selection;

    public Selection getSelection() {
        return selection;
    }

    private boolean selecting = false;

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
        scrollPane.setPannable(!selecting);

        if (selecting) {
            if (selection == null) {
                selection = new Selection(groupImage);
            }
            selection.setDisabled(false);
        } else {
            if (selection != null) {
                selection.setDisabled(true);
            }
        }
    }

    private HandDrawing handDrawing;

    public HandDrawing getHandDrawing() {
        return handDrawing;
    }

    private boolean drawing = false;

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
        scrollPane.setPannable(!drawing);

        if (drawing) {
            if (handDrawing == null) {
                handDrawing = new HandDrawing(getBufferedImage(), groupImage);
            }
        } else {
            if (handDrawing != null) {
                handDrawing.finish();
                handDrawing = null;
            }
        }
    }

    private final DoubleProperty zoomRatio = new SimpleDoubleProperty(1.0);

    public DoubleProperty zoomRatioProperty() {
        return zoomRatio;
    }

    public double getZoomRatio() {
        return zoomRatio.get();
    }

    public void setZoomRatio(double zoomRatio) {
        if (zoomRatio < 0.1) {
            zoomRatio = 0.1;
        } else if (zoomRatio > 16.0) {
            zoomRatio = 16.0;
        }

        this.zoomRatio.set(zoomRatio);
    }

    private final BooleanProperty fitToView = new SimpleBooleanProperty(false);

    public BooleanProperty fitToViewProperty() {
        return fitToView;
    }

    public boolean getFitToView() {
        return fitToView.get();
    }

    public void setFitToView(boolean fitToView) {
        this.fitToView.set(fitToView);

        if (fitToView) {
            setZoomRatio(1.0);
            scrollPane.widthProperty().addListener(scrollPaneChangeListener);
            scrollPane.heightProperty().addListener(scrollPaneChangeListener);
            doFitToView();
        } else {
            scrollPane.widthProperty().removeListener(scrollPaneChangeListener);
            scrollPane.heightProperty().removeListener(scrollPaneChangeListener);
        }
    }

    private final ChangeListener<Number> scrollPaneChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            doFitToView();
        }
    };

    public void doFitToView() {
        Image image = imageView.getImage();
        double widthRatio = scrollPane.getWidth() / image.getWidth();
        double heightRatio = scrollPane.getHeight() / image.getHeight();
        if (widthRatio > heightRatio) {
            setZoomRatio(heightRatio - 0.005);
        } else {
            setZoomRatio(widthRatio - 0.005);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.isControlDown() && !event.isAltDown()) {
                    setZoomRatio(getZoomRatio() + (event.getDeltaY() / 1000.0));
                    if (getFitToView() == true) {
                        setFitToView(false);
                    }
                    event.consume();
                } else if (event.isAltDown() && !event.isControlDown()) {
                    scrollPane.setHvalue(scrollPane.getHvalue() + (-event.getDeltaY() / 750.0));
                    event.consume();
                }
            }
        });
    }

    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group groupImage;
}
