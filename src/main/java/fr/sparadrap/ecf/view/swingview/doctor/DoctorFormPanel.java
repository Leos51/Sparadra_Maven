package fr.sparadrap.ecf.view.swingview.doctor;

import fr.sparadrap.ecf.database.dao.DoctorDAO;
import fr.sparadrap.ecf.model.lists.person.DoctorList;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorFormPanel extends JFrame {
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
    private JTextField doctorField;
    private JTextField mutualField;
    private JTextField rppsField;
    private JLabel rppsLabel;
    private Doctor doctor;
    private DoctorsPanel.FormModes mode;


    public DoctorFormPanel(Doctor currentDoctor, DoctorsPanel.FormModes mode) {
        this.doctor = currentDoctor;
        this.mode = mode;

        setupFrame();

        this.setContentPane(formPanel);


        if (mode == DoctorsPanel.FormModes.EDIT && currentDoctor != null) {
            //remplissage du formulaire avec client selectionné
            populateFields(currentDoctor);
        }


        cancelButton.addActionListener(e -> {
            this.dispose();
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    submitForm(currentDoctor, mode);
                } catch (SaisieException exception) {
                    JOptionPane.showMessageDialog(DoctorFormPanel.this, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }


            }
        });
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 600);
        this.setResizable(false);
        this.setLocationRelativeTo(null);


        String title = (mode == DoctorsPanel.FormModes.ADD) ? "Nouveau Médecin" : "Modifier le Médecin";
        this.setTitle(title);
        String submitText = (mode == DoctorsPanel.FormModes.ADD) ? "➕ Ajouter" : "💾 Modifier";
        submitButton.setText(submitText);
    }


    /**
     * Soumet et valide le formulaire d'ajout/edition d''un médecin
     *
     * @param doctor
     * @param mode   ajout ou edition : FormModes.ADD /FormModes.EDIT
     * @throws SaisieException
     */
    private void submitForm(Doctor doctor, DoctorsPanel.FormModes mode) throws SaisieException {
        if (!validateFields()) {
            return;
        }

        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String address = addressField.getText().trim();
        String postCode = postCodeField.getText().trim();
        String city = cityField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String rpps = rppsField.getText().trim();


        if (mode == DoctorsPanel.FormModes.ADD) {

            Doctor newDoctor = new Doctor(lastName, firstName, address, postCode, city, phone, email, rpps);
            try{
                DoctorDAO doctorDAO = new DoctorDAO();
                doctorDAO.create(newDoctor);
            }catch(Exception exception){
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Médecin ajouté avec succès!",
                    "Succès", JOptionPane.INFORMATION_MESSAGE);
        } else {

            doctor.setLastName(lastName);
            doctor.setFirstName(firstName);
            doctor.setAddress(address);
            doctor.setPostCode(postCode);
            doctor.setCity(city);
            doctor.setPhone(phone);
            doctor.setEmail(email);
            doctor.setRpps(rpps);

            try{
                DoctorDAO doctorDAO = new DoctorDAO();
                doctorDAO.update(doctor);
            }catch(Exception exception){
                JOptionPane.showMessageDialog(this, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }


            JOptionPane.showMessageDialog(this, "Medecin mis à jour !", "Succes", JOptionPane.INFORMATION_MESSAGE);
        }
        this.dispose();
    }

    /**
     * rempli les champs du formulaires avec les données du médecin selectionné
     *
     * @param doctor
     */
    private void populateFields(Doctor doctor) {

        titleLabel.setText("Modifier le medecin");
        lastNameField.setText(doctor.getLastName());
        firstNameField.setText(doctor.getFirstName());
        addressField.setText(doctor.getAddress());
        postCodeField.setText(doctor.getPostCode());
        cityField.setText(doctor.getCity());
        phoneField.setText(doctor.getPhone());
        emailField.setText(doctor.getEmail());
        rppsField.setText(doctor.getRpps());
    }

    /**
     * verifie si un des champ du formulaire est vide
     *
     * @return true/false
     */
    private boolean validateFields() {
        if (lastNameField.getText().trim().isEmpty() ||
                firstNameField.getText().trim().isEmpty() ||
                addressField.getText().trim().isEmpty() ||
                postCodeField.getText().trim().isEmpty() ||
                cityField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                rppsField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(this, "Tous les champs sont obligatoires!",
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setHorizontalTextPosition(10);
        titleLabel.setText("Ajouter un medecin");
        editPanel.add(titleLabel, BorderLayout.NORTH);
        formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        editPanel.add(formPanel, BorderLayout.CENTER);
        lastNameLabel = new JLabel();
        lastNameLabel.setText("Nom");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lastNameLabel, gbc);
        lastNameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(lastNameField, gbc);
        addressLabel = new JLabel();
        addressLabel.setText("Adresse");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(addressLabel, gbc);
        addressField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(addressField, gbc);
        firstNameLabel = new JLabel();
        firstNameLabel.setText("Prenom");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(firstNameLabel, gbc);
        firstNameField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(firstNameField, gbc);
        postCodeLabel = new JLabel();
        postCodeLabel.setText("Code Postal");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(postCodeLabel, gbc);
        cityLabel = new JLabel();
        cityLabel.setText("Ville");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(cityLabel, gbc);
        postCodeField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(postCodeField, gbc);
        cityField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cityField, gbc);
        phoneLabel = new JLabel();
        phoneLabel.setText("Téléphone");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(phoneLabel, gbc);
        emailLabel = new JLabel();
        emailLabel.setText("Email");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(emailLabel, gbc);
        phoneField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(phoneField, gbc);
        emailField = new JTextField();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(emailField, gbc);
        cancelButton = new JButton();
        cancelButton.setText("Annuler");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(cancelButton, gbc);
        submitButton = new JButton();
        submitButton.setText("Ajouter");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(submitButton, gbc);
        rppsField = new JTextField();
        rppsField.setText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(rppsField, gbc);
        rppsLabel = new JLabel();
        rppsLabel.setText("Numero d'agréement");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(rppsLabel, gbc);
        lastNameLabel.setLabelFor(lastNameField);
        addressLabel.setLabelFor(addressField);
        firstNameLabel.setLabelFor(firstNameField);
        postCodeLabel.setLabelFor(postCodeField);
        cityLabel.setLabelFor(cityField);
        phoneLabel.setLabelFor(phoneField);
        emailLabel.setLabelFor(emailField);
        rppsLabel.setLabelFor(rppsField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return editPanel;
    }
}
