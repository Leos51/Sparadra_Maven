package fr.sparadrap.ecf.view.swingview.menu;

import fr.sparadrap.ecf.view.swingview.HomePanel;
import fr.sparadrap.ecf.view.swingview.MainPanel;
import fr.sparadrap.ecf.view.swingview.customer.CustomersPanel;
import fr.sparadrap.ecf.view.swingview.doctor.DoctorsPanel;
import fr.sparadrap.ecf.view.swingview.purchases.PurchaseHistoryPanel;
import fr.sparadrap.ecf.view.swingview.purchases.PurchaseManagementPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel {
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
        //containerMenu = new JPanel();
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


    private void updateButtonStates() {
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
        containerMenu = new JPanel();
        containerMenu.setLayout(new BorderLayout(0, 0));
        containerMenu.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        menu = new JPanel();
        menu.setLayout(new GridBagLayout());
        containerMenu.add(menu, BorderLayout.CENTER);
        purchaseLabel = new JLabel();
        purchaseLabel.setText("Achat");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        menu.add(purchaseLabel, gbc);
        directPurchaseBtn = new JButton();
        directPurchaseBtn.setText("Achat Direct");
        directPurchaseBtn.setToolTipText("");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(directPurchaseBtn, gbc);
        presciptedPurchaseBtn = new JButton();
        presciptedPurchaseBtn.setText("Achat avec Ordonnance");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(presciptedPurchaseBtn, gbc);
        final JSeparator separator1 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        menu.add(separator1, gbc);
        final JLabel label1 = new JLabel();
        label1.setText("Gestion");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.WEST;
        menu.add(label1, gbc);
        doctorsManagerBtn = new JButton();
        doctorsManagerBtn.setText("Gestion Médecins");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(doctorsManagerBtn, gbc);
        medicineManagerBtn = new JButton();
        medicineManagerBtn.setText("Gestion des Stocks");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(medicineManagerBtn, gbc);
        final JSeparator separator2 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        menu.add(separator2, gbc);
        exitButton = new JButton();
        exitButton.setText("Quitter");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 15;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(exitButton, gbc);
        homeBtn = new JButton();
        homeBtn.setText("Acceuil");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(homeBtn, gbc);
        final JSeparator separator3 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        menu.add(separator3, gbc);
        final JSeparator separator4 = new JSeparator();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        menu.add(separator4, gbc);
        prescriptionsHistoryBtn = new JButton();
        prescriptionsHistoryBtn.setText("Historique des ordonnances");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(prescriptionsHistoryBtn, gbc);
        purchaseHistoryBtn = new JButton();
        purchaseHistoryBtn.setText("Historique des achats");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(purchaseHistoryBtn, gbc);
        final JLabel label2 = new JLabel();
        label2.setText("Historique");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        menu.add(label2, gbc);
        customerManagerBtn = new JButton();
        customerManagerBtn.setText("Gestion Clients");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        menu.add(customerManagerBtn, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return containerMenu;
    }
}
