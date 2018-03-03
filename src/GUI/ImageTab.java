/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author CMQ
 */
public class ImageTab extends Tab {

    public ImageTab(File file) {
        super();

        this.file = file;
        super.setText(file.getName());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageTab.fxml"));
            AnchorPane tabPage = (AnchorPane) loader.load();
            controller = loader.getController();

            super.setContent(tabPage);
            Image image = new Image(this.file.toURI().toString());
            controller.getImageView().setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private File file;

    public File getFile() {
        return file;
    }

    private ImageTabController controller;

    public ImageTabController getController() {
        return controller;
    }
}
