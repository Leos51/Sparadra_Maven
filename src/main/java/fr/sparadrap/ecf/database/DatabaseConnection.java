package fr.sparadrap.ecf.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String PATHCONF = "conf.properties";
    private static final HikariDataSource dataSource;

    static {
        try {
            dataSource = initDataSource();
        } catch (IOException e) {
            throw new RuntimeException("❌ Erreur lors de l'initialisation du pool HikariCP", e);
        }
    }

    private DatabaseConnection() {}

    private static HikariDataSource initDataSource() throws IOException {
        Properties props = new Properties();
        try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream(PATHCONF)) {
            props.load(is);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName(props.getProperty("jdbc.driver.class"));
            config.setJdbcUrl(props.getProperty("jdbc.url"));
            config.setUsername(props.getProperty("jdbc.login"));
            config.setPassword(props.getProperty("jdbc.password"));

            // Options recommandées
            config.setMaximumPoolSize(50);
            config.setMinimumIdle(5);
            config.setIdleTimeout(300000);
            config.setConnectionTimeout(300000);
            config.setMaxLifetime(1800000);

            return new HikariDataSource(config);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("✅ Pool HikariCP fermé proprement");
            System.out.println("hikari fermé");

        }
    }
}