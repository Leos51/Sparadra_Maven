package fr.sparadrap.ecf.model.medicine;


import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.utils.exception.StockInsuffisantException;
import fr.sparadrap.ecf.utils.validator.Validator;

import java.time.LocalDate;

public class Medicine {
    private String medicineName;
    private Category category;
    private double price;
    private LocalDate releaseDate;
    private int stockQuantity;
    private int threshold = 10;


    public Medicine(String medicineName, Category category, double price, String releaseDate, int stockQuantity) throws SaisieException {
        this.setMedicineName(medicineName);
        this.setCategory(category);
        this.setPrice(price);
        this.setReleaseDate(releaseDate);
        this.setStock(stockQuantity);
    }

    /**
     * Recupere le nom du médicament
     * @return
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * met a jour le nom du médicament avec le nom en parametre
     * @param medicineName
     * @throws SaisieException
     */
    public void setMedicineName(String medicineName) throws SaisieException {
        if(medicineName == null || medicineName.isEmpty()){
            throw new SaisieException("Le nom du médicament ne doit pas etre vide");
        }
        if (!Validator.isValidName(medicineName)) {
            throw new SaisieException("Nom de medicament invalide");
        }
        this.medicineName = medicineName;
    }

    /**
     * recupere la catégorie
     * @return
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Met a jour la categorie du médicament avec celle en param
     * @param category
     * @throws SaisieException
     */
    public void setCategory(Category category) throws SaisieException {
        if(category == null){
            throw new SaisieException("La categorie ne peut pas etre vide");
        }
        this.category = category;
    }

    /**
     * recupere le prix du médicament
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Mets à jour le prix du médicament
     * @param price
     * @throws SaisieException
     */
    public void setPrice(double price) throws SaisieException {
        if(!(price > 0)){
            throw new SaisieException("Prix invalide");
        }
        this.price = price;
    }

    /**
     * recupere la date de mise sur le marché du médicament
     * @return
     */
    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Mets a jour la date de mise sur le marché du médicament
     * @param releaseDate
     * @throws SaisieException
     */
    public void setReleaseDate(String releaseDate) throws SaisieException {
        if(releaseDate == null || releaseDate.isEmpty()){
            throw new SaisieException("La date de sortie ne peut pas etre vide");
        }
        this.releaseDate = DateFormat.parseDateFromString(releaseDate);
    }

    /**
     * recupere la quantité en stock
     * @return
     */
    public int getStock() {
        return stockQuantity;
    }

    /**
     * Méts à jour le stock avec la quantité en parametre
     * @param quantity
     * @throws SaisieException
     */
    public void setStock(int quantity) throws SaisieException {
        if(!(quantity >= 0)){
            throw new SaisieException("Saisie de la quantité de medicament invalide");
        }
        this.stockQuantity = quantity;
    }

    /**
     * Reduit la quantité en stock de la quantité en parametre :
     * Stock -= Param
     * @param quantityToReduce
     */
    public void reduceStock(int quantityToReduce)  {
        if (quantityToReduce > this.stockQuantity) {
            throw new StockInsuffisantException("Stock insuffisant !");
        }
        this.stockQuantity -= quantityToReduce;
        if(isLowStock(threshold)) {
            System.out.println("Stock bas - A restocker : " + this.getMedicineName());
        }
    }

    /**
     * reduit la quantité en stock d'une unité
     */
    public void reduceQuantityByOne() {
        if (stockQuantity > 0) {
            stockQuantity --;
        }
        if(isLowStock(threshold)) {
            System.out.println("Stock bas - A restocker : " + this.getMedicineName());
        }
    }

    /**
     * ajoute de la quantité en parametre au stock actuel :
     * Stock += Param
     * @param quantity
     */
    public void restock(int quantity) throws SaisieException {
        if (!(quantity > 0)) {
            throw new SaisieException("Saisie de quantité invalide !");
        }

        this.stockQuantity += quantity;
    }


    /**
     * Verifie si le medicament n'est plus en stock
     * @return true/false
     */
    public boolean isOutOfStock(){
        return this.stockQuantity == 0;
    }

    /**
     * verifie si la quantité en stock passe sous le seuil d'alerte(parametre)
     * @param threshold (seuil d'alerte)
     * @return
     */
    public boolean isLowStock(int threshold) {
       return this.getStock() <= threshold ;
    }




    @Override
    public String toString() {
        return "nom : " + medicineName + " " +
                ", Prix : " + price +
                ", Quantité Stock : " + this.getStock() +"\n";
    }
}
