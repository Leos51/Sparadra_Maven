package fr.sparadrap.ecf.view.swingview.customer;

import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.person.DoctorList;
import fr.sparadrap.ecf.model.lists.person.MutualInsuranceList;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.model.person.MutualInsurance;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerFormPanel extends JFrame {
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
        $$$setupUI$$$();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setContentPane(formPanel);
        this.setTitle(
                mode == CustomersPanel.FormModes.ADD ?
                        "Nouveau client" :
                        "Modification du client"
        );


        if (mode == CustomersPanel.FormModes.EDIT && currentCustomer != null) {
            //remplissage du formulaire avec client selectionné
            populateFields(currentCustomer);
        }


        cancelButton.addActionListener(e -> {
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
     *
     * @param customer client selectionné
     * @param mode     FormMode.ADD ou FormModes.EDIT
     * @throws SaisieException
     */
    private void submitForm(Customer customer, CustomersPanel.FormModes mode) throws SaisieException {
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
            customer = new Customer(lastName, firstName, address, postCode, city, phone, email, nir, birthDate, mutualInsurance, doctor);
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
            customer.setDoctorByLicenseNumber(doctorNir);
            customer.setMutualInsurance(mutualInsurance);
            JOptionPane.showMessageDialog(this, "Client mis à jour !");
        }
        repaint();
        revalidate();
        this.dispose();


    }


    /**
     * Remplis le formulaiure avec les donnée du client en parametre
     *
     * @param c Le client
     */
    private void populateFields(Customer c) {
        titleLabel.setText("Modifier le client");
        submitButton.setText("Valider la modification");

        lastNameField.setText(c.getLastName());
        firstNameField.setText(c.getFirstName());
        addressField.setText(c.getAddress());
        postCodeField.setText(c.getPostCode());
        cityField.setText(c.getCity());
        phoneField.setText(c.getPhone());
        emailField.setText(c.getEmail());
        birthDateField.setText(DateFormat.formatDate(c.getBirthDate(), "dd/MM/yyyy"));
        nirField.setText(c.getNir());
        if (c.getMutualInsurance() != null) {
            mutualField.setText(c.getMutualInsurance().getCompagnyName());
        }
        if (c.getDoctor() != null) {
            doctorField.setText(c.getDoctor().getRpps());
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout(0, 0));
        formPanel.setLayout(new GridBagLayout());
        editPanel.add(formPanel, BorderLayout.CENTER);
        lastNameLabel = new JLabel();
        lastNameLabel.setText("Nom");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lastNameLabel, gbc);
        lastNameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lastNameField, gbc);
        addressLabel = new JLabel();
        addressLabel.setText("Adresse");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(addressLabel, gbc);
        addressField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(addressField, gbc);
        firstNameLabel = new JLabel();
        firstNameLabel.setText("Prenom");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(firstNameLabel, gbc);
        firstNameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(firstNameField, gbc);
        postCodeLabel = new JLabel();
        postCodeLabel.setText("Code Postal");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(postCodeLabel, gbc);
        cityLabel = new JLabel();
        cityLabel.setText("Ville");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(cityLabel, gbc);
        postCodeField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(postCodeField, gbc);
        cityField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cityField, gbc);
        phoneLabel = new JLabel();
        phoneLabel.setText("Téléphone");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(phoneLabel, gbc);
        emailLabel = new JLabel();
        emailLabel.setText("Email");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailLabel, gbc);
        phoneField = new JTextField();
        phoneField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(phoneField, gbc);
        emailField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        mutualInsurranceLabel = new JLabel();
        mutualInsurranceLabel.setText("Mutuelle");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(mutualInsurranceLabel, gbc);
        doctorLabel = new JLabel();
        doctorLabel.setText("Medecin");
        doctorLabel.setToolTipText("N° agreement");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(doctorLabel, gbc);
        cancelButton = new JButton();
        cancelButton.setText("Annuler");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cancelButton, gbc);
        submitButton = new JButton();
        submitButton.setText("Ajouter");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 13;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(submitButton, gbc);
        doctorField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(doctorField, gbc);
        mutualField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(mutualField, gbc);
        birthDateField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        formPanel.add(birthDateField, gbc);
        birthDateLabel = new JLabel();
        birthDateLabel.setText("Date de naissance");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        formPanel.add(birthDateLabel, gbc);
        nirField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(nirField, gbc);
        nirLabel = new JLabel();
        nirLabel.setText("Numero securité Sociale");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nirLabel, gbc);
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setHorizontalTextPosition(10);
        titleLabel.setText("Ajouter un client");
        editPanel.add(titleLabel, BorderLayout.CENTER);
        lastNameLabel.setLabelFor(lastNameField);
        addressLabel.setLabelFor(addressField);
        firstNameLabel.setLabelFor(firstNameField);
        postCodeLabel.setLabelFor(postCodeField);
        cityLabel.setLabelFor(cityField);
        phoneLabel.setLabelFor(phoneField);
        emailLabel.setLabelFor(emailField);
        mutualInsurranceLabel.setLabelFor(mutualField);
        doctorLabel.setLabelFor(doctorField);
        birthDateLabel.setLabelFor(birthDateField);
        nirLabel.setLabelFor(nirField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return editPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
