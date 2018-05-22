/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class AboutController extends Tab {

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public void start(Stage stage) throws Exception {
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("About.fxml"));
        primaryStage.setResizable(false);
        stage.show();
    }
}
//    @FXML
//    private Button closeButton;
