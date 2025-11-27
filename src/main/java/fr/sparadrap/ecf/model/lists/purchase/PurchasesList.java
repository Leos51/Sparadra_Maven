package fr.sparadrap.ecf.model.lists.purchase;

import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.utils.DateFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PurchasesList {
    private static List<Purchase> purchases = new ArrayList<>();

    public static List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * Ajoute un achat a la liste d'achat
     * @param purchase Achat effectué
     */
    public static void addPurchase(Purchase purchase){
        purchases.add(purchase);
    }

    /**
     * Supprime un achat de la liste
     * @param purchase
     */
    public void removePurchase(Purchase purchase){
        purchases.remove(purchase);
    }


    /**
     * Liste les achats par CLient
     * @param customerNir Numero de securité social
     * @return  une liste des achat effectué par un client
     */
    public static List<Purchase> findPurchaseByCustomerNir(String customerNir){
        List<Purchase> customerPurchases = new ArrayList<>();
      /*  for (Purchase purchase : purchases) {
            if(customerNir.equalsIgnoreCase(purchase.getCustomerID().toString)){
                customerPurchases.add(purchase);
            }
        }*/
        return  customerPurchases;
    }

    /**
     * Liste les achats effectué à une date precise
     * @param date  date au format : "dd/MM/aaaa"
     * @return liste d'achat
     */
    public static List<Purchase> findPurchaseByDate(String date){
        LocalDate formatedDate = DateFormat.parseDateFromString(date);
        assert formatedDate != null;
        List<Purchase> customerPurchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if(formatedDate.equals(purchase.getPurchaseDate())){
                customerPurchases.add(purchase);
            }
        }
        return  customerPurchases;
    }

    /**
     * Liste des achats effectué le jour meme
     * @return liste d'achat
     */
    public static List<Purchase> findPurchaseOfDay(){
        List<Purchase> customerPurchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if(purchase.getPurchaseDate().isEqual(LocalDate.now())){
                customerPurchases.add(purchase);
            }
        }
        return  customerPurchases;
    }

    /**
     * liste les achats effecué pendant une periode donnée
     * @param startDate
     * @param endDate
     * @return liste d'achat
     */
    public static List<Purchase> findPurchasebyPeriod(String startDate, String endDate){
        LocalDate formatedStartDate = DateFormat.parseDateFromString(startDate);
        LocalDate formatedEndDate = DateFormat.parseDateFromString(endDate);
        assert formatedStartDate != null;
        assert formatedEndDate != null;
        List<Purchase> customerPurchases = new ArrayList<>();
        for (Purchase purchase : purchases) {
            if(
                    formatedStartDate.minusDays(1).isBefore(purchase.getPurchaseDate())
                    && formatedEndDate.plusDays(1).isAfter(purchase.getPurchaseDate())
            ){
                customerPurchases.add(purchase);
            }
        }
        return  customerPurchases;
    }

    /**
     * Recherche la facture dont la date est la plus ancienne et recupere sa date
     * @return date
     */
    public static LocalDate getOldestPurchaseDate() {
        return purchases.stream()
                .map(Purchase::getPurchaseDate).min(Comparator.naturalOrder()).get();
    }
}
