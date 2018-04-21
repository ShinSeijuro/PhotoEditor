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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

}
