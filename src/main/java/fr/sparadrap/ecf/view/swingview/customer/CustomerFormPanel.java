package fr.sparadrah.ecf.view.swingview.customer;

import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.model.person.MutualInsurance;
import fr.sparadrah.ecf.utils.DateFormat;
import fr.sparadrah.ecf.utils.exception.SaisieException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerFormPanel extends  JFrame {
    private JPanel editPanel;
    private JPanel formPanel;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JTextField addressField;
    private JTextField postCodeField;
    private JTextField cityField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField birthDateField;
    private JButton submitButton;
    private JButton cancelButton;
    private JLabel titleLabel;
    private JLabel lastNameLabel;
    private JLabel addressLabel;
    private JLabel firstNameLabel;
    private JLabel postCodeLabel;
    private JLabel cityLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel birthDateLabel;
    private JLabel mutualInsurranceLabel;
    private JLabel doctorLabel;
    private JTextField doctorField;
    private JTextField mutualField;
    private JTextField nirField;
    private JLabel nirLabel;
    private Customer customer;




    public CustomerFormPanel(Customer currentCustomer, CustomersPanel.FormModes mode) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(formPanel);
        this.setTitle(
                mode == CustomersPanel.FormModes.ADD?
                        "Nouveau client":
                        "Modification du client"
        );



        if(mode == CustomersPanel.FormModes.EDIT && currentCustomer != null) {
            //remplissage du formulaire avec client selectionné
            populateFields(currentCustomer);
        }



        cancelButton.addActionListener( e ->{
           this.dispose();
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    submitForm(currentCustomer, mode);

                } catch (SaisieException exception) {
                    JOptionPane.showMessageDialog(CustomerFormPanel.this, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }


            }
        });
    }


    /**
     * Validation du formulaire avec comportement different si nouveau client ou client existant
     * @param customer client selectionné
     * @param mode  FormMode.ADD ou FormModes.EDIT
     * @throws SaisieException
     */
    private void submitForm(Customer customer ,CustomersPanel.FormModes mode) throws SaisieException {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String address = addressField.getText().trim();
        String postCode = postCodeField.getText().trim();
        String city = cityField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String birthDate = birthDateField.getText().trim();
        String nir = nirField.getText().trim();
        String mutualInsuranceName = mutualField.getText().trim();
        MutualInsurance mutualInsurance = MutualInsuranceList.findMutualInsuranceByName(mutualInsuranceName);
        String doctorNir = doctorField.getText().trim();
        Doctor doctor = DoctorList.findDoctorByLicenseNumber(doctorNir);

            if (lastName.isEmpty() || firstName.isEmpty() || address.isEmpty() || postCode.isEmpty() || city.isEmpty() || phone.isEmpty() || email.isEmpty() || birthDate.isEmpty() || nir.isEmpty()) {
                throw new SaisieException("Champs obligatoires manquants.");
            }


            if (mode == CustomersPanel.FormModes.ADD) {
                customer = new Customer(lastName,firstName,address, postCode,city, phone, email, nir, birthDate, mutualInsurance, doctor);
                CustomersList.addCustomer(customer);



                JOptionPane.showMessageDialog(this, "Client ajouté !");
            } else {

                customer.setLastName(lastName);
                customer.setFirstName(firstName);
                customer.setAddress(address);
                customer.setPostCode(postCode);
                customer.setCity(city);
                customer.setPhone(phone);
                customer.setEmail(email);
                customer.setNir(nir);
                customer.setBirthDate(DateFormat.parseDateFromString(birthDate));
                customer.setDoctorByLicenseNumber( doctorNir);
                customer.setMutualInsurance(mutualInsurance);
                JOptionPane.showMessageDialog(this, "Client mis à jour !");
            }
            repaint();
            revalidate();
            this.dispose();


    }


    /**
     * Remplis le formulaiure avec les donnée du client en parametre
     * @param c Le client
     */
    private void populateFields(Customer c){
        titleLabel.setText("Modifier le client");
        submitButton.setText("Valider la modification");

        lastNameField.setText(c.getLastName());
        firstNameField.setText(c.getFirstName());
        addressField.setText(c.getAddress());
        postCodeField.setText(c.getPostCode());
        cityField.setText(c.getCity());
        phoneField.setText(c.getPhone());
        emailField.setText(c.getEmail());
        birthDateField.setText(DateFormat.formatDate(c.getBirthDate(),  "dd/MM/yyyy"));
        nirField.setText(c.getNir());
        if (c.getMutualInsurance() != null) {
            mutualField.setText(c.getMutualInsurance().getCompagnyName());
        }
        if (c.getDoctor() != null) {
            doctorField.setText(c.getDoctor().getRpps());
        }
    }

}
