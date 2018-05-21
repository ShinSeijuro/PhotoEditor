/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 *
 * @author Yuuki
 */
public class PhotoEditor extends Application {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    private static boolean openCVAvailable = false;

    public static boolean isOpenCVAvailable() {
        return openCVAvailable;
    }

    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage);
        try {
            primaryStage.getIcons().add(new Image("/Resources/icon.png"));
        } catch (Exception ex) {

        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Workspace.fxml"));
        Parent root = loader.load();
        WorkspaceController controller = (WorkspaceController) loader.getController();

        Scene scene = new Scene(root);
        controller.initializeScene(scene);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("PhotoEditor");
        stage.setMinWidth(500.0);
        stage.setMinHeight(400.0);
        stage.show();
    }

    public static String getStartupPath() {
        return System.getProperty("user.dir");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        loadLibrary();
        launch(args);
    }

    private static void loadLibrary() {

        if (!openCVAvailable) {
            try {
                // load the native OpenCV library
                System.load(PhotoEditor.getStartupPath() + "\\lib\\" + Core.NATIVE_LIBRARY_NAME + ".dll");
                openCVAvailable = true;
            } catch (UnsatisfiedLinkError | SecurityException ex) {
                Logger.getLogger(WorkspaceController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
