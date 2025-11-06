package fr.sparadrap.ecf.controller.person;

import fr.sparadrap.ecf.model.lists.person.DoctorList;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.exception.SaisieException;

public class DoctorController {

    public static void seedDoctorData() throws SaisieException {
        Doctor medecin1 = new Doctor("Docteur", "Smith", "789 boulevard St-Michel",
                "75005", "Paris", "0147258369",
                "dr.smith@hopital.fr", "52548298913");
        DoctorList.addDoctor(medecin1);
        DoctorList.addDoctor(new Doctor("Nimous", "Ano","Adress 1", "51000", "Sanru","0123456987","ano@nimous.fr","12345678913"));
        DoctorList.addDoctor(new Doctor("Doc", "Abi","10 Parru", "52000", "Parus","0123456997","ano@daim.fr","12345678076"));
    }

    public static void displayDoctors(){
        System.out.println("Liste des medecins");
        System.out.println("--------------");
        for(Doctor doctor : DoctorList.getDoctors()){
            System.out.println(doctor);
        }
        System.out.println("--------------");
    }
    public static void displayDetailDoctor(Doctor doctor){
        System.out.println(doctor.showDetails());
    }

}
