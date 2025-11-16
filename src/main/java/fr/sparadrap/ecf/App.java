package fr.sparadrap.ecf;


import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrap.ecf.view.consoleview.MainMenu;
import fr.sparadrap.ecf.view.swingview.MainFrame;
import fr.sparadrap.ecf.view.swingview.customer.CustomersPanel;

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
    public static void main(String[] args) {

        System.out.println("Hello World!");
        App main = new App();
        main.run();
    }
    private void run(){
        URL url = CustomersPanel.class.getResource("/fr/sparadrap/ecf/view/swingview/ressources/ajouter.png");
        System.out.println("URL image : " + url);
        //initData();
        //askConsoleOrSwing();
        startSwing();
        //startConsole();
    }
    private void startConsole(){
        MainMenu.display();
    }

    private void startSwing(){
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }

    private void initData() {
        try{
            seedCategoriesData();
            seedMedicationData();
            seedMutualInsuranceData();
            seedDoctorData();
            seedPrecriptionData();
            seedPurchaseData();
            System.out.println("Nombre client : " + CustomersList.getCustomers().size());
            System.out.println("Nombre mutuelle : " + MutualInsuranceList.getMutualInsuranceList().size());
        }catch(Exception e){
            System.err.println("Erreur d'initialisation des données : " + e.getMessage());
        }
    }



}
