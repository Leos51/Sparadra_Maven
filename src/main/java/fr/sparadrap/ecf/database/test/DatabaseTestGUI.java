package fr.sparadrap.ecf.database.test;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DatabaseTestGUI extends JFrame {

    private JTextField urlField;
    private JTextField userField;
    private JPasswordField passwordField;
    private JTextArea resultArea;
    private JButton testButton;
    private JProgressBar progressBar;

    public DatabaseTestGUI() {
        setTitle("Test de Connexion MySQL - Sparadrah");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panneau de configuration
        JPanel configPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        configPanel.setBorder(BorderFactory.createTitledBorder("Configuration"));

        configPanel.add(new JLabel("URL:"));
        urlField = new JTextField("jdbc:mysql://localhost:3306/sparadrah_db");
        configPanel.add(urlField);

        configPanel.add(new JLabel("Utilisateur:"));
        userField = new JTextField("root");
        configPanel.add(userField);

        configPanel.add(new JLabel("Mot de passe:"));
        passwordField = new JPasswordField();
        configPanel.add(passwordField);

        testButton = new JButton("🔍 Tester la Connexion");
        testButton.setFont(new Font("Arial", Font.BOLD, 14));
        testButton.setBackground(new Color(76, 175, 80));
        testButton.setForeground(Color.WHITE);
        testButton.addActionListener(e -> testConnection());
        configPanel.add(new JLabel()); // Spacer
        configPanel.add(testButton);

        mainPanel.add(configPanel, BorderLayout.NORTH);

        // Zone de résultats
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(30, 30, 30));
        resultArea.setForeground(new Color(0, 255, 0));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Résultats"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Barre de progression
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(false);
        progressBar.setVisible(false);
        mainPanel.add(progressBar, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void testConnection() {
        testButton.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        resultArea.setText("Démarrage des tests...\n");

        // Exécuter le test dans un thread séparé
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                String url = urlField.getText();
                String user = userField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Test 1: Driver
                    publish("\n📦 Test du driver MySQL...\n");
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    publish("   ✅ Driver chargé avec succès!\n");

                    // Test 2: Connexion
                    publish("\n🔌 Test de connexion...\n");
                    publish("   URL: " + url + "\n");
                    publish("   User: " + user + "\n");

                    Connection conn = DriverManager.getConnection(url, user, password);

                    if (conn != null && !conn.isClosed()) {
                        publish("   ✅ CONNEXION RÉUSSIE!\n");

                        DatabaseMetaData meta = conn.getMetaData();
                        publish("\n📊 Informations:\n");
                        publish("   • Produit: " + meta.getDatabaseProductName() + "\n");
                        publish("   • Version: " + meta.getDatabaseProductVersion() + "\n");

                        // Test 3: Tables
                        publish("\n📋 Tables disponibles:\n");
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SHOW TABLES");
                        int count = 0;
                        while (rs.next()) {
                            publish("   ✓ " + rs.getString(1) + "\n");
                            count++;
                        }
                        publish("   Total: " + count + " tables\n");

                        // Test 4: Données
                        publish("\n📊 Statistiques:\n");
                        rs = stmt.executeQuery("SELECT COUNT(*) FROM customers");
                        if (rs.next()) {
                            publish("   • Clients: " + rs.getInt(1) + "\n");
                        }

                        rs = stmt.executeQuery("SELECT COUNT(*) FROM medicines");
                        if (rs.next()) {
                            publish("   • Médicaments: " + rs.getInt(1) + "\n");
                        }

                        rs = stmt.executeQuery("SELECT COUNT(*) FROM purchases");
                        if (rs.next()) {
                            publish("   • Achats: " + rs.getInt(1) + "\n");
                        }

                        rs.close();
                        stmt.close();
                        conn.close();

                        publish("\n╔════════════════════════════════════╗\n");
                        publish("║   ✅ TOUS LES TESTS RÉUSSIS! ✅   ║\n");
                        publish("╚════════════════════════════════════╝\n");
                    }

                } catch (ClassNotFoundException e) {
                    publish("\n❌ ERREUR: Driver MySQL non trouvé!\n");
                    publish("💡 Ajoutez mysql-connector-java dans pom.xml\n");

                } catch (SQLException e) {
                    publish("\n❌ ERREUR DE CONNEXION!\n");
                    publish("Message: " + e.getMessage() + "\n");
                    publish("\n💡 Solutions:\n");
                    publish("   1. Vérifiez que MySQL est démarré\n");
                    publish("   2. Vérifiez l'URL, user et password\n");
                    publish("   3. Vérifiez que la base existe\n");
                }

                return null;
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String chunk : chunks) {
                    resultArea.append(chunk);
                }
                resultArea.setCaretPosition(resultArea.getDocument().getLength());
            }

            @Override
            protected void done() {
                progressBar.setVisible(false);
                testButton.setEnabled(true);
            }
        };

        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DatabaseTestGUI gui = new DatabaseTestGUI();
            gui.setVisible(true);
        });
    }
}
