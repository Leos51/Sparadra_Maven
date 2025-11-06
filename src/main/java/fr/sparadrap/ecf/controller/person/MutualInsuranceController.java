package fr.sparadrap.ecf.controller.person;

import fr.sparadrap.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrap.ecf.model.person.MutualInsurance;
import fr.sparadrap.ecf.utils.exception.SaisieException;

public class MutualInsuranceController {
    public static void seedMutualInsuranceData() throws SaisieException {
        MutualInsuranceList.addInsuranceCompany(new MutualInsurance("Macif", "Marne", 0.15, "49 rue Verso", "51000", "Chalons", "0325426512", "contact@macif.com"));
        MutualInsuranceList.addInsuranceCompany(new MutualInsurance("Assurtou", "Marne", 0.3, "49 rue Rerso", "51540", "Balons", "0325426513", "contact@massurtou.com"));

    }


}
