package resources.old;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Klasse zur Verwaltung des DirectoryChoosers
 *
 * @author danvi
 */
public class DirChooser_fxmlController implements Initializable {

    File selectedDir;
    public static String selectedDirPath = new String();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private Button chooseCloudButton;
    @FXML
    private TextArea cloudPathField = new TextArea();
    @FXML
    private Button confirmCloudButton = new Button();
    @FXML
    private Button closeCloudChooserButton = new Button();

    /**
     * Methode öffnet den DirectoryChooser und speichert den ausgewählten
     * Ordnerpfad in das Textfeld.
     *
     * @param e
     */
    @FXML
    public void chooseCloud(ActionEvent e) {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Clouds auswähle");
        dirChooser.setInitialDirectory(new File("C:\\Users\\danvi\\Desktop\\Praktikum"));
        selectedDir = dirChooser.showDialog(stage);

        selectedDirPath = selectedDir.getAbsolutePath();
        if (selectedDirPath != null) {
            cloudPathField.setText(selectedDirPath);
            confirmCloudButton.setDisable(false);
        } else {
            cloudPathField.setText("-");
        }

    }

    /**
     * Schließt das Fenster
     *
     * @param e
     */
    @FXML
    public void confirmCloud(ActionEvent e) {
        //DataModel.setDirPath(selectedDir.getAbsolutePath());
        Stage stage = (Stage) confirmCloudButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Schließt das Fenster
     *
     * @param e
     */
    @FXML
    public void closeCloudChooser(ActionEvent e) {
        Stage stage = (Stage) closeCloudChooserButton.getScene().getWindow();
        stage.close();
    }

}
