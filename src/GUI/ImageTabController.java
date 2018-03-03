/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.net.URL;
import java.util.ResourceBundle;
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

    @FXML
    private ImageView imageView;

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
}
