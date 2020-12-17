package resources.old;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Klasse zur Verwaltung des Einstellungsfensters der Anwendung
 * @author danvi
 */
public class Config_fxmlController implements Initializable {
    
    /**
     * Beim Öffnen des Fensters werde die Anzahl der Clouds
     * und der Zielordner mit den Daten aus DataModel initialisiert
     * @param url
     * @param rb 
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // numberOfCloudsLabel.setText(Integer.toString((DataModel.getParts())));
        //targetCloudLabel.setText(DataModel.getDirPath());
    }
    
    String getNumberFromField;
    
    @FXML
    private Button confirmEditClouds;
    @FXML
    private Button confirmConfig;
     @FXML
    private Button closeConfig;
    @FXML
    private Label numberOfCloudsLabel = new Label();
    @FXML
    private TextField numberOfCloudsField = new TextField();
    @FXML
    private Label targetCloudLabel = new Label();
    
    
    /**
     * Methode öffnet das Fenster für den DirectoryChooser, in dem er die 
     * zugehörige FXML-Datei lädt.
     * Label für die Anzahl der Clouds und den Zielordner werden dann neu gesetzt.
     */
    /*
    @FXML
    public void confirmNumberOfCloudsButton() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../resources/DirChooser_fxml.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Einstellungen");
            stage.setScene(new Scene(root, 400, 200));
            stage.showAndWait();
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        //Ausgwählte Anzahl und Ordner 
        getNumberFromField = numberOfCloudsField.getText();
        numberOfCloudsLabel.setText(getNumberFromField);
        targetCloudLabel.setText(DirChooser_fxmlController.selectedDirPath);
        numberOfCloudsField.clear();
        
    }
    */
    /**
     * Methode speichert die neue gesetzte Anzahl der Clouds und den neuen 
     * Zielordner ins DataModel.
     */
    /*
    @FXML
    public void confirmConfigButton() {
        DataModel.setDirPath(DirChooser_fxmlController.selectedDirPath);
        DataModel.setParts(Integer.parseInt(getNumberFromField));
        Stage stage = (Stage) confirmConfig.getScene().getWindow();
        stage.close();
    }
    */
    /**
     * Schließen des Fenster mit keiner Einstellungsänderung
     */
    /*
    @FXML
    public void closeConfigButton() {
        Stage stage = (Stage) closeConfig.getScene().getWindow();
        stage.close();
    }
*/
    
}
