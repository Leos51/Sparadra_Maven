package fr.sparadrap.ecf.model.purchase;

import fr.sparadrap.ecf.model.lists.medicine.MedicineList;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Purchase {
    private Integer id;
    private LocalDate purchaseDate ;
    private Integer customerID;
    private boolean isPrescriptionBased;
    private Prescription prescription;
    private List<CartItem> purchasedMedicines;



    public Purchase() {

        this.setPurchaseDate(LocalDate.now());
        this.customerID = 0;
        this.setPrescriptionBased(false);

        this.purchasedMedicines = new ArrayList<>();
    }

    public Purchase(int customerID) {


    }
    public Purchase(int customerID, boolean isPrescriptionBased) {

        this.setCustomer(customerID);
        this.setPurchaseDate(LocalDate.now());
        this.setPrescriptionBased(isPrescriptionBased);
        this.purchasedMedicines = new ArrayList<>();
    }

    public Purchase(LocalDate purchaseDate , int customerID, boolean isPrescriptionBased) {
        this.setCustomer(customerID);
        this.setPurchaseDate(purchaseDate);
        this.setPrescriptionBased(isPrescriptionBased);
        this.purchasedMedicines = new ArrayList<>();

    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Prescription getPrescription() {
        return prescription;
    }



    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }


    public int getCustomerID() {
        return this.customerID;
    }

    public void setCustomer(int customerID) {
        this.customerID = customerID;
    }



    public void  setPurchasedMedicines(List<CartItem> medicines) {
        purchasedMedicines = medicines;
    }

    public  List<CartItem> getMedicines() {
        return purchasedMedicines;
    }

    public boolean isPrescriptionBased() {
        return isPrescriptionBased;
    }

    public void setPrescriptionBased(boolean isPrescriptionBased) {
        this.isPrescriptionBased = isPrescriptionBased;
    }

    public double getTotal(){
        double total = 0;
        if(!this.getMedicines().isEmpty()){
            total = this.getMedicines().stream().mapToDouble(CartItem::getLinePrice).sum();
        }
        return total;
    }




    /**
     * Ajoute un medicament et sa quantité dans la liste d'achat
     * @param medicine Nom du medicament
     * @param quantity Quantité de medicament acheté par le client
     */
    public void addMedicine(Medicine medicine, int quantity, double price) {
        this.purchasedMedicines.add(new CartItem(medicine,  quantity, price));
        medicine.reduceStock(quantity);
    }

    public void reducePurchasedMedicine(Medicine medicine, int quantity) {

    }


    /**
     * Supprime un médicament de la liste des medicaments
     * @param medicine
     */
    public void removePurchasedMedicine(MedicineList medicine) {
        purchasedMedicines.remove(medicine);
    }



    public String showDetails() {
        StringBuilder details = new StringBuilder();
        details.append(this);
        for(CartItem p : this.purchasedMedicines) {
            details.append(p.toString());
        }
        details.append("\n");
        return details.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Date d'achat : ").append(this.purchaseDate);
        sb.append(", Client : ").append(customerID);
        return sb.toString();
    }
}
