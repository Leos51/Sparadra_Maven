package fr.sparadrah.ecf.view.swingview;

import fr.sparadrah.ecf.model.lists.medicine.MedicineList;
import fr.sparadrah.ecf.model.lists.medicine.PrescriptionList;
import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.medicine.Medicine;
import fr.sparadrah.ecf.model.medicine.Prescription;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.utils.DateFormat;
import fr.sparadrah.ecf.utils.exception.SaisieException;
import fr.sparadrah.ecf.utils.validator.Validator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class PrescriptionCreationPanel extends JDialog {
    private Customer customer;

    // Composants de l'interface
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel formPanel;
    private JPanel medicinesPanel;
    private JPanel buttonPanel;

    private JLabel titleLabel;
    private JLabel customerLabel;
    private JLabel doctorLabel;
    private JLabel dateLabel;
    private JLabel medicinesLabel;

    private JComboBox<Doctor> doctorComboBox;
    private JTextField dateField;
    private JList<Medicine> availableMedicinesList;
    private JList<Medicine> selectedMedicinesList;
    private JScrollPane availableScrollPane;
    private JScrollPane selectedScrollPane;

    private JButton addMedicineBtn;
    private JButton removeMedicineBtn;
    private JButton createBtn;
    private JButton cancelBtn;

    private DefaultListModel<Medicine> availableMedicinesModel;
    private DefaultListModel<Medicine> selectedMedicinesModel;

    public PrescriptionCreationPanel(JFrame parent, Customer customer) {
        super(parent, "Créer une ordonnance", true);
        this.customer = customer;

        initComponents();
        loadDoctors();
        loadMedicines();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        pack();
    }

    private void initComponents() {
        // Configuration de la fenêtre
        setSize(800, 600);
        setResizable(false);

        // Panel principal
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Panel d'en-tête
        headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titleLabel = new JLabel("Créer une nouvelle ordonnance");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        customerLabel = new JLabel("Client: " + customer.getFullName());
        customerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createHorizontalStrut(20));
        headerPanel.add(customerLabel);

        // Panel de formulaire
        formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Médecin prescripteur
        gbc.gridx = 0; gbc.gridy = 0;
        doctorLabel = new JLabel("Médecin référent:");
        formPanel.add(doctorLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        doctorComboBox = new JComboBox<>();
        doctorComboBox.setPreferredSize(new Dimension(300, 25));
        formPanel.add(doctorComboBox, gbc);

        // Date de prescription
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        dateLabel = new JLabel("Date de prescription:");
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dateField = new JTextField(DateFormat.formatDate(LocalDate.now(), "dd/MM/yyyy"));
        dateField.setPreferredSize(new Dimension(300, 25));
        formPanel.add(dateField, gbc);

        // Panel des médicaments
        medicinesPanel = createMedicinesPanel();

        // Panel des boutons
        buttonPanel = createButtonPanel();

        // Ajout des panels au panel principal
        contentPane.add(headerPanel, BorderLayout.NORTH);
        contentPane.add(formPanel, BorderLayout.NORTH);
        contentPane.add(medicinesPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createMedicinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Sélection des médicaments"));

        // Panel pour les listes
        JPanel listsPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        // Liste des médicaments disponibles
        JPanel availablePanel = new JPanel(new BorderLayout());
        availablePanel.add(new JLabel("Médicaments disponibles:"), BorderLayout.NORTH);

        availableMedicinesModel = new DefaultListModel<>();
        availableMedicinesList = new JList<>(availableMedicinesModel);
        availableMedicinesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        availableScrollPane = new JScrollPane(availableMedicinesList);
        availableScrollPane.setPreferredSize(new Dimension(250, 200));
        availablePanel.add(availableScrollPane, BorderLayout.CENTER);

        // Panel des boutons d'action
        JPanel actionPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 50, 10));

        addMedicineBtn = new JButton("Ajouter >>");
        removeMedicineBtn = new JButton("<< Retirer");

        addMedicineBtn.addActionListener(e -> addSelectedMedicine());
        removeMedicineBtn.addActionListener(e -> removeSelectedMedicine());

        actionPanel.add(new JLabel()); // Spacer
        actionPanel.add(addMedicineBtn);
        actionPanel.add(removeMedicineBtn);
        actionPanel.add(new JLabel()); // Spacer

        // Liste des médicaments sélectionnés
        JPanel selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.add(new JLabel("Médicaments prescrits:"), BorderLayout.NORTH);

        selectedMedicinesModel = new DefaultListModel<>();
        selectedMedicinesList = new JList<>(selectedMedicinesModel);
        selectedMedicinesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectedScrollPane = new JScrollPane(selectedMedicinesList);
        selectedScrollPane.setPreferredSize(new Dimension(250, 200));
        selectedPanel.add(selectedScrollPane, BorderLayout.CENTER);

        // Ajout des composants
        listsPanel.add(availablePanel);
        listsPanel.add(actionPanel);
        listsPanel.add(selectedPanel);

        panel.add(listsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        createBtn = new JButton("Créer l'ordonnance");
        cancelBtn = new JButton("Annuler");

        createBtn.addActionListener(e -> createPrescription());
        cancelBtn.addActionListener(e -> dispose());

        panel.add(cancelBtn);
        panel.add(createBtn);

        return panel;
    }

    private void loadDoctors() {
        doctorComboBox.removeAllItems();
        List<Doctor> doctors = DoctorList.getDoctors();

        for (Doctor doctor : doctors) {
            doctorComboBox.addItem(doctor);
        }

        // Sélectionner le médecin réferent du client s'il existe
        if (customer.getDoctor() != null) {
            doctorComboBox.setSelectedItem(customer.getDoctor());
        }
    }

    private void loadMedicines() {
        availableMedicinesModel.clear();
        List<Medicine> medicines = MedicineList.getMedicines();

        for (Medicine medicine : medicines) {
            // N'afficher que les médicaments en stock
            if (medicine.getStock() > 0) {
                availableMedicinesModel.addElement(medicine);
            }
        }
    }


    private void addSelectedMedicine() {
        Medicine selectedMedicine = availableMedicinesList.getSelectedValue();
        if (selectedMedicine != null) {
            // Vérifier que le médicament n'est pas déjà dans la liste des prescrits
            if (!selectedMedicinesModel.contains(selectedMedicine)) {
                selectedMedicinesModel.addElement(selectedMedicine);
                availableMedicinesModel.removeElement(selectedMedicine);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un médicament à ajouter.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void removeSelectedMedicine() {
        Medicine selectedMedicine = selectedMedicinesList.getSelectedValue();
        if (selectedMedicine != null) {
            selectedMedicinesModel.removeElement(selectedMedicine);
            availableMedicinesModel.addElement(selectedMedicine);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un médicament à retirer.",
                    "Aucune sélection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void createPrescription() {
        try {
            // Validation des champs
            Doctor selectedDoctor = (Doctor) doctorComboBox.getSelectedItem();
            String prescriptionDate = dateField.getText().trim();

            if (selectedDoctor == null) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner un médecin prescripteur.",
                        "Champ manquant",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (prescriptionDate.isEmpty() || !Validator.isValidDate(prescriptionDate)) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez saisir la date de prescription. Format valide : \"dd/MM/yyyy\"",
                        "Champ manquant",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedMedicinesModel.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner au moins un médicament.",
                        "Aucun médicament",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Créer la liste des médicaments prescrits
            List<Medicine> prescribedMedicines = new ArrayList<>();
            for (int i = 0; i < selectedMedicinesModel.size(); i++) {
                prescribedMedicines.add(selectedMedicinesModel.elementAt(i));
            }

            // Créer l'ordonnance
            Prescription newPrescription = new Prescription(
                    prescriptionDate,
                    selectedDoctor,
                    customer,
                    prescribedMedicines
            );

            // Ajouter à la liste des ordonnances
            PrescriptionList.addPrescription(newPrescription);

            JOptionPane.showMessageDialog(this,
                    "Ordonnance créée avec succès !\n" +
                            "Date: " + prescriptionDate + "\n" +
                            "Médecin: " + selectedDoctor.getFullName() + "\n" +
                            "Nombre de médicaments: " + prescribedMedicines.size(),
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();

        } catch (SaisieException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de la création de l'ordonnance:\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur :\n" + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
