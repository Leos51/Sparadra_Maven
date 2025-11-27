package fr.sparadrap.ecf;


import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrap.ecf.view.consoleview.MainMenu;
import fr.sparadrap.ecf.view.swingview.MainFrame;
import fr.sparadrap.ecf.view.swingview.customer.CustomersPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.net.URL;

import static fr.sparadrap.ecf.controller.medecine.CategoriesController.seedCategoriesData;
import static fr.sparadrap.ecf.controller.medecine.MedicationController.seedMedicationData;
import static fr.sparadrap.ecf.controller.medecine.PrescriptionController.seedPrecriptionData;
import static fr.sparadrap.ecf.controller.person.DoctorController.seedDoctorData;
import static fr.sparadrap.ecf.controller.person.MutualInsuranceController.seedMutualInsuranceData;
import static fr.sparadrap.ecf.controller.purchase.PurchaseController.seedPurchaseData;

/**
 * Hello world!
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
    private void run(){

        try{
            // Initialisation des données (si nécessaire)
            //initData();
            // Test de connexion à la base de données
            testDatabaseConnection();

            //askConsoleOrSwing();

            // Démarrage de l'interface Swing
            startSwing();
            //startConsole();
        }catch(Exception e){
            logger.error("❌ Erreur fatale lors du démarrage de l'application", e);
            showErrorDialog("Erreur de démarrage",
                    "Impossible de démarrer l'application : " + e.getMessage());
            System.exit(1);
        }


    }
    private void startConsole(){
        MainMenu.display();
    }

    private void startSwing(){
        logger.info("Démarrage de l'interface graphique...");
        SwingUtilities.invokeLater(() -> {
            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                logger.info("✅ Interface graphique démarrée");
            } catch (Exception e) {
                logger.error("❌ Erreur lors du démarrage de l'interface", e);
                showErrorDialog("Erreur UI", "Erreur lors du démarrage de l'interface : " + e.getMessage());
            }
        });
    }

    private void initData() {
        logger.info("Initialisation des données...");
        try{
            seedCategoriesData();
            seedMedicationData();
            seedMutualInsuranceData();
            seedDoctorData();
            seedPrecriptionData();
            seedPurchaseData();
            logger.info("✅ Données initialisées");
        }catch(Exception e){
            logger.error("❌ Erreur lors de l'initialisation des données", e);
        }
    }

    private void showErrorDialog(String title, String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    title,
                    JOptionPane.ERROR_MESSAGE
            );
        });
    }

    /**
     * Teste la connexion à la base de données
     */
    private void testDatabaseConnection() {
        try {
            logger.info("Test de connexion à la base de données...");
            DatabaseConnection.getConnection().close();
            logger.info("✅ Connexion à la base de données réussie");
        } catch (Exception e) {
            logger.error("❌ Échec de connexion à la base de données", e);
            throw new RuntimeException("Impossible de se connecter à la base de données", e);
        }
    }

}
