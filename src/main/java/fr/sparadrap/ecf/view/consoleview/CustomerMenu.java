package fr.sparadrah.ecf.view.consoleview;

import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.model.person.MutualInsurance;
import fr.sparadrah.ecf.utils.UserInput;
import fr.sparadrah.ecf.utils.exception.SaisieException;

import static fr.sparadrah.ecf.utils.UserInput.exitApp;

public class CustomerMenu {
    public static void detailCustomerMenu(){
        while(true){
            System.out.println("------------------------------");
            System.out.println("Afficher les detail d'un client ?");
            System.out.println("1 - Choisir un client par numSecu");
            System.out.println("0 - Retour au menu principal");
            System.out.println("Q - Quitter");
            choiceDetailCustomerMenu();
        }
    }

    public static void choiceDetailCustomerMenu(){

            String userChoice = UserInput.getStringValue("Votre Choix : ").toUpperCase().trim();
            switch (userChoice) {
                case "1" -> {
                    String r = UserInput.getStringValue("Saisir le N° de secu : ");
                    Customer c = CustomersList.findByNir(r);
                    if(c == null){
                        System.err.println("Le numero n'existe pas");
                    }
                    else{
                        System.out.println(c.showDetails());
                        updataOrDeleteCustomerDetail(c);
                    }
                }
                case "0" -> MainMenu.display();
                case "Q" -> exitApp();
                default -> System.err.println("Choix invalide! Réessayez");
            }
    }

    public static void updataOrDeleteCustomerDetail(Customer c){
        while(true){
            System.out.println("-----------------------------");
            System.out.println("1 - Modification du client");
            System.out.println("2 - Supprimer le client");
            System.out.println("0 - Retour");
            System.out.println("Q - Quitter");
            String userChoice = UserInput.getStringValue("Votre Choix : ");
            switch (userChoice) {
                case "1" -> {
                    try{
                        updateCustomerDetail(c);
                    }catch(Exception e){
                    System.err.println("Erreur : " + e.getMessage());
                    }

                }
                case "2" -> deleteCustomerDetail(c);
                case "0" -> detailCustomerMenu();
                case "Q" -> exitApp();
                default -> System.err.println("Choix invalide! Ressayez");
            }
        }
    }

    public static void deleteCustomerDetail(Customer c){
        String response;
        do{
            response = UserInput.getStringValue("Suppression du client? Vraimment ? (o/n)").trim().toLowerCase();
        }while(!response.equals("o") && !response.equals("n") );
        if(response.equals("o")){
            CustomersList.getCustomers().remove(c);
            Customer a = CustomersList.findByNir(c.getNir());
            if(a == null){
                System.err.println("Client supprimé");
            }else{
                System.err.println("Erreur de suppression du client");
            }
            MainMenu.display();
        }else{
            System.err.println("Suppression du client annulé");
        }
    }


    public static void updateCustomerDetail(Customer c) throws SaisieException {
        while(true){
            System.out.println("------------------------------");
            System.out.println("Champ a modifier :");
            System.out.println("1 - Nom");
            System.out.println("2 - Prenom");
            System.out.println("3 - Adresse");
            System.out.println("4 - Téléphone");
            System.out.println("5 - Email");
            System.out.println("6 - Mututelle");
            System.out.println("7 - Medecin traitant");
            System.out.println("0 - Retour");
            System.out.println("Q - Quitter");
            String userChoice = UserInput.getStringValue("Votre Choix : ");
            switch (userChoice) {
                case "1" -> {
                    System.out.println("Nom : " + c.getLastName());
                    String lastName = UserInput.getStringValue("Nouveau nom : ");
                    c.setLastName(lastName);
                }
                case "2" -> {
                    System.out.println("Prenom : " + c.getFirstName());
                    String firstName = UserInput.getStringValue("Nouveau prenom : ");
                    c.setFirstName(firstName);
                }
                case "3" -> {
                    System.out.println("Adresse : " + c.getAddress());
                    String address = UserInput.getStringValue("Nouvelle adresse : ");
                    c.setAddress(address);
                }
                case "4" -> {
                    System.out.println("Téléphone : " + c.getPhone());
                    String phone = UserInput.getStringValue("Nouveau numero : ");
                    c.setPhone(phone);
                }
                case "5" -> {
                    System.out.println("Email : " + c.getEmail());
                    String email = UserInput.getStringValue("Nouvel email : ");
                    c.setEmail(email);
                }
                case "6" -> {
                    System.out.println("Mutuelle : " + c.getMutualInsurance());
                    String mutualInsurance = UserInput.getStringValue("Nouvelle mutuelle : ");
                    MutualInsurance selectedMutualInsurance = MutualInsuranceList.findMutualInsuranceByName( mutualInsurance );
                    if(selectedMutualInsurance == null){
                        System.err.println("La mutuelle n'est pas enregistré");
                    }
                    c.setMutualInsurance(selectedMutualInsurance);
                }
                case "7" -> {
                    System.out.println("Medecin referent : " + c.getDoctor());
                    String doctor = UserInput.getStringValue("Nouveau medecin : ");
                    Doctor selectedDoctor = DoctorList.findDoctorByLicenseNumber( doctor );
                    if(selectedDoctor == null){
                        System.err.println("Le medecin n'existe pas");
                    }
                    assert selectedDoctor != null;
                    c.setDoctorByLicenseNumber(selectedDoctor.getRpps());
                }
                case "0" -> detailCustomerMenu();
                case "Q" -> exitApp();
                default -> System.err.println("Choix incorrect! Réessayez.");
            }
        }
    }
}
