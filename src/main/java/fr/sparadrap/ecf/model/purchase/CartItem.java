package fr.sparadrap.ecf.model.purchase;

import fr.sparadrap.ecf.model.medicine.Medicine;

public class CartItem {
    private Medicine medicine;
    private int quantity;
    private final double PRICE; //devrait enregistrer le prix au moment de l'achat


    public CartItem(Medicine medicine, int quantity,  double price) {
        this.setMedicine(medicine);
        this.setQuantity(quantity);
        this.PRICE = price;
    }


    /**
     * Met a jour le médicament dans le panier
     * @param medicine
     */
    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }


    /**
     * recupere un medicament
     * @return
     */
    public Medicine getMedicine() {
        return medicine;
    }

    /**
     * Recupere la quantité d'un médicament au panier
     * @return
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Mets a jours la quantité de médicament au panier
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * recupere le prix d'un medicament au panier d'achat
     * @return
     */
    public double getPrice() {
        return this.PRICE;
    }


    /**
     * Calcule le prix d'un medicament en fonction de la quantité de ce medicament au panier
     * ( Prix medicament x quantité au panier )
     * @return
     */
    public double getLinePrice() {
        return this.getPrice() * this.getQuantity();
    }





    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Medicament : ").append(medicine);
        sb.append(", Quantité : ").append(quantity);
        return sb.toString();
    }
}
