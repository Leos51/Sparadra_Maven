package fr.sparadrap.ecf.controller.medecine;

import fr.sparadrap.ecf.model.lists.medicine.CategoriesList;
import fr.sparadrap.ecf.model.lists.medicine.MedicineList;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.utils.exception.SaisieException;

public class MedicationController {
    public static void seedMedicationData() {
        try{

            Category analgesique = CategoriesList.findCategoryByName("Analgésiques");
            Category ai =  CategoriesList.findCategoryByName("anti-inflammatoires");


            // Médicaments fictifs
            Medicine medicine1 = new Medicine("Paracétamol", "analgesique", 5.50,
                    "01/01/2020", 100);

            Medicine doliprane = new Medicine("Doliprane", "analgesique",2.3,"18/10/1982", 200);
            Medicine ibuprofen = new Medicine("Ibuprofen","analgesique",1.9, "03/06/1990", 50);
            Medicine advil = new Medicine("Advil", "analgesique",2.3,"18/10/1982", 200);
            Medicine clamoxyl = new Medicine("Clamoxyl", "analgesique",2.3,"18/10/1982", 150);
            Medicine pfizer = new Medicine("Pfizer", "analgesique",2.3,"18/10/1982", 200);

            MedicineList.addMedicine(medicine1);
            MedicineList.addMedicine(doliprane);
            MedicineList.addMedicine(ibuprofen);
            MedicineList.addMedicine(advil);
            MedicineList.addMedicine(clamoxyl);
            MedicineList.addMedicine(pfizer);

        }catch(SaisieException e){
           System.err.println("Erreur initialisation Medicaments : " + e.getMessage());
        }

    }

    public static void displayInventory() {
        System.out.println("Inventaire des medicaments");
        System.out.println("--------------------------");
        for (Medicine medicine : MedicineList.getMedicines()) {
            System.out.println(medicine.toString());
        }

    }
}
