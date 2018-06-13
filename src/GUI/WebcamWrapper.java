/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

/**
 *
 * @author CMQ
 */
public class WebcamWrapper {

    private Webcam webcam;

    public Webcam getWebcam() {
        return webcam;
    }

    private String name;

    public String getName() {
        return name;
    }

    private final ReadOnlyObjectProperty<Image> image = new ReadOnlyObjectWrapper<>();

    public Image getImage() {
        return image.get();
    }

    private void setImage(Image image) {
        ((ReadOnlyObjectWrapper<Image>) this.image).set(image);
    }

    public ReadOnlyObjectProperty imageProperty() {
        return image;
    }

    private boolean stopRequested;

    public boolean isStopRequested() {
        return stopRequested;
    }

    public WebcamWrapper(Webcam webcam) {
        this.webcam = webcam;
        this.name = webcam.getName();
    }

    public void open(boolean startStream) {
        if (!webcam.isOpen()) {
            startThread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    webcam.open();
                    if (startStream) {
                        startStream();
                    }
                    return null;
                }
            });
        }
    }

    public void open() {
        open(true);
    }

    public void startStream() {
        if (!webcam.isOpen()) {
            throw new IllegalStateException("Webcam is not open yet.");
        }

        stopRequested = false;

        startThread(new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                final AtomicReference<WritableImage> ref = new AtomicReference<>();
                BufferedImage img = null;

                while (!stopRequested) {
                    try {
                        if ((img = webcam.getImage()) != null) {
                            ref.set(SwingFXUtils.toFXImage(img, ref.get()));
                            img.flush();

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    setImage(ref.get());
                                }
                            });
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                return null;
            }
        });
    }

    public void stopStream() {
        stopRequested = true;
    }

    public void close() {
        stopStream();
        if (webcam.isOpen()) {
            startThread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    webcam.close();
                    return null;
                }
            });
        }
    }

    private void startThread(Task task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public String toString() {
        return name;
    }
}
