package fr.sparadrap.ecf.controller.purchase;

import fr.sparadrap.ecf.database.dao.CustomerDAO;
import fr.sparadrap.ecf.database.dao.PurchaseDAO;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.view.consoleview.purchase.PurchaseHistoryMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class PurchaseController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    /**
     * Initialisation d'achats
     */
    public static void seedPurchaseData() {
        /*
        CustomerDAO customerDAO = new CustomerDAO();
        Customer c = customerDAO.findById(2);

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
        System.out.println("price m1: " + p1.getMedicines().stream().findFirst().get().getPrice());*/
    }


    /**
     * Affiche tous les achats depuis la base de données
     */
    public static void displayAllPurchases() {
        try (PurchaseDAO purchaseDAO = new PurchaseDAO()) {
            List<Purchase> purchases = purchaseDAO.findAll();
            for (Purchase p : purchases) {
                System.out.println(p);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de l'affichage des achats: {}", e.getMessage(), e);
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

    public static double calculateReimbursement(Purchase purchase, double totalPrice) throws SQLException, IOException, ClassNotFoundException {
        if (purchase == null || !purchase.isPrescriptionBased()) {
            return 0.0;
        }

        try (CustomerDAO customerDAO = new CustomerDAO()) {
            Customer customer = customerDAO.findById(purchase.getCustomerID());

            if (customer == null) {
                logger.warn("Client non trouvé pour l'achat ID: {}", purchase.getId());
                return 0.0;
            }

            if (customer.getMutualInsurance() == null) {
                logger.info("Pas de mutuelle pour le client: {}", customer.getFullName());
                return 0.0;
            }

            double reimbursementRate = customer.getMutualInsurance().getReimbursementRate();
            double reimbursement = totalPrice * reimbursementRate;

            logger.debug("Remboursement calculé: {} (taux: {}%)", reimbursement, reimbursementRate * 100);
            return reimbursement;

        } catch (SQLException | IOException | ClassNotFoundException e) {
            logger.error("Erreur lors du calcul du remboursement: {}", e.getMessage(), e);
            return 0.0;
        }
    }

    /**
     * Calcule le total d'un achat
     * @param purchase L'achat
     * @return Le total
     */
    public static double calculateTotal(Purchase purchase){
        if (purchase == null || purchase.getMedicines() == null) {
            return 0.0;
        }

        double total = purchase.getMedicines().stream().mapToDouble(item -> {
           double medPrice =  item.getMedicine().getPrice();
           int quantity = item.getQuantity();
           return medPrice * quantity;

        }).sum();
        return total;
    };

    /**
     * Affiche le détail des lignes d'un achat (ticket de caisse)
     * @param purchase L'achat
     */
    public static void printLineReceipt(Purchase purchase){
        if (purchase == null || purchase.getMedicines() == null) {
            System.out.println("Aucun article");
            return;
        }
        purchase.getMedicines().forEach(item -> {
            String medName = item.getMedicine().getMedicineName();
            double medPrice = item.getMedicine().getPrice();
            int quantity = item.getQuantity();
            double lineTotal = medPrice * quantity;
            System.out.println(medName + " " + medPrice + " x " + quantity  + " " + lineTotal);
        });
    }












}
