package fr.sparadrap.ecf.view.consoleview.purchase;

import fr.sparadrap.ecf.controller.purchase.PurchaseController;
import fr.sparadrap.ecf.model.lists.medicine.MedicineList;
import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.purchase.PurchasesList;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.utils.UserInput;
import fr.sparadrap.ecf.utils.exception.StockInsuffisantException;
import fr.sparadrap.ecf.view.consoleview.MainMenu;

import static fr.sparadrap.ecf.utils.UserInput.exitApp;

public class PurchaseMenu {

    /**
     * Affiche le menu d'achat
     */
    public static void display() {
        String userChoice;
        while(true){
            System.out.println("-----------------------------");
            System.out.println("1 - Achat direct");
            System.out.println("2 - Achat avec ordonnance");
            System.out.println("0 - Retour au menu principal");
            System.out.println("Q - Quitter l'application");

            userChoice = UserInput.getStringValue("Votre Choix : ").trim().toUpperCase();
            switch (userChoice) {
                case "1" -> createPurchase(false);
                case "2" -> createPurchase(true);
                case "0" -> MainMenu.display();
                case "Q" -> exitApp();
                default -> System.err.println("Choix invalide");
            }
        }
    }

    /**
     * Enregistre un achat de medicament et liste celui-ci dans l'historique
     * @param isPrescriptionBased  permet de modifier le label et d'enclencher une reduction
     */
    private static void createPurchase(Boolean isPrescriptionBased){
        displayPurchaseHeader(isPrescriptionBased); //Header de la zone d'achat


        // Saisie client
        String nir = UserInput.getStringValue("Numéro de sécurité sociale du client : ");
        Customer customer = CustomersList.findByNir(nir);
        if(customer == null){
            System.err.println("Client introuvable");
            return;
        }
        // Création de l’achat
        Purchase p = new Purchase(customer , isPrescriptionBased);

        // Ajout de medicament
        addMedicineToPurchase(p);


        //Ajout de l'achat effectué dasn la liste dans l'historique des achats
        PurchasesList.addPurchase(p);
        System.out.println(" Achat enregistré !");

        printReceipt(p);
    }

    private static void displayPurchaseHeader(Boolean isPrescriptionBased) {
        String label = isPrescriptionBased ?
                "Achat avec ordonnance":
                "Achat sans ordonnance";

        System.out.println("-----------------------------");
        System.out.println("Achat de medicaments - " + label);
        System.out.println("-----------------------------");
    }

    /**
     * Ajoute des medicaments en stock à la liste d'achat
     * @param purchase Achat en cours
     */
    public static void addMedicineToPurchase(Purchase purchase){
        boolean addMore = false;
        do{

            String medicineName = UserInput.getStringValue("Nom du medicament : ");
            Medicine medicine = MedicineList.findMedicineByName(medicineName);
            if(medicine == null){
                System.err.println("Médicament introuvable");
                continue;
            }
            int quantity = UserInput.getIntValue("Quantite de ce medicament :");
            if(!(quantity >= 0)){
                System.err.println("Quantité non valide! Refaire l'entrée du medicament ");
                quantity = 0;
            }
            try{
                purchase.addMedicine(medicine, quantity, medicine.getPrice());


            }catch(StockInsuffisantException e){
                System.err.println(e.getMessage());
            }
            String res = "";
            do{
                res = UserInput.getStringValue("Ajouter un autre medicament? (o/n)").trim();
            }while(
                    !res.equalsIgnoreCase("o") &&
                            !res.equalsIgnoreCase("n") );

            addMore = res.equalsIgnoreCase("o");
        }while(addMore);

    }

    public static void receiptHeader(Purchase purchase){
        String customerName = purchase.getCustomer().getFullName();
        String purchaseDate = purchase.getPurchaseDate().toString();
        String purchaseType = purchase.isPrescriptionBased() ?
                "Avec prescription":
                "Sans prescription";

        System.out.println("============================");
        System.out.println("Ticket de caisse - sparadrap");
        System.out.println("============================");
        System.out.println("Client : " + customerName);
        System.out.println("Date: " + purchaseDate);
        System.out.println("Type d'achat : " + purchaseType);
        System.out.println("----------------------------");
    }





    public static void printReceipt(Purchase purchase){
        double totalPrice = PurchaseController.calculateTotal(purchase);
        boolean isPrescriptionBased = purchase.isPrescriptionBased();
        boolean isMutualInsurance = purchase.getCustomer().getMutualInsurance() != null;

        receiptHeader(purchase);
        System.out.println("__________________________________________________");
        System.out.println("| Designation | Prix unitaire x quantité | Total |");
        System.out.println("--------------------------------------------------");
        PurchaseController.printLineReceipt(purchase);


        System.out.println("Total : " + totalPrice);
        if(isPrescriptionBased && isMutualInsurance){
            double reimbursement = PurchaseController.calculateReimbursement(purchase, totalPrice);
            double totalAfterReimbursement = totalPrice - reimbursement;
            System.out.println("Reduction : " + reimbursement);
            System.out.println("Total apres reduction : " + totalAfterReimbursement);
        }
        System.out.println("____________________________");
    }
}
