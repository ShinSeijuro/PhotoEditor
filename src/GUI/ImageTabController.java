/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import History.History;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author CMQ
 */
public class ImageTabController extends Tab implements Initializable {

    public ImageView getImageView() {
        return imageView;
    }

    public BufferedImage getBufferedImage() {
        return SwingFXUtils.fromFXImage(imageView.getImage(), null);
    }

    public void setBufferedImage(BufferedImage image) {
        imageView.setImage(SwingFXUtils.toFXImage(image, null));
    }

    private Selection selection;

    public Selection getSelection() {
        return selection;
    }

    private BooleanProperty isSelecting = new SimpleBooleanProperty(false);

    public BooleanProperty isSelectingProperty() {
        return isSelecting;
    }

    public boolean isIsSelecting() {
        return isSelecting.get();
    }

    public void setIsSelecting(boolean isSelecting) {
        this.isSelecting.set(isSelecting);

        if (isSelecting) {
            if (selection == null) {
                selection = new Selection(groupImage);
            }
            selection.setIsDisable(false);
        } else {
            if (selection != null) {
                selection.setIsDisable(true);
            }
        }
    }

    private DoubleProperty zoomRatio = new SimpleDoubleProperty(1.0);

    public DoubleProperty zoomRatioProperty() {
        return zoomRatio;
    }

    public double getZoomRatio() {
        return zoomRatio.get();
    }

    public void setZoomRatio(double zoomRatio) {
        this.zoomRatio.set(zoomRatio);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Group groupImage;
}
