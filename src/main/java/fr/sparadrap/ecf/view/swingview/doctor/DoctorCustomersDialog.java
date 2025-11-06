package fr.sparadrah.ecf.view.swingview.doctor;

import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.person.Doctor;
import fr.sparadrah.ecf.view.swingview.DisplayList;

import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;

import static fr.sparadrah.ecf.view.swingview.DisplayList.HEADER_CUSTOMERS;
import static fr.sparadrah.ecf.view.swingview.DisplayList.USER_COLUMN_CLASSES;

public class DoctorCustomersDialog extends JDialog {
    private Doctor doctor;
    private JPanel contentPane;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel centerpanel;
    private JPanel bottomPanel;

    private JTable customersTable;
    private DisplayList customersDisplayList;


    public DoctorCustomersDialog(Doctor doctor) {
        this.doctor = doctor;
        this.setContentPane(contentPane);
        customersDisplayList = new DisplayList(0);
        setupDialog();
        initializeComponents();
    }

    private void setupDialog() {
        this.setSize(800, 500);
        this.setLocationRelativeTo(getParent());
        this.setResizable(true);
    }

    private void initializeComponents() {
        titleLabel.setText("Patient du médecin " + doctor.getFullName());

        // Table des patients
        java.util.List<Customer> customers = getCustomersByDoctor();
        System.out.println(customers.size());

        if (customers.isEmpty()) {
            JLabel noCustomersLabel = new JLabel("Aucun patient assigné à ce médecin.");
            noCustomersLabel.setHorizontalAlignment(JLabel.CENTER);
            centerpanel.add(noCustomersLabel, BorderLayout.CENTER);
        } else {
            customersDisplayList.configTable(customers,HEADER_CUSTOMERS, USER_COLUMN_CLASSES);
            System.out.println("customersDisplayList.getTable().size() = " + customersDisplayList.getTable().size());



            JScrollPane scrollPane = new JScrollPane(customersTable);
            scrollPane.setBorder(BorderFactory.createTitledBorder(
                    "Liste des patients (" + customers.size() + " patient(s))"));
            centerpanel.add(customersDisplayList, BorderLayout.CENTER);


        }
    }


    /**
     * filtre les client dun médecin selectionné  a parti de son RPPS
     * @return Liste des clients pour le medecin selectionné
     */
    private java.util.List<Customer> getCustomersByDoctor(){
        java.util.List<Customer> customers = CustomersList.getCustomers().stream()
                .filter(customer -> customer.getDoctor() != null && customer.getDoctor().equals(doctor))
                .toList();
        return customers;
    }

}
