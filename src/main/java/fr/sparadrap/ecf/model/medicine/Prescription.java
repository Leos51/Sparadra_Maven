package fr.sparadrap.ecf.model.medicine;



import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import java.time.LocalDate;

import java.util.List;

public class Prescription {
    private LocalDate prescriptingDate;
    private Doctor doctor;
    private Customer customer;
    private List<Medicine> prescriptedMedicines;


    public Prescription(String prescriptingDate, Doctor doctor, Customer customer, List<Medicine> medicines) throws SaisieException {
        setPrescriptingDate(prescriptingDate);
        setDoctor(doctor);
        setCustomer(customer);
        setPrescriptedMedicines(medicines);
    }

    /**
     * Recupere la date de l"ordonnance
     * @return
     */
    public LocalDate getPrescriptingDate() {

        return prescriptingDate;
    }

    /**
     * Recupere le medecin associé à l'ordonnance
     * @return
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Recupere le patient associé à l'ordonnance
     * @return
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Recupere la liste des médicament prescrit
     * @return
     */
    public List<Medicine> getPrescriptedMedicines() {
        return prescriptedMedicines;
    }

    /**
     * ajoute des médicmament à l'ordonnance
     * @param medicine
     */
    public void addMedicine(Medicine medicine) {
        if(medicine == null){
            throw new IllegalArgumentException("Le medicament ne peut pas etre null");
        }
        if(getPrescriptedMedicines().contains(medicine)){
            System.err.println("Medicament deja present dans la liste");
            return;
        }
        this.getPrescriptedMedicines().add(medicine);
    }

    /**
     * Retire un médicament de l'ordonnance
     * @param medicine
     * @throws SaisieException
     */
    public void removeMedicine(Medicine medicine) throws SaisieException {
        if(medicine == null){
            throw new IllegalArgumentException("Le medicament ne peut pas etre null");
        }
        if(!this.prescriptedMedicines.contains(medicine)){
            throw new SaisieException("Le medicament n'est pas dans la liste");
        }
        this.prescriptedMedicines.remove(medicine);
    }


    /**
     * Mets a jour la date de prescription
     * @param prescriptingDate
     * @throws SaisieException
     */
    public void setPrescriptingDate(String prescriptingDate) throws SaisieException {
        if(prescriptingDate == null || prescriptingDate.isEmpty()){
            throw new SaisieException("La date de prescription ne doit pas etre null ou vide !");
        }
        this.prescriptingDate = DateFormat.parseDateFromString(prescriptingDate);
    }

    /**
     * Met a jour le medecin associé à l'ordonnance
     * @param doctor
     * @throws SaisieException
     */
    public void setDoctor(Doctor doctor) throws SaisieException {
        if(doctor == null){
            throw new SaisieException("Le medecin ne peut pas etre null");
        }
        this.doctor = doctor;
    }

    /**
     * Mets a jour le patient associé à l'ordonnance
     * @param customer
     * @throws SaisieException
     */
    public void setCustomer(Customer customer) throws SaisieException {
        if(customer == null){
            throw new SaisieException("Le client ne doit pas etre null");
        }
        this.customer = customer;
    }

    /**
     * Mets a jour la liste de médicament
     * @param medicines
     * @throws SaisieException
     */
    public void setPrescriptedMedicines(List<Medicine> medicines) throws SaisieException {
        if(medicines == null){
            throw new IllegalArgumentException("La liste des medicament ne peut pas etre null");
        }
        if(medicines.isEmpty()){
            throw new SaisieException("La liste de medicaments ne doit pas etre vide");
        }
        this.prescriptedMedicines = medicines;
    }





    @Override
    public String toString() {
        return "date prescription : " + getPrescriptingDate() + " - Customer : " + getCustomer();
    }
}
