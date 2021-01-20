package cloudtestfxml;

import database.CloudsTable;
import database.OriginalFileTable;
import database.PartFiles;
import database.PartFilesTable;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.logging.*;
/**
 * Die Klasse MoveFile verschiebt Dateien in die Cloud. Dabei wird die
 * Originaldatei des Anwenders in die Anzahl der Clouds geteilt, diese Teile
 * werden in die Cloud verschoben, während die Original-Datei des Anwenders
 * gelöscht wird.
 *
 * @author danvi
 */
public class MoveFile {

    private static final Logger logger = Logger.getLogger(MoveFile.class.getName());
    //Klasseninstanzen
    CloudsTable cloudsTable = new CloudsTable();
    OriginalFileTable originalFileTable = new OriginalFileTable();
    PartFilesTable partFilesTable = new PartFilesTable();
    PlaceholderPath placeholderPath = new PlaceholderPath();
    //Globale Variablen
    long sizePerPart[];
    int chunkSize;

    /**
     * Methode ruft die Methoden splitFile() auf, um die Originaldatei zu teilen
     * und diese Teile in die Cloud zu verschieben. Danach löscht sie die
     * Originaldatei und ruft die Methode createUserFile() auf.
     *
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SQLException
     */
    public void moveFile() throws IOException, FileNotFoundException, SQLException {

        splitFile(originalFileTable.getLastEntryPath(), cloudsTable.getNumberOfCloudsFromDatabase(), calculateCloudSpace(originalFileTable.getLastEntryPath(), cloudsTable.getNumberOfCloudsFromDatabase()));
        String deleteFile = placeholderPath.replacePlaceholder(originalFileTable.getLastEntryPath());
        Files.delete(Paths.get(deleteFile));
        createUserFile(chunkSize);

    }

    /**
     * Berechnet die Anzahl der Bytes, die jede Cloud pro Datei bekommt, in
     * Abhängigkeit der Größe der Cloud und berechnet daraus die Anzahl der
     * Bytes, die jeder Datei-Part von der Originaldatei bekommen soll
     *
     * @param filePath Dateipfad der Datei, die verschoben werden soll
     * @param numberOfClouds Anzahl der Clouds
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public long[] calculateCloudSpace(String filePath, int numberOfClouds) throws FileNotFoundException, IOException, SQLException {
        int sizePerCloud[] = cloudsTable.getCloudSize();
        int allSize = 0;
        double percentPerCloud[] = new double[numberOfClouds];
        double sizePerPartDouble[] = new double[numberOfClouds];
        double fractionalPart[] = new double[numberOfClouds];
        double rest = 0;
        //Berechnen der Gesamtkapazität aller zu verwendenen Clouds
        sizePerPart = new long[numberOfClouds];
        for (int i = 0; i < numberOfClouds; i++) {
            allSize += sizePerCloud[i];

        }
        //Berechnen des prozentalen Anteil für jede Cloud von der Gesamtkapazität
        for (int i = 0; i < percentPerCloud.length; i++) {
            percentPerCloud[i] = ((double) sizePerCloud[i] / (double) allSize) * 100.0;
            //System.out.println("Prozent pro Cloud " + percentPerCloud[i]);
        }

        //Berechnen Größe der Teil-Dateien in Abhängigkeit von den Cloud-Kapazitäten und der Dateigröße
        filePath = placeholderPath.replacePlaceholder(filePath);
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            long sourceSize = file.length();
            
            for (int i = 0; i < sizePerPart.length; i++) {
                sizePerPartDouble[i] = (double) sourceSize * (percentPerCloud[i] / 100);
                //System.out.println(sizePerPartDouble[i]);
                BigDecimal temp = (new BigDecimal(sizePerPartDouble[i])).remainder(BigDecimal.ONE);
                fractionalPart[i] = temp.doubleValue();
                rest += fractionalPart[i];
                sizePerPart[i] = (long) sizePerPartDouble[i];
                //System.out.println("Kommastelle " + fractionalPart[i]);
                //System.out.println("gerundet " + sizePerPart[i]);
            }
            sizePerPart[0] += (long) rest;
            for (int i = 0; i < sizePerPart.length; i++) {
                //System.out.println("Finale Größe der Parts: " + sizePerPart[i]);
            }
        }
        return sizePerPart;

    }

    /**
     * Teilt die Original-Datei im Round-Robin Verfahren chunk-weise in
     * Teil-Dateien auf.
     *
     * @param filePath Dateipfad, der zu verschiebenen Datei
     * @param numberOfClouds Anzahl der zu verwendenen Clouds
     * @param sizeOfParts Soll-Größe der Teil-Dateien
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SQLException
     */
    public void splitFile(String filePath, int numberOfClouds, long sizeOfParts[]) throws FileNotFoundException, IOException, SQLException {
        chunkSize = 0;
        final int numberOfParts = sizeOfParts.length;
        String name = originalFileTable.getLastEntryName();
        String fileType = originalFileTable.getLastEntryType();

        //String Array mit den Teil-Dateipfaden
        String[] outPath = new String[numberOfClouds];
        for (int i = 0; i < numberOfClouds; i++) {
            outPath[i] = cloudsTable.getCloudsPathsFromDatabase(i) + '\\' + name + (i + 1) + fileType;
            outPath[i] = placeholderPath.replacePlaceholder(outPath[i]);
        }

        InputStream in = null;
        OutputStream[] out = new OutputStream[numberOfParts];
        filePath = placeholderPath.replacePlaceholder(filePath);
        try {
            in = new BufferedInputStream(new FileInputStream(filePath));
            for (int part = 0; part < numberOfParts; part++) {
                out[part] = new BufferedOutputStream(new FileOutputStream(outPath[part]));
            }
            long sourceSize;
            try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
                sourceSize = file.length();
            }
            byte[] buf = new byte[4096]; //Standard Chunk-Größe 4KiB 4096
            /*Wenn die Datei-Größe größer ist, als die Standard Chunk-Größe * die Anzahl der
            zu verwendenen Clouds, dann soll die Standard Chunk-Größe verwendet werden
             */
            if (sourceSize > buf.length * numberOfClouds) {
                chunkSize = buf.length;
                long[] remain = sizeOfParts.clone();
                for (boolean done = false; !done;) {
                    done = true;
                    for (int part = 0; part < numberOfParts; part++) {
                        if (remain[part] > 0) {
                            int len = in.read(buf, 0, (int) Math.min(remain[part], buf.length));
                            if (len == -1) {
                                done = true;
                                break;
                            }
                            remain[part] -= len;
                            //System.out.println("file " + part + " " + (sizeOfParts[part] - remain[part]));
                            out[part].write(buf, 0, len);
                            done = false;
                        }
                    }
                }
                /*
                Ist die Datei-Größe kleiner, als die Standard Chunk-Größe * die Anzahl der
                zu verwendenen Clouds, dann soll die Chunk-Größe abghängig von der Datei-Größe
                und der Anzahl der zu verwendenen Clouds sein
                 */
            } else {
                byte[] buf2 = new byte[((int) sourceSize / numberOfClouds)];
                chunkSize = buf2.length;
                long[] remain = sizeOfParts.clone();
                for (boolean done = false; !done;) {
                    done = true;
                    for (int part = 0; part < numberOfParts; part++) {
                        if (remain[part] > 0) {
                            int len = in.read(buf2, 0, (int) Math.min(remain[part], buf2.length));
                            if (len == -1) {
                                done = true;
                                break;
                            }
                            remain[part] -= len;
                            //System.out.println("file " + part + " " + (sizeOfParts[part] - remain[part]));
                            out[part].write(buf2, 0, len);
                            done = false;
                        }
                    }
                }
            }

        } finally {
            if (in != null) {
                in.close();
            }
            for (int part = 0; part < out.length; part++) {
                if (out[part] != null) {
                    out[part].close();
                }
            }
        }

    }

    /**
     * Sucht in den Cloud-Ordnern nach den Teil-Dateien, um diese in die
     * Datenbank zu speichern.
     *
     * @param chunkSize individuelle Chunk-Größe, die benutzt wurde, um die
     * Datei zu teilen
     * @throws IOException
     * @throws SQLException
     */
    public void createUserFile(int chunkSize) throws IOException, SQLException {
        //Filter nach Teil-Dateien, die den Originaldatei-Namen enthalten
        FilenameFilter textFilefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                try {
                    if (lowercaseName.contains(originalFileTable.getLastEntryName().toLowerCase())) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MoveFile.class.getName()).log(Level.SEVERE, null, ex);
                }
                return false;
            }
        };

        //Einfügen, der durch den Filter ermittelten, Teilpfade in String Array    
        File directoryPath = null;
        int j = 0;
        String partsPath[] = new String[cloudsTable.getNumberOfCloudsFromDatabase()];
        for (int i = 0; i < cloudsTable.getNumberOfCloudsFromDatabase(); i++) {
            String temp = placeholderPath.replacePlaceholder(cloudsTable.getCloudsPathsFromDatabase(i));
            directoryPath = new File(temp);
            File filesList[] = directoryPath.listFiles(textFilefilter);
            //TODO bei manchen pdf bricht er ab?? index out ouf bounds
            //FEHLER: Manchmal nimmt er alle Dateien aus den Ordnern - Filter funktioniert nicht??
            System.out.println("List of the text files in the specified directory:" + filesList.length);
            for (File files : filesList) {
                partsPath[j] = files.getAbsolutePath();
                j++;
            }

        }
        j = 0;
        //Speichern der Teil-Dateien in die Datenbank
        for (int i = 0; i < partsPath.length; i++) {
            try {
                PartFiles partFiles = this.createPartFileObject(originalFileTable.getLastEntryId() + i, originalFileTable.getLastEntryName(), partsPath[i], i + 1, sizePerPart[i], chunkSize);
                int userId = partFilesTable.savePartFile(partFiles);
            } catch (SQLException exception) {
                logger.log(Level.SEVERE, exception.getMessage());
            }
        }

    }

    /**
     * Erstellt ein Object für die Informationen der Tei-Dateien
     *
     * @param id
     * @param name
     * @param path
     * @param part
     * @param size
     * @param chunkSize
     * @return
     */
    public PartFiles createPartFileObject(String id, String name, String path, int part, long size, int chunkSize) {
        path = placeholderPath.setPlaceholder(path);
        PartFiles partFiles = new PartFiles();
        partFiles.setId(id);
        partFiles.setName(name);
        partFiles.setPath(path);
        partFiles.setPart(part);
        partFiles.setPartsSize(size);
        partFiles.setChunkSize(chunkSize);
        return partFiles;
    }

}
