/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Admin
 */
public class AboutController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initStage(Stage stage) {
        stage.setTitle("About");
        stage.initStyle(StageStyle.UTILITY);
        stage.getIcons().add(PhotoEditor.ICON_IMAGE);
    }

}
