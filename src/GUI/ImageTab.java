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

    private ImageTabController controller;

    public ImageTabController getController() {
        return controller;
    }

    private History history;

    public History getHistory() {
        return history;
    }

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if (this.file == null || !this.file.equals(file)) {
            this.file = file;
            name = file.getName();
            modified = false;
            savePivot = 0;
        }

        // ImageIO.write
        updateText();
    }

    private Dimension2D originalDimension2D;

    public Dimension2D getOriginalDimension2D() {
        return originalDimension2D;
    }

    private String name;

    public String getName() {
        return name;
    }

    private boolean modified;

    public boolean isModified() {
        return modified;
    }

    private int savePivot;

    private void addPivot(int step) {
        savePivot += step;

        if (file == null) {
            return;
        }

        this.modified = (savePivot != 0);
        updateText();
    }

    private ImageTab() throws IOException {
        super();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageTab.fxml"));
            AnchorPane tabPage = (AnchorPane) loader.load();
            super.setContent(tabPage);
            controller = loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }

        history = new History();
        history.setOnUndone(e -> addPivot(-1));
        history.setOnRedone(e -> addPivot(1));

        savePivot = 0;
    }

    private ImageTab(BufferedImage image, String name) throws IOException, IllegalArgumentException {
        this();

        if (image == null) {
            throw new IllegalArgumentException("Unsupported file type.");
        }

        this.name = name;
        originalDimension2D = new Dimension2D(image.getWidth(), image.getHeight());
        controller.setBufferedImage(image);
    }

    public ImageTab(BufferedImage image) throws IOException, IllegalArgumentException {
        this(image, "new");
        modified = true;
        updateText();
    }

    public ImageTab(File file) throws IOException, IllegalArgumentException {
        this(ImageIO.read(file), file.getName());
        this.file = file;
        modified = false;
        updateText();
    }

    private void updateText() {
        if (modified) {
            setText(name + "*");
        } else {
            setText(name);
        }
    }
}
