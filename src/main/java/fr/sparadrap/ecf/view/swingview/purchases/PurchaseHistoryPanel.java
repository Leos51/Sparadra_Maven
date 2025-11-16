package fr.sparadrap.ecf.view.swingview.purchases;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import fr.sparadrap.ecf.model.lists.purchase.PurchasesList;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.purchase.CartItem;
import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.utils.validator.Validator;
import fr.sparadrap.ecf.view.swingview.DisplayList;
import fr.sparadrap.ecf.view.swingview.tablemodele.TableModele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import static fr.sparadrap.ecf.model.lists.purchase.PurchasesList.getOldestPurchaseDate;
import static fr.sparadrap.ecf.view.swingview.DisplayList.*;

public class PurchaseHistoryPanel extends JPanel {
    private JPanel contentPane;
    private JPanel titlePanel;
    private JPanel topPanel;
    private JPanel filterPanel;
    private JComboBox periodComboBox;
    private JButton filterBtn;
    private JPanel centerPanel;
    private JPanel buttonPanel;
    private JButton detailsBtn;
    private JPanel tablePanel;
    private JPanel detailsContainer;
    private JTable table1;
    private JPanel detailsTitlePanel;
    private JPanel detailsPanel;
    private JLabel detailsTitle;
    private JLabel dateLabel;
    private JLabel customerNameLabel;
    private JLabel typeLabel;
    private JLabel doctorNameLabel;
    private JLabel totalAmount;
    private JLabel medicinesLabel;
    private JLabel purchasesHistoryTitleLabel;
    private JLabel dateLabelDetail;
    private JLabel customerLabelDetail;
    private JLabel typeDetail;
    private JLabel doctorDetail;
    private JLabel totatAmountDetail;
    private JTextArea detailMedicinesArea;
    private JTextField dateField;
    private JTextField periodStartField;
    private JTextField periodEndField;
    DisplayList purchases;

    JTable purchasesTable;

    public PurchaseHistoryPanel() {
        contentPane = new JPanel();
        this.add(contentPane);

        purchases = new DisplayList(4);
        purchasesTable = purchases.getTable();
        tablePanel.add(purchases);


        // purchasesTable = purchases.getTable();
        //purchasesHistoryTitleLabel.setText("Historique des Achats (" + purchasesTable.getRowCount() + " achats)");


        detailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                displayPurchaseDetails();
            }
        });
        periodComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = (String) periodComboBox.getSelectedItem();
                updateFieldsVisibility(selected);
            }
        });
        filterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    filterPurchases();
                } catch (SaisieException ex) {
                    JOptionPane.showMessageDialog(null, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    /**
     * Selectionne un achat dans le tableau
     *
     * @return un achat enregistré
     */
    private Purchase selectPurchase() {
        Purchase selectedPurchase = getSelectedItem(purchasesTable, (TableModele<Purchase>) purchasesTable.getModel());
        return selectedPurchase;
    }

    ;


    /**
     * Affiche le detail d'un achat qui a été séléctionné
     */
    private void displayPurchaseDetails() {

        if (selectPurchase() != null) {
            detailsPanel.setVisible(true);
            Purchase selectedPurchase = selectPurchase();
            Customer customer = selectedPurchase.getCustomer();

            detailsTitle.setText("Détails de l'achat");
            customerLabelDetail.setText(customer.getFullName());
            dateLabelDetail.setText(DateFormat.formatDate(selectedPurchase.getPurchaseDate(), "dd/MM/yyyy"));
            typeDetail.setText(selectedPurchase.isPrescriptionBased() ? "Achat sur ordonnance" : "Achat direct");
            if (selectedPurchase.getCustomer().getDoctor() != null) {
                doctorDetail.setText(selectedPurchase.getCustomer().getDoctor().getFullName());
            } else {
                doctorDetail.setText("Aucun médecin");
            }
            totatAmountDetail.setText(selectedPurchase.getTotal() + "");

            StringBuilder medicinesList = new StringBuilder();
            for (CartItem med : selectedPurchase.getMedicines()) {
                medicinesList.append("• ").append(med.getMedicine().getMedicineName())
                        .append(" (").append(med.getMedicine().getCategory()).append(")")
                        .append("\n  Prix : ").append((med.getPrice()))
                        .append("\n  Quantité : ").append(med.getQuantity())
                        .append("\n\n");
            }
            detailMedicinesArea.setText(medicinesList.toString());

        } else {
            detailsPanel.setVisible(false);
        }
    }

    /**
     * Met a jour la visibilité des Inputs en fonction de l'item selectionné dans la combobox
     *
     * @param selected
     */
    private void updateFieldsVisibility(String selected) {
        boolean isDate = "Date personnalisée".equalsIgnoreCase(selected);
        boolean isPeriod = "Période personnalisée".equalsIgnoreCase(selected);

        dateField.setVisible(isDate);
        periodStartField.setVisible(isPeriod);
        periodEndField.setVisible(isPeriod);

        periodComboBox.revalidate();
        periodComboBox.repaint();
    }


    /**
     * remplit l'input avec la date du jour
     *
     * @param dateField Le nom de la JtextField
     */
    private void fillEmptyDateField(JTextField dateField) {
        if (dateField.getText().trim().isEmpty()) {
            dateField.setText(DateFormat.formatDate(LocalDate.now(), "dd/MM/yyyy"));
        }
    }

    /**
     * Remplit Les Input de la Periode avec la date de la plus ancienne facture et la date du jour
     */
    private void fillEmptyPeriodsFields() {
        fillEmptyDateField(periodEndField);
        if (periodStartField.getText().trim().isEmpty()) {
            periodStartField.setText(DateFormat.formatDate(getOldestPurchaseDate(), "dd/MM/yyyy"));
        }
    }

    /**
     * Filtre l'historique d'achat en fonction de la selection dans la combobox
     * - aujourd'hui
     * - Date Precise
     * - Periode
     *
     * @throws SaisieException
     */
    private void filterPurchases() throws SaisieException {
        List<Purchase> filteredPurchases;
        switch (periodComboBox.getSelectedItem().toString()) {
            case "Aujourd'hui":
                filteredPurchases = PurchasesList.findPurchaseOfDay();
                break;
            case "Date personnalisée":
                fillEmptyDateField(dateField);
                if (!Validator.isValidDate(dateField.getText())) {
                    throw new SaisieException("Remplir le champ date au format : \"dd/MM/yyyy\" avant de filtrer les achats");
                }
                filteredPurchases = PurchasesList.findPurchaseByDate(dateField.getText());
                break;
            case "Période personnalisée":
                fillEmptyPeriodsFields();
                if (!Validator.isValidDate(periodStartField.getText()) || !Validator.isValidDate(periodEndField.getText())) {
                    throw new SaisieException("Remplir les champs date au format : \"dd/MM/yyyy\"");
                }

                filteredPurchases = PurchasesList.findPurchasebyPeriod(periodStartField.getText(), periodEndField.getText());
                break;
            default:
                filteredPurchases = PurchasesList.getPurchases();
                System.out.println("Aucun filtre appliqué");
                break;
        }
        purchases.configTable(filteredPurchases, HEADER_PURCHASES, PURCHASE_COLUMN_CLASSES);
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        contentPane.setPreferredSize(new Dimension(1000, 700));
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(topPanel, BorderLayout.NORTH);
        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout(0, 0));
        topPanel.add(titlePanel, BorderLayout.NORTH);
        purchasesHistoryTitleLabel = new JLabel();
        purchasesHistoryTitleLabel.setHorizontalAlignment(0);
        purchasesHistoryTitleLabel.setText("Historique des Achats");
        titlePanel.add(purchasesHistoryTitleLabel, BorderLayout.CENTER);
        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        topPanel.add(filterPanel, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Filtre");
        filterPanel.add(label1);
        periodComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        defaultComboBoxModel1.addElement("Aucun filtre");
        defaultComboBoxModel1.addElement("Aujourd'hui");
        defaultComboBoxModel1.addElement("Date personnalisée");
        defaultComboBoxModel1.addElement("Période personnalisée");
        periodComboBox.setModel(defaultComboBoxModel1);
        filterPanel.add(periodComboBox);
        dateField = new JTextField();
        dateField.setPreferredSize(new Dimension(100, 36));
        dateField.setText("");
        dateField.setVisible(false);
        filterPanel.add(dateField);
        periodStartField = new JTextField();
        periodStartField.setPreferredSize(new Dimension(100, 36));
        periodStartField.setVisible(false);
        filterPanel.add(periodStartField);
        periodEndField = new JTextField();
        periodEndField.setPreferredSize(new Dimension(100, 36));
        periodEndField.setVisible(false);
        filterPanel.add(periodEndField);
        filterBtn = new JButton();
        filterBtn.setText("Filtrer");
        filterPanel.add(filterBtn);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 0));
        contentPane.add(centerPanel, BorderLayout.CENTER);
        final JSplitPane splitPane1 = new JSplitPane();
        centerPanel.add(splitPane1, BorderLayout.CENTER);
        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setLeftComponent(tablePanel);
        final JScrollPane scrollPane1 = new JScrollPane();
        tablePanel.add(scrollPane1, BorderLayout.CENTER);
        table1 = new JTable();
        scrollPane1.setViewportView(table1);
        detailsContainer = new JPanel();
        detailsContainer.setLayout(new BorderLayout(0, 0));
        detailsContainer.setVisible(true);
        splitPane1.setRightComponent(detailsContainer);
        detailsTitlePanel = new JPanel();
        detailsTitlePanel.setLayout(new BorderLayout(0, 0));
        detailsContainer.add(detailsTitlePanel, BorderLayout.NORTH);
        detailsTitle = new JLabel();
        detailsTitle.setHorizontalAlignment(0);
        detailsTitle.setText("Sélectionnez un achat dans la liste");
        detailsTitlePanel.add(detailsTitle, BorderLayout.CENTER);
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayoutManager(7, 3, new Insets(0, 0, 0, 0), -1, -1));
        detailsPanel.setVisible(false);
        detailsContainer.add(detailsPanel, BorderLayout.CENTER);
        dateLabel = new JLabel();
        dateLabel.setText("Date : ");
        detailsPanel.add(dateLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateLabelDetail = new JLabel();
        dateLabelDetail.setText("Label");
        detailsPanel.add(dateLabelDetail, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customerLabelDetail = new JLabel();
        customerLabelDetail.setText("Label");
        detailsPanel.add(customerLabelDetail, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customerNameLabel = new JLabel();
        customerNameLabel.setText("Client : ");
        detailsPanel.add(customerNameLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        typeDetail = new JLabel();
        typeDetail.setText("Label");
        detailsPanel.add(typeDetail, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        typeLabel = new JLabel();
        typeLabel.setText("Type d'achat : ");
        detailsPanel.add(typeLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doctorDetail = new JLabel();
        doctorDetail.setText("Label");
        detailsPanel.add(doctorDetail, new GridConstraints(3, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totatAmountDetail = new JLabel();
        totatAmountDetail.setText("Label");
        detailsPanel.add(totatAmountDetail, new GridConstraints(4, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        doctorNameLabel = new JLabel();
        doctorNameLabel.setText("Médecin : ");
        detailsPanel.add(doctorNameLabel, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        totalAmount = new JLabel();
        totalAmount.setText("Montant total : ");
        detailsPanel.add(totalAmount, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        medicinesLabel = new JLabel();
        medicinesLabel.setText("Médicaments prescrits :");
        detailsPanel.add(medicinesLabel, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        detailsPanel.add(scrollPane2, new GridConstraints(6, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        detailMedicinesArea = new JTextArea();
        detailMedicinesArea.setEditable(false);
        scrollPane2.setViewportView(detailMedicinesArea);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        detailsBtn = new JButton();
        detailsBtn.setText("Afficher le detail de l'achat");
        buttonPanel.add(detailsBtn);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

}
