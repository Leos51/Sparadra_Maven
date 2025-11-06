package fr.sparadrah.ecf.controller.medecine;


import fr.sparadrah.ecf.model.lists.medicine.CategoriesList;
import fr.sparadrah.ecf.model.medicine.Category;
import fr.sparadrah.ecf.utils.exception.SaisieException;

public class CategoriesController {
    public static void seedCategoriesData(){
        try {
            CategoriesList.addCategory(new Category("Analgésiques"));
            CategoriesList.addCategory(new Category("Antiviraux"));
            CategoriesList.addCategory(new Category("Antibiotiques"));
            CategoriesList.addCategory(new Category("Anti-inflammatoires"));
        }catch(SaisieException e){
            System.out.println("Erreur d'initialisation des categories : " + e.getMessage());
        }


    }


}
