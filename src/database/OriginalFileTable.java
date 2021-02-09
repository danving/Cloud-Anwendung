package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verwaltet die Original-Datei-Tabelle der Datenbank
 *
 * @author danvi
 */
public class OriginalFileTable {

    public static final Logger logger = Logger.getLogger(OriginalFileTable.class.getName());

    /**
     * Setzte den Auto-Incement auf 1 zurück
     */
    public void resetAutoIncrement() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM sqlite_sequence WHERE name = 'originalfile'";
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
     * Überprüft, ob die eingefügte Datei bereits in der Cloud existiert
     *
     * @param fileName Name der Datei, die überprüft werden soll
     * @return boolean, ob Datei bereits in DB gespeichert wurde
     * @throws SQLException
     */
    public boolean fileExists(String fileName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean exists = false;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE name = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, fileName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
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
     * Speichert die Daten der eingefügten Datei in die Datenbank
     *
     * @param originalFile Objekt OriginalFile
     * @return 0
     * @throws SQLException
     */
    public int saveOriginalFile(OriginalFile originalFile) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO originalfile(path, name, type, size, date) VALUES(?,?,?,?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, originalFile.getPath());
            statement.setString(counter++, originalFile.getName());
            statement.setString(counter++, originalFile.getType());
            statement.setString(counter++, originalFile.getSize());
            statement.setString(counter++, originalFile.getDate());
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
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
     * Gibt die Gesamzanzahl der Dateien zurück, die in die Clouds verschoben
     * wurden und in der DB gespeichert sind
     *
     * @return Anzahl der Dateien, die in der DB gespeichert wurden
     * @throws SQLException
     */
    public int getNumberOfFiles() throws SQLException {
        int number = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM originalfile";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                number = resultSet.getInt(1);
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
        return number;
    }

    /**
     * Gibt den Letzten Pfad einer Datei zurück, der in die Datenbank eingefügt
     * wurde
     *
     * @return letzter gespeicherte Pfad
     * @throws SQLException
     */
    public String getLastEntryPath() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String path = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                path = resultSet.getString("path");
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
        return path;
    }

    /**
     * Gibt den Datei-Name der letzten Datei zurück, der in die Datenbank
     * eingefügt
     *
     * @return letzter gespeicherter Name
     * @throws SQLException
     */
    public String getLastEntryName() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String name = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                name = resultSet.getString("name");
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
        return name;
    }

    /**
     * Gibt den Typ der letzten Datei zurück, der in die Datenbank eingefügt
     * wurde
     *
     * @return letzter gespeicherter Datentyp
     * @throws SQLException
     */
    public String getLastEntryType() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String type = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                type = resultSet.getString("type");
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
        return type;
    }

    /**
     * Gibt die Größe der letzten Datei zurück, die in die Datenbank eingefügt
     * wurde
     *
     * @return letzte gespeicherte Datei-Größe
     * @throws SQLException
     */
    public String getLastEntrySize() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String size = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                size = resultSet.getString("size");
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
        return size;
    }

    /**
     * Gibt das Datum der Datei zurück, die als letztes in die Datenbank
     * eingefügt wurde
     *
     * @return letztes gespeichertes Datum
     * @throws SQLException
     */
    public String getLastEntryDate() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String date = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                date = resultSet.getString("date");
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
        return date;
    }

    /**
     * Gibt die Id der letzten Datei zurück, die in die Datenbank eingefügt
     * wurde
     *
     * @return letzte gespeicherte id
     * @throws SQLException
     */
    public String getLastEntryId() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String id = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM originalfile WHERE id = (SELECT MAX(id) from originalfile)";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getString("id");
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
        return id;
    }

    /**
     * Gibt eine Liste aller gespeicherten Dateien, in der Datenbank zurück
     *
     * @return Liste mit alles bisher gespeicherten Dateien
     * @throws SQLException
     */
    public List getOriginalFilesForTable() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        List<OriginalFile> filesList = new ArrayList<>();

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT name, date, type, size FROM originalfile";
            statement = connection.prepareStatement(query);
            int counter = 1;
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                OriginalFile file = new OriginalFile();
                file.setName(resultSet.getString(1));
                file.setDate(resultSet.getString(2));
                file.setType(resultSet.getString(3));
                file.setSize(resultSet.getString(4));
                filesList.add(file);
                counter += 1;
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
        for (int i = 0; i < filesList.size(); i++) {
            System.out.println(i + " " + filesList.get(i).getName());
            System.out.println(i + " " + filesList.get(i).getDate());
            System.out.println(i + " " + filesList.get(i).getType());
            System.out.println(i + " " + filesList.get(i).getSize());
            
        }
         */
        return filesList;
    }

    /**
     * Gibt den Typ der gewünschten Datei zurück
     *
     * @param name Name der Datei
     * @return Datei-Typ einer gewünschten Datei
     * @throws SQLException
     */
    public String getTypeOfFile(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String type = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT type FROM originalfile WHERE name = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                type = resultSet.getString("type");
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
        return type;
    }

    /**
     * Gibt die Checksumme der gewünschten Datei zurück
     *
     * @param name Name der Datei, deren Checksumme zurück gegeben werden soll
     * @return Checksumme, der gewünschten Datei
     * @throws SQLException
     */
    public String getCheckSum(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String checkSum = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT checksum FROM originalfile WHERE name = ?";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                checkSum = resultSet.getString("checksum");
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
        return checkSum;
    }

    /**
     * Löscht einen gewünschten Eintrag in der Datenbank
     *
     * @param name Name der zu löschenden Datei
     * @throws SQLException
     */
    public void deleteOriginalFile(String name) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM originalFile WHERE name = ?";
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
    public void deleteAllFiles() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM originalfile";
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

}
