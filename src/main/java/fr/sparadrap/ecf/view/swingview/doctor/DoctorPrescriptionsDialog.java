package fr.sparadrap.ecf.view.swingview.doctor;



import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.view.swingview.DisplayList;

import javax.swing.*;
import java.awt.*;

import static fr.sparadrap.ecf.model.lists.medicine.PrescriptionList.findPrescriptionsByDoctor;
import static fr.sparadrap.ecf.view.swingview.DisplayList.*;

public class DoctorPrescriptionsDialog extends JDialog {
    private Doctor doctor;
    private JPanel contentPane;
    private JPanel titlePanel;
    private JLabel titleLabel;
    private JPanel centerpanel;
    private JPanel bottomPanel;
    private JTable customersTable;

    private JTable precriptionsTable;
    private DisplayList prescriptionsDisplayList;

    public DoctorPrescriptionsDialog(Doctor doctor) {
        this.doctor = doctor;
        this.setContentPane(contentPane);
        prescriptionsDisplayList = new DisplayList(5);
        setupDialog();
        initializeComponents();
    }

    private void setupDialog() {
        this.setSize(800, 500);
        this.setLocationRelativeTo(getParent());
        this.setResizable(true);
    }

    private void initializeComponents() {
        titleLabel.setText("Ordonnances du médecin " + doctor.getFullName());

        // Table des patients
        java.util.List<Prescription> prescriptions = getPrescriptionsByDoctor();
        System.out.println(prescriptions.size());

        if (prescriptions.isEmpty()) {
            JLabel noPrescriptionsLabel = new JLabel("Aucune ordonnance associée à ce médecin.");
            noPrescriptionsLabel.setHorizontalAlignment(JLabel.CENTER);
            centerpanel.add(noPrescriptionsLabel, BorderLayout.CENTER);
        } else {
            prescriptionsDisplayList.configTable(prescriptions,HEADER_PRESCRIPTIONS, PRESCRIPTION_COLUMN_CLASSES);

            centerpanel.add(prescriptionsDisplayList, BorderLayout.CENTER);


        }
    }


    /**
     * filtre les client dun médecin selectionné  a parti de son RPPS
     * @return Liste des clients pour le medecin selectionné
     */
    private java.util.List<Prescription> getPrescriptionsByDoctor(){
        return findPrescriptionsByDoctor(doctor);
    }

}
