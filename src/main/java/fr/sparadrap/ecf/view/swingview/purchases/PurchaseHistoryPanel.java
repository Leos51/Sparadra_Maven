package fr.sparadrap.ecf.view.swingview.purchases;

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
                try{
                    filterPurchases();
                }catch (SaisieException ex){
                    JOptionPane.showMessageDialog(null,"Erreur de saisie : " + ex.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }


    /**
     * Selectionne un achat dans le tableau
     * @return un achat enregistré
     */
    private Purchase  selectPurchase(){
        Purchase selectedPurchase = getSelectedItem(purchasesTable, (TableModele<Purchase>) purchasesTable.getModel());
        return selectedPurchase;
    };


    /**
     * Affiche le detail d'un achat qui a été séléctionné
     */
    private void displayPurchaseDetails() {

        if(selectPurchase()!=null){
            detailsPanel.setVisible(true);
            Purchase selectedPurchase = selectPurchase();
            Customer customer = selectedPurchase.getCustomer();

            detailsTitle.setText("Détails de l'achat");
            customerLabelDetail.setText( customer.getFullName());
            dateLabelDetail.setText(DateFormat.formatDate(selectedPurchase.getPurchaseDate(), "dd/MM/yyyy"));
            typeDetail.setText(selectedPurchase.isPrescriptionBased()? "Achat sur ordonnance" : "Achat direct");
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

        }else{
            detailsPanel.setVisible(false);
        }
    }

    /**
     * Met a jour la visibilité des Inputs en fonction de l'item selectionné dans la combobox
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
     * @param dateField Le nom de la JtextField
     */
    private void fillEmptyDateField(JTextField dateField){
        if(dateField.getText().trim().isEmpty()){
            dateField.setText(DateFormat.formatDate(LocalDate.now(), "dd/MM/yyyy"));
        }
    }

    /**
     * Remplit Les Input de la Periode avec la date de la plus ancienne facture et la date du jour
     */
    private void fillEmptyPeriodsFields(){
        fillEmptyDateField(periodEndField);
        if(periodStartField.getText().trim().isEmpty()){
            periodStartField.setText(DateFormat.formatDate(getOldestPurchaseDate(), "dd/MM/yyyy"));
        }
    }

    /**
     * Filtre l'historique d'achat en fonction de la selection dans la combobox
     * - aujourd'hui
     * - Date Precise
     * - Periode
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
                if (!Validator.isValidDate(dateField.getText())){
                    throw new SaisieException("Remplir le champ date au format : \"dd/MM/yyyy\" avant de filtrer les achats");
                }
                filteredPurchases = PurchasesList.findPurchaseByDate(dateField.getText());
                break;
            case "Période personnalisée":
                fillEmptyPeriodsFields();
                if(!Validator.isValidDate(periodStartField.getText()) || !Validator.isValidDate(periodEndField.getText())) {
                    throw new SaisieException("Remplir les champs date au format : \"dd/MM/yyyy\"");
                }

                filteredPurchases = PurchasesList.findPurchasebyPeriod(periodStartField.getText(), periodEndField.getText());
                break;
            default:
                filteredPurchases = PurchasesList.getPurchases();
                System.out.println("Aucun filtre appliqué");
                break;
        }
        purchases.configTable(filteredPurchases, HEADER_PURCHASES, PURCHASE_COLUMN_CLASSES );
    }


}
