package fr.sparadrap.ecf.model.person;

import fr.sparadrap.ecf.utils.RegexPatterns;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.utils.validator.Validator;

public class Doctor extends Person {
    private Integer id;
    private String rpps;

    public Doctor(){
        super();
    }

    public Doctor(String lastName, String firstName, String address, String postalCode, String city, String phone, String email, String rpps) throws SaisieException {
        super(lastName, firstName, address, postalCode, city, phone, email);
        this.setRpps(rpps);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    /**
     * recupere le n°d'agréement du médecin
     * @return
     */
    public String getRpps() {
        return rpps;
    }

    /**
     * Mets a jour le numero d'agréement du médecin
     * @param rpps
     * @throws SaisieException
     */
    public void setRpps(String rpps) throws SaisieException {
        if (rpps == null) {
            throw new SaisieException("Le numero d'agréement ne peut pas etre vide ou null");
        }
        if (!rpps.matches(RegexPatterns.RPPS_REGEX)) {
            throw new SaisieException("Numero d'agréement invalide");
        }

        this.rpps = rpps;
    }




    @Override
    public String showDetails() {
        StringBuilder details = new StringBuilder();
        details.append(this);
        details.append("\n------------------");
        details.append(super.showDetails());
        return details.toString();
    }

    @Override
    public String toString() {
        return super.toString() + " - N° agréement : " + this.rpps;
    }
}
