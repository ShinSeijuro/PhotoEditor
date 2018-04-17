/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Adjustment.GrayScale;
import Transformation.*;
import History.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.*;

/**
 *
 * @author Yuuki
 */
public class WorkspaceController implements Initializable {

    private HashMap<String, ImageTab> tabs = new HashMap<>();

    public HashMap<String, ImageTab> getTabs() {
        return tabs;
    }

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

    public void loadFile(List<File> files) {
        for (File file : files) {
            loadFile(file);
        }
    }

    public ImageTab getCurrentTab() {
        return (ImageTab) tabPane.getSelectionModel().getSelectedItem();
    }

    public ImageTabController getCurrentController() {
        return getCurrentTab().getController();
    }

    private File lastDirectory;

    @FXML
    public void onFileOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));

        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(PhotoEditor.getPrimaryStage());

        if (selectedFiles.size() > 0) {
            loadFile(selectedFiles);
            lastDirectory = selectedFiles.get(0).getParentFile();
        }
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        PhotoEditor.getPrimaryStage().close();

        //TODO: You have unsaved documents. Do you want to exit?
    }

    @FXML
    public void onRotateRight90(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rotation rotate = new Rotation(image, Math.toRadians(90));
        image = rotate.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onBlur(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        BoxBlur blur = new BoxBlur(image);
        image = blur.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onRotateLeft90(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rotation rotate = new Rotation(image, Math.toRadians(-90));
        image = rotate.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onRotateRight180(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rotation rotate = new Rotation(image, Math.toRadians(180));
        image = rotate.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onRotateLeft180(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rotation rotate = new Rotation(image, Math.toRadians(-180));
        image = rotate.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onFlipHorizontal(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Flip flip = new Flip(image, Flip.Orientation.Horizontal);
        image = flip.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    public void onFlipVertical(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Flip flip = new Flip(image, Flip.Orientation.Vertical);
        image = flip.applyTransform();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onBlackAndWhite(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        GrayScale grayScale = new GrayScale(image);
        image = grayScale.applyTransform();
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

    @FXML
    public void onFileSave(ActionEvent event) {

    }

    @FXML
    void onMenuEditShowing(Event event) {
        if (History.isUndoable()) {
            menuUndo.setDisable(false);
            menuUndo.setText("Undo " + History.getUndoDeque().getFirst().getName());
        } else {
            menuUndo.setDisable(true);
            menuUndo.setText("Undo");
        }

        if (History.isRedoable()) {
            menuRedo.setDisable(false);
            menuRedo.setText("Redo " + History.getRedoDeque().getFirst().getName());
        } else {
            menuRedo.setDisable(true);
            menuRedo.setText("Redo");
        }
    }

    /* Controls */
    @FXML
    private TabPane tabPane;
    @FXML
    private Button buttonRotate;
    @FXML
    private MenuItem menuUndo;
    @FXML
    private MenuItem menuRedo;
}
