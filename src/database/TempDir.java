package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verwaltet die temp Tabelle aus der Datenbank
 *
 * @author danvi
 */
public class TempDir {

    public static final Logger logger = Logger.getLogger(OriginalFileTable.class.getName());

    /**
     * Überprüft, ob bereits ein Temp-Ordner angegeben wurde
     *
     * @return
     * @throws SQLException
     */
    public boolean tempDirExists() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean exists = false;

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM temp";
            statement = connection.prepareStatement(query);
            int counter = 1;
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                exists = true;
            } else {
                exists = false;
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

        return exists;
    }

    /**
     * Falls ein neuer Temp-Ordner gespeicher werden soll, wird der zuvor
     * gespeicherte gelöscht
     *
     * @throws SQLException
     */
    public void deletetempDirForReplacement() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();

            String query = "DELETE FROM temp";
            statement = connection.prepareStatement(query);
            //System.out.println(statement);
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
     * Speichert den ausgewählten Temp-Ordner in der Datenbank
     *
     * @param tempDir
     * @return
     * @throws SQLException
     */
    public int saveTempDir(String tempDir) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO temp(tempDir) VALUES(?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, tempDir);
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
            exception.printStackTrace();
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
     * Gibt den gespeicherten Pfad des Temp-Ordners zurück
     * @return
     * @throws SQLException 
     */
    public String getTempDir() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String tempDirPath = "";
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM temp";
            statement = connection.prepareStatement(query);
            int counter = 1;
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tempDirPath = resultSet.getString("tempDir");
            } else {
                System.out.println("Kein Temp Ordner in der Datenbank");
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
        return tempDirPath;
    }
}
