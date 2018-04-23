/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
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

    private final BooleanProperty isSelecting = new SimpleBooleanProperty(false);

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

    private final DoubleProperty zoomRatio = new SimpleDoubleProperty(1.0);

    public DoubleProperty zoomRatioProperty() {
        return zoomRatio;
    }

    public double getZoomRatio() {
        return zoomRatio.get();
    }

    public void setZoomRatio(double zoomRatio) {
        if (zoomRatio < 0.1) {
            zoomRatio = 0.1;
            return;
        } else if (zoomRatio > 2.0) {
            zoomRatio = 2.0;
            return;
        }

        this.zoomRatio.set(zoomRatio);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.isControlDown() && !event.isAltDown()) {
                    setZoomRatio(getZoomRatio() + (event.getDeltaY() / 1000.0));
                    event.consume();
                } else if (event.isAltDown() && !event.isControlDown()) {
                    scrollPane.setHvalue(scrollPane.getHvalue() + (-event.getDeltaY() / 750.0));
                    event.consume();
                }
            }
        });
    }

    @FXML
    private ImageView imageView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Group groupImage;
}
