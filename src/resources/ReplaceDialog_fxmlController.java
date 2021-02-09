package resources;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller für den Dialog, der fragt, ob die bereits existierende Datei
 * durch du zu verschiebende Datei ersetzt werden soll
 *
 * @author danvi
 */
public class ReplaceDialog_fxmlController implements Initializable {
    public static boolean replace = false;

    @FXML
    private Button closeDialogButton;
    @FXML
    private Button replaceFileButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Button, der der Main_fxmlController Klasse sagt,
     * dass die bereits existierende Datei ersetzt werden soll
     *
     * @param e
     * @throws SQLException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     */
    public synchronized void replaceFileInDatabase(ActionEvent e) throws SQLException, IOException, FileNotFoundException, NoSuchAlgorithmException {
        synchronized (this) {
            Main_fxmlController.waiting = true;
            replace = true;
            notify();
            Main_fxmlController.waiting = false;
        }

        Stage stage = (Stage) replaceFileButton.getScene().getWindow();
        stage.close();

    }

    /**
     * Schließt den AlertDialog
     *
     * @param e
     */
    public void closeDialog(ActionEvent e) {
        replace = false;
        Stage stage = (Stage) closeDialogButton.getScene().getWindow();
        stage.close();
    }

}
