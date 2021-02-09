/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import database.CloudsTable;
import database.OriginalFileTable;
import database.PartFilesTable;
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
    
    public static boolean deleteContent;
    public static boolean moveContent;
    
    @FXML
    private Button deleteContentButton;
    @FXML
    private Button closeDialogButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void closeDialog() {
        Stage stage = (Stage) closeDialogButton.getScene().getWindow();
        stage.close();
    }
    
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
