package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Verwaltet die Cloud Tabelle aus der Datenbank
 * @author danvi
 */
public class CloudsTable {

    public static final Logger logger = Logger.getLogger(OriginalFileTable.class.getName());

    /**
     * Überprüft, ob in der Datenbank bereits diese Cloud gespeichert wurde
     *
     * @param cloudName Name der Cloud, die ausgewählt wurde
     * @return
     * @throws SQLException
     */
    public boolean cloudExists(String cloudName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean exists = false;

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM clouds WHERE cloud = ?";
            statement = connection.prepareStatement(query);
            int counter = 1;
            statement.setString(counter++, cloudName);
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
     * Speichert die ausgewählte Cloud in der Datenbank
     * @param cloudName
     * @return
     * @throws SQLException 
     */
    public int saveCloud(Clouds cloudName) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            //TODO wenn nur eine Cloud angegeben wird, wird diese nicht gespeichert
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO clouds(cloud, id, cloudsize, numberOfClouds) VALUES(?,?,?,?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int counter = 1;
            statement.setString(counter++, cloudName.getCloud());
            statement.setInt(counter++, cloudName.getId());
            statement.setInt(counter++, cloudName.getSize());
            statement.setInt(counter++, cloudName.getNumber());
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
     * Gibt die Anzahl der ausgewählten Clouds zurück
     * @return
     * @throws SQLException 
     */
    public int getNumberOfCloudsFromDatabase() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int size = 1;
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT COUNT(*) FROM clouds";
            statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                size = resultSet.getInt(1);
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
     * Gibt ein double-Array mit den Speicherkapazitäten aller Clouds,
     * die in der Datenbank gespeichert sind zurück
     * @return int[] size
     * @throws SQLException 
     */
    public int[] getCloudSize() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        int size[] = new int[5];
        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT cloudsize FROM clouds";
            statement = connection.prepareStatement(query);
            int counter = 0;
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                size[counter] = resultSet.getInt("cloudsize");
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
        
        return size;
    }

    /**
     * Gibt den Pfad der gewünschten Cloud zurück
     *
     * @param index
     * @return
     * @throws SQLException
     */
    public String getCloudsPathsFromDatabase(int index) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String cloudsList[] = new String[5];

        try {
            connection = Database.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT cloud FROM clouds";
            statement = connection.prepareStatement(query);
            int counter = 0;
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cloudsList[counter] = resultSet.getString("cloud");
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
        for(int i = 0; i < cloudsList.length; i++) {
            //System.out.println(cloudsList[i]);
        }
        return cloudsList[index];
    }

    /**
     * Löscht früher ausgwählte Clouds, die nun vom Anwender nicht mehr gebraucht werden
     * @param numberOfClouds
     * @throws SQLException 
     */
    public void deleteCloudFromDatabase(int numberOfClouds) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            
            String query = "DELETE FROM clouds WHERE id > ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, numberOfClouds);
            //System.out.println(statement);
            statement.executeUpdate();
            if(!connection.getAutoCommit()) {
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
     * Aktualisiert die Anzahl der Clouds in der Datenbank, sobald diese sich ändert
     * @param numberOfClouds
     * @throws SQLException 
     */
    public void updateCloudNumber(int numberOfClouds) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = Database.getDBConnection();
            
            String query = "UPDATE clouds SET numberOfClouds = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, numberOfClouds);
            statement.executeUpdate();
            if(!connection.getAutoCommit()) {
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

}
