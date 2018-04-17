/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import History.History;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class ImageTabController extends Tab implements Initializable {

    private History history;

    public History getHistory() {
        return history;
    }

    @FXML
    private ImageView imageView;

    public ImageView getImageView() {
        return imageView;
    }

    public BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(imageView.getImage(), null);
    }

    public void setBufferedImage(BufferedImage image) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        history = new History();
    }
}
