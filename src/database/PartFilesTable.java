package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verwaltet die Teil-Dateien-Tabelle der Datenbank
 *
 * @author danvi
 */
public class PartFilesTable {

    /**
     * neue Klasseninstanz für die Klasse OriginalFileTable()
     */
    OriginalFileTable originalfile = new OriginalFileTable();

    private static final Logger logger = Logger.getLogger(PartFilesTable.class.getName());

    /**
     * Setzte den Auto-Incement auf 1 zurück
     */
    public void resetAutoIncrement() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM sqlite_sequence WHERE name = 'partfiles'";
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            exception.printStackTrace();
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
    }

    /**
     * Überprüft, ob die Teil-Datei bereits in die Datenbank eingefügt wurde
     *
     * @param partsName Name der zu prüfenden Teil-Datei
     * @return boolean, ob Teil-Datei bereits existiert
     * @throws SQLException
     */
    public boolean partFileExists(String partsName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean exists = false;

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, partsName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                exists = true;
            } else {
                exists = false;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        return exists;
    }

    /**
     * Speiechern die Teil-Dateien in die Datenbank
     *
     * @param partFiles Obejekt PartFiles
     * @return 0
     * @throws SQLException
     */
    public int savePartFile(PartFiles partFiles) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO partfiles(name, path, part, partsSize, chunksize) VALUES(?,?,?,?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, partFiles.getName());
            statement.setString(counter++, partFiles.getPath());
            statement.setInt(counter++, partFiles.getPart());
            statement.setLong(counter++, partFiles.getPartsSize());
            statement.setInt(counter++, partFiles.getChunkSize());
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getByte(1);
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            if (null != connection) {
                connection.rollback();
            }
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }

            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return 0;
    }

    /**
     * Gibt ein String-Array mit allen Namen der Partfiles, ohne Dupilkationen
     *
     * @return String-Array mit allen Namen der Original-Datei der Teil-Dateien (einmal)
     * @throws SQLException
     */
    public String[] getNamesOfParts() throws SQLException {
        String[] partsName = new String[originalfile.getNumberOfFiles()];
        //System.out.println(originalfile.getNumberOfFiles());
        int count = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT DISTINCT name FROM partfiles";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                partsName[count] = resultSet.getString(1);
                count++;

            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        return partsName;
    }

    /**
     * Gibt die Anzahl der Teil-Dateien zurück
     *
     * @return Anzahl aller Teil-Dateien
     * @throws SQLException
     */
    public int getNumberOfParts() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int numberOfParts = 0;
        int counter = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM partfiles";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                numberOfParts = resultSet.getInt(1);

            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        //System.out.println(numberOfParts);
        return numberOfParts;
    }

    /**
     * Gibt die Anzahl der Teil-Dateien mit einem bestimmten Namen zurück
     *
     * @param name Name der gewünschten Teil-Dateien
     * @return Anzahl von bestimmten Teil-Dateien
     * @throws SQLException
     */
    public int getNumberOfPartsPerName(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int numberOfParts = 0;
        int counter = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM partfiles where name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                numberOfParts = resultSet.getInt(1);

            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        //System.out.println(numberOfParts);
        return numberOfParts;
    }

    /**
     * Gibt die Chunk-Größe zurück, die zum Teilen der gewünschten Datei
     * verwendet wurde
     *
     * @param name Name der Datei, deren Chunk-Größe zurück gegeben werden soll
     * @return Chunk-Größe von bestimmter Teil-Datei
     * @throws SQLException
     */
    public int getChunkSize(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int chunkSize = 0;
        int counter = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT chunksize FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                chunkSize = resultSet.getInt(1);

            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        //System.out.println(chunkSize);
        return chunkSize;
    }

    /**
     * Gibt die Pfade der Teil-Dateien zurück
     *
     * @return String-Array mit allen Teil-Datei-Pfaden
     * @throws SQLException
     */
    public String[] getPartsPath() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String paths[] = new String[getNumberOfParts()];
        int counter = 1;
        int i = 0;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT path FROM partfiles";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                paths[i] = resultSet.getString("path");
                i += 1;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return paths;
    }

    /**
     * Gibt in einem String[] die Pfade der Teil-Dateien von einem bestimmt
     * Namen zurück
     *
     * @param name Name der zurückgenenen Teil-Dateien
     * @return String-Array mit Pfaden von bestimmten Teil-Dateien
     * @throws SQLException
     */
    public String[] getPartsPathPerName(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String paths[] = new String[5];
        int counter = 1;
        int i = 0;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT path FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                paths[i] = resultSet.getString("path");
                i += 1;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        /*
        for (int j = 0; j < paths.length; j++) {
            System.out.println("Paths: " + paths[j]);
        }
         */
        return paths;
    }

    /**
     * Gibt die Größe der Teil-Dateien mit einem bestimmten Namen zurück
     *
     * @param name Name der Original-Datei
     * @return Long-Array mit der Datei-Größe von bestimmten Teil-Dateien
     * @throws SQLException
     */
    public long[] getPartsSize(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        long partsSize[] = new long[5];
        int counter = 1;
        int i = 0;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT partsSize FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, name);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                partsSize[i] = resultSet.getLong("partsSize");
                i += 1;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
        /*
        for (int j = 0; j < partsSize.length; j++) {
            System.out.println("PathSize: " + partsSize[j]);
        }
         */
        return partsSize;
    }

    /**
     * Löscht einen gewünschten Eintrag in der Datenbank, indem der Name
     * mitgeben wird
     *
     * @param name Name der zu löschenden Datei
     * @throws SQLException
     */
    public void deletePartsFiles(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }
    }

    /**
     * Löscht alle Einträge aus der Datenbank
     *
     * @throws SQLException
     */
    public void deleteAllPartFiles() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM partfiles";
            statement = connection.prepareStatement(query);
            statement.executeUpdate();
            if (!connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            exception.printStackTrace();
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

    }

    /**
     * Gibt die Gesamt-Größe der Dateien zurück, die bereits in die ausgewählte
     * Cloud hinzugefügt wurde.
     *
     * @param cloud id der ausgwählten Cloud
     * @return Bisher  belegter Speicher
     * @throws SQLException
     */
    public long getCloudCapacity(int cloud) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int counter = 1;
        long cloudCapacity = 0;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT partsSize FROM partfiles WHERE part = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(counter++, cloud);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                cloudCapacity += resultSet.getLong("partsSize");
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }

            if (null != connection) {
                connection.close();
            }
        }

        return cloudCapacity;
    }
}
