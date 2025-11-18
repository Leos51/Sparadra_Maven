package fr.sparadrap.ecf.view.swingview.purchases;

import com.intellij.uiDesigner.core.Spacer;
import fr.sparadrap.ecf.database.dao.CategoryDAO;
import fr.sparadrap.ecf.database.dao.CustomerDAO;
import fr.sparadrap.ecf.model.lists.medicine.CategoriesList;
import fr.sparadrap.ecf.model.lists.medicine.MedicineList;
import fr.sparadrap.ecf.model.lists.medicine.PrescriptionList;
import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.purchase.PurchasesList;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.model.purchase.CartItem;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import fr.sparadrap.ecf.view.swingview.DisplayList;
import fr.sparadrap.ecf.view.swingview.PrescriptionCreationPanel;
import fr.sparadrap.ecf.view.swingview.tablemodele.TableModele;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import static fr.sparadrap.ecf.view.swingview.DisplayList.*;

public class PurchaseManagementPanel extends JPanel {
    private static List<CartItem> cart = new ArrayList<>();

    private Customer selectedCustomer;
    private Medicine selectedMedicine;
    private Prescription selectedPrescription;
    private CartItem selectedItem;


    Component PurchaseManagementPanel;
    private JPanel purchasePanel;
    private JPanel titlePanel;

    private JPanel centerPanel;
    private JPanel buttonPanel;
    private JPanel customerPanel;
    private JPanel customerSearchPanel;
    private JPanel cartPanel;
    private JPanel leftPanel;
    private JPanel medicinePanel;
    private JPanel cartButtonPanel;
    private JPanel bottomPanel;

    private JLabel titleLabel;

    private JButton validatePurchaseBtn, annulerButton;
    private JButton removeFromCartBtn;
    private JButton selectCustomerButton;

    private JTextField customerSearchField;

    private JSpinner quantitySpinner;


    private JComboBox categoryComboBox;
    private JPanel customerTablePanel;
    private JPanel medicineTablePanel;
    private JPanel medicineSearchPanel;
    private JPanel cartTablePanel;
    private JPanel totalPanel;
    private JLabel totalLabel;
    private JPanel InformationsPanel;
    private JTextArea infoArea;
    private JLabel customerSelectedLabel;
    private JButton addToCartBtn;
    private JButton searchMedicineBtn;
    private JButton searchCustomerBtn;
    private JTextField medicineSearchField;
    private JButton createPrescriptionBtn;
    private JButton addItemCartBtn;
    private JButton removeUnitItemCartBtn;


    // Panneaux avec DisplayList
    private DisplayList medicineDisplayList;
    DisplayList customerDisplayList;
    private DisplayList cartDisplayList;

    JTable customerTable;
    JTable medicineTable;
    JTable cartTable;

    Boolean hasPrescription;


    Purchase currentPurchase = new Purchase();


    public PurchaseManagementPanel(Boolean hasPrescription) {
        this.hasPrescription = hasPrescription;

        this.add(purchasePanel);
        if (hasPrescription) {
            this.titleLabel.setText("Achat avec ordonnance!");
        } else {
            this.titleLabel.setText("Achat direct!");
        }
        try {
            initialize();
            cartDisplayList.getTable().getSelectionModel().addListSelectionListener(e -> {
                boolean selected = customerDisplayList.getTable().getSelectedRow() != -1;
                addItemCartBtn.setEnabled(selected);
                removeFromCartBtn.setEnabled(selected);
                removeUnitItemCartBtn.setEnabled(selected);
            });
            updateButtonStates();
        } catch (Exception e) {
            System.out.println("Erreur init" + e.getMessage());
        }


        selectCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectCustomer();
                updateCustomerInfo(selectedCustomer);
                updateButtonStates();
            }
        });
        addToCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addToCart();
                updateButtonStates();
            }
        });
        removeFromCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFromCart();
                updateButtonStates();
            }
        });
        annulerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelPurchase();
            }
        });
        validatePurchaseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validatePurchase();
            }
        });
        customerSearchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        searchCustomerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                searchCustomers();
            }
        });
        searchMedicineBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //searchMedicines();
                List<Medicine> tempList = searchMedicinesByCategory();
                searchMedicinesWithCategoryFilter(tempList);

            }
        });
        createPrescriptionBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPrescriptionDialog();
            }
        });

        addItemCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCartItem();

            }
        });
        removeUnitItemCartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCartItem();
            }
        });
    }

    public static List<CartItem> getCart() {
        return cart;
    }

    public void setCart(List<CartItem> cart) {
        this.cart = cart;
    }


    private void initialize() throws SaisieException, SQLException, IOException, ClassNotFoundException {
        CategoryDAO categoryDAO = new CategoryDAO();

        //Combobox Selection des categories des médicaments
        Category allCategories = new Category("Catégorie : Toutes", null);
        categoryComboBox.addItem(allCategories.getCategoryName());
        try {
            for (Category c : categoryDAO.findAll()) {
                categoryComboBox.addItem(c.getCategoryName());
            }
        } catch (Exception e) {
            System.out.println("Erreur init" + e.getMessage());
        }


        // Utilisation de DisplayList pour les clients (type 0)
        customerDisplayList = new DisplayList(0);

        customerTablePanel.add(customerDisplayList);
        // Utilisation de DisplayList pour les médicaments (type 2)
        medicineDisplayList = new DisplayList(2);
        medicineTablePanel.add(medicineDisplayList);
        // Utilisation de DisplayList pour le panier (type 3)
        cartDisplayList = new DisplayList(3);
        cartTablePanel.add(cartDisplayList);

        customerTable = customerDisplayList.getTable();
        medicineTable = medicineDisplayList.getTable();
        cartTable = cartDisplayList.getTable();


        quantitySpinner.setModel(new SpinnerNumberModel(1, 0, 100, 1));

    }

    private void updateCustomerInfo(Customer selectedCustomer) {

        if (selectedCustomer != null) {

            if (infoArea != null) {
                StringBuilder info = new StringBuilder();
                info.append("Client: ").append(selectedCustomer.getFullName()).append("\n");

                if (hasPrescription) {
                    if (selectedCustomer.getMutualInsurance() != null) {
                        double rate = selectedCustomer.getMutualInsurance().getReimbursementRate();
                        info.append("Mutuelle: ").append(selectedCustomer.getMutualInsurance().getCompagnyName()).append("\n");
                        info.append("Taux de remboursement: ").append((int) (rate * 100)).append("%");
                    } else {
                        info.append("Aucune mutuelle\n");
                        info.append("Pas de remboursement");
                    }
                } else {
                    if (selectedCustomer.getMutualInsurance() != null) {
                        info.append("Mutuelle: ").append(selectedCustomer.getMutualInsurance().getCompagnyName()).append("\n");
                        info.append("Pas de remboursement (Pas de prescription)");
                    } else {
                        info.append("Aucune mutuelle\n");
                        info.append("Pas de remboursement");
                    }
                }


                infoArea.setText(info.toString());
            }
        }
    }

    private void selectCustomer() {
        selectedCustomer = getSelectedItem(customerTable, (TableModele<Customer>) customerTable.getModel());
        if (selectedCustomer != null) {
            customerSelectedLabel.setText("Client: " + selectedCustomer);
            customerSelectedLabel.setForeground(new Color(0, 128, 0));

            currentPurchase.setCustomer(selectedCustomer);
            currentPurchase.setPrescriptionBased(hasPrescription);

            // Mettre à jour les informations du client
            updateCustomerInfo(selectedCustomer);

            // Si achat avec ordonnance, charger les ordonnances du client
            if (hasPrescription) {
                loadCustomerPrescriptions();
            }

            updateButtonStates();
        }
    }

    private void addToCart() {
        selectedMedicine = getSelectedItem(medicineDisplayList.getTable(), (TableModele<Medicine>) medicineDisplayList.getTable().getModel());
        int quantity = (Integer) quantitySpinner.getValue();
        if (!(quantity > 0)) {
            JOptionPane.showMessageDialog(this,
                    "Quantité invalide " + quantity,
                    "Erreur de quantité", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Vérifier le stock
        if (quantity > selectedMedicine.getStock()) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuffisant! Stock disponible: " + selectedMedicine.getStock(),
                    "Stock insuffisant", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verifier si Item deja au panier
        CartItem existingItem = cart.stream()
                .filter(item -> item.getMedicine().equals(selectedMedicine))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > selectedMedicine.getStock()) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuffisant! Stock disponible: " + selectedMedicine.getStock() +
                                ", Quantité déjà dans le panier: " + existingItem.getQuantity(),
                        "Stock insuffisant", JOptionPane.WARNING_MESSAGE);
                return;
            }
            existingItem.setQuantity(newQuantity);
            updateTotal();


        } else {
            cart.add(new CartItem(selectedMedicine, quantity, selectedMedicine.getPrice()));
            updateTotal();
        }
        medicineTable.repaint();
        medicineTable.revalidate();
        cartTable.repaint();
        cartTable.revalidate();
    }

    private void removeFromCart() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le Panier est deja vide!");
            return;
        }
        CartItem selectedItem = getSelectedItem(cartTable, (TableModele<CartItem>) cartTable.getModel());
        int selectedRow = cartTable.getSelectedRow();
        if (selectedRow != -1) {
            CartItem removed = cart.remove(selectedRow);
            updateTotal();
            cartTable.repaint();
            cartTable.revalidate();

            JOptionPane.showMessageDialog(this,
                    removed.getMedicine().getMedicineName() + " retiré du panier!",
                    "Suppression réussie", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addCartItem() {
        CartItem selectedItem = getSelectedItem(cartTable, (TableModele<CartItem>) cartTable.getModel());

        if (selectedItem.getQuantity() >= selectedItem.getMedicine().getStock()) {
            JOptionPane.showMessageDialog(this,
                    "Stock insuffisant! Stock disponible: " + selectedMedicine.getStock(),
                    "Stock insuffisant", JOptionPane.WARNING_MESSAGE);
            return;
        }
        selectedItem.setQuantity(selectedItem.getQuantity() + 1);

        updateTotal();
        cartTable.repaint();
        cartTable.revalidate();
    }

    private void removeCartItem() {
        CartItem selectedItem = getSelectedItem(cartTable, (TableModele<CartItem>) cartTable.getModel());
        if (selectedItem.getQuantity() <= 1) {
            removeFromCart();
            updateButtonStates();
            return;
        } else {
            selectedItem.setQuantity(selectedItem.getQuantity() - 1);
        }
        updateTotal();
        cartTable.repaint();
        cartTable.revalidate();
    }


    private double calculateTotal() {
        return cart.stream().mapToDouble(CartItem::getLinePrice).sum();
    }

    private void updateTotal() {
        double total = calculateTotal();
        totalLabel.setText("Total: " + total);

        // Afficher aussi le montant après remboursement
        if (selectedCustomer != null && selectedCustomer.getMutualInsurance() != null && currentPurchase.isPrescriptionBased()) {
            double reimbursement = total * selectedCustomer.getMutualInsurance().getReimbursementRate();
            double finalAmount = total - reimbursement;
            totalLabel.setText("Total: " + (total) +
                    "\nÀ payer: " + finalAmount);
        }
    }

    private void validatePurchase() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le panier est vide!", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Si achat avec ordonnance, valider la conformité
        if (hasPrescription && !validatePrescriptionCompliance()) {
            return;
        }

        // Calculer le total et créer le résumé
        double total = calculateTotal();
        double reimbursement = 0;
        if (selectedCustomer.getMutualInsurance() != null && currentPurchase.isPrescriptionBased()) {
            reimbursement = total * selectedCustomer.getMutualInsurance().getReimbursementRate();
        }

        double finalAmount = total - reimbursement;

        StringBuilder summary = new StringBuilder();
        summary.append("=== RÉSUMÉ DE L'ACHAT ===\n\n");
        summary.append("Client: ").append(selectedCustomer.getFullName()).append("\n");
        summary.append("Type d'achat: ").append(titleLabel.getText()).append("\n\n");

        summary.append("Détail des articles:\n");
        for (CartItem item : cart) {
            summary.append(String.format("• %s x%d = %s\n",
                    item.getMedicine().getMedicineName(),
                    item.getQuantity(),
                    item.getLinePrice()));
        }
        summary.append("\n");
        summary.append("Sous-total: ").append(total).append("\n");

        if (reimbursement > 0) {
            summary.append("Remboursement mutuelle: ").append(reimbursement).append("\n");
            summary.append("TOTAL À PAYER: ").append(finalAmount).append("\n");
        } else {
            summary.append("TOTAL À PAYER: ").append(total).append("\n");
        }
        int result = JOptionPane.showConfirmDialog(this,
                summary.toString(),
                "Confirmer l'achat",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            processPurchase();
        }
    }

    private void processPurchase() {
        // Ajouter les médicaments à l'achat et réduire les stocks
        for (CartItem item : cart) {
            currentPurchase.addMedicine(item.getMedicine(), item.getQuantity(), item.getLinePrice());
        }
        // Ajouter l'achat à la liste des achats
        PurchasesList.addPurchase(currentPurchase);
        JOptionPane.showMessageDialog(this, "Achat validé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
//        resetForm();
    }

    private void cancelPurchase() {
        if (!cart.isEmpty() || selectedCustomer != null) {
            int result = JOptionPane.showConfirmDialog(this,
                    "Êtes-vous sûr de vouloir annuler cet achat?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                resetForm();
            }
        }
    }

    private void resetForm() {
        selectedCustomer = null;
        currentPurchase = new Purchase();
        for (CartItem cartItem : cart) {
            cart.remove(cartItem);
        }
        customerSelectedLabel.setText("Aucun client sélectionné");
        customerSelectedLabel.setForeground(Color.RED);
        updateTotal();

        quantitySpinner.setValue(1);

        // Reset info area
        if (infoArea != null) {
            infoArea.setText("Sélectionnez un client pour voir les informations de remboursement.");
        }
    }

    /**
     * Creer une nouvelle liste client filtré
     */
    private void searchCustomers() {
        String search = customerSearchField.getText().trim();

        try {
            CustomerDAO customerDAO = new CustomerDAO();
            List<Customer> filteredList = customerDAO.search(search);
            customerDisplayList.configTable(filteredList, HEADER_CUSTOMERS, USER_COLUMN_CLASSES);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

    }


    /**
     * Crée une nouvelle liste de medicament fitlré
     */
    private void searchMedicines() {
        String search = medicineSearchField.getText().trim();
        List<Medicine> filteredList = MedicineList.filterMedicines(search);
        medicineDisplayList.configTable(filteredList, HEADER_MEDICINE, MEDICINE_COLUMN_CLASSES);
    }


    private List<Medicine> searchMedicinesByCategory() {

        String selectedCategory = categoryComboBox.getSelectedItem().toString().toLowerCase();
        List<Medicine> filteredList;
        if (selectedCategory.equals("catégorie : toutes")) {
            filteredList = MedicineList.getMedicines();
        } else {
            filteredList = MedicineList.getMedicines().stream()
                    .filter(item -> item
                            .getCategory()
                            .getCategoryName().toLowerCase()
                            .equals(selectedCategory))
                    .collect(Collectors.toList());
        }

        return filteredList;

        //medicineDisplayList.configTable(filteredList,HEADER_MEDICINE, MEDICINE_COLUMN_CLASSES);

    }

    private void searchMedicinesWithCategoryFilter(List<Medicine> filteredCategoryList) {
        String search = medicineSearchField.getText().trim();
        List<Medicine> filteredList = filteredCategoryList.stream().filter(item -> item.getMedicineName().toLowerCase().contains(search)).toList();
        medicineDisplayList.configTable(filteredList, HEADER_MEDICINE, MEDICINE_COLUMN_CLASSES);
    }

    private void updateButtonStates() {
        boolean hasCustomer = selectedCustomer != null;
        boolean hasPrescription = this.hasPrescription;
        boolean hasItemsInCart = !cart.isEmpty();

        addToCartBtn.setEnabled(hasCustomer);
        validatePurchaseBtn.setEnabled(hasCustomer && hasItemsInCart);
        createPrescriptionBtn.setVisible(hasPrescription);
        addItemCartBtn.setEnabled(hasItemsInCart);
        removeFromCartBtn.setEnabled(hasItemsInCart);
        removeUnitItemCartBtn.setEnabled(hasItemsInCart);
    }


    private void showPrescriptionDialog() {
        List<Prescription> prescriptions = PrescriptionList.findRecentPrescriptions(selectedCustomer);

        if (prescriptions.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Aucune ordonnance trouvée pour ce client.\nVoulez-vous créer une nouvelle ordonnance ?",
                    "Pas d'ordonnance", JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                openPrescriptionCreationDialog();
            }
            return;
        }

        Prescription selected = (Prescription) JOptionPane.showInputDialog(this,
                "Choisir une ordonnance :",
                "Sélection d'ordonnance",
                JOptionPane.QUESTION_MESSAGE,
                null,
                prescriptions.toArray(),
                prescriptions.get(0));

        if (selected != null) {
            selectedPrescription = selected;
            currentPurchase.setPrescription(selected);
            populateCartFromPrescription(selected);
        }
    }


    private boolean validatePrescriptionCompliance() {
        if (selectedPrescription == null) {
            JOptionPane.showMessageDialog(this,
                    "Aucune ordonnance sélectionnée pour cet achat avec ordonnance!",
                    "Ordonnance manquante",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<Medicine> prescribedMedicines = selectedPrescription.getPrescriptedMedicines();
        List<Medicine> cartMedicines = cart.stream()
                .map(CartItem::getMedicine)
                .collect(Collectors.toList());

        // Vérifier que tous les médicaments du panier sont dans l'ordonnance
        for (Medicine cartMedicine : cartMedicines) {
            if (!prescribedMedicines.contains(cartMedicine)) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Le médicament '" + cartMedicine.getMedicineName() +
                                "' n'est pas dans l'ordonnance!\n" +
                                "Voulez-vous continuer quand même ?",
                        "Non conforme à l'ordonnance",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (choice != JOptionPane.YES_OPTION) {
                    return false;
                }
            }
        }
        return true;
    }


    private void openPrescriptionCreationDialog() {
        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez d'abord sélectionner un client.",
                    "Client requis",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Créer et afficher le dialog
        PrescriptionCreationPanel dialog = new PrescriptionCreationPanel(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                selectedCustomer
        );
        dialog.setVisible(true);

        // Après fermeture du dialog, recharger les ordonnances du client
        loadCustomerPrescriptions();
    }

    private void loadCustomerPrescriptions() {
        if (selectedCustomer == null || !hasPrescription) {
            return;
        }

        List<Prescription> prescriptions = PrescriptionList.findRecentPrescriptions(selectedCustomer);

        if (prescriptions.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Aucune ordonnance récente trouvée pour ce client.\n" +
                            "Voulez-vous créer une nouvelle ordonnance ?",
                    "Pas d'ordonnance",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                openPrescriptionCreationDialog();
            }
            return;
        }

        // Afficher le dialog de sélection d'ordonnance
        showPrescriptionSelectionDialog(prescriptions);
    }

    private void showPrescriptionSelectionDialog(List<Prescription> prescriptions) {
        Prescription selected = (Prescription) JOptionPane.showInputDialog(this,
                "Choisir une ordonnance :",
                "Sélection d'ordonnance",
                JOptionPane.QUESTION_MESSAGE,
                null,
                prescriptions.toArray(),
                prescriptions.get(0));

        if (selected != null) {
            selectedPrescription = selected;
            currentPurchase.setPrescription(selected);
            populateCartFromPrescription(selected);
        }
    }

    private void populateCartFromPrescription(Prescription prescription) {
        cart.clear();

        for (Medicine medicine : prescription.getPrescriptedMedicines()) {
            // Vérifier le stock disponible
            if (medicine.getStock() > 0) {
                cart.add(new CartItem(medicine, 1, medicine.getPrice())); // Quantité par défaut à 1
            } else {
                JOptionPane.showMessageDialog(this,
                        "Le médicament '" + medicine.getMedicineName() +
                                "' prescrit n'est pas en stock.",
                        "Stock insuffisant",
                        JOptionPane.WARNING_MESSAGE);
            }
        }

        // Rafraîchir l'affichage du panier
        cartDisplayList.configTable(cart, DisplayList.HEADER_CARTITEM,
                new Class<?>[]{String.class, Integer.class, Double.class, Double.class});
        updateTotal();

        JOptionPane.showMessageDialog(this,
                "Médicaments de l'ordonnance ajoutés au panier.\n" +
                        "Vous pouvez modifier les quantités si nécessaire.",
                "Ordonnance chargée",
                JOptionPane.INFORMATION_MESSAGE);
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
        purchasePanel = new JPanel();
        purchasePanel.setLayout(new BorderLayout(0, 0));
        purchasePanel.setPreferredSize(new Dimension(1200, 750));
        purchasePanel.setRequestFocusEnabled(true);
        purchasePanel.setVerifyInputWhenFocusTarget(true);
        titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout(0, 0));
        purchasePanel.add(titlePanel, BorderLayout.NORTH);
        titleLabel = new JLabel();
        titleLabel.setHorizontalAlignment(0);
        titleLabel.setText("Achat de médicaments");
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(0, 0));
        purchasePanel.add(centerPanel, BorderLayout.CENTER);
        final JSplitPane splitPane1 = new JSplitPane();
        centerPanel.add(splitPane1, BorderLayout.CENTER);
        cartPanel = new JPanel();
        cartPanel.setLayout(new BorderLayout(0, 0));
        cartPanel.setEnabled(true);
        cartPanel.setPreferredSize(new Dimension(400, 0));
        splitPane1.setRightComponent(cartPanel);
        cartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "3. Panier d'Achat", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        cartButtonPanel = new JPanel();
        cartButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        cartPanel.add(cartButtonPanel, BorderLayout.NORTH);
        removeUnitItemCartBtn = new JButton();
        removeUnitItemCartBtn.setEnabled(false);
        removeUnitItemCartBtn.setText("-");
        removeUnitItemCartBtn.setToolTipText("Retirer 1 unité a la ligne sélectionné");
        cartButtonPanel.add(removeUnitItemCartBtn);
        addItemCartBtn = new JButton();
        addItemCartBtn.setEnabled(false);
        addItemCartBtn.setText("+");
        addItemCartBtn.setToolTipText("Ajouter 1 unité a la ligne");
        cartButtonPanel.add(addItemCartBtn);
        removeFromCartBtn = new JButton();
        removeFromCartBtn.setBackground(new Color(-13947600));
        removeFromCartBtn.setEnabled(false);
        removeFromCartBtn.setHorizontalAlignment(0);
        removeFromCartBtn.setText("poubelle");
        removeFromCartBtn.setToolTipText("Effacer la ligne selectionnée");
        cartButtonPanel.add(removeFromCartBtn);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout(0, 0));
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);
        totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel.add(totalPanel, BorderLayout.NORTH);
        totalPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Total", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        totalLabel = new JLabel();
        totalLabel.setHorizontalAlignment(4);
        totalLabel.setText("0.00");
        totalPanel.add(totalLabel, BorderLayout.NORTH);
        InformationsPanel = new JPanel();
        InformationsPanel.setLayout(new BorderLayout(0, 0));
        bottomPanel.add(InformationsPanel, BorderLayout.SOUTH);
        InformationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Informations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setText("Sélectionnez un client pour voir les informations de remboursement.");
        InformationsPanel.add(infoArea, BorderLayout.CENTER);
        cartTablePanel = new JPanel();
        cartTablePanel.setLayout(new BorderLayout(0, 0));
        cartPanel.add(cartTablePanel, BorderLayout.CENTER);
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setLeftComponent(leftPanel);
        final JSplitPane splitPane2 = new JSplitPane();
        splitPane2.setOrientation(0);
        leftPanel.add(splitPane2, BorderLayout.CENTER);
        medicinePanel = new JPanel();
        medicinePanel.setLayout(new BorderLayout(0, 0));
        medicinePanel.setEnabled(true);
        medicinePanel.setOpaque(true);
        medicinePanel.setVisible(true);
        splitPane2.setRightComponent(medicinePanel);
        medicinePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "2. Selection des médicaments", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, this.$$$getFont$$$(null, -1, -1, medicinePanel.getFont()), null));
        medicineSearchPanel = new JPanel();
        medicineSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        medicinePanel.add(medicineSearchPanel, BorderLayout.NORTH);
        categoryComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        categoryComboBox.setModel(defaultComboBoxModel1);
        medicineSearchPanel.add(categoryComboBox);
        final JLabel label1 = new JLabel();
        label1.setText("Rechercher:");
        medicineSearchPanel.add(label1);
        medicineSearchField = new JTextField();
        medicineSearchField.setMinimumSize(new Dimension(100, 25));
        medicineSearchField.setPreferredSize(new Dimension(100, 25));
        medicineSearchPanel.add(medicineSearchField);
        searchMedicineBtn = new JButton();
        searchMedicineBtn.setText("recherche");
        medicineSearchPanel.add(searchMedicineBtn);
        quantitySpinner = new JSpinner();
        quantitySpinner.setMaximumSize(new Dimension(150, 34));
        quantitySpinner.setPreferredSize(new Dimension(93, 34));
        medicineSearchPanel.add(quantitySpinner);
        addToCartBtn = new JButton();
        addToCartBtn.setBackground(new Color(-14513374));
        addToCartBtn.setEnabled(false);
        addToCartBtn.setText("Ajouter");
        medicineSearchPanel.add(addToCartBtn);
        medicineTablePanel = new JPanel();
        medicineTablePanel.setLayout(new BorderLayout(0, 0));
        medicinePanel.add(medicineTablePanel, BorderLayout.CENTER);
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        purchasePanel.add(buttonPanel, BorderLayout.SOUTH);
        createPrescriptionBtn = new JButton();
        createPrescriptionBtn.setEnabled(true);
        createPrescriptionBtn.setText("Ordonnance");
        createPrescriptionBtn.setVisible(false);
        buttonPanel.add(createPrescriptionBtn);
        annulerButton = new JButton();
        annulerButton.setBackground(new Color(-5658199));
        annulerButton.setText("Annuler");
        buttonPanel.add(annulerButton);
        validatePurchaseBtn = new JButton();
        validatePurchaseBtn.setBackground(new Color(-16744448));
        validatePurchaseBtn.setEnabled(false);
        validatePurchaseBtn.setSelected(false);
        validatePurchaseBtn.setText("Valider");
        buttonPanel.add(validatePurchaseBtn);
        customerPanel = new JPanel();
        customerPanel.setLayout(new BorderLayout(0, 0));
        customerPanel.setMinimumSize(new Dimension(340, 200));
        customerPanel.setPreferredSize(new Dimension(340, 200));
        buttonPanel.add(customerPanel);
        customerPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "1. Sélection du Client", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        customerSearchPanel = new JPanel();
        customerSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        customerPanel.add(customerSearchPanel, BorderLayout.NORTH);
        final JLabel label2 = new JLabel();
        label2.setText("Rechercher:");
        customerSearchPanel.add(label2);
        customerSearchField = new JTextField();
        customerSearchField.setPreferredSize(new Dimension(100, 25));
        customerSearchField.setText("");
        customerSearchPanel.add(customerSearchField);
        searchCustomerBtn = new JButton();
        searchCustomerBtn.setText("Search");
        customerSearchPanel.add(searchCustomerBtn);
        final Spacer spacer1 = new Spacer();
        customerSearchPanel.add(spacer1);
        final Spacer spacer2 = new Spacer();
        customerSearchPanel.add(spacer2);
        final Spacer spacer3 = new Spacer();
        customerSearchPanel.add(spacer3);
        selectCustomerButton = new JButton();
        selectCustomerButton.setBackground(new Color(-12156236));
        selectCustomerButton.setText("Selectionner le client");
        customerSearchPanel.add(selectCustomerButton);
        customerTablePanel = new JPanel();
        customerTablePanel.setLayout(new BorderLayout(0, 0));
        customerPanel.add(customerTablePanel, BorderLayout.CENTER);
        customerSelectedLabel = new JLabel();
        customerSelectedLabel.setForeground(new Color(-8388608));
        customerSelectedLabel.setText("Aucun client sélectionné");
        customerPanel.add(customerSelectedLabel, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return purchasePanel;
    }
}
