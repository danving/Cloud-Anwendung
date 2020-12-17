package cloudtestfxml;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.scene.control.*;

/**
 *Klasse, die die Ordnerstruktur anzeigt
 * @author danvi
 */
public class FolderHierarchy {

    /**
     * Zeigt rekursiv die Ordnerstruktur des ausgwählten Ordners an
     * (Alle Unter-Ordner und Dateien, die sich darin befinden)
     * @param directory
     * @return
     * @throws FileNotFoundException 
     */
    public TreeItem<String> displayFolderTreeView(File directory) throws FileNotFoundException { //Returns a TreeItem representation of the specified directory
        //TODO CloudInhalte darstellen
        //FileInputStream folderInput = new FileInputStream("/resources.image/folder.png");
       // Image folder = new Image(getClass().getClassLoader().getResourceAsStream("resources.image/folder.png"));
        //FileInputStream fileInput = new FileInputStream("resources.image.file.png");
        TreeItem<String> root = new TreeItem<>(directory.getName());
        for(File f : directory.listFiles()) {
            System.out.println("Loading " + f.getName());
            if(f.isDirectory()) { //Then we call the function recursively
                root.getChildren().addAll(displayFolderTreeView(f));
            } else {
                root.getChildren().add(new TreeItem<String>(f.getName()));
            }
        }
        return root;
    }
}