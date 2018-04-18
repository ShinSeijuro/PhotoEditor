/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import ImageProcessing.*;
import Action.*;
import Adjustment.*;
import Transformation.*;
import History.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.*;
import javafx.scene.control.Tab;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.ScrollEvent;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Yuuki
 */
public class WorkspaceController implements Initializable {

    private final HashMap<String, ImageTab> tabs = new HashMap<>();

    public HashMap<String, ImageTab> getTabs() {
        return tabs;
    }

    private ImageTab currentTab;

    public ImageTab getCurrentTab() {
        return currentTab;
    }

    private void setCurrentTab(ImageTab currentTab) {
        this.currentTab = currentTab;

        if (currentTab != null) {
            setCurrentController(currentTab.getController());
        } else {
            setCurrentController(null);
        }
    }

    private ImageTabController currentController;

    public ImageTabController getCurrentController() {
        return currentController;
    }

    private void setCurrentController(ImageTabController currentController) {
        this.currentController = currentController;
    }

    public History getCurrentHistory() {
        if (currentController == null) {
            return null;
        }
        return currentController.getHistory();
    }

    public BufferedImage getCurrentImage() {
        return currentController.getBufferedImage();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                setCurrentTab((ImageTab) newValue);
            }
        });
        brSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness((double) new_val);
                getCurrentController().getImageView().setEffect(colorAdjust);
            }
        });
        hueSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setHue((double) new_val);
                getCurrentController().getImageView().setEffect(colorAdjust);
            }
        });
        sarSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setSaturation((double) new_val);
                getCurrentController().getImageView().setEffect(colorAdjust);
            }
        });
        contSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setContrast((double) new_val);
                getCurrentController().getImageView().setEffect(colorAdjust);
            }
        });

        refreshMenuBar();
    }

    public void applyAction(AbstractImageAction action) {
        BufferedImage image = action.applyTransform();
        currentController.setBufferedImage(image);
        getCurrentHistory().add(action);
    }

    public void refreshMenuBar() {
        boolean isDisable = tabs.isEmpty();
        menuSave.setDisable(isDisable);
        menuSaveAs.setDisable(isDisable);
        menuEdit.setDisable(isDisable);
        menuImage.setDisable(isDisable);
    }

    public void loadFile(File file) {
        String tabName = file.getName();

        if (tabs.containsKey(tabName)) {
            tabPane.getSelectionModel().select(tabs.get(tabName));
        } else {
            ImageTab tab = new ImageTab(file);
            tab.setOnClosed((e) -> {
                tabs.remove(tabName);
                refreshMenuBar();
            });

            tabPane.getTabs().add(tab);
            tabs.put(tabName, tab);
            tabPane.getSelectionModel().selectLast();
            refreshMenuBar();
        }
    }

    public void loadFile(List<File> files) {
        for (File file : files) {
            loadFile(file);
        }
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
    public void onFileSave(ActionEvent event) throws IOException {
        File outputfile = new File(this.currentTab.getFile().getPath());
        ImageIO.write(this.getCurrentImage(), "png", outputfile);
    }

    @FXML
    public void onFileClose(ActionEvent event) {
        PhotoEditor.getPrimaryStage().close();

        //TODO: You have unsaved documents. Do you want to exit?
    }

    @FXML
    public void onRotateRight90(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), Math.toRadians(90)));
    }

    @FXML
    public void onRotateLeft90(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), Math.toRadians(-90)));
    }

    @FXML
    public void onRotateRight180(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), Math.toRadians(180)));
    }

    @FXML
    public void onRotateLeft180(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), Math.toRadians(-180)));
    }

    @FXML
    public void onFlipHorizontal(ActionEvent event) {
        applyAction(new Flip(getCurrentImage(), Flip.Orientation.Horizontal));
    }

    public void onFlipVertical(ActionEvent event) {
        applyAction(new Flip(getCurrentImage(), Flip.Orientation.Vertical));
    }

    @FXML
    public void onBlackAndWhite(ActionEvent event) {
        applyAction(new GrayScale(getCurrentImage()));
    }

    @FXML
    public void onBlur(ActionEvent event) {
        applyAction(new BoxBlur(getCurrentImage()));
    }

    public void onSharpen(ActionEvent event) {
        applyAction(new Sharpen(getCurrentImage()));
    }

    @FXML
    public void onGaussianBlur(ActionEvent event) {
        applyAction(new GaussianBlur(getCurrentImage(), 2, 49));
    }

    @FXML
    public void onUndo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.undo();
        BufferedImage image = currentHistory.getCurrentImage();
        currentController.setBufferedImage(image);
    }

    @FXML
    public void onRedo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.redo();
        BufferedImage image = currentHistory.getCurrentImage();
        currentController.setBufferedImage(image);
    }

    @FXML
    public void onCrop(ActionEvent event) {
        BufferedImage image = getCurrentController().getBufferedImage();
        Rectangle rect = getCurrentController().getRect();
        if (rect == null) {
            return;
        }
        Crop crop = new Crop(image, rect);
        image = crop.applyTransform();
        getCurrentController().setBufferedImage(image);
        getCurrentController().getPane().getChildren().remove(rect);
    }

    @FXML
    void onMenuEditShowing(Event event) {
        History currentHistory = getCurrentHistory();

        if (currentHistory.isUndoable()) {
            menuUndo.setDisable(false);
            menuUndo.setText("Undo " + currentHistory.getUndoDeque().getFirst().getName());
        } else {
            menuUndo.setDisable(true);
            menuUndo.setText("Undo");
        }

        if (currentHistory.isRedoable()) {
            menuRedo.setDisable(false);
            menuRedo.setText("Redo " + currentHistory.getRedoDeque().getFirst().getName());
        } else {
            menuRedo.setDisable(true);
            menuRedo.setText("Redo");
        }
    }

    @FXML
    void onBrigtnessChange(ScrollEvent event) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(event.getDeltaX());
        getCurrentController().getImageView().setEffect(colorAdjust);
    }

    /* Controls */
    @FXML
    private TabPane tabPane;
    @FXML
    private Menu menuEdit;
    @FXML
    private Menu menuImage;
    @FXML
    private Button buttonRotate;
    @FXML
    private MenuItem menuUndo;
    @FXML
    private MenuItem menuRedo;
    @FXML
    private MenuItem menuSave;
    @FXML
    private MenuItem menuSaveAs;
    @FXML
    private Slider brSlider;
    @FXML
    private Slider hueSlider;
    @FXML
    private Slider sarSlider;
    @FXML
    private Slider contSlider;
}
