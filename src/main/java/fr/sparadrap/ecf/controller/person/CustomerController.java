package fr.sparadrah.ecf.controller.person;

import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.model.person.MutualInsurance;
import fr.sparadrah.ecf.utils.exception.SaisieException;
import fr.sparadrah.ecf.view.consoleview.MainMenu;

public class CustomerController {

    public static void seedCustomersData() throws SaisieException {

        Doctor selectedDoctor1 = DoctorList.findDoctorByLicenseNumber("12345678913");
        Doctor selectedDoctor2 = DoctorList.findDoctorByLicenseNumber("12345678076");



        MutualInsurance selectedMutual1 = MutualInsuranceList.getMutualInsuranceList().getFirst();
        MutualInsurance selectedMutual2 = MutualInsuranceList.getMutualInsuranceList().getLast();

        Customer Customer1 = new Customer("Martin", "Jean", "123 rue de la Paix",
                "75001", "Paris", "0123456789",
                "jean.martin@email.com", "1234567890123",
                "14/05/1980", selectedMutual1, selectedDoctor2);

        Customer Customer2 = new Customer("Durand", "Marie", "456 avenue des Champs",
                "75008", "Paris", "0987654321",
                "marie.durand@email.com", "2167267890123",
                    "22/05/1991", selectedMutual2, selectedDoctor1);


        CustomersList.addCustomer(Customer1);
        CustomersList.addCustomer(Customer2);
        CustomersList.addCustomer(new Customer("Recto", "Verso", "3 rue Maurice de Broglie", "51000", "Chalons en Champagne", " 03 26 68 03 00", "rectoverso@gmail.com", "1885621486527", "18/01/1988" , selectedMutual1, selectedDoctor1));
        CustomersList.addCustomer(new Customer("Lunch", "Happy", "10 rue des hauts", "51510", "Soudail", " 03 26 68 02 00", "happylunch@gmail.com", "1885621486522", "18/01/1782" , selectedMutual2, selectedDoctor2 ));
    }

    public static void displayCustomersData(){
        System.out.println("Liste des Clients:");
        System.out.println("--------------------");
//        for(Customer customer : CustomersList.getCustomers()){
//            System.out.println(customer.toString() );
//        }
        CustomersList.getCustomers().forEach(System.out::println);
    }
}
