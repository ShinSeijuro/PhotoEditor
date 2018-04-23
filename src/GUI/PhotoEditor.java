/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Workspace.fxml"));
        Parent root = loader.load();
        WorkspaceController controller = (WorkspaceController) loader.getController();

        Scene scene = new Scene(root);
        controller.attachHotKeys(scene);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("PhotoEditor");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
