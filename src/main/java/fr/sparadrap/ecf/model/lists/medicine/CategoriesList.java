package fr.sparadrap.ecf.model.lists.medicine;

import fr.sparadrap.ecf.model.medicine.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoriesList {
    private static List<Category> categories = new ArrayList<Category>();

    public static List<Category> getCategories() {

        return categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * Ajoute une nouvelle Categorie a la liste categories
     * @param category
     */
    public static void addCategory(Category category) {
        getCategories().add(category);
    }

    /**
     * Supprimer la categorie en parametre
     * @param category
     */
    public void deleteCategory(Category category) {
        this.getCategories().remove(category);
    }

    /**
     * Recherche la categorie existante
     * @param name nom de la categorie
     * @return
     */
    public static Category findCategoryByName(String name){
        for (Category category : categories) {
            if(name.equalsIgnoreCase(category.getCategoryName())){
                return category;
            }
        }
        return null;
    }


}
