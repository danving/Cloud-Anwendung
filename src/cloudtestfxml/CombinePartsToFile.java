package cloudtestfxml;

import database.CloudsTable;
import database.OriginalFileTable;
import database.PartFilesTable;
import database.TempDir;
import java.awt.Desktop;
import java.io.*;
import java.nio.channels.Channel;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * Die Klasse CombinePartsToFile fügt die entstandenen Teil-Dateien wieder
 * zusammen. Wird aufgerufen, sobald der Anwender auf die Datei in der Tabelle 
 * im Hauptfenster klickt.
 *
 * @author danvi
 */
public class CombinePartsToFile {

    //KlassenInstanzen
    CloudsTable cloudsTable = new CloudsTable();
    PartFilesTable partFilesTable = new PartFilesTable();
    OriginalFileTable originalFileTable = new OriginalFileTable();
    PlaceholderPath placeholderPath = new PlaceholderPath();
    TempDir tempDir = new TempDir();

    /**
     * Fügt die Teil-Dateien wieder zusammen,indem chunkweise aus den Teil-Dateien
     * gelesen wird und dann in eine neue Datei (Original) geschrieben wird
     * @param sizeOfParts Soll-Länge der einzelnen Parts
     * @param name Name der urprünglichen Datei
     * @param type Typ der ursprünglichen Datei
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException 
     */
    public void combinePartsToFile(long sizeOfParts[], String name, String type) throws FileNotFoundException, IOException, SQLException {
        final int numberOfParts = partFilesTable.getNumberOfParts(name);
        String tempDirPath = tempDir.getTempDir();
        tempDirPath = placeholderPath.replacePlaceholder(tempDirPath);
        File originalFile = new File(tempDirPath + "\\" + name + "_Original" + type);
        try {
            if (originalFile.createNewFile()) {
                System.out.println("File created: " + originalFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException ex) {
            Logger.getLogger(CombinePartsToFile.class.getName()).log(Level.SEVERE, null, ex);
        }

        //String Array für die Teil-Pfade
        String[] inPath = new String[numberOfParts];
        inPath = partFilesTable.getPartsPath(name);
        for (int i = 0; i < numberOfParts; i++) {
            inPath[i] = placeholderPath.replacePlaceholder(inPath[i]);
            //System.out.println(inPath[i]);
        }

        InputStream in[] = new InputStream[numberOfParts];
        OutputStream out = new BufferedOutputStream(new FileOutputStream(originalFile));
        try {

            for (int part = 0; part < numberOfParts; part++) {
                in[part] = new BufferedInputStream(new FileInputStream(inPath[part]));
            }
            
            byte[] buf = new byte[partFilesTable.getChunkSize(name)]; //Byte Array mit der Chunk-Länge, die zum aufteilen benutzt wurde
            long[] remain = sizeOfParts.clone();
            for (boolean done = false; !done;) {
                done = true;
                for (int part = 0; part < numberOfParts; part++) {
                    if (remain[part] > 0) {
                        int len = in[part].read(buf, 0, (int) Math.min(remain[part], buf.length));
                        if (len == -1) {
                            done = true;
                            break;
                        }
                        remain[part] -= len;
                        //System.out.println("file " + part + " " + (sizeOfParts[part] - remain[part]));
                        out.write(buf, 0, len);
                        done = false;
                    }
                }
            }
        } finally {
            if (in != null) {
                for (int part = 0; part < in.length; part++) {
                    in[part].close();
                }

            }
            if (out != null) {
                out.close();
            }
        }

        //Öffnet die neu erstellte Datei
        Desktop.getDesktop().open(originalFile);

        //Timer der Originaldatei nach 5 sek. wieder löscht
        long timeToSleep = 5L;
        TimeUnit time = TimeUnit.SECONDS;

        try {
            time.sleep(timeToSleep);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        if (isFileClosed(originalFile)) {
            originalFile.delete();
        }
        //System.out.println("Gesamtgröße: " + combined);
    }

    /**
     * Überprüft, ob die Datei geschlossen wurde
     *
     * @param file Datei mit zusammengefühgtem Inhalt
     * @return
     */
    private static boolean isFileClosed(File file) {
        boolean closed;
        Channel channel = null;
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
            closed = true;
        } catch (FileNotFoundException ex) {
            closed = false;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ex) {
                }
            }
        }
        return closed;
    }
}
