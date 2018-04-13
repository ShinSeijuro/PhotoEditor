/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Transformation.*;
import History.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Yuuki
 */
public class WorkspaceController implements Initializable {

    private HashMap<String, ImageTab> tabs = new HashMap<>();

    public HashMap<String, ImageTab> getTabs() {
        return tabs;
    }

    @FXML
    private TabPane tabPane;
    @FXML
    private Button buttonRotate;
    @FXML
    private MenuItem menuUndo;
    @FXML
    private MenuItem menuRedo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void loadFile(File file) {
        String tabName = file.getName();

        if (tabs.containsKey(tabName)) {
            tabPane.getSelectionModel().select(tabs.get(tabName));
        } else {
            ImageTab tab = new ImageTab(file);
            tab.setOnClosed(e -> tabs.remove(tabName));
            tabPane.getTabs().add(tab);
            tabs.put(tabName, tab);

            tabPane.getSelectionModel().selectLast();
        }
    }

    public ImageTab getCurrentTab() {
        return (ImageTab) tabPane.getSelectionModel().getSelectedItem();
    }

    public ImageTabController getCurrentController() {
        return getCurrentTab().getController();
    }

    @FXML
    public void onFileOpen(ActionEvent event) {
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

    @FXML
    public void onFileClose(ActionEvent event) {
        PhotoEditor.getPrimaryStage().close();

        //TODO: You have unsaved documents. Do you want to exit?
    }

    @FXML
    public void onRotate(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rotation rotate = new Rotation(image, Math.toRadians(90));
        image = rotate.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onUndo(ActionEvent event) {
        History.undo();
        BufferedImage image = History.getCurrentImage();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onRedo(ActionEvent event) {
        History.redo();
        BufferedImage image = History.getCurrentImage();
        getCurrentController().setBufferedImage(image);
    }
}
