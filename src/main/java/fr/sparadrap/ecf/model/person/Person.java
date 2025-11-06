package fr.sparadrap.ecf.model.person;

import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.utils.validator.Validator;

import static fr.sparadrap.ecf.utils.UserInput.capitalize;

public class Person {
    private String firstName;
    private String lastName;
    private String address;
    private String postCode;
    private String city;
    private String phone;
    private String email;

    public Person() {

    }

    public Person(String lastName, String firstName, String address, String postCode, String city, String phone, String email) throws SaisieException {
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setAddress(address);
        this.setPostCode(postCode);
        this.setCity(city);
        this.setPhone(phone);
        this.setEmail(email);

    }


    /**
     * recupere le prénom de cette personne
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Mets a jour le prénom de cette personne
     * @param firstName
     */
    public void setFirstName(String firstName) {
        firstName = firstName.trim();
        this.firstName = capitalize(firstName);
    }

    /**
     * Recupere le nom de cette personne
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Mets a jour le nom
     * @param lastName
     */
    public void setLastName(String lastName) {
        lastName = lastName.trim();
        this.lastName = capitalize(lastName);
    }

    /**
     * recupere l'adresse
     * @return
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Mets a jour l'adresse
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * recupere le code postal
     * @return
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Mets a jour le code postal
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
     * Recupere la ville
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     * Mets a jour la ville
     * @param city
     */
    public void setCity(String city) {;
        this.city = city;
    }

    /**
     * recupere le numero de téléphone
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * mets a jour le numero de téléphone
     * @param phone
     */
    public void setPhone(String phone) {

        this.phone = phone;
    }

    /**
     * recupere le mail
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * Mets a jour le mail
     * @param email
     * @throws SaisieException
     */
    public void setEmail(String email) throws SaisieException {
        email = email
                .trim()
                .toLowerCase();
        if(!Validator.isValidEmail(email)) {
            throw new SaisieException("Email invalide");
        }
        this.email = email;
    }


    /**
     * renvoie la concatenation avec prénom et nom
     * @return
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }



    public String showDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAdresse: " + this.getAddress());
        sb.append("\nCode Postal: " + this.getPostCode());
        sb.append("\nVille: " + this.getCity());
        sb.append("\nTéléphone: " + this.getPhone());
        sb.append("\nEmail: " + this.getEmail());
        return sb.toString();
    }


    @Override
    public String toString() {
        return this.getFullName();
    }
}
