package fr.sparadrah.ecf.controller.purchase;

import fr.sparadrah.ecf.model.lists.purchase.PurchasesList;
import fr.sparadrah.ecf.model.medicine.Medicine;
import fr.sparadrah.ecf.model.purchase.Purchase;
import fr.sparadrah.ecf.model.lists.medicine.MedicineList;
import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.utils.DateFormat;
import fr.sparadrah.ecf.view.consoleview.purchase.PurchaseHistoryMenu;

import java.time.LocalDate;


public class PurchaseController {

    /**
     * Initialisation d'achats
     */
    public static void seedPurchaseData() {

        Customer c = CustomersList.findByNir("1885621486527");
        Purchase p1 = new Purchase(c, true);
        Purchase p2 = new Purchase(DateFormat.parseDateFromString("18/10/2022"),c, false);
        p1.setCustomer(c);
        p2.setCustomer(c);

        Medicine m1 = MedicineList.findMedicineByName("Advil");
        Medicine m2 =  MedicineList.findMedicineByName("Doliprane");
        Medicine m3 =  MedicineList.findMedicineByName("Ibuprofen");
        Medicine m4 =  MedicineList.findMedicineByName("Doliprane");

        p1.addMedicine(m1, 20, m1.getPrice() );
        p1.addMedicine(m2, 5,  m2.getPrice() );
        p1.addMedicine(m3, 2,   m3.getPrice() );
        p2.addMedicine(m4, 1,   m4.getPrice() );
        PurchasesList.addPurchase(p1);
        PurchasesList.addPurchase(p2);
        System.out.println("price m1: " + p1.getMedicines().stream().findFirst().get().getPrice());
    }


    /**
     * Affiche les achats effectué
     */
    public static void displayAllPurchases() {
        for (Purchase p : PurchasesList.getPurchases()) {
            System.out.println(p);
        }
    }

    /**
     * Ajoute un titre au dessus de l affichage des achat
     */
    public static void displayPurchaseData() {
        System.out.println("Historique des achats");
        System.out.println("---------------------");
        displayAllPurchases();
        PurchaseHistoryMenu.displayPurchaseHistoryMenu();
    }

    public static double calculateReimbursement(Purchase p, double totalPrice){

        double reimbursementRate = p.getCustomer().getMutualInsurance().getReimbursementRate();
        double reimbursement = totalPrice * reimbursementRate;
        return reimbursement;
    }

    public static double calculateTotal(Purchase p){
        double total = p.getMedicines().stream().mapToDouble(item -> {
           double medPrice =  item.getMedicine().getPrice();
           int quantity = item.getQuantity();
           return medPrice * quantity;

        }).sum();
        return total;
    };

    public static void printLineReceipt(Purchase p){
        p.getMedicines().forEach(item -> {
            String medName = item.getMedicine().getMedicineName();
            double medPrice = item.getMedicine().getPrice();
            int quantity = item.getQuantity();
            double lineTotal = medPrice * quantity;
            System.out.println(medName + " " + medPrice + " x " + quantity  + " " + lineTotal);
        });
    }












}
