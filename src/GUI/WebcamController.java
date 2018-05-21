/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.Dimension;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class WebcamController implements Initializable {

    private WebcamWrapper currentWebcam;

    public WebcamWrapper getCurrentWebcam() {
        return currentWebcam;
    }

    public void setCurrentWebcam(WebcamWrapper currentWebcam) {
        this.currentWebcam = currentWebcam;
    }

    private Image image;

    public Image getImage() {
        return image;
    }

    private final BooleanProperty mirrored = new SimpleBooleanProperty(true);

    public boolean isMirrored() {
        return mirrored.get();
    }

    public void setMirrored(boolean value) {
        mirrored.set(value);
    }

    public BooleanProperty mirroredProperty() {
        return mirrored;
    }

    private Stage stage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<WebcamWrapper> webcams = getWebcams();
        comboboxCameras.setItems(webcams);
        comboboxCameras.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebcamWrapper>() {

            @Override
            public void changed(ObservableValue<? extends WebcamWrapper> arg0, WebcamWrapper arg1, WebcamWrapper arg2) {
                if (arg1 != null) {
                    arg1.close();
                }

                if (arg2 != null) {
                    setCurrentWebcam(arg2);
                    imageView.imageProperty().bind(arg2.imageProperty());
                    arg2.open();
                }
            }
        });

        if (webcams.size() > 0) {
            comboboxCameras.getSelectionModel().select(0);
        }

        toggleMirror.selectedProperty().bindBidirectional(mirrored);
        toggleMirror.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue == true) {
                    imageView.setScaleX(-1.0);
                } else {
                    imageView.setScaleX(1.0);
                }
            }
        });

        imageView.setScaleX(-1.0);
    }

    public void initStage(Stage stage) {
        stage.setTitle("PhotoEditor - Take a picture");
        stage.getIcons().add(PhotoEditor.ICON_IMAGE);
        stage.setMinWidth(300.0);
        stage.setMinHeight(300.0);
        stage.setMaximized(true);
        stage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (currentWebcam != null) {
                    currentWebcam.close();
                }
            }
        });
        stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (currentWebcam != null) {
                    if (newValue == true) {
                        currentWebcam.close();
                    } else {
                        currentWebcam.open();
                    }
                }
            }
        });
        this.stage = stage;
    }

    private ObservableList<WebcamWrapper> getWebcams() {
        ObservableList<WebcamWrapper> options = FXCollections.observableArrayList();

        Dimension[] resolutions = new Dimension[]{
            WebcamResolution.HD.getSize()};

        for (Webcam webcam : Webcam.getWebcams()) {
            webcam.setCustomViewSizes(resolutions);
            webcam.setViewSize(resolutions[0]);
            WebcamWrapper webcamWrapper = new WebcamWrapper(webcam);
            options.add(webcamWrapper);
        }

        return options;
    }

    @FXML
    private void onCapture(ActionEvent event) {
        if (currentWebcam != null) {
            image = currentWebcam.getImage();
        }
        stage.close();
    }

    @FXML
    private void onSetting(ActionEvent event) {
        borderPaneSetting.setVisible(true);
    }

    @FXML
    private void onClose(ActionEvent event) {
        borderPaneSetting.setVisible(false);
    }

    @FXML
    private ToggleButton toggleMirror;
    @FXML
    private BorderPane borderPaneSetting;
    @FXML
    private ImageView imageView;
    @FXML
    private StackPane stackPane;
    @FXML
    private ComboBox<WebcamWrapper> comboboxCameras;
}
