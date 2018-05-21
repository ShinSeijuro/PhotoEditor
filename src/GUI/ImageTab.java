/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import History.History;
import PlugIn.RecycleBin;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.awt.print.PageFormat;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import static java.awt.print.PrinterJob.getPrinterJob;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.ceil;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Logger.getLogger;
import static javafx.embed.swing.SwingFXUtils.fromFXImage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Dimension2D;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javax.imageio.IIOImage;
import static javax.imageio.ImageIO.createImageOutputStream;
import static javax.imageio.ImageIO.getImageWritersByFormatName;
import static javax.imageio.ImageIO.write;
import javax.imageio.ImageWriteParam;
import static javax.imageio.ImageWriteParam.MODE_EXPLICIT;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 *
 * @author CMQ
 */
public class ImageTab extends Tab {

    private static int newFileCount = 0;

    private ImageTabController controller;
    private History<Image> history;
    private File file;
    private Dimension2D originalDimension2D;
    private String name;
    private boolean modified;
    private boolean deleteRequested;
    private int savePivot;

    private ImageTab() throws IOException {
        super();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImageTab.fxml"));
            AnchorPane tabPage = (AnchorPane) loader.load();
            super.setContent(tabPage);
            controller = loader.getController();
        } catch (IOException ex) {
            getLogger(WorkspaceController.class.getName()).log(SEVERE, null, ex);
            throw ex;
        }

        savePivot = 0;
    }

    private ImageTab(Image image, String name) throws IOException, IllegalArgumentException {
        this();

        if (image == null) {
            throw new IllegalArgumentException("Unsupported file type.");
        }

        history = new History<>(image);
        history.setOnUndone(e -> addPivot(-1));
        history.setOnRedone(e -> addPivot(1));

        this.name = name;
        originalDimension2D = new Dimension2D(image.getWidth(), image.getHeight());
        controller.setImage(image);

        // Set fit to view = true by default
        controller.setFitToView(true);
    }

    public ImageTab(Image image) throws IOException, IllegalArgumentException {
        this(image, "new (" + ++newFileCount + ")");
        modified = true;
        updateText();
    }

    public ImageTab(File file) throws IOException, IllegalArgumentException {
        this(new Image(file.toURI().toString()), file.getName());
        this.file = file;
        modified = false;
        updateText();
    }

    public ImageTabController getController() {
        return controller;
    }

    public History<Image> getHistory() {
        return history;
    }

    public File getFile() {
        return file;
    }

    public Dimension2D getOriginalDimension2D() {
        return originalDimension2D;
    }

    public String getName() {
        return name;
    }

    public boolean isModified() {
        return modified;
    }

    public boolean isDeleteRequested() {
        return deleteRequested;
    }

    private void addPivot(int step) {
        savePivot += step;

        if (file == null) {
            return;
        }

        this.modified = (savePivot != 0);
        updateText();
    }

    public String getActualName() {
        if (file == null) {
            return null;
        }
        return file.getAbsolutePath();
    }

    private void updateText() {
        if (modified) {
            setText(name + "*");
        } else {
            setText(name);
        }
    }

    public String getFileName() {
        if (file == null) {
            return name;
        }
        return name.substring(0, name.lastIndexOf('.'));
    }

    public String getExtension() {
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public void saveFile(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File to save cannot be null.");
        }

        if (this.file == null || !this.file.equals(file)) {
            this.file = file;
            name = file.getName();
        }

        saveFile();
    }

    public void saveFile() throws IOException {
        if (file == null) {
            throw new IllegalStateException("This tab is not associated with any file.");
        }

        savePivot = 0;
        modified = false;

        writeImage();
        updateText();
    }

    private void writeImage() throws IOException {
        String extension = getExtension();
        BufferedImage output = fromFXImage(getController().getImage(), null);
        if (extension.equals("jpg") || extension.equals("jpeg")) {
            // Eliminate bug: confusion between ARGB & CYMK
            // Create a new fixed image with only RGB channel
            int width = output.getWidth();
            int height = output.getHeight();
            BufferedImage fixedImage = new BufferedImage(width, height, TYPE_INT_RGB);
            int[] rgb = output.getRGB(0, 0, width, height, null, 0, width);
            fixedImage.setRGB(0, 0, width, height, rgb, 0, width);

            // maximize quality
            ImageWriter writer = getImageWritersByFormatName(extension).next();
            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(MODE_EXPLICIT); // see javadoc
            param.setCompressionQuality(1.0F); // Highest quality
            try (ImageOutputStream imageOutputStream = createImageOutputStream(file)) {
                writer.setOutput(imageOutputStream);
                writer.write(null, new IIOImage(fixedImage, null, null), param);
            }
        } else {
            if (!write(output, extension, file)) {
                throw new IOException("No appropriate writer.");
            }
        }
    }

    public void print() throws PrinterException {
        BufferedImage output = fromFXImage(getController().getImage(), null);
        PrinterJob printJob = getPrinterJob();
        printJob.setPrintable((Graphics graphics, PageFormat pageFormat, int pageIndex) -> {
            // Get the upper left corner that it printable
            int x = (int) ceil(pageFormat.getImageableX());
            int y = (int) ceil(pageFormat.getImageableY());
            if (pageIndex != 0) {
                return NO_SUCH_PAGE;
            }
            graphics.drawImage(output, x, y, output.getWidth(), output.getHeight(), null);
            return PAGE_EXISTS;
        });
        try {
            printJob.print();
        } catch (PrinterException ex) {
            getLogger(ImageTab.class.getName()).log(SEVERE, null, ex);
            throw ex;
        }
    }

    public void delete() throws IOException {
        deleteRequested = true;
        if (file != null) {
            RecycleBin.delete(file);
        }
    }
}
