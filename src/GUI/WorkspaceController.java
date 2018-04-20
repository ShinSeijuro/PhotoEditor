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
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tab;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
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
        if (getCurrentController() == null) {
            return null;
        }
        return getCurrentController().getHistory();
    }

    public BufferedImage getCurrentImage() {
        return getCurrentController().getBufferedImage();
    }

    private BooleanProperty isEmpty = new SimpleBooleanProperty(true);

    public BooleanProperty isEmptyProperty() {
        return this.isEmpty;
    }

    public boolean getIsEmpty() {
        return isEmpty.get();
    }

    private void setIsEmpty(boolean value) {
        isEmpty.set(value);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                if (newValue == null) {
                    setCurrentTab(null);
                    return;
                }

                setCurrentTab((ImageTab) newValue);
                sliderZoom.setValue(getCurrentController().getZoomRatio() * 100.0);
            }
        });

        accordionEdit.setManaged(false);

        sliderBrightness.valueProperty().addListener(colorAdjustChangeListener);
        sliderHue.valueProperty().addListener(colorAdjustChangeListener);
        sliderSaturation.valueProperty().addListener(colorAdjustChangeListener);
        sliderContrast.valueProperty().addListener(colorAdjustChangeListener);

        sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {

                getCurrentController().setZoomRatio(newValue.doubleValue() / 100.0);
                labelZoom.setText(newValue.intValue() + "%");
            }
        }
        );
        bblurSLider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                BoxBlur bb = new BoxBlur();
                bb.setWidth((double) new_val);
                bb.setHeight((double) new_val);
                bb.setIterations(3);
                getCurrentController().getImageView().setEffect(bb);
            }
        });

        GlowSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                Glow bb = new Glow((double) new_val);
                getCurrentController().getImageView().setEffect(bb);
            }
        });

        gsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                GaussianBlur gs = new GaussianBlur();
                gs.setRadius((double) new_val);
                getCurrentController().getImageView().setEffect(gs);
            }
        });

    }

    public void applyAction(AbstractImageAction action) {
        BufferedImage image = action.applyTransform();
        getCurrentController().setBufferedImage(image);
        getCurrentHistory().add(action);
    }

    public void loadFile(File file) {
        String tabName = file.getName();

        if (tabs.containsKey(tabName)) {
            tabPane.getSelectionModel().select(tabs.get(tabName));
        } else {
            ImageTab tab = new ImageTab(file);
            tab.setOnClosed((e) -> {
                tabs.remove(tabName);
                setIsEmpty(tabs.isEmpty());
            });

            tabPane.getTabs().add(tab);
            tabs.put(tabName, tab);
            tabPane.getSelectionModel().selectLast();
            setIsEmpty(false);
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
    public void onFileSaveAs(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName(getCurrentTab().getText());
        File savedFile = fileChooser.showSaveDialog(PhotoEditor.getPrimaryStage());

        if (savedFile != null) {

            try {
                ImageIO.write(this.getCurrentImage(), "png", savedFile);
            } catch (IOException e) {
            }
        }

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

    private ChangeListener<Number> colorAdjustChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            ColorAdjust colorAdjust = new ColorAdjust(
                    sliderHue.getValue() / 100.0,
                    sliderSaturation.getValue() / 100.0,
                    sliderBrightness.getValue() / 100.0,
                    sliderContrast.getValue() / 100.0);
            getCurrentController().getImageView().setEffect(colorAdjust);
        }
    };

    @FXML
    public void onBlur(ActionEvent event) {
        //applyAction(new BoxBlur(getCurrentImage()));
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);
        currentController.getImageView().setEffect(bb);
    }

    @FXML
    public void onGlow(ActionEvent event) {
        Glow bb = new Glow(0.5);
        currentController.getImageView().setEffect(bb);
    }

    @FXML
    public void onSharpen(ActionEvent event) {
        applyAction(new Sharpen(getCurrentImage()));
        //Sharpen s = new Sharp();
    }

    @FXML
    public void onGaussianBlur(ActionEvent event) {
        //applyAction(new GaussianBlur(getCurrentImage(), 2, 49));
    }

    @FXML
    public void onUndo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.undo();
        BufferedImage image = currentHistory.getCurrentImage();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    public void onRedo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.redo();
        BufferedImage image = currentHistory.getCurrentImage();
        getCurrentController().setBufferedImage(image);
    }

    @FXML
    private void onColorAdjustApply(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onColorAdjustUndoAll(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onColorAdjustUndoAll(ActionEvent event) {
        sliderBrightness.setValue(0);
        sliderContrast.setValue(0);
        sliderHue.setValue(0);
        sliderSaturation.setValue(0);
    }

    @FXML
    public void onToggleCrop(ActionEvent event) {
        if (toggleCrop.isSelected()) {
            getCurrentController().setIsSelecting(true);
            return;
        }

        Selection selection = getCurrentController().getSelection();
        if (selection.isNothing()) {
            return;
        }

        applyAction(new Crop(getCurrentImage(), selection.getRect()));
        getCurrentController().setIsSelecting(false);
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
    void onToggleEdit(ActionEvent event) {
        accordionEdit.setManaged(toggleEdit.isSelected());
    }

    /* Controls */
    @FXML
    private TabPane tabPane;
    @FXML
    private MenuItem menuUndo;
    @FXML
    private MenuItem menuRedo;
    @FXML
    private Slider sliderZoom;
    @FXML
    private Label labelZoom;
    @FXML
    private Slider sliderBrightness;
    @FXML
    private Slider sliderHue;
    @FXML
    private Slider sliderSaturation;
    @FXML
    private Slider sliderContrast;
    @FXML
    private Accordion accordionEdit;
    @FXML
    private ToggleButton toggleEdit;
    @FXML
    private ToggleButton toggleCrop;
    @FXML
    private Slider bawSlider;
    @FXML
    private Slider bblurSLider;
    @FXML
    private Slider GlowSlider;
    @FXML
    private Slider gsSlider;
    @FXML
    private Slider sharpSlider;
}
