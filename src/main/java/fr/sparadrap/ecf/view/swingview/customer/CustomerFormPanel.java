package fr.sparadrap.ecf.view.swingview.customer;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import fr.sparadrap.ecf.database.dao.CustomerDAO;
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
import java.io.IOException;
import java.sql.SQLException;

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
        // Vérifier que formPanel est initialisé
        if (editPanel == null) {
            editPanel = new JPanel(new BorderLayout());
            System.out.println("edit null");
        }
        this.setContentPane(editPanel);
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

                } catch (SaisieException | SQLException | IOException | ClassNotFoundException exception) {
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
    private void submitForm(Customer customer, CustomersPanel.FormModes mode) throws SaisieException, SQLException, IOException, ClassNotFoundException {
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

        CustomerDAO customerDAO = new CustomerDAO();
        if (mode == CustomersPanel.FormModes.ADD) {
            customer = new Customer(lastName, firstName, address, postCode, city, phone, email, nir, birthDate, mutualInsurance, doctor);
            customerDAO.create(customer);
            JOptionPane.showMessageDialog(this, "Client ajouté !");
        } else {
            customer.setLastName(lastName);
            customer.setFirstName(firstName);
            customer.setNir(nir);
            customer.setAddress(address);
            customer.setPostCode(postCode);
            customer.setCity(city);
            customer.setPhone(phone);
            customer.setEmail(email);
            customer.setNir(nir);
            customer.setBirthDate(DateFormat.parseDateFromString(birthDate));
            customer.setDoctorByLicenseNumber(doctorNir);
            customer.setMutualInsurance(mutualInsurance);
            customerDAO.update(customer);
            JOptionPane.showMessageDialog(this, "Client mis à jour !");
        }
        repaint();
        revalidate();
        this.dispose();


    }

    private void populateCustomerFromFields(Customer currentCustomer) {

    }


    /**
     * Remplis le formulaire avec les donnée du client en parametre
     *
     * @param c Le client
     */
    private void populateFields(Customer c) {
        System.out.println(c.toString());
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        editPanel = new JPanel();
        editPanel.setLayout(new BorderLayout(0, 0));
        editPanel.setEnabled(true);
        formPanel = new JPanel();
        formPanel.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow", "center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:max(d;4px):noGrow,top:4dlu:noGrow,center:d:grow,top:4dlu:noGrow,center:max(d;4px):noGrow"));
        formPanel.setEnabled(true);
        editPanel.add(formPanel, BorderLayout.CENTER);
        lastNameLabel = new JLabel();
        lastNameLabel.setText("Nom");
        CellConstraints cc = new CellConstraints();
        formPanel.add(lastNameLabel, cc.xy(1, 1));
        lastNameField = new JTextField();
        formPanel.add(lastNameField, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        addressLabel = new JLabel();
        addressLabel.setText("Adresse");
        formPanel.add(addressLabel, cc.xy(1, 5));
        addressField = new JTextField();
        formPanel.add(addressField, cc.xyw(1, 7, 2, CellConstraints.FILL, CellConstraints.DEFAULT));
        firstNameLabel = new JLabel();
        firstNameLabel.setText("Prenom");
        formPanel.add(firstNameLabel, cc.xy(3, 1));
        firstNameField = new JTextField();
        formPanel.add(firstNameField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        postCodeLabel = new JLabel();
        postCodeLabel.setText("Code Postal");
        formPanel.add(postCodeLabel, cc.xy(1, 9));
        cityLabel = new JLabel();
        cityLabel.setText("Ville");
        formPanel.add(cityLabel, cc.xy(3, 9));
        postCodeField = new JTextField();
        formPanel.add(postCodeField, cc.xy(1, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        cityField = new JTextField();
        formPanel.add(cityField, cc.xy(3, 11, CellConstraints.FILL, CellConstraints.DEFAULT));
        phoneLabel = new JLabel();
        phoneLabel.setText("Téléphone");
        formPanel.add(phoneLabel, cc.xy(1, 13));
        emailLabel = new JLabel();
        emailLabel.setText("Email");
        formPanel.add(emailLabel, cc.xy(3, 13));
        phoneField = new JTextField();
        phoneField.setText("");
        formPanel.add(phoneField, cc.xy(1, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        emailField = new JTextField();
        formPanel.add(emailField, cc.xy(3, 15, CellConstraints.FILL, CellConstraints.DEFAULT));
        mutualInsurranceLabel = new JLabel();
        mutualInsurranceLabel.setText("Mutuelle");
        formPanel.add(mutualInsurranceLabel, cc.xy(1, 21));
        doctorLabel = new JLabel();
        doctorLabel.setText("Medecin");
        doctorLabel.setToolTipText("N° agreement");
        formPanel.add(doctorLabel, cc.xy(3, 21));
        cancelButton = new JButton();
        cancelButton.setText("Annuler");
        formPanel.add(cancelButton, cc.xy(1, 27));
        submitButton = new JButton();
        submitButton.setText("Ajouter");
        formPanel.add(submitButton, cc.xy(3, 27));
        doctorField = new JTextField();
        formPanel.add(doctorField, cc.xy(3, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
        mutualField = new JTextField();
        formPanel.add(mutualField, cc.xy(1, 23, CellConstraints.FILL, CellConstraints.DEFAULT));
        birthDateLabel = new JLabel();
        birthDateLabel.setText("Date de naissance");
        formPanel.add(birthDateLabel, cc.xy(1, 17));
        nirField = new JTextField();
        formPanel.add(nirField, cc.xy(3, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
        nirLabel = new JLabel();
        nirLabel.setText("Numero securité Sociale");
        formPanel.add(nirLabel, cc.xy(3, 17));
        birthDateField = new JTextField();
        formPanel.add(birthDateField, cc.xy(1, 19, CellConstraints.FILL, CellConstraints.DEFAULT));
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

}
