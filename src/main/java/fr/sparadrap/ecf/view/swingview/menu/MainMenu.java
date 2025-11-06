package fr.sparadrah.ecf.view.swingview.menu;

import fr.sparadrah.ecf.view.swingview.HomePanel;
import fr.sparadrah.ecf.view.swingview.MainPanel;
import fr.sparadrah.ecf.view.swingview.customer.CustomersPanel;
import fr.sparadrah.ecf.view.swingview.doctor.DoctorsPanel;
import fr.sparadrah.ecf.view.swingview.purchases.PurchaseHistoryPanel;
import fr.sparadrah.ecf.view.swingview.purchases.PurchaseManagementPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public  class MainMenu extends JPanel {
    private MainPanel mainPanel;
    private CustomersPanel customersPanel = new CustomersPanel();
    private DoctorsPanel doctorsPanel;
    private PurchaseHistoryPanel purchaseHistoryPanel = new PurchaseHistoryPanel();
    private HomePanel homePanel = new HomePanel();





    private JPanel containerMenu;
    private JButton directPurchaseBtn;
    private JButton customerManagerBtn;
    private JButton presciptedPurchaseBtn;
    private JButton doctorsManagerBtn;
    private JButton medicineManagerBtn;
    private JButton exitButton;
    private JLabel purchaseLabel;
    private JPanel menu;
    private JButton purchaseHistoryBtn;
    private JButton homeBtn;
    private JButton prescriptionsHistoryBtn;
    private Boolean hasPrescription = false;
    private JButton activeButton = null;


    public MainMenu(MainPanel mainPanel) {

        this.setBackground(Color.lightGray);
        this.mainPanel = mainPanel;
        showHome();
        this.add(containerMenu);

        homeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHome();
                resetBtnMenu();
                homeBtn.setEnabled(false);
            }
        });

        directPurchaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDirectPurchase();
                resetBtnMenu();
                directPurchaseBtn.setEnabled(false);

            }
        });
        presciptedPurchaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPrescriptionPurchase();
                resetBtnMenu();
                presciptedPurchaseBtn.setEnabled(false);


            }
        });

        purchaseHistoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPurchaseHistory();
                resetBtnMenu();
                purchaseHistoryBtn.setEnabled(false);


            }
        });

        customerManagerBtn.addActionListener(e -> {
            showCustomers();
            resetBtnMenu();
            customerManagerBtn.setEnabled(false);


        });


        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitApp();
            }
        });


        doctorsManagerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDoctors();
                resetBtnMenu();
                doctorsManagerBtn.setEnabled(false);

            }
        });
        medicineManagerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMedicines();
                resetBtnMenu();
                medicineManagerBtn.setEnabled(false);

            }
        });
        prescriptionsHistoryBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPrescriptionsHistory();
                resetBtnMenu();
                prescriptionsHistoryBtn.setEnabled(false);

            }
        });
    }

    private void resetBtnMenu() {
        homeBtn.setEnabled(true);
        directPurchaseBtn.setEnabled(true);
        presciptedPurchaseBtn.setEnabled(true);
        purchaseHistoryBtn.setEnabled(true);
        customerManagerBtn.setEnabled(true);
        doctorsManagerBtn.setEnabled(true);
        medicineManagerBtn.setEnabled(true);
        prescriptionsHistoryBtn.setEnabled(true);


    }

    private void exitApp() {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Voulez vous vraiment quitter l'application?",
                "Quitter?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    private void showHome() {
        if (mainPanel != null) mainPanel.showView(homePanel);
    }

    private void showDirectPurchase() {
        if (mainPanel != null) {
            PurchaseManagementPanel panel = new PurchaseManagementPanel(false);
            mainPanel.showView(panel);
        }
    }

    private void showPrescriptionPurchase() {
        if (mainPanel != null) {
            PurchaseManagementPanel panel = new PurchaseManagementPanel(true);
            mainPanel.showView(panel);
        }
    }


    private void showCustomers() {
        mainPanel.showView(customersPanel);
    }

    private void showPurchaseHistory() {
        mainPanel.showView(purchaseHistoryPanel);
    }

    private void showPrescriptionsHistory() {
        // TODO: Implémenter PrescriptionHistoryPanel
        JOptionPane.showMessageDialog(getParent(), "Fonctionnalité en développement");
    }
    private void showDoctors() {
        doctorsPanel = new DoctorsPanel();
        mainPanel.showView(doctorsPanel);
       }

    private void showMedicines() {
        // TODO: Implémenter MedicinesPanel
        JOptionPane.showMessageDialog(getParent(), "Fonctionnalité en développement");
    }



    private void updateButtonStates(){
        resetBtnMenu();
        this.setEnabled(false);
    }

    private void setActiveButton(JButton button) {
        // Réinitialiser l'ancien bouton actif
        if (activeButton != null) {
            activeButton.setBackground(Color.black);
        }

        // Définir le nouveau bouton actif
        activeButton = button;
        if (activeButton != null) {
            activeButton.setBackground(Color.white);
        }
    }

}
