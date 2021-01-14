package resources;

import cloudtestfxml.PlaceholderPath;
import database.Clouds;
import database.CloudsTable;
import database.TempDir;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Klasse zuer Verwaltung des Einstellungsfensters
 *
 * @author danvi
 */
public class Configuration_fxmlController implements Initializable {

    File selectedDir;
    String selectedDirPath[] = new String[6];
    int cloudSize[] = new int[5];
    TextField textfield[] = new TextField[10];
    int spinnerValue;
    int i = 1;
    String tempDirPath;
    String homeDir = System.getProperty("user.home");

    private static final Logger logger = Logger.getLogger(Configuration_fxmlController.class.getName());
    CloudsTable cloudsTable = new CloudsTable();
    PlaceholderPath placeholderPath = new PlaceholderPath();
    TempDir tempDir = new TempDir();

    //Felder und Button für Cloud
    @FXML
    private Button confirmConfigButton;
    @FXML
    private Button closeConfigButton;
    @FXML
    private Spinner numberOfCloudsSpinner;

    @FXML
    private Button openDirChooserButton1, openDirChooserButton2,
            openDirChooserButton3, openDirChooserButton4, openDirChooserButtonTemp;
    @FXML
    private TextField cloudField0, cloudField1, cloudField2, cloudField3, cloudField4, tempField;

    @FXML
    private Label cloudLabel1, cloudLabel2, cloudLabel3, cloudLabel4;
    @FXML
    private Spinner sizeSpinner0, sizeSpinner1, sizeSpinner2, sizeSpinner3, sizeSpinner4;

    /**
     * Initialisiert die Daten, die sich bereits in der Datenbank befindet
     * (Cloudpfade, die bereits ausgewählt wurden, Anzahl der ausgwählten Clouds
     * und die Größe der bereits ausgewählten Clouds)
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            numberOfCloudsSpinner.getValueFactory().setValue(cloudsTable.getNumberOfCloudsFromDatabase());
            if (cloudsTable.getNumberOfCloudsFromDatabase() == 0) {
                spinnerValue = 2;
            } else {
                spinnerValue = cloudsTable.getNumberOfCloudsFromDatabase();
            }

            try {
                cloudField0.setText(placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(0)));
                cloudField1.setText(placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(1)));
                cloudField2.setText(placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(2)));
                cloudField3.setText(placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(3)));
                cloudField4.setText(placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(4)));
                sizeSpinner0.getValueFactory().setValue(cloudsTable.getCloudSize()[0]);
                sizeSpinner1.getValueFactory().setValue(cloudsTable.getCloudSize()[1]);
                sizeSpinner2.getValueFactory().setValue(cloudsTable.getCloudSize()[2]);
                sizeSpinner3.getValueFactory().setValue(cloudsTable.getCloudSize()[3]);
                sizeSpinner4.getValueFactory().setValue(cloudsTable.getCloudSize()[4]);
                tempField.setText(placeholderPath.replacePlaceholder(tempDir.getTempDir()));

            } catch (SQLException ex) {
                Logger.getLogger(Configuration_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Configuration_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
        checkNumberOfClouds(spinnerValue);

    }

    /**
     * Methode speichert die gewünschte Anzahl an Clouds und
     * aktiviert/deaktiviert die passende Anzahl der Textfelder
     *
     * @param e
     */
    @FXML
    public void confirmNumberOfCloud(ActionEvent e) {
        spinnerValue = (Integer) numberOfCloudsSpinner.getValue();
        checkNumberOfClouds(spinnerValue);

    }

    @FXML
    public void openDirChooserTemp(ActionEvent e) throws SQLException {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte einen Temp-Ordner auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));

        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                tempDirPath = selectedDir.getAbsolutePath();
                tempField.setText(tempDirPath);
                tempField.setText(tempDirPath);

            }
        } catch (NullPointerException exception) {
            tempField.setText("Es wurde kein Temp-Ordner ausgewählt");
        }

    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 0
     *
     * @param e
     * @throws SQLException
     */
    @FXML
    public void openDirChooser0(ActionEvent e) throws SQLException {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Cloud auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));

        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                selectedDirPath[0] = selectedDir.getAbsolutePath();
                cloudField0.setText(selectedDirPath[0]);

            }
        } catch (NullPointerException exception) {
            cloudField0.setText("Es wurde keine Cloud ausgewählt");
        }

    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 1
     *
     * @param e
     * @throws SQLException
     */
    public void openDirChooser1(ActionEvent e) {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Cloud auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));
        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                selectedDirPath[1] = selectedDir.getAbsolutePath();
                cloudField1.setText(selectedDirPath[1]);

            }
        } catch (NullPointerException exception) {
            cloudField1.setText("Es wurde keine Cloud ausgewählt");
        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 2
     *
     * @param e
     * @throws SQLException
     */
    public void openDirChooser2(ActionEvent e) {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Cloud auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));
        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                selectedDirPath[2] = selectedDir.getAbsolutePath();
                cloudField2.setText(selectedDirPath[2]);

            }
        } catch (NullPointerException exception) {
            cloudField2.setText("Es wurde keine Cloud ausgewählt");
        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 3
     *
     * @param e
     * @throws SQLException
     */
    public void openDirChooser3(ActionEvent e) {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Cloud auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));
        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                selectedDirPath[3] = selectedDir.getAbsolutePath();
                cloudField3.setText(selectedDirPath[3]);

            }
        } catch (NullPointerException exception) {
            cloudField3.setText("Es wurde keine Cloud ausgewählt");
        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 4
     *
     * @param e
     * @throws SQLException
     */
    public void openDirChooser4(ActionEvent e) {
        Stage stage = new Stage();
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Bitte eine Cloud auswählen");
        dirChooser.setInitialDirectory(new File(homeDir));
        try {
            selectedDir = dirChooser.showDialog(stage);
            if (selectedDir.getAbsolutePath() != null) {
                selectedDirPath[4] = selectedDir.getAbsolutePath();
                cloudField4.setText(selectedDirPath[4]);

            }
        } catch (NullPointerException exception) {
            cloudField4.setText("Es wurde keine Cloud ausgewählt");
        }
    }

    /**
     * Methode speichert die ausgewählten Pfade der Clouds und ihre
     * Speicherkapazität in die Datenbank und schließt das Fenster
     *
     * @param e
     */
    @FXML
    public void confirmConfig(ActionEvent e) throws SQLException, IOException {
        boolean check = false;
        /*
        if (tempField.getText().equals("") || tempField.getText().equals("Es wurde kein Temp-Ordner ausgewählt")
                || cloudField0.getText().equals("") || cloudField0.getText().equals("Es wurde keine Cloud ausgewählt")
                || cloudField1.getText().equals("") || cloudField1.getText().equals("Es wurde keine Cloud ausgewählt")) {
            check = true;
        }
         */
        if (tempDirPath != null && selectedDirPath[0] != null && selectedDirPath[1] != null) {
            check = true;
        }

        if (check == true) {
            if (sizeSpinner0.getValue() != null) {
                cloudSize[0] = (int) sizeSpinner0.getValue();
            }
            if (sizeSpinner1.getValue() != null) {
                cloudSize[1] = (int) sizeSpinner1.getValue();
            }
            if (sizeSpinner2.getValue() != null) {
                cloudSize[2] = (int) sizeSpinner2.getValue();
            }
            if (sizeSpinner3.getValue() != null) {
                cloudSize[3] = (int) sizeSpinner3.getValue();
            }
            if (sizeSpinner4.getValue() != null) {
                cloudSize[4] = (int) sizeSpinner4.getValue();
            }

            for (int i = 0; i < selectedDirPath.length; i++) {
                if (selectedDirPath[i] != null) {
                    try {
                        if (!cloudsTable.cloudExists(i + 1)) {
                            Clouds clouds = createCloudsObject(selectedDirPath[i], i + 1, cloudSize[i], spinnerValue);
                            cloudsTable.saveCloud(clouds);
                        } else {
                            String cloudName = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
                            if (!cloudName.equals(selectedDirPath[i])) {
                                cloudsTable.deleteCloudForReplacement(i + 1);
                                Clouds clouds = createCloudsObject(selectedDirPath[i], i + 1, cloudSize[i], spinnerValue);
                                cloudsTable.saveCloud(clouds);
                            }
                        }
                    } catch (Exception exception) {
                        logger.log(Level.SEVERE, exception.getMessage());
                    }
                }
            }
            cloudsTable.deleteCloudFromDatabase(spinnerValue);
            cloudsTable.updateCloudNumber(spinnerValue);

            System.out.println("Anzahl der Clouds: " + cloudsTable.getNumberOfCloudsFromDatabase());

            if (tempDirPath != null) {
                if (!tempDir.tempDirExists()) {
                    tempDir.saveTempDir(placeholderPath.setPlaceholder(tempDirPath));
                } else {
                    tempDir.deletetempDirForReplacement();
                    tempDir.saveTempDir(tempDirPath);
                }
            }
            Stage stage = (Stage) confirmConfigButton.getScene().getWindow();
            stage.close();
        } else {
            Parent root;
            FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("ConfigDialog_fxml.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Angaben fehlen");
            stage.setScene(new Scene(root, 450, 200));
            stage.showAndWait();
        }

    }

    /**
     * Methode schließt das Fenster, ohne Einstellungsänderungen
     *
     * @param e
     */
    @FXML
    public void closeConfig(ActionEvent e) {
        Stage stage = (Stage) closeConfigButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Erstellt ein Objekt, welches die Informationen zur ausgewählten Cloud
     * speichert
     *
     * @param cloud
     * @param id
     * @param size
     * @param number
     * @return
     */
    public Clouds createCloudsObject(String cloud, int id, int size, int number) {
        String cloudPlaceholder = placeholderPath.setPlaceholder(cloud);
        Clouds clouds = new Clouds();
        clouds.setCloud(cloudPlaceholder);
        clouds.setId(id);
        clouds.setSize(size);
        clouds.setNumber(number);
        return clouds;
    }

    /**
     * Überprüft die gewünschte Anzahl der Clouds und versteckt bzw. macht die
     * gewünschte Anzahl an Textfeldern sichtbar
     *
     * @param number
     */
    private void checkNumberOfClouds(int number) {
        switch (number) {

            case 2:
                openDirChooserButton1.setDisable(false);
                openDirChooserButton2.setDisable(true);
                openDirChooserButton3.setDisable(true);
                openDirChooserButton4.setDisable(true);
                cloudField1.setDisable(false);
                cloudField2.setDisable(true);
                cloudField3.setDisable(true);
                cloudField4.setDisable(true);
                cloudLabel1.setDisable(false);
                cloudLabel2.setDisable(true);
                cloudLabel3.setDisable(true);
                cloudLabel4.setDisable(true);
                sizeSpinner1.setDisable(false);
                sizeSpinner2.setDisable(true);
                sizeSpinner3.setDisable(true);
                sizeSpinner4.setDisable(true);
                cloudField2.setText("");
                cloudField3.setText("");
                cloudField4.setText("");
                break;
            case 3:
                openDirChooserButton1.setDisable(false);
                openDirChooserButton2.setDisable(false);
                openDirChooserButton3.setDisable(true);
                openDirChooserButton4.setDisable(true);
                cloudField1.setDisable(false);
                cloudField2.setDisable(false);
                cloudField3.setDisable(true);
                cloudField4.setDisable(true);
                cloudLabel1.setDisable(false);
                cloudLabel2.setDisable(false);
                cloudLabel3.setDisable(true);
                cloudLabel4.setDisable(true);
                sizeSpinner1.setDisable(false);
                sizeSpinner2.setDisable(false);
                sizeSpinner3.setDisable(true);
                sizeSpinner4.setDisable(true);
                cloudField3.setText("");
                cloudField4.setText("");
                break;
            case 4:
                openDirChooserButton1.setDisable(false);
                openDirChooserButton2.setDisable(false);
                openDirChooserButton3.setDisable(false);
                openDirChooserButton4.setDisable(true);
                cloudField1.setDisable(false);
                cloudField2.setDisable(false);
                cloudField3.setDisable(false);
                cloudField4.setDisable(true);
                cloudLabel1.setDisable(false);
                cloudLabel2.setDisable(false);
                cloudLabel3.setDisable(false);
                cloudLabel4.setDisable(true);
                sizeSpinner1.setDisable(false);
                sizeSpinner2.setDisable(false);
                sizeSpinner3.setDisable(false);
                sizeSpinner4.setDisable(true);
                cloudField4.setText("");
                break;
            case 5:
                openDirChooserButton1.setDisable(false);
                openDirChooserButton2.setDisable(false);
                openDirChooserButton3.setDisable(false);
                openDirChooserButton4.setDisable(false);
                cloudField1.setDisable(false);
                cloudField2.setDisable(false);
                cloudField3.setDisable(false);
                cloudField4.setDisable(false);
                cloudLabel1.setDisable(false);
                cloudLabel2.setDisable(false);
                cloudLabel3.setDisable(false);
                cloudLabel4.setDisable(false);
                sizeSpinner1.setDisable(false);
                sizeSpinner2.setDisable(false);
                sizeSpinner3.setDisable(false);
                sizeSpinner4.setDisable(false);
                break;
        }
    }
}
