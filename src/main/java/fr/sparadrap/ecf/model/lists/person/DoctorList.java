package fr.sparadrap.ecf.model.lists.person;

import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorList {
    private static List<Doctor> doctors = new ArrayList<>();


    public static List<Doctor> getDoctors() {
        return doctors;
    }


    /**
     * Filtre les médecins par rapport au mot en parametre - le filtrage s'effectue  sur :
     * - nom
     * - prenom
     * - n° d'agréement
     * - ville
     * @param pSearch
     * @return une liste de médecins filtré
     */
    public static List<Doctor> filterDoctor(String pSearch) {
        String search = pSearch.toLowerCase().trim();
        List<Doctor> filteredDoctors = getDoctors().stream()
                .filter(doctor ->
                        doctor.getFullName().toLowerCase().contains(search) ||
                                doctor.getRpps().contains(search) ||
                                doctor.getEmail().contains(search) ||
                                doctor.getCity().toLowerCase().contains(search)

                ).toList();
        return filteredDoctors;
    }

    public void setDoctors(List<Doctor> doctors) {

        this.doctors = doctors;
    }

    /**
     * ajoute un medecin a la liste de medecin existante
     * @param doctor
     */
    public static void addDoctor(Doctor doctor) {
        getDoctors().add(doctor);
    }

    /**
     * Supprime un medecin de la liste des medecins
     * @param doctor
     */
    public static void removeDoctor(Doctor doctor) {
        doctors.remove(doctor);
    }

    /**
     * Recherche un medecin à partir de son numéro d'agreéement
     * @param rpps
     * @return un Medecin si correspondance trouve
     */
    public static Doctor findDoctorByLicenseNumber(String rpps) {
        for (Doctor d : doctors) {
            if(rpps.equals(d.getRpps())){
                return d;
            };
        }
        return null;
    }

    /**
     * recherche un medecin par son nom complet
     * @param fullName
     * @return doctor
     */
    public static Doctor findByFullName(String fullName) {
        String search = fullName.toLowerCase().trim();
        for(Doctor doctor : getDoctors()) {
            if(doctor.getFullName().toLowerCase().equalsIgnoreCase(search)) {
                return doctor;
            }
        }
        return null;
    };


}
