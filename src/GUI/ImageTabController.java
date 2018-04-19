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

    private History history;

    public History getHistory() {
        return history;
    }

    public ImageView getImageView() {
        return imageView;
    }

    //public Pane
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

    private boolean isSelecting;

    public boolean isIsSelecting() {
        return isSelecting;
    }

    public void setIsSelecting(boolean isSelecting) {
        this.isSelecting = isSelecting;

        if (isSelecting) {
            if (selection == null) {
                selection = new Selection(groupImage);
            }
        } else {
            if (selection != null) {
                selection.removeRect();
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        history = new History();
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
