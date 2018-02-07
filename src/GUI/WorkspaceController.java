/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 *
 * @author Yuuki
 */
public class WorkspaceController implements Initializable {

    @FXML
    private ImageView img;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

    }

    public void loadFile(File file) {
        //File file = new File("D:\\calendar.png");
        Image image = new Image(file.toURI().toString());
        // simple displays ImageView the image as is
        img.setImage(image);
    }

    @FXML
    public void onOpen(ActionEvent event) {
        // Doesn't go into this method on click of the menu
        System.out.println("Inside On Action!!!");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(PhotoEditor.getPrimaryStage());
        if (selectedFile != null) {
            loadFile(selectedFile);
        }

    }
}
