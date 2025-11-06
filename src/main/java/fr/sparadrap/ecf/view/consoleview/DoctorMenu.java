package fr.sparadrah.ecf.view.consoleview;

import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.utils.UserInput;
import fr.sparadrah.ecf.utils.exception.SaisieException;

import static fr.sparadrah.ecf.utils.UserInput.exitApp;


public class DoctorMenu {
    public static void detailDoctorMenu(){
        while(true){
            System.out.println("------------------------------");
            System.out.println("Afficher les detail d'un medecin ?");
            System.out.println("1 - Choisir un medecin par num cerf");
            System.out.println("2 - Ajouter un nouveau medecin");
            System.out.println("0 - Retour au menu principal");
            System.out.println("Q - Quitter");
            choiceDetailDoctorMenu();
        }

    }

    public static void choiceDetailDoctorMenu(){
        String userChoice = UserInput.getStringValue("Votre Choix : ").trim().toUpperCase();
        switch (userChoice) {
            case "1" -> {
                String licenseNum = UserInput.getStringValue("Saisir le N° du medecin : ");
                Doctor selectedDoctor = DoctorList.findDoctorByLicenseNumber(licenseNum);
                if(selectedDoctor == null){
                    System.err.println("Le numero n'existe pas");
                }
                else{
                    System.out.println(selectedDoctor.showDetails());
                }
            }
            case "2" -> addNewDoctor();
            case "0" -> MainMenu.display();
            case "Q" -> exitApp();
            default -> System.err.println("Choix invalide! Réessayez");
        }





    }

    /**
     * Enregistre un nouveau medecin dans la liste des medecins
     */
    private static void addNewDoctor(){
        try{

            //Creation de l instance du nouveau medecin
            Doctor newDoctor ;

            // saisie des champs par l utilisateur
            String lastname = UserInput.getStringValue("Nom du medecin : ");
            if(lastname == null || lastname.isEmpty()){
                throw new SaisieException("Le nom du medecin est vide !");
            }
            String firstname = UserInput.getStringValue("Prenom du medecin : ");
            if(firstname == null || firstname.isEmpty()){
                throw new SaisieException("Le prennom du medecin est vide !");
            }
            String address = UserInput.getStringValue("Adresse : ");
            if(address == null || address.isEmpty()){
                throw new SaisieException("Le adresse est vide !");
            }
            String postCode = UserInput.getStringValue("CodePostal : ");
            if(postCode == null || postCode.isEmpty()){
                throw new SaisieException("Le code postal n'est pas valide !");
            }
            String city = UserInput.getStringValue("Ville : ");
            if(city == null || city.isEmpty()){
                throw new SaisieException("Le ville est vide !");
            }
            String phone = UserInput.getStringValue("Telephone : ");
            if(phone == null || phone.isEmpty()){
                throw new SaisieException("Le telephone est vide !");
            }
            String email = UserInput.getStringValue("Email : ");
            if(email == null || email.isEmpty()){
                throw new SaisieException("Le email est vide !");
            }
            String licenseNumber = UserInput.getStringValue("N° agreement : ");
            if(licenseNumber == null || licenseNumber.isEmpty()){
                throw new SaisieException("Erreur saisie N° agreement !");
            }


            //entrée des valeur dans l instance
            newDoctor = new Doctor(lastname, firstname, address, postCode, city, phone, email, licenseNumber);

            // Ajout du medecin dans la liste des medecins
            DoctorList.addDoctor(newDoctor);

        } catch(SaisieException e){
            System.out.println("ezre" + e.getMessage());
        }
    }
}
