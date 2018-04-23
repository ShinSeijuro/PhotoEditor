/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import PlugIn.ScreenCapture;
import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class FullScreenController implements Initializable {

    public ImageView getImageView() {
        return imageView;
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
            return;
        }

        this.zoomRatio.set(zoomRatio);
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
                setZoomRatio(getZoomRatio() + (event.getDeltaY() / 1000.0));
                event.consume();
            }
        });
    }

    public void setupImageView(ImageView otherImageView, Stage stage) {
        Image image = otherImageView.getImage();
        imageView.setImage(image);
        imageView.setEffect(otherImageView.getEffect());

        Dimension screenDimension = ScreenCapture.getScreenSize();
        if (!(image.getWidth() > screenDimension.getWidth()
                || image.getHeight() > screenDimension.getHeight())) {
            return;
        }

        if (image.getWidth() > image.getHeight()) {
            getImageView().fitWidthProperty().bind(stage.widthProperty());
        } else {
            getImageView().fitHeightProperty().bind(stage.heightProperty());
        }
    }

    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;

}
