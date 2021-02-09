package resources;

import cloudtestfxml.CombinePartsToFile;
import cloudtestfxml.MoveFile;
import cloudtestfxml.PlaceholderPath;
import database.Clouds;
import database.CloudsTable;
import database.OriginalFile;
import database.OriginalFileTable;
import database.PartFilesTable;
import database.TempDir;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.TableView;
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
    String selectedDirPath[] = new String[5];
    int cloudSize[] = new int[5];
    TextField textfield[] = new TextField[10];
    int spinnerValue;
    //int i = 1;
    String tempDirPath;
    String homeDir = System.getProperty("user.home");

    public static boolean waitingReplaceCloud = false;
    public boolean deleteTableView = false;
    public TableView mainTableView;

    private static final Logger logger = Logger.getLogger(Configuration_fxmlController.class.getName());
    //Klasseninstanzen
    CloudsTable cloudsTable = new CloudsTable();
    OriginalFileTable originalfileTable = new OriginalFileTable();
    PartFilesTable partfilesTable = new PartFilesTable();
    CombinePartsToFile combinePartsToFile = new CombinePartsToFile();
    MoveFile moveFile = new MoveFile();
    PlaceholderPath placeholderPath = new PlaceholderPath();
    TempDir tempDir = new TempDir();
    Main_fxmlController mainController = new Main_fxmlController();

    //Scenebuiler-Elemente
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
            ex.printStackTrace();
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

    /**
     * öffnet den DirectoryChooser und speichert ausgeählten Pfad als
     * Temp-Ordner
     *
     * @param e
     * @throws SQLException
     */
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
            //tempField.setText("Es wurde kein Temp-Ordner ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

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
            //cloudField0.setText("Es wurde keine Cloud ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

        }

    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 1
     *
     * @param e
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
            // cloudField1.setText("Es wurde keine Cloud ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 2
     *
     * @param e
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
            //cloudField2.setText("Es wurde keine Cloud ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 3
     *
     * @param e
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
            //cloudField3.setText("Es wurde keine Cloud ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

        }
    }

    /**
     * Methode öffnet den DirectoryChooser und speichert ausgeählten Pfad in das
     * Textfeld 4
     *
     * @param e
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
            //cloudField4.setText("Es wurde keine Cloud ausgewählt");
            System.out.println("Es wurde kein Temp-Ordner ausgewählt");

        }
    }

    /**
     * Methode speichert die festgelegten Einstellungen
     *
     * @param e
     * @throws SQLException
     * @throws IOException
     */
    @FXML
    public void confirmConfig(ActionEvent e) throws SQLException, IOException, FileNotFoundException, NoSuchAlgorithmException {
        boolean check;
        String temp1, temp2, temp3;
        temp1 = tempField.getText();
        temp2 = cloudField0.getText();
        temp3 = cloudField1.getText();

        //String array mit Pfaden der angegebenen Ordner
        String[] allDirPaths = new String[selectedDirPath.length];
        String[] tempTextField = new String[selectedDirPath.length];
        tempTextField[0] = cloudField0.getText();
        tempTextField[1] = cloudField1.getText();
        tempTextField[2] = cloudField2.getText();
        tempTextField[3] = cloudField3.getText();
        tempTextField[4] = cloudField4.getText();
        for (int i = 0; i < allDirPaths.length; i++) {
            if (selectedDirPath[i] != null) {
                allDirPaths[i] = selectedDirPath[i];
            } else if (!tempTextField[i].equals("")) {
                allDirPaths[i] = tempTextField[i];
            }

        }

        //Überprüft, mindestens zwei Ordner ausgewählt wurden
        if (temp1.equals("Es wurde kein Temp-Ordner ausgewählt") || temp1.equals("") || temp2.equals("Es wurde keine Cloud ausgewählt") || temp2.equals("")
                || temp3.equals("Es wurde keine Cloud ausgewählt") || temp3.equals("")) {
            check = false;
        } else {
            check = true;
        }

        //Wenn zwei ordner ausgewählt wurden
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

            //Zählt, wie viele Pfade tatsächlich angegeben wurdne
            int numberOfPaths = 0;
            for (int i = 0; i < allDirPaths.length; i++) {
                if (allDirPaths[i] != null) {
                    numberOfPaths += 1;
                }
            }
            //Anzahl der Clouds, die bereits in der DB gespeichert sind
            int numberOfClouds = cloudsTable.getNumberOfCloudsFromDatabase();
            //Wenn noch keine Clouds gespeichert wurden, werden die ausgewählten Clouds ohne weiteres gespeichert
            if (numberOfClouds == 0) {
                for (int i = 0; i < numberOfPaths; i++) {
                    Clouds clouds = createCloudsObject(selectedDirPath[i], i + 1, cloudSize[i], spinnerValue);
                    cloudsTable.saveCloud(clouds);
                }
                //wenn die angegeben Clouds, weniger oder genauso viele sind, wie bereits in der DB angegeben
            } else if (numberOfPaths < numberOfClouds || numberOfPaths == numberOfClouds) {
                Parent rootReplace;
                try {
                    FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("ReplaceCloudDialog_fxml.fxml"));
                    rootReplace = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Einstellungen wurden verändert");
                    stage.setScene(new Scene(rootReplace, 450, 250));
                    stage.showAndWait();

                } catch (IOException ex) {
                    System.out.println("Dialog is not opening");
                }
                ReplaceCloudDialog_fxmlController dialog = new ReplaceCloudDialog_fxmlController();
                synchronized (dialog) {
                    try {
                        while (waitingReplaceCloud) {
                            dialog.wait();
                        }
                    } catch (InterruptedException inE) {
                        System.out.println("No Sync");
                    }
                }

                //System.out.println(dialog.deleteContent);
                if (dialog.deleteContent) { //Content mit Cloud löschen
                    //Falls, die angegebene Cloud an der Stelle i nicht der Cloud in der DB enspricht, wird die in der DB ersetzt
                    for (int i = 0; i < numberOfPaths; i++) {
                        String cloudName = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
                        if (!cloudName.equals(allDirPaths[i])) {
                            cloudsTable.updateCloud(allDirPaths[i], cloudSize[i], i + 1);
                        }
                    }
                    //Alle alten Informationen zu den Teil-Dateien werden gelöscht
                    if (partfilesTable.getNumberOfParts() > 0) {
                        deleteOldCloudContent();
                    }
                    mainTableView.getItems().clear();

                    //Löschen der Clouds, die in der DB zu viel sind
                    cloudsTable.deleteCloudFromDatabase(spinnerValue);
                    cloudsTable.updateCloudNumber(spinnerValue);
                    originalfileTable.resetAutoIncrement();
                    partfilesTable.resetAutoIncrement();
                    
                    //Content wird verschoben
                } else if (dialog.moveContent) {
                    //Falls, die angegebene Cloud an der Stelle i nicht der Cloud in der DB enspricht, wird die in der DB ersetzt
                    for (int i = 0; i < numberOfPaths; i++) {
                        String cloudName = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
                        if (!cloudName.equals(allDirPaths[i])) {
                            cloudsTable.updateCloud(allDirPaths[i], cloudSize[i], i + 1);
                        }
                    }
                    cloudsTable.deleteCloudFromDatabase(spinnerValue);
                    cloudsTable.updateCloudNumber(spinnerValue);
                    if (partfilesTable.getNumberOfParts() > 0) {
                        //Content wird verschoben
                        moveOldCloudContent();
                    }

                }
                //Wenn mehr Clouds angegeben wurden, als in der DB gespeichert sind
            } else if (numberOfPaths > numberOfClouds) {
                Parent rootReplace;
                try {
                    FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("ReplaceCloudDialog_fxml.fxml"));
                    rootReplace = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Einstellungen wurden verändert");
                    stage.setScene(new Scene(rootReplace, 450, 250));
                    stage.showAndWait();

                } catch (IOException ex) {
                    System.out.println("Dialog is not opening");
                }
                ReplaceCloudDialog_fxmlController dialog = new ReplaceCloudDialog_fxmlController();
                synchronized (dialog) {
                    try {
                        while (waitingReplaceCloud) {
                            dialog.wait();
                        }
                    } catch (InterruptedException inE) {
                        System.out.println("No Sync");
                    }
                }
                if (dialog.deleteContent) { //Content mit Cloud löschen
                    //Falls, die angegebene Cloud an der Stelle i nicht der Cloud in der DB enspricht, wird die in der DB ersetzt
                    for (int i = 0; i < numberOfClouds; i++) {
                        String cloudName = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
                        if (!cloudName.equals(allDirPaths[i])) {
                            cloudsTable.updateCloud(allDirPaths[i], cloudSize[i], i + 1);
                        }
                    }
                    //Die Clouds, die mehr angegeben wurde, werden gespeichert
                    for (int i = numberOfClouds; i < numberOfPaths; i++) {
                        Clouds clouds = createCloudsObject(allDirPaths[i], i + 1, cloudSize[i], spinnerValue);
                        cloudsTable.saveCloud(clouds);
                    }
                    //Alle Informationen über alte Teil-Dateien werden gelöscht
                    if (partfilesTable.getNumberOfParts() > 0) {
                        deleteOldCloudContent();
                    }
                    cloudsTable.updateCloudNumber(spinnerValue);
                    
                    mainTableView.getItems().clear();
                    originalfileTable.resetAutoIncrement();
                    partfilesTable.resetAutoIncrement();
                    
                    //Verschieben des alten Contents
                } else if (dialog.moveContent) {
                    //Falls, die angegebene Cloud an der Stelle i nicht der Cloud in der DB enspricht, wird die in der DB ersetzt
                    for (int i = 0; i < numberOfClouds; i++) {
                        String cloudName = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
                        if (!cloudName.equals(allDirPaths[i])) {
                            cloudsTable.updateCloud(allDirPaths[i], cloudSize[i], i + 1);
                        }
                    }
                    //Clouds die mehr angegeben wurde, als in der DB, werde gespeichert
                    for (int i = numberOfClouds; i < numberOfPaths; i++) {
                        Clouds clouds = createCloudsObject(allDirPaths[i], i + 1, cloudSize[i], spinnerValue);
                        cloudsTable.saveCloud(clouds);
                    }
                    cloudsTable.deleteCloudFromDatabase(spinnerValue);
                    cloudsTable.updateCloudNumber(spinnerValue);
                    if (partfilesTable.getNumberOfParts() > 0) {
                        //Content wird verschoben
                        moveOldCloudContent();
                    }
                }
            }

            cloudsTable.deleteCloudFromDatabase(spinnerValue);
            cloudsTable.updateCloudNumber(spinnerValue);

            //System.out.println("Anzahl der Clouds: " + cloudsTable.getNumberOfCloudsFromDatabase());
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
    public void closeConfig(ActionEvent e
    ) {
        Stage stage = (Stage) closeConfigButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Erstellt ein Objekt, welches die Informationen zur ausgewählten Cloud
     * speichert
     *
     * @param cloud Pfad zu cloud
     * @param id id-Nummer
     * @param size angegebene Größe der Cloud
     * @param number Gesamt-Anzahl der gespeicherten Clouds
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

    /**
     * Verschiebt den alten Inhalt der alten Cloud in die neue Cloud
     *
     * @throws SQLException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     */
    public void moveOldCloudContent() throws SQLException, IOException, FileNotFoundException, NoSuchAlgorithmException {
        String tempDirPath = tempDir.getTempDir();
        tempDirPath = placeholderPath.replacePlaceholder(tempDirPath);
        //Namen und Dateityp der bisher gespeicherten Dateien, werden in jeweils in ein String-Array gespeichert
        String[] namesOfFiles = partfilesTable.getNamesOfParts();
        String[] typesOfFiles = new String[namesOfFiles.length];
        for (int i = 0; i < namesOfFiles.length; i++) {
            typesOfFiles[i] = originalfileTable.getTypeOfFile(namesOfFiles[i]);
            //System.out.println("types: " + typesOfFiles[i]);
        }

        //bisher gespeicherte Teil-Datei Pfade werden in ein String-Array gespeichert
        String[] pathsOfFiles = new String[partfilesTable.getNumberOfParts()];
        for (int j = 0; j < pathsOfFiles.length; j++) {
            pathsOfFiles = partfilesTable.getPartsPath();

        }
        //Platzhalterder Pfade wieder ersetzten
        for (int j = 0; j < pathsOfFiles.length; j++) {
            pathsOfFiles[j] = placeholderPath.replacePlaceholder(pathsOfFiles[j]);
        }
        //Löschen aller alten Informationen über doe Originalpfade
        originalfileTable.deleteAllFiles();
        originalfileTable.resetAutoIncrement();

        //Cloud ersetzten
        //Alte Teil dateien zusammenfügen
        for (int j = 0; j < namesOfFiles.length; j++) {
            combinePartsToFile.combinePartsToFile(partfilesTable.getPartsSize(namesOfFiles[j]), namesOfFiles[j], typesOfFiles[j], false);
            File combinedFile = new File(tempDirPath + "\\" + namesOfFiles[j] + "_Original" + typesOfFiles[j]);
            File newFileName = new File(tempDirPath + "\\" + namesOfFiles[j] + typesOfFiles[j]);
            if (combinedFile.renameTo(newFileName)) {
                System.out.println("File rename succsess");
            } else {
                System.out.println("File rename failed");
            }
            OriginalFile newFile = createFileObject(newFileName.toString());
            originalfileTable.saveOriginalFile(newFile);
            String[] pathsToDelete = partfilesTable.getPartsPathPerName(namesOfFiles[j]);
            partfilesTable.resetAutoIncrement();
            for (int i = 0; i < pathsToDelete.length; i++) {
                if (pathsToDelete[i] != null) {
                    pathsToDelete[i] = placeholderPath.replacePlaceholder(pathsToDelete[i]);
                    try {
                        //Dateien werden aus Ordnern gelöscht
                        Files.deleteIfExists(Paths.get(pathsToDelete[i]));
                    } catch (IOException ex) {
                        Logger.getLogger(ReplaceCloudDialog_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    break;
                }

            }
            partfilesTable.deletePartsFiles(namesOfFiles[j]);
            moveFile.moveFile();
        }

    }

    /**
     * Löscht den alten Inhalt der Cloud und in der DB
     *
     * @throws SQLException
     */
    public void deleteOldCloudContent() throws SQLException {
        //bisher gespeicherte Teil-Datei Pfade werden in ein String-Array gespeichert
        String[] pathsOfFiles = new String[partfilesTable.getNumberOfParts()];
        for (int j = 0; j < pathsOfFiles.length; j++) {
            pathsOfFiles = partfilesTable.getPartsPath();

        }
        //Platzhalterder Pfade wieder ersetzten
        for (int j = 0; j < pathsOfFiles.length; j++) {
            pathsOfFiles[j] = placeholderPath.replacePlaceholder(pathsOfFiles[j]);
        }
        //Teil-Dateien werden in der Cloud gelöscht
        if (pathsOfFiles[0] != null) {
            for (int w = 0; w < pathsOfFiles.length; w++) {
                try {
                    //Dateien werden aus Ordnern gelöscht
                    Files.deleteIfExists(Paths.get(pathsOfFiles[w]));
                } catch (IOException ex) {
                    Logger.getLogger(ReplaceCloudDialog_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //Alte Informationen werden aus DB gelöscht
        partfilesTable.deleteAllPartFiles();
        originalfileTable.deleteAllFiles();

    }

    /**
     * Erstellt ein Objekt, welches die Informationen zu Original-Datei
     * speichert
     *
     * @param path
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     */
    public OriginalFile createFileObject(String path) throws IOException, FileNotFoundException, NoSuchAlgorithmException {
        OriginalFile file = new OriginalFile();
        //System.out.println(path);
        String filePath = path;
        String fileName = "";
        String type = "";
        String size = "";

        for (int i = filePath.length() - 1; i >= 0; i--) {
            for (int j = filePath.length() - 1; j >= 0; j--) {
                if (filePath.charAt(j) == '\\') {
                    if (filePath.charAt(i) == '.') {
                        fileName = (filePath.substring(j + 1, i));
                        break;
                    }

                }
            }
        }
        //DateiEndung
        for (int i = filePath.length() - 1; i >= 0; i--) {
            if (filePath.charAt(i) == '.') {
                type = (filePath.substring(i, filePath.length()));

            }
        }

        //DateiGröße
        size = (Long.toString(Files.size(Paths.get(filePath))));
        double sizeNext = Double.parseDouble(size);
        sizeNext = Math.ceil(sizeNext / 1000) * 1000;
        int sizeNextInt = (int) sizeNext / 1000;
        size = (Integer.toString(sizeNextInt) + " KB");

        //Datum
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();

        //file.setId(dtf.format(now) + type + size);
        filePath = placeholderPath.setPlaceholder(filePath);
        file.setPath(filePath);
        file.setName(fileName);
        file.setType(type);
        file.setSize(size);
        file.setDate(dtf.format(now));
        //file.setCheckSum(checksum);

        return file;

    }

}
