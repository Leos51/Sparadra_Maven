package fr.sparadrap.ecf.database.test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;

public class QuickTest {
    private static final Properties props = new Properties();
    private static Connection connection;

    private static final String PATHCONF = "conf.properties";

    public static void main(String[] args) {
        System.out.println("test");
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(PATHCONF);) {
            if (inputStream == null) {
                throw new RuntimeException("❌ Fichier conf.properties introuvable dans le classpath !");
            }

            props.load(inputStream);



        } catch (Exception e) {
            System.out.println("❌ ERREUR: " + e.getMessage());
        }

        try{
            Class.forName(props.getProperty("jdbc.driver.class"));

            String url = props.getProperty("jdbc.url");
            String user = props.getProperty("jdbc.login");
            String password = props.getProperty("jdbc.password");

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("✅ CONNEXION OK! " + connection);

            ResultSet rs = connection.createStatement().executeQuery("SELECT COUNT(*) FROM customers");
            if (rs.next()) {
                System.out.println("👥 Nombre de clients: " + rs.getInt(1));
            }

            connection.close();
        }catch(Exception e){
            System.out.println("❌ ERREUR 2: " + e.getMessage());
        }
    }
}
