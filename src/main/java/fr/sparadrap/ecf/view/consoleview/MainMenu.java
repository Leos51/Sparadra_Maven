package fr.sparadrah.ecf.view.consoleview;

import fr.sparadrah.ecf.controller.person.CustomerController;
import fr.sparadrah.ecf.controller.person.DoctorController;
import fr.sparadrah.ecf.controller.purchase.PurchaseController;

import fr.sparadrah.ecf.utils.UserInput;
import fr.sparadrah.ecf.view.consoleview.purchase.PurchaseMenu;

import static fr.sparadrah.ecf.utils.UserInput.exitApp;


/**
 * affiche le menu principal
 */
public class MainMenu {
    public static void display(){
        while(true){
            System.out.println("---------------------------------");
            System.out.println("| \uD83E\uDE79 Sparadrah - Menu Principal |");
            System.out.println("---------------------------------");
            System.out.println("1 - Effectuer un achat");
            System.out.println("2 - Consulter l'historique d'achat");
            System.out.println("3 - Consulter les medecins");
            System.out.println("4 - Consulter les clients");
            System.out.println("Q - Quitter");
            System.out.println("----------------");

            String userChoice = UserInput.getStringValue("Votre Choix : ").trim().toUpperCase();
            switch (userChoice) {
                case "1" -> PurchaseMenu.display();
                case "2" -> PurchaseController.displayPurchaseData();
                case "3" -> {
                    DoctorController.displayDoctors();
                    DoctorMenu.detailDoctorMenu();
                }
                case "4" -> {
                    CustomerController.displayCustomersData();
                    CustomerMenu.detailCustomerMenu();
                }
                case "Q" -> exitApp();
                default -> System.err.println("Choix invalide! Réessayez");
            }
        }
    }
}


