package cloudtestfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasse um die Cloud-Anwendung zu starten
 *
 * @author danvi
 */
public class Main extends Application {

    /**
     * Startet die Anwendung
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Ladet die FXML-Datei für das Hauptfenster
     *
     * @param stage Stage die geöffnet werden soll
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/resources/Main_fxml.fxml"));
        Parent content = loader.load();

        Scene scene = new Scene(content, 800, 550);
        stage.setTitle("Cloud Anwendung");
        stage.setScene(scene);
        stage.show();


    }
}
