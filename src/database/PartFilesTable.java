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
 * @author danvi
 */
public class PartFilesTable {

    private static final Logger logger = Logger.getLogger(PartFilesTable.class.getName());

    /**
     * Überprüft, ob die Teil-Datei bereits in die Datenbank eingefügt wurde
     *
     * @param partPath
     * @return
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
     * @param partFiles
     * @return
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
     * Gibt die Anzahl der Teil-Dateien zurück
     *
     * @param id Name der Original-Datei
     * @return
     * @throws SQLException
     */
    public int getNumberOfParts(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int numberOfParts = 0;
        int counter = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, id);
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
     * Gibt die Chunk-Größe zurück, die zum Teilen der gewünschten Datei verwendet wurde
     * @param id Name der Datei, deren Chunk-Größe zurück gegeben werden soll
     * @return
     * @throws SQLException 
     */
    public int getChunkSize(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int chunkSize = 0;
        int counter = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT chunksize FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(counter++, id);
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
     * @param id Name der Original-Datei
     * @return
     * @throws SQLException
     */
    public String[] getPartsPath(String id) throws SQLException {
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
            statement.setString(counter++, id);
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
     * Gibt die Größe der Teil-Dateien zurück
     *
     * @param id Name der Original-Datei
     * @return
     * @throws SQLException
     */
    public long[] getPartsSize(String id) throws SQLException {
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
            statement.setString(counter++, id);
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
     * @param id Name der zu löschenden Datei
     * @throws SQLException
     */
    public void deletePartsFiles(String id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM partfiles WHERE name = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, id);
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
     * Gibt die Gesamt-Größe der Dateien zurück, die bereits in die ausgewählte Cloud
     * hinzugefügt wurde.
     * @param cloud id der ausgwählten Cloud
     * @return
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

