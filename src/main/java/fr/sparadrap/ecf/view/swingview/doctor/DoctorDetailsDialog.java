package fr.sparadrah.ecf.view.swingview.doctor;

import fr.sparadrah.ecf.model.lists.person.CustomersList;
import fr.sparadrah.ecf.model.person.Doctor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DoctorDetailsDialog extends JDialog {
    private Doctor doctor;

    private JPanel contentPane;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JTextArea detailsArea;
    private JPanel buttonPanel;
    private JButton closeBtn;

    public DoctorDetailsDialog(Frame frame, Doctor doctor) {
        super(frame,"Details du medecin", true);
        this.doctor = doctor;
        setContentPane(contentPane);
        setModal(true);

        setupDialog();
        initializeComponents();

        closeBtn.addActionListener(e-> this.dispose());
    }
    private void setupDialog() {
        this.setSize(500, 400);
        this.setLocationRelativeTo(getParent());
        this.setResizable(false);
    }

    private void initializeComponents(){
        long customersCount = CustomersList.getCustomers()
                .stream()
                .filter(customer -> customer.getDoctor().equals(doctor))
                .count();

        titleLabel.setText(doctor.getFullName());

        StringBuilder details = new StringBuilder();
        details.append("INFORMATIONS PERSONNELLES\n");
        details.append("═══════════════════════════\n\n");
        details.append("Nom complet: ").append(doctor.getFullName()).append("\n");
        details.append("Numéro d'agrément: ").append(doctor.getRpps()).append("\n\n");

        details.append("COORDONNÉES\n");
        details.append("═══════════════════════════\n\n");
        details.append("Adresse: ").append(doctor.getAddress()).append("\n");
        details.append("Code postal: ").append(doctor.getPostCode()).append("\n");
        details.append("Ville: ").append(doctor.getCity()).append("\n");
        details.append("Téléphone: ").append(doctor.getPhone()).append("\n");
        details.append("Email: ").append(doctor.getEmail()).append("\n\n");
        details.append("PATIENTS\n");
        details.append("═══════════════════════════\n\n");
        details.append("Nombre de patients: ").append(customersCount).append("\n");

        detailsArea.setText(details.toString());




    }
}
