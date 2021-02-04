package resources;

import cloudtestfxml.CheckSumSHA;
import cloudtestfxml.CloudTableView;
import cloudtestfxml.CombinePartsToFile;
import cloudtestfxml.FolderHierarchy;
import database.OriginalFile;
import database.OriginalFileTable;
import database.PartFilesTable;
import cloudtestfxml.MoveFile;
import cloudtestfxml.PlaceholderPath;
import database.CloudsTable;
import database.TempDir;
import java.io.*;
import javafx.event.ActionEvent;
import java.net.URL;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.lang.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Klasse zur Verwaltung des Hauptfensters der Anwendung
 *
 * @author danvi
 */
public class Main_fxmlController extends Thread implements Initializable {

    private static final Logger logger = Logger.getLogger(Main_fxmlController.class.getName());
    //Klasseninstanzen
    OriginalFileTable originalFileTable = new OriginalFileTable();
    CombinePartsToFile combinePartsToFile = new CombinePartsToFile();
    PartFilesTable partfiles = new PartFilesTable();
    CloudsTable cloudsTable = new CloudsTable();
    CheckSumSHA checkSum = new CheckSumSHA();
    MoveFile moveFile = new MoveFile();
    FolderHierarchy folderHierarchy = new FolderHierarchy();
    PlaceholderPath placeholderPath = new PlaceholderPath();
    TempDir tempDir = new TempDir();

    public static String fileName = "-";
    public static String filePath = "-";
    File treeViewFile;
    public static boolean waiting = false;
    public static boolean waitDeleteTableView = false;

    @FXML
    private MenuItem exitButton;
    @FXML
    private MenuItem configButton;
    @FXML
    private VBox dragTarget;
    @FXML
    public Label droppedPathLabel;
    @FXML
    public TableColumn tableFile;
    @FXML
    private TableColumn tableDate;
    @FXML
    private TableColumn tableType;
    @FXML
    private TableColumn tableSize;
    @FXML
    public TableView cloudTableView;
    @FXML
    private static Button moveFileToCloudButton;
    @FXML
    public TreeView treeview;
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * Initialisiert die Tabelle mit allen Dateien, die sich bereits in der
     * Datenbank befinden und den TreeView der die Ordnerhierarchy anzeigt
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<OriginalFile> filesTable = new ArrayList<>();
        try {
            filesTable = originalFileTable.getOriginalFilesForTable();
        } catch (SQLException ex) {
            Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (filesTable != null) {
            for (int i = 0; i < filesTable.size(); i++) {
                try {
                    addTableViewEntry(filesTable.get(i).getName(), filesTable.get(i).getDate(), filesTable.get(i).getType(), filesTable.get(i).getSize());
                } catch (SQLException ex) {
                    Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        /*
        String homeDir = System.getProperty("user.home");
        String tempTree = homeDir + "//Desktop";
        try {
            if(tempDir.tempDirExists() == true) {
                try {
                    tempTree = tempDir.getTempDir();
                } catch (SQLException ex) {
                    Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
                }
                tempTree = placeholderPath.replacePlaceholder(tempTree);
                System.out.println(tempTree);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        treeViewFile = new File(tempTree);
        try {
            treeview.setRoot(folderHierarchy.displayFolderTreeView(treeViewFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }
         */

    }

    /**
     * Öffnet das Einstellungsfenster, in dem die dazugehörige FXML-Datei
     * geladen wird.
     *
     * @param event Event mit dem auf die Menüzeile geklickt wird
     */
    @FXML
    public void configButton(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("Configuration_fxml.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Einstellungen");
            stage.setScene(new Scene(root, 600, 520));
            stage.showAndWait();

        } catch (IOException e) {
        }

    }

    /**
     * Methode für das Drag-And-Drop Feld, um eine Datei in die Anwendung zu
     * ziehen
     *
     * @param e Drag Event
     * @throws IOException
     */
    @FXML
    public void dragAndDrop(DragEvent e) throws IOException {

        dragTarget.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != dragTarget
                        && event.getDragboard().hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        dragTarget.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    filePath = db.getFiles().toString();
                    droppedPathLabel.setText(filePath);
                    fileName = "";
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

                    db.clear();
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                event.consume();
            }
        });

    }

    /**
     * Methode für den Button, um die eingefügte Datei in die Cloud zu
     * verschieben. Überprüft zunächst, ob noch genügend Speicher da ist, für
     * die Datei, die eingefügt werden soll. Überprüft, dann ob die Datei
     * bereits in der Cloud existiert. Wenn ja, wird der Anwender gefragt ob die
     * Datei ersetzt werden soll und ersetzt dann die Datei.
     *
     * @param e Event für Button
     * @throws IOException
     * @throws FileNotFoundException
     * @throws NoSuchAlgorithmException
     */
    @FXML
    public void moveFileToCloud(ActionEvent e) throws IOException, FileNotFoundException, NoSuchAlgorithmException {
        //Überprüfen, ob in der Cloud noch genügend Platz für die zuverschobene Datei ist
        try {
            int[] cloudsize = cloudsTable.getCloudSize();
            boolean isSmaller = true;
            long[] partsSize = moveFile.calculateCloudSpace((filePath.substring(1, filePath.length() - 1)), cloudsTable.getNumberOfCloudsFromDatabase());
            for (int i = 0; i < cloudsTable.getNumberOfCloudsFromDatabase(); i++) {
                long capacityPerCloud = ((long) cloudsize[i] * 1073741824); // * 1073741824, für GB in Byte
                long partsCapacity = partfiles.getCloudCapacity(i + 1) + partsSize[i];
                if (partsCapacity >= capacityPerCloud) {
                    isSmaller = false;
                }
                //System.out.println("Kapazität belegt: " + partfiles.getCloudCapacity(i + 1));
                //System.out.println("Kapazität gesamt: " + capacityPerCloud);
            }
            //Wenn die Datei zu groß ist, wird der Anwender darüber informiert
            if (isSmaller == false) {
                Parent rootCloudFull;
                FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("CloudFullDialog_fxml.fxml"));
                rootCloudFull = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Maximale Speicherkapazität überschritten");
                stage.setScene(new Scene(rootCloudFull, 450, 200));
                stage.showAndWait();
            } else {
                //Überprüfen, ob die Datei bereits in der Cloud/Datenbank existiert
                if (!originalFileTable.fileExists(fileName)) {
                    OriginalFile originalFile = createFileObject(filePath);
                    int fileId = originalFileTable.saveOriginalFile(originalFile);
                    /*Wenn die Datei sehr groß ist, und das verschieben viel Zeit in anspruch nimmt,
                        dann wird ein Dialog Fenster geöffnet, dass dem Anwender mitteilt, dass es
                        noch ein wenig dauern wird
                     */
                    String fileSize = originalFileTable.getLastEntrySize();
                    int size = 0;
                    for (int i = fileSize.length() - 1; i >= 0; i--) {
                        if (fileSize.charAt(i) == ' ') {
                            size = Integer.parseInt(fileSize.substring(0, i));
                            break;
                        }
                    }
                    if (size > 800) {
                        progressLabel.setOpacity(1);
                        progressIndicator.setOpacity(1);

                    }
                    //Verschieben der Datei, Erstellen eines Tabelleneintrags
                    moveFile.moveFile();
                    progressLabel.setOpacity(0);
                    progressIndicator.setOpacity(0);
                    addTableViewEntry(originalFileTable.getLastEntryName(), originalFileTable.getLastEntryDate(), originalFileTable.getLastEntryType(), originalFileTable.getLastEntrySize());
                    droppedPathLabel.setText("-");
                    filePath = null;
                    //treeview.setRoot(folderHierarchy.displayFolderTreeView(treeViewFile));

                    //Datei existier bereits, Anwender wird in einem Dialog gefragt, ob Datei ersetzt werden soll
                } else {
                    Parent rootReplace;

                    FXMLLoader loader = new FXMLLoader(Main_fxmlController.class.getResource("ReplaceDialog_fxml.fxml"));
                    rootReplace = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Datei existiert bereits");
                    stage.setScene(new Scene(rootReplace, 450, 200));
                    stage.showAndWait();

                    ReplaceDialog_fxmlController dialog = new ReplaceDialog_fxmlController();
                    synchronized (dialog) {
                        try {
                            while (waiting) {
                                dialog.wait();
                            }
                        } catch (InterruptedException inE) {
                            System.out.println("No Sync");
                        }
                    }
                    //System.out.println("Sync: " + dialog.replace);
                    //Wenn Datei ersetzt werden soll
                    if (dialog.replace) {
                        originalFileTable.deleteOriginalFile(fileName);

                        partfiles.deletePartsFiles(fileName);
                        OriginalFile originalFile = createFileObject(filePath);
                        int fileId = originalFileTable.saveOriginalFile(originalFile);
                        /*Wenn die Datei sehr groß ist, und das verschieben viel Zeit in anspruch nimmt,
                        dann wird ein Dialog Fenster geöffnet, dass dem Anwender mitteilt, dass es
                        noch ein wenig dauern wird
                         */
                        String fileSize = originalFileTable.getLastEntrySize();
                        int size = 0;
                        for (int i = fileSize.length() - 1; i >= 0; i--) {
                            if (fileSize.charAt(i) == ' ') {
                                size = Integer.parseInt(fileSize.substring(0, i));
                                break;
                            }
                        }
                        if (size > 800) {
                            progressLabel.setOpacity(1);
                            progressIndicator.setOpacity(1);

                        }
                        moveFile.moveFile();
                        progressLabel.setOpacity(0);
                        progressIndicator.setOpacity(0);
                        CloudTableView item;
                        String data;
                        TableColumn col = tableFile;
                        for (int i = 0; i < cloudTableView.getItems().size(); i++) {
                            item = (CloudTableView) cloudTableView.getItems().get(i);
                            data = (String) col.getCellObservableValue(item).getValue();
                            //System.out.println(data);
                            if (data.equals(fileName)) {
                                CloudTableView selectedItem = (CloudTableView) cloudTableView.getItems().get(i);
                                cloudTableView.getItems().remove(selectedItem);
                                //System.out.println("Table Row deleted");
                            }

                            addTableViewEntry(originalFileTable.getLastEntryName(), originalFileTable.getLastEntryDate(), originalFileTable.getLastEntryType(), originalFileTable.getLastEntrySize());
                            //treeview.setRoot(folderHierarchy.displayFolderTreeView(treeViewFile));
                        }
                    }

                }
                droppedPathLabel.setText("-");
            }

        } catch (SQLException | IOException ex) {
            Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Methode um die , in der Tabelle ausgewählte Datei,zu öffnen
     *
     * @param click Event, um auf die Datei zu klicken
     * @throws IOException
     */
    @FXML
    public void handleClickOnTable(MouseEvent click) throws IOException, FileNotFoundException, SQLException {

        if (click.getClickCount() == 2) {
            if (cloudTableView.getSelectionModel().getSelectedItem() != null) {
                CloudTableView file = (CloudTableView) cloudTableView.getSelectionModel().getSelectedItem();
                combinePartsToFile.combinePartsToFile(partfiles.getPartsSize(file.getFile()), file.getFile(), file.getType(), true);
            }
        }
    }

    /**
     * Schließt die Anwendung
     *
     * @param e
     */
    @FXML
    public void exit(ActionEvent e) {
        System.exit(0);
        
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
        String filePath = path.substring(1, path.length() - 1);
        String fileName = "";
        String type = "";
        String size = "";
        //String checksum = checkSum.checkSum(filePath);

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

    /**
     * Erstellt einen Eintrag in die Tabelle
     *
     * @param file
     * @param date
     * @param type
     * @param size
     * @throws SQLException
     */
    public void addTableViewEntry(String file, String date, String type, String size) throws SQLException {
        //Erstellen einer Tabellenzeile für die eingefügte Datei
        tableFile.setCellValueFactory(new PropertyValueFactory<CloudTableView, String>("file"));
        tableDate.setCellValueFactory(new PropertyValueFactory<CloudTableView, String>("date"));
        tableType.setCellValueFactory(new PropertyValueFactory<CloudTableView, String>("type"));
        tableSize.setCellValueFactory(new PropertyValueFactory<CloudTableView, String>("size"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        cloudTableView.getItems().add(new CloudTableView(file, date, type, size));

    }

    public void deleteTableView() {
        Configuration_fxmlController config = new Configuration_fxmlController();
        synchronized (config) {
            try {
                while(waitDeleteTableView) {
                    config.wait();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Main_fxmlController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        System.out.println(config.deleteTableView);
        if(config.deleteTableView) {
            cloudTableView.getItems().clear();
        }
        
    }
}
