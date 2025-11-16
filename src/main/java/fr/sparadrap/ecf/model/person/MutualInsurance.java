package fr.sparadrap.ecf.model.person;

import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.utils.validator.Validator;

public class MutualInsurance {
    private Integer id;
    private String compagnyName;
    private String address;
    private String postCode;
    private String city;
    private String phone;
    private String email;
    private String departementName;
    private double reimbursementRate;



    public MutualInsurance(String compagnyName, String departementName, double reimbursementRate, String address, String postalCode, String city, String phone, String email) throws SaisieException {
        this.setCompagnyName(compagnyName);
        this.setAddress(address);
        this.setPostCode(postalCode);
        this.setCity(city);
        this.setPhone(phone);
        this.setEmail(email);
        this.setDepartementName(departementName);
        this.setReimbursementRate(reimbursementRate);
    }


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * mets a jour le nom de la mutuelle
     * @param compagnyName
     * @throws SaisieException
     */
    public void setCompagnyName(String compagnyName) throws SaisieException {
        if(compagnyName == null || compagnyName.isEmpty()){
            throw new SaisieException("Le nom de la mutuelle ne doit pas etre vide");
        }

        this.compagnyName = compagnyName;
    }

    /**
     * Recupere l"adresse de la mutuelle
     * @return
     */
    public String getAddress() {
        return address;
    }

    /***
     * met a jour l'adresse de la mutuelle
     * @param address
     */
    public void setAddress(String address) {
        if(address.contains(" ")) {
            this.address = address.substring(0, address.indexOf(" "));
        }
        this.address = address;
    }

    /**
     * recupere le code postal de la mutuelle
     * @return
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * mets a jour le code postal de la mutuelle
     * @param postCode
     * @throws SaisieException
     */
    public void setPostCode(String postCode) throws SaisieException {
        if(!Validator.isValidPostalCode(postCode)){
            throw new SaisieException("Code postal non valide");
        }
        this.postCode = postCode;
    }

    /**
     * recupere la vile de la mutuelle
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     * mets a jour la ville de la mutuelle
     * @param city
     * @throws SaisieException
     */
    public void setCity(String city) throws SaisieException {
        if(city == null || city.isEmpty()){
            throw new SaisieException("Ville non valide");
        }
        this.city = city;
    }

    /**
     * recupere le numero de telephone de la mutuelle
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * mets a jour le numero de telephone de la mutuelle
     * @param phone
     * @throws SaisieException
     */
    public void setPhone(String phone) throws SaisieException {
        if(!Validator.isValidPhone(phone)){
            throw new SaisieException("Numero de telephone invalide");
        }
        this.phone = phone;
    }

    /**
     * recupere le mail de la mutuelle
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * mets a jour le mail de la mutuelle
     * @param email
     * @throws SaisieException
     */
    public void setEmail(String email) throws SaisieException {
        if(!Validator.isValidEmail(email)) {
            throw new SaisieException("Email invalide");
        }
        this.email = email;
    }

    /**
     * Mets a jour le nom du departement de la mutuelle
     * @param departementName
     * @throws SaisieException
     */
    public void setDepartementName(String departementName) throws SaisieException {
        if(departementName == null || departementName.isEmpty()){
            throw new SaisieException("Le departement ne doit pas etre vide !");
        }
        this.departementName = departementName;
    }

    /**
     * mets a jour le taux de remboursement de la mutuelle
     * @param reimbursementRate
     * @throws SaisieException
     */
    public void setReimbursementRate(double reimbursementRate) throws SaisieException {
        if(!(reimbursementRate >= 0 && reimbursementRate <= 1)){
            throw new SaisieException("Taux de remboursement invalide");
        }
        this.reimbursementRate = reimbursementRate;
    }

    /**
     * recupere le nom de la mutuelle
     */
    public String getCompagnyName() {
        return compagnyName;
    }

    /**
     * recuprere le departement de la mutuelle
     * @return
     */
    public String getDepartementName() {
        return departementName;
    }

    /**
     * recupere le taux de rembousement de la mutuelle
     * @return
     */
    public double getReimbursementRate() {
        return reimbursementRate;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MutualInsurance{");
        sb.append("compagnyName='").append(compagnyName).append('\'');
        sb.append(", reimbursementRate=").append(reimbursementRate);
        sb.append('}');
        return sb.toString();
    }
}
