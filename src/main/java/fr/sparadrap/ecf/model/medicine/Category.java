package fr.sparadrap.ecf.model.medicine;


import fr.sparadrap.ecf.utils.exception.SaisieException;

public class Category {
    private int id;
    private String categoryName;
    private String description;

    public Category(String categoryName, String description) throws SaisieException {
        this.setCategoryName(categoryName);
    }

    public Category() {

    }


    /**
     * recupere le nom d'une categorie
     * @return
     */
    public String getCategoryName() {

        return categoryName;
    }

    /**
     * Crée une nouvelle categorie
     * @param categoryName
     * @throws SaisieException
     */
    public void setCategoryName(String categoryName) throws SaisieException {
        if (categoryName == null || categoryName.isEmpty()){
            throw new SaisieException("Le nom de la categorie ne peut pas etre vide");
        }
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;


    }
    public void setDescription(String description) throws SaisieException {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.getCategoryName();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

