package fr.sparadrap.ecf.view.swingview.customer;

import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.view.swingview.DisplayList;
import fr.sparadrap.ecf.view.swingview.tablemodele.TableModele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import static fr.sparadrap.ecf.view.swingview.DisplayList.*;

public class CustomersPanel extends JPanel {
    private JPanel customersPanel;
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
    private JTable customersTable;
    private JScrollPane scrollPane;
    private JPanel tablePanel;
    private JPanel left;
    private JPanel right;
    private JTable customerTable;
    private DisplayList customerDisplayList;
    public enum FormModes{
        ADD,
        EDIT
    }

    public CustomersPanel(){

        this.setLayout(new GridLayout(1,1));
        customersPanel = new JPanel();
        this.add(customersPanel);
        customerDisplayList = new DisplayList(0);
        tablePanel.add(customerDisplayList);


        customerDisplayList.getTable().getSelectionModel().addListSelectionListener(e -> {
            boolean selected = customerDisplayList.getTable().getSelectedRow() != -1;
            showDetailsBtn.setEnabled(selected);
            editButton.setEnabled(selected);
            deleteButton.setEnabled(selected);
        });

        showDetailsBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCustomersDetails();

            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCustomer();
            }
        });

        deleteButton.addActionListener(e-> {
            deleteCustomer();
        });


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchCustomers();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCustomer();
            }
        });
    }

    /**
     * recherche des patients dans le tableau  et affiche un nouveau tableau filtré
     */
    private void searchCustomers() {
        String search = searchField.getText().trim();
        List<Customer> filteredList = CustomersList.filterCustomers(search);
        customerDisplayList.configTable(filteredList,HEADER_CUSTOMERS, USER_COLUMN_CLASSES);
    }


    /**
     * Met a jour la visibilité des boutons
     */
    private void updateButtonsState() {
        boolean hasCustomers = !CustomersList.getCustomers().isEmpty();
        boolean hasSelection = customersTable.getSelectedRow() != -1;
        showDetailsBtn.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
        editButton.setEnabled(hasSelection);
        searchButton.setEnabled(true);
        addButton.setEnabled(true);
    }

    /**
     * affiche le detail du client séléctionné
     */
    private void showCustomersDetails(){
        Customer selectedCustomer = getSelectedCustomer();
        if(selectedCustomer == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un client","Aucun client selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(customersPanel, selectedCustomer.showDetails(), "Détails du CLient",  JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * selectionne un client
     * @return le client
     */
    private Customer getSelectedCustomer(){
        return getSelectedItem(customerDisplayList.getTable(), (TableModele<Customer>) customerDisplayList.getTable().getModel());

    }


    /**
     * affiche la fenetre d'ajout d'un nouveau patient
     */
    private void addCustomer(){
        CustomerFormPanel formPanel = new CustomerFormPanel(new Customer(), FormModes.ADD);
        formPanel.setVisible(true);
        formPanel.pack();
    }


    /**
     * affiche la fenetre d'edition d'un patient existant
     */
    private void editCustomer(){
        Customer selectedCustomer = getSelectedCustomer();
        if(selectedCustomer == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un client","Aucun client selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        CustomerFormPanel formPanel = new CustomerFormPanel(selectedCustomer, FormModes.EDIT);
        formPanel.setVisible(true);
        formPanel.pack();
    }

    /**
     * supprime le patient selectionné
     */
    private void deleteCustomer(){
        Customer selectedCustomer = getSelectedCustomer();
        if(selectedCustomer == null){
            JOptionPane.showMessageDialog(this, "Selectionner d'abord un client","Aucun client selectionné",  JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment supprimer le client \"" + selectedCustomer.getFullName() + "\" ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            CustomersList.removeCustomer(selectedCustomer);
            repaint();
            revalidate();
            updateButtonsState();
        }
    }

    /**
     * Remise a zero du tableau
     */
    private void refreshTable(){
        customerDisplayList.configTable(CustomersList.getCustomers(),HEADER_CUSTOMERS, USER_COLUMN_CLASSES);
    }
}


