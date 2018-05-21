/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import static PlugIn.ScreenCapture.getScreenSize;
import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import static javafx.scene.input.ScrollEvent.SCROLL;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class FullScreenController implements Initializable {

    private final DoubleProperty zoomRatio = new SimpleDoubleProperty(1.0);
    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;

    public ImageView getImageView() {
        return imageView;
    }

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
        scrollPane.addEventFilter(SCROLL, (ScrollEvent event) -> {
            setZoomRatio(getZoomRatio() + (event.getDeltaY() / 1000.0));
            event.consume();
        });
    }

    public void initStage(Stage stage) {
        stage.setTitle("PhotoEditor - Fullscreen");
        stage.getIcons().add(PhotoEditor.ICON_IMAGE);
        stage.setFullScreen(true);
        stage.fullScreenProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue == false) {
                stage.close();
            }
        });
    }

    public void setupImageView(ImageView otherImageView, Stage stage) {
        Image image = otherImageView.getImage();
        imageView.setImage(image);
        imageView.setEffect(otherImageView.getEffect());

        Dimension screenDimension = getScreenSize();
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
}
