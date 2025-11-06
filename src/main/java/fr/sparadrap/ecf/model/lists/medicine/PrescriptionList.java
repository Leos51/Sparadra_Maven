package fr.sparadrap.ecf.model.lists.medicine;

import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class PrescriptionList {
    private static List<Prescription> prescriptionList = new ArrayList<>();

    public static List<Prescription> getPrescriptionList(){
        return prescriptionList;
    }

    public static void addPrescription(Prescription p){
        prescriptionList.add(p);
    }

    /**
     * Trouve les ordonnances d'un client
     */
    public static List<Prescription> findPrescriptionsByCustomer(Customer customer) {
        return prescriptionList.stream()
                .filter(p -> p.getCustomer().getNir().equals(customer.getNir()))
                .collect(toList());
    }

    /**
     * Trouve les ordonnances d'un médecin
     * @param doctor Le médecin dont on cherche les ordonnances
     * @return Liste des ordonnances prescrites par ce médecin
     */
    public static List<Prescription> findPrescriptionsByDoctor(Doctor doctor) {
        return prescriptionList.stream()
                .filter(p -> p.getDoctor().getRpps().equals(doctor.getRpps()))
                .collect(Collectors.toList());
    }

    /**
     * Trouve les ordonnances récentes (moins de 3 mois)
     */
    public static List<Prescription> findRecentPrescriptions(Customer customer) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        return findPrescriptionsByCustomer(customer).stream()
                .filter(p -> p.getPrescriptingDate().isAfter(threeMonthsAgo))
                .collect(toList());
    }

    /**
     * Trouve les ordonnances par date
     * @param date Date de prescription au format LocalDate
     * @return Liste des ordonnances de cette date
     */
    public static List<Prescription> findPrescriptionsByDate(LocalDate date) {
        return prescriptionList.stream()
                .filter(p -> p.getPrescriptingDate().equals(date))
                .collect(Collectors.toList());
    }

    /**
     * Supprime une ordonnance de la liste
     * @param prescription L'ordonnance à supprimer
     */
    public static void removePrescription(Prescription prescription) {
        prescriptionList.remove(prescription);
    }

    /**
     * Vérifie si une ordonnance est encore valide (moins de 3 mois)
     * @param prescription L'ordonnance à vérifier
     * @return true si l'ordonnance est encore valide
     */
    public static boolean isPrescriptionValid(Prescription prescription) {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
        return prescription.getPrescriptingDate().isAfter(threeMonthsAgo);
    }



}
