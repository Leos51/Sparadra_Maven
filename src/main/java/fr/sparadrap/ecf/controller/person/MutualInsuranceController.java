package fr.sparadrah.ecf.controller.person;

import fr.sparadrah.ecf.model.person.MutualInsurance;
import fr.sparadrah.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrah.ecf.utils.exception.SaisieException;

public class MutualInsuranceController {
    public static void seedMutualInsuranceData() throws SaisieException {
        MutualInsuranceList.addInsuranceCompany(new MutualInsurance("Macif", "Marne", 0.15, "49 rue Verso", "51000", "Chalons", "0325426512", "contact@macif.com"));
        MutualInsuranceList.addInsuranceCompany(new MutualInsurance("Assurtou", "Marne", 0.3, "49 rue Rerso", "51540", "Balons", "0325426513", "contact@massurtou.com"));

    }


}
