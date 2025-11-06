package fr.sparadrah.ecf.model.lists.person;

import fr.sparadrah.ecf.model.person.MutualInsurance;

import java.util.ArrayList;
import java.util.List;

public class MutualInsuranceList {
    private static List<MutualInsurance> mutualinsuranceList = new ArrayList<>();



    public static List<MutualInsurance> getMutualInsuranceList() {
        return mutualinsuranceList;
    }


    /**
     * ajoute une mutuelle a la liste des mutuelles
     * @param mutualInsurance
     */
    public static void addInsuranceCompany(MutualInsurance mutualInsurance) {
        getMutualInsuranceList().add(mutualInsurance);
    }

    /**
     * Supprime une mutuelle de la liste
     * @param mutualInsurance
     */
    public void removeInsuranceCompany(MutualInsurance mutualInsurance) {
        getMutualInsuranceList().remove(mutualInsurance);
    }

    /**
     * recherche une mutuelle à partir de son nom
     * @param mutualInsuranceName
     * @return
     */
    public static MutualInsurance findMutualInsuranceByName(String mutualInsuranceName) {
        for (MutualInsurance mutualInsurance : mutualinsuranceList) {
            if(mutualInsuranceName.equalsIgnoreCase(mutualInsurance.getCompagnyName())){
                return mutualInsurance;
            }
        }
        return null;
    }

}
