/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller für Dialog, der dem Anwender darauf hinweist, dass noch nicht alle Pflichtpfelder ausgefüllt sind
 * @author danvi
 */
public class ConfigDialog_fxmlController implements Initializable {

    @FXML
    private Button closeDialogButton;

    /**
     * Button schließt Dialog
     * @param e Action Event
     */
    @FXML
    public void closeDialog(ActionEvent e) {
        Stage stage = (Stage) closeDialogButton.getScene().getWindow();
        stage.close();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
