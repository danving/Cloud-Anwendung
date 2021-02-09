/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author danvi
 */
public class ReplaceCloudDialog_fxmlController implements Initializable {
    /**
     * boolean, ob Dateien gelöscht werden sollen
     */
    public static boolean deleteContent;
    /**
     * boolean, ob Dateien verschoben werden sollen
     */
    public static boolean moveContent;
    
    @FXML
    private Button deleteContentButton;
    @FXML
    private Button closeDialogButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    /**
     * Schließen des Dialoges
     */
    public void closeDialog() {
        Stage stage = (Stage) closeDialogButton.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Gibt dem Einstellungsfenster Bescheid, dass Dateien gelöscht werden sollen
     * @throws SQLException 
     */
    public void deleteContent() throws SQLException {
        synchronized (this) {
            Configuration_fxmlController.waitingReplaceCloud = true;
            deleteContent = true;
            moveContent = false;
            notify();
            Configuration_fxmlController.waitingReplaceCloud = false;
        }

        Stage stage = (Stage) deleteContentButton.getScene().getWindow();
        stage.close();
        
        
        
    }
    
    /**
     * Gibt dem Einstellungsfenster Bescheid, dass Dateien verschoben werden sollen
     * @throws SQLException 
     */
    public void moveContent() throws SQLException {
       synchronized (this) {
            Configuration_fxmlController.waitingReplaceCloud = true;
            deleteContent = false;
            moveContent = true;
            notify();
            Configuration_fxmlController.waitingReplaceCloud = false;
        }

        Stage stage = (Stage) deleteContentButton.getScene().getWindow();
        stage.close();
        
       
    }

}
