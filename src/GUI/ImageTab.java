/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import History.History;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javax.imageio.ImageIO;

/**
 *
 * @author CMQ
 */
public class ImageTab extends Tab {

    private History history;

    public History getHistory() {
        return history;
    }

    private ImageTab() throws IOException {
        super();

        history = new History();
        history.setIsModifiedChangeListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (Objects.equals(oldValue, newValue)) {
                    return;
                }

                if (newValue == true) {
                    setText(tabName + "*");
                } else {
                    setText(tabName);
                }
            }
        });

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageTab.fxml"));
            AnchorPane tabPage = (AnchorPane) loader.load();
            controller = loader.getController();
            super.setContent(tabPage);
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }

    public ImageTab(BufferedImage image, String name) throws IOException, IllegalArgumentException {
        this();

        if (image == null) {
            throw new IllegalArgumentException("Unsupported file type.");
        }

        setTabName(name);
        originalDimension2D = new Dimension2D(image.getWidth(), image.getHeight());
        controller.setBufferedImage(image);
    }

    public ImageTab(BufferedImage image) throws IOException, IllegalArgumentException {
        this(image, "new");
    }

    public ImageTab(File file) throws IOException, IllegalArgumentException {
        this(ImageIO.read(file), file.getName());
        this.file = file;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if (this.file != null) {
            return;
        }

        this.file = file;
        setTabName(file.getName());
    }

    private Dimension2D originalDimension2D;

    public Dimension2D getOriginalDimension2D() {
        return originalDimension2D;
    }

    private ImageTabController controller;

    public ImageTabController getController() {
        return controller;
    }

    private String tabName;

    public String getTabName() {
        return tabName;
    }

    private void setTabName(String tabName) {
        this.tabName = tabName;
        super.setText(tabName);
    }
}
