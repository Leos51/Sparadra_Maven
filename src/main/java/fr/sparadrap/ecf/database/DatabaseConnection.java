package fr.sparadrap.ecf.database;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {

    private static final String PATHCONF = "conf.properties";
    private static Connection connection = initConnection();



    private DatabaseConnection() throws SQLException, IOException, ClassNotFoundException {}

    private static Connection initConnection(){
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(PATHCONF)) {
            props.load(is);
            // chargement du driver
            Class.forName(props.getProperty("jdbc.driver.class"));
            // Création de la connection
            String  url = props.getProperty("jdbc.url");
            String  login = props.getProperty("jdbc.login");
            String  password = props.getProperty("jdbc.password");
            return DriverManager.getConnection(url, login, password);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    };

    public static Connection getInstanceDB() throws SQLException, IOException, ClassNotFoundException {

        try {
            if (getConnection() == null || getConnection().isClosed()) {
                new DatabaseConnection();
                System.out.println("Connected to database : " + getConnection());
            } else {
                System.out.println("Connection already existing");
            }
        } finally {

        }
        return connection;
    }

    public static void closeInstanceDB() throws SQLException {
        try {
            if (getConnection() != null && !getConnection().isClosed()) {
                getConnection().close();
            }
        } catch (SQLException ex) {
            System.err.println("❌ Erreur lors de la fermeture de la connexion : " + ex.getMessage());
        }
    }

    private static Connection getConnection() {
        return connection;
    }



}
