package fr.sparadrap.ecf.view.swingview.doctor;

import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.person.DoctorList;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.view.swingview.DisplayList;
import fr.sparadrap.ecf.view.swingview.tablemodele.TableModele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static fr.sparadrap.ecf.view.swingview.DisplayList.*;

public class DoctorsPanel extends JPanel {
    private JPanel doctorsPanel;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JPanel bottomBar;
    private JPanel topPanel;
    private JLabel titleLabel;
    private JPanel centerPanel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton showDetailsBtn;
    private JPanel tableContainer;
    private JPanel titlePanel;
    private JPanel searchPanel;
    private JTable doctorsTable;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private JPanel left;
    private JPanel right;
    private JButton showCustomersBtn;
    private JButton showDoctorPrescriptionsBtn;
    private DisplayList doctorsDisplayList;
    public enum FormModes{
        ADD,
        EDIT
    }

    public DoctorsPanel(){
        this.setLayout(new GridLayout(1,1));
        this.add(doctorsPanel);
        doctorsDisplayList = new DisplayList(1);
        tablePanel.add(doctorsDisplayList);
        updateButtonsState();


        doctorsDisplayList.getTable().getSelectionModel().addListSelectionListener(e -> {
            updateButtonsState();
        });

        // Boutons d'action

        searchField.addActionListener(e -> searchDoctors());
        showDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDoctorDetails();

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDoctor();
            }
        });

        deleteButton.addActionListener(e-> {
            deleteDoctor();
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDoctors();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                addDoctor();
            }
        });
        showCustomersBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDoctorCustomers();

            }
        });
        showDoctorPrescriptionsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDoctorPrescriptions();
            }
        });
    }




    private void searchDoctors() {
        String search = searchField.getText().trim();
        if (search.isEmpty()) {
            refreshTable();
            return;
        }
        List<Doctor> filteredList = DoctorList.filterDoctor(search);
        doctorsDisplayList.configTable(filteredList,HEADER_DOCTORS, USER_COLUMN_CLASSES);
    }


    private void updateButtonsState() {
        boolean hasDoctors = !DoctorList.getDoctors().isEmpty();
        boolean hasSelection = doctorsDisplayList.getTable().getSelectedRow() != -1;
        showDetailsBtn.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        showCustomersBtn.setEnabled(hasSelection);
        showDoctorPrescriptionsBtn.setEnabled(hasSelection);
        searchButton.setEnabled(true);
        addButton.setEnabled(true);
    }

    /**
     * affiche la fenetre de details du medecin selectionné
     */
    private void showDoctorDetails(){
        Doctor selectedDoctor = getSelectedDoctor();

        if(selectedDoctor == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un médecin","Aucun client selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(doctorsPanel, selectedDoctor.showDetails(), "Détails du Médecin",  JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * selectionne un medecin dans le tableau
     * @return un Medecin
     */
    private Doctor getSelectedDoctor(){
        return getSelectedItem(doctorsDisplayList.getTable(), (TableModele<Doctor>) doctorsDisplayList.getTable().getModel());

    }


    /**
     * Filtre les patients associé a un médecin en particulier
     * @param doctor
     * @return Liste de patients
     */
    private List<Customer> getCustomersByDoctor(Doctor doctor){
        List<Customer> customers = CustomersList.getCustomers().stream()
                .filter(customer -> customer.getDoctor() != null && customer.getDoctor().equals(doctor))
                .toList();
        return customers;
    }


    /**
     * affiche la fenetre d'ajout de medecin
     */
    private void addDoctor() {
        DoctorFormPanel formPanel = new DoctorFormPanel(null, FormModes.ADD);
        formPanel.setVisible(true);
    }

    /**
     * affiche la fenetre  qui montre la liste des patients du médecin selectionné
     */
    public void showDoctorCustomers(){
        Doctor selectedDoctor = getSelectedDoctor();
        if (selectedDoctor != null) {
            DoctorCustomersDialog dialog = new DoctorCustomersDialog(selectedDoctor);
            dialog.setVisible(true);
        }
    }

    /**
     * affiche la fenetre montrant les ordonnances d'un médecin
     */
    private void showDoctorPrescriptions() {
        Doctor selectedDoctor = getSelectedDoctor();
        if (selectedDoctor != null) {
            DoctorPrescriptionsDialog dialog = new DoctorPrescriptionsDialog(selectedDoctor);
            dialog.setVisible(true);
        }
    }


    private void editDoctor(){
        Doctor selectedDoctors = getSelectedDoctor();
        if(selectedDoctors == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un Medecin","Aucun médecin selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        DoctorFormPanel formPanel = new DoctorFormPanel(selectedDoctors, FormModes.EDIT);
        formPanel.setVisible(true);
    }

    private void deleteDoctor(){
        Doctor selectedDoctor = getSelectedDoctor();
        if(selectedDoctor == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un médecin","Aucun médecin selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Customer> customers = getCustomersByDoctor(selectedDoctor);
        if(!customers.isEmpty()){
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Attention : Ce médecin a " + customers.size() + " patient(s) associé(s).\n" +
                            "Si vous le supprimez, ces patients n'auront plus de médecin référent.\n" +
                            "Voulez-vous vraiment continuer ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }
        int result = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer le médecin \"" + selectedDoctor.getFullName() + "\" ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (result == JOptionPane.YES_OPTION) {
            for (Customer c : customers) {
                try {
                   c.setDoctorByLicenseNumber(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Le patient " + c.getFullName() + " n'a plus de médecin réferent", "Patients abandonné", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        DoctorList.removeDoctor(selectedDoctor);
        refreshTable();
        updateButtonsState();
        JOptionPane.showMessageDialog(this,
                "Médecin supprimé avec succès!",
                "Suppression réussie",
                JOptionPane.INFORMATION_MESSAGE);


    }

    private void refreshTable(){
        doctorsDisplayList.configTable(DoctorList.getDoctors(),HEADER_DOCTORS, USER_COLUMN_CLASSES);
    }





}


