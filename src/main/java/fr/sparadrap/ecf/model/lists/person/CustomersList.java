package fr.sparadrah.ecf.model.lists.person;

import fr.sparadrah.ecf.model.person.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomersList {
    private static List<Customer> customers =  new ArrayList<>();

    public static List<Customer> getCustomers() {
        return customers;
    }

    public static void addCustomer(Customer customer) {
        getCustomers().add(customer);
    }

    public static void removeCustomer(Customer customer) {
        getCustomers().remove(customer);
    }

    /**
     * Recherche un client par son n° de securité social
     * @param nir (n° de securité sociale)
     * @return le client dont le numero correspond
     */
    public static Customer findByNir(String nir) {
        return customers
                .stream()
                .filter(c -> c.getNir().equals(nir))
                .findFirst()
                .orElse(null);
    }

    /**
     * Filtre les client par rapport au mot en parametre - le filtrage s'effectue  sur :
     * - nom
     * - prenom
     * - numero de securité social
     * - ville
     * @param pSearch
     * @return une liste de clients
     */
    public static List<Customer> filterCustomers(String pSearch) {
        String search = pSearch.toLowerCase().trim();
        List<Customer> filteredCustomers = getCustomers().stream()
                .filter(customer ->
                                customer.getFullName().toLowerCase().contains(search) ||
                                        customer.getNir().contains(search) ||
                                        customer.getEmail().contains(search) ||
                                        customer.getCity().toLowerCase().contains(search)

                        ).toList();
        return filteredCustomers;
    };

    /**
     * recherche un client a partir de son nom complet accompagné de son prenom : ("nom prenom")
     * @param fullName
     * @return
     */
    public static Customer findByFullName(String fullName) {
        String search = fullName.toLowerCase().trim();
        for(Customer customer : getCustomers()) {
            if(customer.getFullName().toLowerCase().equalsIgnoreCase(search)) {
                return customer;
            }
        }
        return null;
    };


}
