/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

//<editor-fold defaultstate="collapsed" desc="import">
import Adjustment.ImageViewEffectAction;
import Action.*;
import ImageProcessing.*;
import Adjustment.*;
import Drawing.*;
import Transformation.*;
import History.*;
import PlugIn.ImageFromClipboard;
import PlugIn.ScreenCapture;
import PlugIn.WallpaperChanger;
import Preset.*;
import java.awt.print.PrinterException;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import javafx.animation.PauseTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
//</editor-fold>

/**
 *
 * @author Admin
 */
public class WorkspaceController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Properties">
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
        setEmpty(currentTab == null);

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

    public Image getCurrentImage() {
        return getCurrentImageView().getImage();
    }

    public ImageView getCurrentImageView() {
        return getCurrentController().getImageView();
    }

    private BooleanProperty empty = new SimpleBooleanProperty(true);

    public BooleanProperty emptyProperty() {
        return this.empty;
    }

    public boolean isEmpty() {
        return empty.get();
    }

    private void setEmpty(boolean value) {
        empty.set(value);
    }

    private final DoubleProperty actualZoomRatio = new SimpleDoubleProperty(1.0);

    public double getActualZoom() {
        return actualZoomRatio.get();
    }

    public void setActualZoom(double value) {
        actualZoomRatio.set(value);
    }

    public DoubleProperty actualZoomRatioProperty() {
        return actualZoomRatio;
    }

    private final ObjectProperty<Task> currentTask = new SimpleObjectProperty<>(null);

    public Task getCurrentTask() {
        return currentTask.get();
    }

    public void setCurrentTask(Task value) {
        currentTask.set(value);
        hBoxProgress.setManaged(value != null);
        hBoxProgress.setVisible(value != null);
    }

    public ObjectProperty currentTaskProperty() {
        return currentTask;
    }
    //</editor-fold>

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

        scrollPaneEdit.setManaged(false);
        hBoxProgress.setManaged(false);
        hBoxProgress.setVisible(false);

        //<editor-fold defaultstate="collapsed" desc="Crop & Rotate">
        titledPaneCropAndRotate.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    if (toggleCrop.isSelected()) {
                        getCurrentController().setSelecting(false);
                        toggleCrop.setSelected(false);
                    }
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Draw">
        sliderHandDrawThickness.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                getCurrentController().getHandDrawing().setStrokeWidth(newValue.doubleValue());
            }
        });
        colorPickerHandDraw.setValue(Color.BLACK);

        titledPaneDraw.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ImageTabController controller = getCurrentController();
                if (controller != null) {
                    if (newValue == false) {
                        toggleHandDrawPen.setSelected(true);
                        if (controller.getHandDrawing().getPathList().size() > 0) {
                            onApplyHandDraw(null);
                        }
                    }

                    controller.setDrawing(newValue);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Adjustments">
        sliderBrightness.valueProperty().addListener(colorAdjustChangeListener);
        sliderHue.valueProperty().addListener(colorAdjustChangeListener);
        sliderSaturation.valueProperty().addListener(colorAdjustChangeListener);
        sliderContrast.valueProperty().addListener(colorAdjustChangeListener);

        titledPaneAdjustment.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetColorAdjust(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Effects">
        titledPaneEffects.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    for (TitledPane titledPane : accordionEffects.getPanes()) {
                        if (titledPane.isExpanded()) {
                            titledPane.setExpanded(false);
                            return;
                        }
                    }
                }
            }
        });

        //<editor-fold defaultstate="collapsed" desc="GaussianBlur">
        sliderGaussianRadius.valueProperty().addListener(gaussianBlurChangeListener);

        titledPaneGaussianBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetGaussianBlur(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="BoxBlur">
        sliderBoxBlurHeight.valueProperty().addListener(boxBlurChangeListener);
        sliderBoxBlurWidth.valueProperty().addListener(boxBlurChangeListener);
        sliderBoxBlurIteration.valueProperty().addListener(boxBlurChangeListener);

        titledPaneBoxBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetBoxBlur(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="MotionBlur">
        sliderMotionBlurAngle.valueProperty().addListener(motionBlurChangeListener);
        sliderMotionBlurRadius.valueProperty().addListener(motionBlurChangeListener);

        titledPaneMotionBlur.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetMotionBlur(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Glow">
        sliderGlowLevel.valueProperty().addListener(glowChangeListener);

        titledPaneGlow.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetGlow(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SepiaTone">
        sliderSepiaToneLevel.valueProperty().addListener(sepiaChangeListener);

        titledPaneSepiaTone.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == false) {
                    onResetSepiaTone(null);
                }
            }
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Zoom">
        actualZoomRatioProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sliderZoom.setValue(sliderZoomConvertFrom(newValue.doubleValue()));
                labelZoom.setText((int) (newValue.doubleValue() * 100.0) + "%");
            }
        });

        sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                setActualZoom(sliderZoomConvertTo(newValue.doubleValue()));
            }
        });
        //</editor-fold>
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Presets">
        titledPanePresets.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    ImageTabController controller = getCurrentController();
                    if (!controller.isPresetPreviewUpdated()) {
                        runTask(controller.getUpdatePresetPreviewTask());
                    }
                }
            }
        });
        //</editor-fold>

        currentController.addListener(new ChangeListener<ImageTabController>() {
            @Override
            public void changed(ObservableValue<? extends ImageTabController> observable, ImageTabController oldValue, ImageTabController newValue) {
                if (oldValue != null) {
                    actualZoomRatioProperty().unbindBidirectional(oldValue.zoomRatioProperty());
                    toggleFitToView.selectedProperty().unbindBidirectional(oldValue.fitToViewProperty());
                }

                if (newValue != null) {
                    actualZoomRatioProperty().bindBidirectional(newValue.zoomRatioProperty());
                    toggleFitToView.selectedProperty().bindBidirectional(newValue.fitToViewProperty());

                    if (titledPanePresets.isExpanded() && !newValue.isPresetPreviewUpdated()) {
                        runTask(newValue.getUpdatePresetPreviewTask());
                    }
                } else {
                    setActualZoom(1.0);
                    toggleFitToView.setSelected(false);
                }
            }
        });

    }

    //<editor-fold defaultstate="collapsed" desc="Events, Listeners">
    private EventHandler<WindowEvent> onWindowCloseRequest = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            ObservableList<Tab> tabList = tabPane.getTabs();
            for (int i = tabList.size() - 1; i >= 0; i--) {
                ImageTab tab = (ImageTab) tabList.get(i);
                EventHandler<Event> handler = tab.getOnCloseRequest();
                if (handler != null) {
                    Event e = new Event(tab, null, Tab.TAB_CLOSE_REQUEST_EVENT);
                    handler.handle(e);
                    if (!e.isConsumed()) {
                        tabList.remove(i);
                    }
                } else {
                    tabList.remove(i);
                }
            }

            if (tabList.size() > 0) {
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

            if (titledPaneCropAndRotate.isExpanded()) {
                titledPaneCropAndRotate.setExpanded(false);
            }
            if (titledPaneDraw.isExpanded()) {
                titledPaneDraw.setExpanded(false);
            }

            ImageTab tab = (ImageTab) event.getSource();
            if (tab.isModified()) {
                tabPane.getSelectionModel().select(tab);

                Alert alert = makeDialog(
                        "Close tab",
                        null,
                        "Do you want to save changes you have made to \"" + tab.getName() + "\"?",
                        AlertType.CONFIRMATION,
                        ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.YES) {
                    onFileSave(null);
                } else if (result.get() == ButtonType.NO) {

                } else {
                    event.consume();
                    return;
                }
            }
        }
    };

    private final ChangeListener<Number> colorAdjustChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            ColorAdjust colorAdjust = new ColorAdjust(
                    sliderHue.getValue() / 100.0,
                    sliderSaturation.getValue() / 100.0,
                    sliderBrightness.getValue() / 100.0,
                    sliderContrast.getValue() / 100.0);
            getCurrentImageView().setEffect(colorAdjust);
        }
    };

    private final ChangeListener<Number> gaussianBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            GaussianBlur gaussianBlur = new GaussianBlur(
                    sliderGaussianRadius.getValue());
            getCurrentImageView().setEffect(gaussianBlur);
        }

    };

    private final ChangeListener<Number> boxBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            BoxBlur boxBlur = new BoxBlur(
                    sliderBoxBlurWidth.getValue(),
                    sliderBoxBlurHeight.getValue(),
                    (int) sliderBoxBlurIteration.getValue());
            getCurrentImageView().setEffect(boxBlur);
        }
    };

    private final ChangeListener<Number> motionBlurChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            MotionBlur motionBlur = new MotionBlur(
                    sliderMotionBlurAngle.getValue(),
                    sliderMotionBlurRadius.getValue());
            getCurrentImageView().setEffect(motionBlur);
        }
    };

    private final ChangeListener<Number> glowChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            Glow glow = new Glow(
                    sliderGlowLevel.getValue() / 100.0);
            getCurrentImageView().setEffect(glow);
        }
    };

    private final ChangeListener<Number> sepiaChangeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            SepiaTone sepiaTone = new SepiaTone(
                    sliderSepiaToneLevel.getValue() / 100.0);
            getCurrentImageView().setEffect(sepiaTone);
        }
    };
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Functions">
    public void initializeScene(Scene scene) {
        KeyCombination keyComb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (keyComb.match(event)) {
                    onFullScreen(null);
                }
            }
        });

        scene.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                } else {
                    event.consume();
                }
            }
        });

        scene.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    loadFile(db.getFiles());
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
    }

    public Alert makeDialog(String title, String header, String content, AlertType alertType, ButtonType... buttonTypes) {
        final Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        if (buttonTypes.length > 0) {
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(buttonTypes);
        }
        return alert;
    }

    public void runTask(Task task) {
        EventHandler<WorkerStateEvent> handler = new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                task.removeEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, this);
                task.removeEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, this);
                task.removeEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, this);
                setCurrentTask(null);
            }
        };

        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, handler);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, handler);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, handler);
        setCurrentTask(task);

        new Thread(task).start();
    }

    public void applyAction(AbstractImageAction action) {
        if (action instanceof ImageSnapshotAction) {
            Image image = action.applyTransform();
            updateImage(image);
            getCurrentHistory().add(action);
        } else {
            Task task = action.getApplyTransformTask();
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, (e) -> {
                Image image = action.getModifiedImage();
                updateImage(image);
                getCurrentHistory().add(action);

            });
            runTask(task);
        }
    }

    public void updateImage(Image image) {
        getCurrentController().setImage(image);

        if (titledPanePresets.isExpanded()) {
            runTask(getCurrentController().getUpdatePresetPreviewTask());
        }
    }

    public void loadFile(File file) {
        String tabName = file.getName();

        if (tabs.containsKey(tabName)) {
            tabPane.getSelectionModel().select(tabs.get(tabName));
        } else {
            ImageTab tab = null;
            try {
                tab = new ImageTab(file);
            } catch (IOException | IllegalArgumentException ex) {
                makeDialog("Open image...",
                        "ERROR: Unable to open file",
                        "Unable to open file: " + file.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).show();
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

    public double sliderZoomConvertTo(double value) {
        if (value <= 0.5) {
            return (1.6 * value * value) + (value) + 0.1;
        } else {
            return (16.0 * value * value) + (6.0 * value) - 6.0;
        }
    }

    public double sliderZoomConvertFrom(double value) {
        if (value <= 1.0) {
            return (-1.0 + Math.sqrt(1.0 - (4.0 * 1.6 * (0.1 - value)))) / (2.0 * 1.6);
        } else {
            return (-6.0 + Math.sqrt(36.0 - (4.0 * 16.0 * (-6.0 - value)))) / (2.0 * 16.0);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Events">
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

        try {
            getCurrentTab().saveFile();
        } catch (IOException ex) {
            makeDialog("Save",
                    null,
                    "Unable to save file: " + outputFile.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                    AlertType.ERROR).show();
        }
    }

    @FXML
    public void onFileSaveAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("PNG file", "*.png"),
                new ExtensionFilter("JPG/JPEG file", "*.jpg"),
                new ExtensionFilter("GIF file", "*.gif"),
                new ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Save file");
        fileChooser.setInitialFileName(getCurrentTab().getFileName() + ".png");
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(lastDirectory);
        }

        File outputFile = fileChooser.showSaveDialog(PhotoEditor.getPrimaryStage());
        if (outputFile != null) {
            try {
                getCurrentTab().saveFile(outputFile);
            } catch (IOException ex) {
                makeDialog("Save as...",
                        null,
                        "Unable to save file: " + outputFile.getPath() + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).show();
            }
        }

    }

    @FXML
    public void onFileClose(ActionEvent event) {
        PhotoEditor.getPrimaryStage().close();
    }

    @FXML
    public void onRotateRight90(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), getCurrentImageView(), 90.0));
    }

    @FXML
    public void onRotateLeft90(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), getCurrentImageView(), -90));
    }

    @FXML
    public void onRotateRight180(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), getCurrentImageView(), 180));
    }

    @FXML
    public void onRotateLeft180(ActionEvent event) {
        applyAction(new Rotation(getCurrentImage(), getCurrentImageView(), -180));
    }

    @FXML
    public void onFlipHorizontal(ActionEvent event) {
        applyAction(new Flip(getCurrentImage(), getCurrentImageView(), Flip.Orientation.Horizontal));
    }

    public void onFlipVertical(ActionEvent event) {
        applyAction(new Flip(getCurrentImage(), getCurrentImageView(), Flip.Orientation.Vertical));
    }

    @FXML
    public void onBlackAndWhite(ActionEvent event) {
        applyAction(new GrayScale(getCurrentImage()));
    }

    @FXML
    public void onPrint(ActionEvent event) {
        try {
            getCurrentTab().print();
        } catch (PrinterException ex) {
            makeDialog("Print",
                    null,
                    "Unable to print.\n\nDetails:\n" + ex.getMessage(),
                    AlertType.ERROR).show();
        }
    }

    @FXML
    public void onSharpen(ActionEvent event) {
        applyAction(new Sharpen(getCurrentImage()));
    }

    @FXML
    public void onUndo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.undo();
        Image image = currentHistory.getCurrentImage();

        if (image != null) {
            updateImage(image);
        }
    }

    @FXML
    public void onRedo(ActionEvent event) {
        History currentHistory = getCurrentHistory();
        currentHistory.redo();
        Image image = currentHistory.getCurrentImage();

        if (image != null) {
            updateImage(image);
        }
    }

    @FXML
    private void onCopy(ActionEvent event) {
        ImageFromClipboard.set(getCurrentImageView().getImage());
        makeDialog("Copy", null, "Image copied to clipboard!", AlertType.INFORMATION).show();
    }

    @FXML
    private void onDelete(ActionEvent event) {
        Alert confirm = makeDialog(
                "Delete",
                null,
                "Are you sure you want to delete \"" + getCurrentTab().getName() + "\"?",
                AlertType.CONFIRMATION,
                ButtonType.YES,
                ButtonType.NO);
        if (confirm.showAndWait().get() == ButtonType.YES) {
            ImageTab tab = getCurrentTab();

            try {
                tab.delete();
            } catch (IOException ex) {
                makeDialog("Delete",
                        null,
                        "Unable to delete file. The tab will now close.\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).showAndWait();
            }

            if (tab.getFile() != null) {
                tabs.remove(tab.getName());
            }
            tabPane.getTabs().remove(tab);
        }
    }

    @FXML
    private void onApplyColorAdjust(ActionEvent event) {
        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetColorAdjust(null);
    }

    @FXML
    private void onApplyHandDraw(ActionEvent event) {
        ImageTabController controller = getCurrentController();
        applyAction(controller.getHandDrawing());
        controller.getHandDrawing().getPathList().clear();
    }

    @FXML
    private void onUndoAllHandDraw(ActionEvent event) {
        getCurrentController().getHandDrawing().getPathList().clear();
    }

    @FXML
    private void onToggleHandDrawPen(ActionEvent event) {
        getCurrentController().getHandDrawing().setTool(HandDrawing.Tool.PEN);
    }

    @FXML
    private void onToggleHandDrawEraser(ActionEvent event) {
        getCurrentController().getHandDrawing().setTool(HandDrawing.Tool.ERASER);
    }

    @FXML
    private void onDenoise(ActionEvent event) {
        applyAction(new Denoise(getCurrentImage()));
    }

    @FXML
    private void onResetColorAdjust(ActionEvent event) {
        sliderBrightness.setValue(0);
        sliderContrast.setValue(0);
        sliderHue.setValue(0);
        sliderSaturation.setValue(0);
    }

    @FXML
    private void onApplyGaussianBlur(ActionEvent event) {

        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetGaussianBlur(null);

    }

    @FXML
    private void onResetGaussianBlur(ActionEvent event) {
        sliderGaussianRadius.setValue(0);
    }

    @FXML
    private void onApplyBoxBlur(ActionEvent event) {

        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetBoxBlur(null);

    }

    @FXML
    private void onResetBoxBlur(ActionEvent event) {
        sliderBoxBlurWidth.setValue(0);
        sliderBoxBlurHeight.setValue(0);
    }

    @FXML
    private void onApplyMotionBlur(ActionEvent event) {

        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetMotionBlur(null);

    }

    @FXML
    private void onResetMotionBlur(ActionEvent event) {
        sliderMotionBlurAngle.setValue(0);
        sliderMotionBlurRadius.setValue(0);
    }

    @FXML
    private void onApplyGlow(ActionEvent event) {

        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetGlow(null);

    }

    @FXML
    private void onResetGlow(ActionEvent event) {
        sliderGlowLevel.setValue(0);
    }

    @FXML
    private void onApplySepiaTone(ActionEvent event) {

        applyAction(new ImageViewEffectAction(getCurrentImage(), getCurrentImageView()));
        onResetSepiaTone(null);

    }

    @FXML
    private void onResetSepiaTone(ActionEvent event) {
        sliderSepiaToneLevel.setValue(0);
    }

    @FXML
    private void onDrawColorPick(ActionEvent event) {
        getCurrentController().getHandDrawing().setStroke(colorPickerHandDraw.getValue());
    }

    @FXML
    private void onToggleCrop(ActionEvent event) {
        ImageTabController currentController = getCurrentController();
        if (toggleCrop.isSelected()) {
            currentController.setSelecting(true);
            return;
        }

        Selection selection = getCurrentController().getSelection();
        if (!selection.isNothing()) {
            Crop crop = new Crop(getCurrentImage(), selection.getRect());
            if (!crop.isInvalid()) {
                applyAction(crop);
                currentController.setSelecting(false);
                if (toggleFitToView.isSelected()) {
                    currentController.doFitToView();
                }
                return;
            }
        }

        // Selection failed
        makeDialog("Crop", null, "No pixels were selected.", AlertType.ERROR).show();
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
        scrollPaneEdit.setManaged(toggleEdit.isSelected());
    }

    @FXML
    private void onFileInfo(ActionEvent event) {
        File currentFile = currentTab.getFile();

        if (currentFile == null) {
            makeDialog("File info",
                    null,
                    "You need to save this file to view its info.",
                    AlertType.ERROR).show();
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
                makeDialog("Open file location",
                        null,
                        "Folder not found!",
                        AlertType.ERROR).show();
            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    private void onFixRedEye(ActionEvent event) {
        if (!FixRedEye.isSupported()) {
            makeDialog("Fix Red Eye",
                    null,
                    "Fix Red Eye function is missing some libraries!",
                    AlertType.ERROR).show();
            return;
        }

        applyAction(new FixRedEye(getCurrentImage()));
    }

    @FXML
    private void onSetAsWallpaper(ActionEvent event
    ) {
        File currentFile = getCurrentTab().getFile();
        if (currentFile == null) {
            onFileSaveAs(null);
        }
        if (currentFile == null) {
            makeDialog("Set as Wallpaper",
                    null,
                    "You need to save this file to set as wallpaper",
                    AlertType.ERROR).show();
            return;
        }

        WallpaperChanger.setWallpaper(currentFile.getPath());
    }

    private void onScreenCapture(double timer) {
        // Minimize window
        Stage stage = PhotoEditor.getPrimaryStage();
        stage.setIconified(true);

        // Set delay
        PauseTransition wait = new PauseTransition(Duration.seconds(timer));
        wait.setOnFinished(e -> {
            Image image = SwingFXUtils.toFXImage(ScreenCapture.Capture(), null);

            // Restore window;
            if (stage.isIconified()) {
                stage.setIconified(false);
            }

            // Add to workspace
            ImageTab tab = null;
            try {
                tab = new ImageTab(image);
            } catch (IOException | IllegalArgumentException ex) {
                makeDialog("Screen capture",
                        null,
                        "Failed to capture screen." + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).show();
            }

            if (tab == null) {
                return;
            }

            tab.setOnCloseRequest(onTabCloseRequest);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
        });

        wait.play();
    }

    @FXML
    private void onScreenCapture(ActionEvent event) {
        onScreenCapture(1.0);
    }

    @FXML
    private void onScreenCaptureWithTimer(ActionEvent event) {
        List<Integer> choices = new ArrayList<>();
        for (int i = 0; i <= 5; i++) {
            choices.add(i);
        }

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
        dialog.setTitle("Screen capture with timer");
        dialog.setHeaderText(null);
        dialog.setContentText("Set timer in seconds:");

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(e -> onScreenCapture(result.get().doubleValue()));
    }

    @FXML
    private void onTakeAPicture(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Webcam.fxml"));
            Parent root = loader.load();
            WebcamController controller = loader.getController();

            if (controller.getCurrentWebcam() == null) {
                makeDialog("Take a picture...", null, "There are no cameras found.", AlertType.WARNING).show();
                return;
            }

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            controller.initStage(stage);
            stage.showAndWait();

            Image image = controller.getImage();

            // No picture taken
            if (image == null) {
                return;
            }

            // Add to workspace
            ImageTab tab = null;
            try {
                tab = new ImageTab(image);
            } catch (IOException | IllegalArgumentException ex) {
                makeDialog("Take a picture...",
                        null,
                        "Failed to take a picture." + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).show();
            }

            if (tab == null) {
                return;
            }

            tab.setOnCloseRequest(onTabCloseRequest);
            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();

            if (controller.isMirrored()) {
                onFlipHorizontal(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPasteFromClipboard(ActionEvent event) {
        Image image = ImageFromClipboard.get();
        if (image != null) {
            ImageTab tab = null;
            try {
                tab = new ImageTab(image);
            } catch (IOException | IllegalArgumentException ex) {
                makeDialog("Paste from Clipboard",
                        null,
                        "Unable to paste from clipboard." + "\n\nDetails:\n" + ex.getMessage(),
                        AlertType.ERROR).show();
            }

            if (tab == null) {
                return;
            }

            tab.setOnCloseRequest(onTabCloseRequest);

            tabPane.getTabs().add(tab);
            tabPane.getSelectionModel().selectLast();
        } else {
            makeDialog("Paste from Clipboard",
                    null,
                    "Clipboard does not contain any image!",
                    AlertType.ERROR).show();
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

            controller.initStage(stage);
            controller.setupImageView(getCurrentImageView(), stage);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onToggleFitToView(ActionEvent event) {
        getCurrentController().setFitToView(toggleFitToView.isSelected());
    }

    @FXML
    private void onSliderZoomScroll(ScrollEvent event) {
        if (event.isControlDown()) {
            sliderZoom.setValue(sliderZoom.getValue() + (event.getDeltaY() > 0 ? 0.02 : -0.02));
            ImageTabController controller = getCurrentController();
            if (controller.isFitToView() == true) {
                controller.setFitToView(false);
            }
        }
    }

    @FXML
    private void onSliderZoomMouseClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                sliderZoom.setValue(0.5);
            }
            ImageTabController controller = getCurrentController();
            if (controller.isFitToView() == true) {
                controller.setFitToView(false);
            }
        }
    }

    @FXML
    private void onWarmFilter(ActionEvent event) {
        applyAction(new WarmFilter(getCurrentImage()));
    }

    @FXML
    private void onColdFilter(ActionEvent event) {
        applyAction(new ColdFilter(getCurrentImage()));
    }

    @FXML
    private void onGreenFilter(ActionEvent event) {
        applyAction(new GreenFilter(getCurrentImage()));
    }

    @FXML
    private void onInvert(ActionEvent event) {
        applyAction(new Invert(getCurrentImage()));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Controls">
    @FXML
    private TabPane tabPane;
    @FXML
    private MenuItem menuUndo;
    @FXML
    private MenuItem menuRedo;
    @FXML
    private ToggleButton toggleEdit;
    @FXML
    private HBox hBoxProgress;
    @FXML
    private Slider sliderZoom;
    @FXML
    private Label labelZoom;
    @FXML
    private ToggleButton toggleFitToView;
    @FXML
    private ScrollPane scrollPaneEdit;
    @FXML
    private Accordion accordionEdit;
    @FXML
    private TitledPane titledPaneCropAndRotate;
    @FXML
    private ToggleButton toggleCrop;
    @FXML
    private TitledPane titledPaneAdjustment;
    @FXML
    private Slider sliderBrightness;
    @FXML
    private Slider sliderHue;
    @FXML
    private Slider sliderSaturation;
    @FXML
    private Slider sliderContrast;
    @FXML
    private TitledPane titledPaneDraw;
    @FXML
    private ToggleButton toggleHandDrawPen;
    @FXML
    private ToggleButton toggleHandDrawEraser;
    @FXML
    private TitledPane titledPaneEffects;
    @FXML
    private Accordion accordionEffects;
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
    private Slider sliderHandDrawThickness;
    @FXML
    private ColorPicker colorPickerHandDraw;
    @FXML
    private TitledPane titledPanePresets;
    //</editor-fold>
}
