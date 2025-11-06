package fr.sparadrah.ecf.view.swingview;

import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.lists.person.DoctorList;
import fr.sparadrah.ecf.model.person.Customer;
import fr.sparadrah.ecf.model.person.Doctor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePanel extends JPanel {
    private JPanel homePanel;
    private JPanel topPanel;
    private JLabel homeTitleLabel;
    private JPanel centerPanel;
    private JPanel bottomPanel;
    private JPanel detailsClientContainer;
    private JPanel filterPanel;
    private JComboBox customerComboBox;
    private JPanel displayDetailPanel;
    private JComboBox doctorCombobox;
    private JLabel customerDetailLabel;
    private JTextPane customerPane;
    private JPanel detailsDoctorContainer;
    private JPanel StatPanel;
    private JTextPane doctorPane;

    public HomePanel() {
        this.add(homePanel);


        CustomersList.getCustomers().stream().forEach(customer -> {
           customerComboBox.addItem(customer.getFullName());
        });

        DoctorList.getDoctors().stream().forEach(doctor -> {
            doctorCombobox.addItem(doctor.getFullName());
        });


        customerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String customer =  customerComboBox.getSelectedItem().toString();
                if (customer != null) {
                    Customer c = CustomersList.findByFullName(customer);
                    customerPane.setText(c.showDetails());
                }

            }
        });


        doctorCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String doctorName =  doctorCombobox.getSelectedItem().toString();
                if (doctorName != null) {
                    Doctor doctor = DoctorList.findByFullName(doctorName);
                    doctorPane.setText(doctor.showDetails());

                }

            }
        });
    }


}
