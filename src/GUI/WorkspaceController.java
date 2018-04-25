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
import PlugIn.ImageFromClipboard;
import PlugIn.WallpaperChanger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.event.*;
import javafx.scene.control.Tab;
import javafx.scene.effect.*;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import javax.print.PrintException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
        setIsEmpty(currentTab == null);

        if (currentTab != null) {
            setCurrentController(currentTab.getController());
        } else {
            setCurrentController(null);
        }
    }

    private final ObjectProperty<ImageTabController> currentController = new SimpleObjectProperty<>(null);

    public ObjectProperty<ImageTabController> currentControllerProperty() {
        return currentController;
    }

    public ImageTabController getCurrentController() {
        return currentController.get();
    }

    private void setCurrentController(ImageTabController currentController) {
        this.currentController.set(currentController);
    }

    public History getCurrentHistory() {
        if (getCurrentController() == null) {
            return null;
        }
        return getCurrentTab().getHistory();
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

        PhotoEditor.getPrimaryStage().setOnCloseRequest(onWindowCloseRequest);

        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                setCurrentTab((ImageTab) newValue);
            }
        });

        accordionEdit.setManaged(false);

        sliderBrightness.valueProperty().addListener(colorAdjustChangeListener);
        sliderHue.valueProperty().addListener(colorAdjustChangeListener);
        sliderSaturation.valueProperty().addListener(colorAdjustChangeListener);
        sliderContrast.valueProperty().addListener(colorAdjustChangeListener);

        titledPaneAdjustment.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllColorAdjust(null);
                }
            }
        });

        sliderGaussianRadius.valueProperty().addListener(gaussianBlurChangeListener);

        titledPaneGaussianBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllGaussianBlur(null);
                }
            }
        });

        sliderBoxBlurHeight.valueProperty().addListener(boxBlurChangeListener);
        sliderBoxBlurWidth.valueProperty().addListener(boxBlurChangeListener);
        sliderBoxBlurIteration.valueProperty().addListener(boxBlurChangeListener);

        titledPaneBoxBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllBoxBlur(null);
                }
            }
        });

        sliderMotionBlurAngle.valueProperty().addListener(motionBlurChangeListener);
        sliderMotionBlurRadius.valueProperty().addListener(motionBlurChangeListener);

        titledPaneMotionBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllMotionBlur(null);
                }
            }
        });

        sliderGlowLevel.valueProperty().addListener(glowChangeListener);

        titledPaneGlow.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllGlow(null);
                }
            }
        });

        sliderSepiaToneLevel.valueProperty().addListener(sepiaChangeListener);

        titledPaneSepiaTone.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onUndoAllSepiaTone(null);
                }
            }
        });

        sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {

                labelZoom.setText((int) (newValue.doubleValue() * 100.0) + "%");
            }
        });

        currentController.addListener(new ChangeListener<ImageTabController>() {
            @Override
            public void changed(ObservableValue<? extends ImageTabController> observable, ImageTabController oldValue, ImageTabController newValue) {
                if (oldValue != null) {
                    sliderZoom.valueProperty().unbindBidirectional(oldValue.zoomRatioProperty());
                    toggleFitToView.selectedProperty().unbindBidirectional(oldValue.fitToViewProperty());
                }

                if (newValue != null) {
                    sliderZoom.valueProperty().bindBidirectional(newValue.zoomRatioProperty());
                    toggleFitToView.selectedProperty().bindBidirectional(newValue.fitToViewProperty());
                } else {
                    sliderZoom.setValue(1.0);
                    toggleFitToView.setSelected(false);
                }
            }
        });
    }

    public void attachHotKeys(Scene scene) {
        KeyCombination keyComb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyComb.match(event)) {
                    onFullScreen(null);
                }
            }
        });
    }

    public Alert makeDialog(String title, String header, String content, AlertType alertType, ButtonType... buttonTypes) {
        final Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        if (buttonTypes != null) {
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(buttonTypes);
        }
        return alert;
    }

    public void applyAction(AbstractImageAction action) {
        BufferedImage image = action.applyTransform();
        getCurrentController().setBufferedImage(image);
        getCurrentHistory().add(action);
    }

    private EventHandler<WindowEvent> onWindowCloseRequest = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            int leftovers = 0;
            ObservableList<Tab> tabList = tabPane.getTabs();
            while (!tabList.isEmpty() && leftovers < tabList.size()) {
                ImageTab tab = (ImageTab) tabList.get(leftovers);
                EventHandler<Event> handler = tab.getOnCloseRequest();
                if (handler != null) {
                    Event e = new Event(tab, null, Tab.TAB_CLOSE_REQUEST_EVENT);
                    handler.handle(e);
                    if (e.isConsumed()) {
                        leftovers++;
                    } else {
                        tabList.remove(leftovers);
                    }
                } else {
                    tabList.remove(leftovers);
                }
            }

            if (leftovers > 0) {
                event.consume();
            }
        }
    };

    private final EventHandler<Event> onTabCloseRequest = new EventHandler<Event>() {
        @Override
        public void handle(Event event) {
            if (event == null) {
                return;
            }

            ImageTab tab = (ImageTab) event.getSource();
            if (tab.getHistory().isModified()) {
                tabPane.getSelectionModel().select(tab);

                Alert alert = makeDialog(
                        "Close tab",
                        null,
                        "Do you want to save changes you have made to \"" + tab.getTabName() + "\"?",
                        AlertType.CONFIRMATION,
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    onFileSave(null);
                } else if (result.get() == ButtonType.NO) {

                } else {
                    event.consume();
                }
            }
        }
    };

    public void loadFile(File file) {
        String tabName = file.getName();

        if (tabs.containsKey(tabName)) {
            tabPane.getSelectionModel().select(tabs.get(tabName));
        } else {
            ImageTab tab = null;
            try {
                tab = new ImageTab(file);
            } catch (IOException | IllegalArgumentException ex) {
                Alert alert = makeDialog("Open image...",
                        "ERROR: Unable to open file",
                        "Unable to open file: " + file.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR, (ButtonType) null);

                alert.show();
            }

            if (tab == null) {
                return;
            }

            tab.setOnClosed((e) -> {
                tabs.remove(tabName);
            });
            tab.setOnCloseRequest(onTabCloseRequest);

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

        if (selectedFiles != null) {
            loadFile(selectedFiles);
            lastDirectory = selectedFiles.get(0).getParentFile();
        }
    }

    @FXML
    public void onFileSave(ActionEvent event) {
        File outputFile = getCurrentTab().getFile();
        if (outputFile == null) {
            onFileSaveAs(null);
            return;
        }

        String name = outputFile.getName();
        try {
            ImageIO.write(getCurrentImage(), name.substring(name.lastIndexOf('.') + 1), outputFile);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = makeDialog("Save",
                    "ERROR: Unable to save file",
                    "Unable to save file: " + outputFile.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                    AlertType.ERROR, (ButtonType) null);
            alert.show();
        }
    }

    @FXML
    public void onFileSaveAs(ActionEvent event) {
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
                getCurrentTab().setFile(savedFile);
            } catch (IOException ex) {
                Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
                Alert alert = makeDialog("Save as...",
                        "ERROR: Unable to save file",
                        "Unable to save file: " + savedFile.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR, (ButtonType) null);
                alert.show();
            }
        }

    }

    @FXML
    public void onFileClose(ActionEvent event) {
        PhotoEditor.getPrimaryStage().close();
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

    private final ChangeListener<Number> colorAdjustChangeListener = new ChangeListener<Number>() {
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

    private final ChangeListener<Number> gaussianBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            GaussianBlur gaussianBlur = new GaussianBlur(
                    sliderGaussianRadius.getValue());
            getCurrentController().getImageView().setEffect(gaussianBlur);
        }

    };

    private final ChangeListener<Number> boxBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            BoxBlur boxBlur = new BoxBlur(
                    sliderBoxBlurWidth.getValue(),
                    sliderBoxBlurHeight.getValue(),
                    (int) sliderBoxBlurIteration.getValue());
            getCurrentController().getImageView().setEffect(boxBlur);
        }
    };

    private final ChangeListener<Number> motionBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            MotionBlur motionBlur = new MotionBlur(
                    sliderMotionBlurAngle.getValue(),
                    sliderMotionBlurRadius.getValue());
            getCurrentController().getImageView().setEffect(motionBlur);
        }
    };

    private final ChangeListener<Number> glowChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Glow glow = new Glow(
                    sliderGlowLevel.getValue() / 100.0);
            getCurrentController().getImageView().setEffect(glow);
        }
    };

    private final ChangeListener<Number> sepiaChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            SepiaTone sepiaTone = new SepiaTone(
                    sliderSepiaToneLevel.getValue() / 100.0);
            getCurrentController().getImageView().setEffect(sepiaTone);
        }
    };

    @FXML
    public void onPrint(ActionEvent event) throws FileNotFoundException, PrintException, IOException {
        printImage(this.getCurrentImage());
    }

    private void printImage(BufferedImage image) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable((Graphics graphics, PageFormat pageFormat, int pageIndex) -> {
            // Get the upper left corner that it printable
            int x = (int) Math.ceil(pageFormat.getImageableX());
            int y = (int) Math.ceil(pageFormat.getImageableY());
            if (pageIndex != 0) {
                return NO_SUCH_PAGE;
            }
            graphics.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
            return PAGE_EXISTS;
        });
        try {
            printJob.print();
        } catch (PrinterException e1) {
        }
    }

    @FXML
    public void onSharpen(ActionEvent event) {
        applyAction(new Sharpen(getCurrentImage()));
        //Sharpen s = new Sharp();
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
    private void onApplyColorAdjust(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllColorAdjust(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllColorAdjust(ActionEvent event) {
        sliderBrightness.setValue(0);
        sliderContrast.setValue(0);
        sliderHue.setValue(0);
        sliderSaturation.setValue(0);
    }

    @FXML
    private void onApplyGaussianBlur(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllGaussianBlur(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllGaussianBlur(ActionEvent event) {
        sliderGaussianRadius.setValue(0);
    }

    @FXML
    private void onApplyBoxBlur(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllBoxBlur(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllBoxBlur(ActionEvent event) {
        sliderBoxBlurWidth.setValue(0);
        sliderBoxBlurHeight.setValue(0);
    }

    @FXML
    private void onApplyMotionBlur(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllMotionBlur(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllMotionBlur(ActionEvent event) {
        sliderMotionBlurAngle.setValue(0);
        sliderMotionBlurRadius.setValue(0);
    }

    @FXML
    private void onApplyGlow(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllGlow(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllGlow(ActionEvent event) {
        sliderGlowLevel.setValue(0);
    }

    @FXML
    private void onApplySepiaTone(ActionEvent event) {
        ImageView imageView = getCurrentController().getImageView();
        applyAction(new ImageViewEffectAction(getCurrentImage(), imageView));
        onUndoAllSepiaTone(null);
        imageView.setEffect(null);
    }

    @FXML
    private void onUndoAllSepiaTone(ActionEvent event) {
        sliderSepiaToneLevel.setValue(0);
    }

    @FXML
    private void onToggleCrop(ActionEvent event) {
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
    private void onMenuEditShowing(Event event) {
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
    private void onToggleEdit(ActionEvent event) {
        accordionEdit.setManaged(toggleEdit.isSelected());
    }

    @FXML
    private void onFileInfo(ActionEvent event) {
        File currentFile = currentTab.getFile();

        if (currentFile == null) {
            Alert alert = makeDialog("File info",
                    null,
                    "You need to save this file to view its info.",
                    AlertType.ERROR, (ButtonType) null);
            alert.show();
            return;
        }

        Dimension2D dimension = currentTab.getOriginalDimension2D();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm");

        String content
                = "File name: " + currentFile.getName()
                + "\nDate: " + dateFormat.format(currentFile.lastModified())
                + "\nSize: " + (currentFile.length() / 1024) + " KB"
                + "\nDimension: " + (int) dimension.getWidth() + " x " + (int) dimension.getHeight()
                + "\nFolder: " + currentFile.getParent();

        ButtonType buttonTypeOpen = new ButtonType("Open file location");

        Alert info = makeDialog(
                "File info",
                null,
                content,
                AlertType.INFORMATION,
                buttonTypeOpen, ButtonType.OK);

        Optional<ButtonType> result = info.showAndWait();
        if (result.get() == buttonTypeOpen) {
            try {
                Runtime.getRuntime().exec("explorer.exe /select," + currentFile.getPath());
            } catch (IOException ex) {
                Alert alert = makeDialog("Open file location",
                        null,
                        "Folder not found!",
                        AlertType.ERROR, (ButtonType) null);
                alert.show();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void onFixRedEye(ActionEvent event) {
        // Obtain PixelReader
        Image img = getCurrentController().getImageView().getImage();
        PixelReader pixelReader = img.getPixelReader();
        // Create WritableImage
        WritableImage wImage = new WritableImage((int) img.getWidth(), (int) img.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);
                // Color values of the pixel from 0-1
                double r = color.getRed();
                double g = color.getGreen();
                double b = color.getBlue();

                // Color values from 0 to 255
                int red = (int) (r * 255);
                int green = (int) (g * 255);
                int blue = (int) (b * 255);

                /**
                 * **************************************************
                 * TO DO: The code to handle red eye for the left eye is
                 * basically the same as the code for the right eye. Write a
                 * method so an if statement block can be replaced with a single
                 * method, for example, color =
                 * replaceRedIfInBoundary(color,x,y,red,green,blue,133,149,169,185);
                 * would handle the left eye and a similar call could handle the
                 * right eye. **************************************************
                 */
                // If this is a pixel inside the left eye area
                if ((x >= 133) && (x < 149) && (y >= 169) && (y < 185)) {
                    // The red pixels had a red component > 80 and green < 60
                    if ((red > 80) && (green < 60)) {
                        red = (green + blue) / 2;		// Decreases the red to the average of the green and blue
                        color = Color.rgb(red, green, blue);  // Create new color for this pixel that is not so red
                    }
                }
                // If this is a pixel inside the right eye area
                if ((x >= 256) && (x < 270) && (y >= 143) && (y < 157)) {
                    // The red pixels had a red component > 80 and green < 60
                    if ((red > 80) && (green < 60)) {
                        red = (green + blue) / 2;		// Decreases the red to the average of the green and blue
                        color = Color.rgb(red, green, blue);  // Create new color for this pixel that is not so red
                    }
                }

                // Copy the pixel to the writable image
                pixelWriter.setColor(x, y, color);
            }
        }
        getCurrentController().getImageView().setImage(img);
    }

    @FXML
    private void onSetAsWallpaper(ActionEvent event) {
        File currentFile = getCurrentTab().getFile();
        if (currentFile == null) {
            onFileSaveAs(null);
        }
        if (currentFile == null) {
            Alert alert = makeDialog("Set as Wallpaper",
                    null,
                    "You need to save this file to set as wallpaper",
                    AlertType.ERROR, (ButtonType) null);
            alert.show();
            return;
        }

        WallpaperChanger.setWallpaper(currentFile.getPath());
    }

    @FXML
    private void onPasteFromClipboard(ActionEvent event) {
        BufferedImage image = ImageFromClipboard.get();
        if (image != null) {
            ImageTab tab = null;
            try {
                tab = new ImageTab(image);
            } catch (IOException | IllegalArgumentException ex) {
                Alert alert = makeDialog("Paste from Clipboard",
                        null,
                        "Unable to paste from clipboard." + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR, (ButtonType) null);
                alert.show();
            }

            if (tab == null) {
                return;
            }

            tab.setOnCloseRequest(onTabCloseRequest);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
        } else {
            Alert alert = makeDialog("Paste from Clipboard",
                    null,
                    "Clipboard does not contain any image!",
                    AlertType.ERROR, (ButtonType) null);
            alert.show();
        }
    }

    @FXML
    private void onFullScreen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FullScreen.fxml"));
            Parent root = loader.load();
            FullScreenController controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("PhotoEditor - Fullscreen");
            stage.setFullScreen(true);
            stage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (newValue == false) {
                        stage.close();
                    }
                }
            });

            controller.setupImageView(getCurrentController().getImageView(), stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onToggleFitToView(ActionEvent event) {
        getCurrentController().setFitToView(toggleFitToView.isSelected());
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
    private ToggleButton toggleFitToView;
    @FXML
    private TitledPane titledPaneAdjustment;
    @FXML
    private TitledPane titledPaneGaussianBlur;
    @FXML
    private TitledPane titledPaneBoxBlur;
    @FXML
    private TitledPane titledPaneMotionBlur;
    @FXML
    private TitledPane titledPaneGlow;
    @FXML
    private TitledPane titledPaneSepiaTone;
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
    private Slider sliderBoxBlurWidth;
    @FXML
    private Slider sliderBoxBlurHeight;
    @FXML
    private Slider sliderBoxBlurIteration;
    @FXML
    private Slider sliderMotionBlurAngle;
    @FXML
    private Slider sliderMotionBlurRadius;
    @FXML
    private Slider sliderGlowLevel;
    @FXML
    private Slider sliderGaussianRadius;
    @FXML
    private Slider sliderSepiaToneLevel;
    @FXML
    private Slider sliderSharpen;
}
