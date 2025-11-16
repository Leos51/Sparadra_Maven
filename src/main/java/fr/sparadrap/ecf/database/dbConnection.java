package fr.sparadrap.ecf.database;
import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class dbConnection {

    private final String PATHCONF = "conf.properties";
    private static final Properties props = new Properties();
    private static Connection connection;



    private dbConnection() throws SQLException, IOException, ClassNotFoundException {

        try(InputStream is = dbConnection.class.getClassLoader().getResourceAsStream(PATHCONF)) {
            if (is == null) {
                throw new IOException("Fichier conf.properties introuvable dans le classpath");
            }

            // chargement de properties
            props.load(is);
            // chargement du driver
            Class.forName(props.getProperty("jdbc.driver.class"));
            // Création de la connection
            String  url = props.getProperty("jdbc.url");
            String  login = props.getProperty("jdbc.login");
            String  password = props.getProperty("jdbc.password");
            // création de la connexion
            connection = DriverManager.getConnection(url, login, password);

            System.out.println("Connected to database : " + connection);
        }
    }

    public static Connection getInstanceDB() throws SQLException, IOException, ClassNotFoundException {

        try {
            if (getConnection() == null || getConnection().isClosed()) {

                new dbConnection();
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
