package fr.sparadrap.ecf.database.test;

import java.sql.*;

// ============================================
// TEST ULTRA SIMPLE - Connexion MySQL
// ============================================

public class SimpleConnectionTest {
    
    // Configuration à adapter selon votre installation
    private final String PATHCONF = "conf.properties";

    private static final String URL = "jdbc:mysql://localhost:3306/sparadrah_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Mettre votre mot de passe ici
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  TEST DE CONNEXION MYSQL - SPARADRAH   ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        // Test 1: Charger le driver
        testDriver();
        
        // Test 2: Connexion à la base
        testConnection();
        
        // Test 3: Lire des données
        testReadData();
        
        // Test 4: Statistiques
        testStatistics();
    }
    
    /**
     * Test 1: Vérifier que le driver MySQL est disponible
     */
    private static void testDriver() {
        System.out.println("📦 Test 1: Chargement du driver MySQL...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("   ✅ Driver MySQL chargé avec succès!\n");
        } catch (ClassNotFoundException e) {
            System.out.println("   ❌ Driver MySQL non trouvé!");
            System.out.println("   💡 Solution: Ajouter mysql-connector-java dans pom.xml\n");
            System.exit(1);
        }
    }
    
    /**
     * Test 2: Tester la connexion à la base de données
     */
    private static void testConnection() {
        System.out.println("🔌 Test 2: Connexion à la base de données...");
        System.out.println("   URL: " + URL);
        System.out.println("   User: " + USER);
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("   ✅ Connexion établie avec succès!");
                
                // Informations sur la base de données
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("   📊 Informations:");
                System.out.println("      - Produit: " + meta.getDatabaseProductName());
                System.out.println("      - Version: " + meta.getDatabaseProductVersion());
                System.out.println("      - Driver: " + meta.getDriverName());
                System.out.println();
            }
            
        } catch (SQLException e) {
            System.out.println("   ❌ Échec de la connexion!");
            System.out.println("   Erreur: " + e.getMessage());
            System.out.println("\n   💡 Solutions possibles:");
            System.out.println("      1. Vérifier que MySQL est démarré");
            System.out.println("      2. Vérifier l'URL de connexion");
            System.out.println("      3. Vérifier le nom d'utilisateur et le mot de passe");
            System.out.println("      4. Vérifier que la base 'sparadrah_db' existe\n");
            System.exit(1);
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Test 3: Lire des données depuis les tables
     */
    private static void testReadData() {
        System.out.println("📖 Test 3: Lecture des données...");
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Test: Lire les tables disponibles
            System.out.println("   📋 Tables disponibles:");
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables("sparadrah_db", null, "%", new String[]{"TABLE"});
            
            int tableCount = 0;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("      ✓ " + tableName);
                tableCount++;
            }
            
            if (tableCount == 0) {
                System.out.println("      ⚠️  Aucune table trouvée!");
                System.out.println("      💡 Exécutez le script SQL de création de la base\n");
                return;
            }
            
            System.out.println("   ✅ " + tableCount + " tables trouvées\n");
            
            // Test: Compter les enregistrements dans chaque table
            System.out.println("   📊 Nombre d'enregistrements:");
            countRecords(conn, "customers", "Clients");
            countRecords(conn, "doctors", "Médecins");
            countRecords(conn, "medicines", "Médicaments");
            countRecords(conn, "categories", "Catégories");
            countRecords(conn, "mutual_insurances", "Mutuelles");
            countRecords(conn, "purchases", "Achats");
            countRecords(conn, "purchase_items", "Lignes d'achat");
            System.out.println();
            
        } catch (SQLException e) {
            System.out.println("   ❌ Erreur lors de la lecture: " + e.getMessage() + "\n");
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Test 4: Afficher quelques statistiques
     */
    private static void testStatistics() {
        System.out.println("📈 Test 4: Statistiques de la base...");
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            
            // Exemple de clients
            System.out.println("   👥 Exemples de clients:");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT last_name, first_name, city FROM customers LIMIT 3"
            );
            
            while (rs.next()) {
                System.out.printf("      • %s %s (%s)%n", 
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("city")
                );
            }
            rs.close();
            System.out.println();
            
            // Exemple de médicaments
            System.out.println("   💊 Exemples de médicaments:");
            rs = stmt.executeQuery(
                "SELECT medicine_name, price, stock FROM medicines LIMIT 3"
            );
            
            while (rs.next()) {
                System.out.printf("      • %s - %.2f€ (Stock: %d)%n",
                    rs.getString("medicine_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock")
                );
            }
            rs.close();
            System.out.println();
            
            // Statistiques globales
            System.out.println("   📊 Statistiques globales:");
            
            // Total des achats
            rs = stmt.executeQuery("SELECT COUNT(*) as total, SUM(final_amount) as ca FROM purchases");
            if (rs.next()) {
                System.out.printf("      • Nombre d'achats: %d%n", rs.getInt("total"));
                System.out.printf("      • Chiffre d'affaires: %.2f€%n", rs.getDouble("ca"));
            }
            rs.close();
            
            // Date du plus ancien achat
            rs = stmt.executeQuery("SELECT MIN(purchase_date) as oldest FROM purchases");
            if (rs.next()) {
                Timestamp oldest = rs.getTimestamp("oldest");
                if (oldest != null) {
                    System.out.printf("      • Premier achat: %s%n", oldest);
                }
            }
            rs.close();
            
            stmt.close();
            System.out.println();
            
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║     ✅ TOUS LES TESTS RÉUSSIS! ✅     ║");
            System.out.println("╚════════════════════════════════════════╝");
            
        } catch (SQLException e) {
            System.out.println("   ⚠️  Erreur lors des statistiques: " + e.getMessage() + "\n");
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Utilitaire: Compter les enregistrements dans une table
     */
    private static void countRecords(Connection conn, String tableName, String displayName) {
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM " + tableName);
            
            if (rs.next()) {
                int count = rs.getInt("count");
                System.out.printf("      • %-20s : %d%n", displayName, count);
            }
            
            rs.close();
            stmt.close();
            
        } catch (SQLException e) {
            System.out.printf("      • %-20s : Erreur%n", displayName);
        }
    }
    
    /**
     * Utilitaire: Fermer proprement une connexion
     */
    private static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {

            }
        }
    }
}
