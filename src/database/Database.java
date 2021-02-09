package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Verwaltung des Zugangs zur Datenbank
 *
 * @author danvi
 */
public class Database {

    /*
    //MYSQL
    public static final Logger logger = Logger.getLogger(Database.class.getName());
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://LAPTOP-0CF8DLKU:3306/clouddatabase?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "welcome321Welcome";
     */
    private static final Database dbcontroller = new Database();
    private static Connection connection;
    private static String homeDir = System.getProperty("user.home");
    private static final String DB_PATH = "jdbc:sqlite:" + homeDir + "\\Google Drive\\Test\\Datenbank_NICHT_ANFASSEN\\cloudDatenbank-LAPTOP-0CF8DLKU.s3db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    private Database() {

    }

    /**
     * Stellt die Verbindung zur Datenbank her
     *
     * @return Verbindung 
     * @throws SQLException
     */
    public static Connection getDBConnection() throws SQLException {
        /*
        //MYSQL
        Connection connection = null;

        try {
            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return connection;
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, exception.getMessage());
        }

        return connection;
         */
        
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");

        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(DB_PATH);
            return connection;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        

        return connection;
        
    }
}
