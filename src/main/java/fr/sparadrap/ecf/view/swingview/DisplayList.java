package fr.sparadrap.ecf.view.swingview;

import fr.sparadrap.ecf.database.dao.CustomerDAO;
import fr.sparadrap.ecf.model.lists.medicine.MedicineList;
import fr.sparadrap.ecf.model.lists.medicine.PrescriptionList;
import fr.sparadrap.ecf.model.lists.person.CustomersList;
import fr.sparadrap.ecf.model.lists.person.DoctorList;
import fr.sparadrap.ecf.model.lists.purchase.PurchasesList;
import fr.sparadrap.ecf.view.swingview.purchases.PurchaseManagementPanel;
import fr.sparadrap.ecf.view.swingview.tablemodele.TableModele;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DisplayList extends JPanel {
    JPanel listTitlePanel;
    JScrollPane scrollPane = new JScrollPane();
    JTable table = new JTable();
    String tableTitleBorder;


    public static final String[] HEADER_CUSTOMERS = new String[]{"Nom", "Email", "Téléphone", "Ville", "N° NIR"};
    public static final String[] HEADER_DOCTORS = new String[]{"Nom", "Email", "Téléphone", "Ville", "N° agréement"};
    public static final String[] HEADER_MEDICINE = new String[]{"Nom", "Categorie", "Prix", "Stock", "Date"};
    public static final String[] HEADER_CARTITEM = new String[]{"Nom", "Quantité", "Prix", "Total ligne"};
    public static final String[] HEADER_PURCHASES = new String[]{"Date", "Client", "Type", "Montant Total"};
    public static final String[] HEADER_PRESCRIPTIONS = new String[]{"Date", "Médecin", "Client", "Nombre de medicaments"};


    public static final Class<?>[] USER_COLUMN_CLASSES = {String.class, String.class, String.class, String.class, String.class};

    public static final Class<?>[] MEDICINE_COLUMN_CLASSES = {String.class, String.class, Double.class, Integer.class, String.class};
    private final Class<?>[] CARTITEM_COLUMN_CLASSES = {String.class, Integer.class, Double.class, Double.class};
    public static final Class<?>[] PURCHASE_COLUMN_CLASSES = {String.class, String.class, String.class, String.class, Double.class};
    public static final Class<?>[] PRESCRIPTION_COLUMN_CLASSES = {String.class, String.class, String.class, Integer.class};



    public DisplayList(int type)  {
        CustomerDAO customerDAO = new CustomerDAO();
        this.setLayout(new BorderLayout());
        JLabel tableLabel = new JLabel();
        tableLabel.setForeground(Color.white);
       this.add(tableLabel, BorderLayout.NORTH);
       this.add(scrollPane, BorderLayout.CENTER);
       scrollPane.setViewportView(table);

       try{
           switch (type) {
               case 0:
                   tableTitleBorder = "Liste des CLients";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(customerDAO.findAll(),HEADER_CUSTOMERS,USER_COLUMN_CLASSES);
                   System.out.println("NB clients:" + customerDAO.findAll().size());
                   break;
               case 1:
                   tableTitleBorder = "Liste des Medecins";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(DoctorList.getDoctors(),HEADER_DOCTORS,USER_COLUMN_CLASSES);
                   break;
               case 2:
                   tableTitleBorder = "Liste des médicaments";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(MedicineList.getMedicines(),HEADER_MEDICINE,MEDICINE_COLUMN_CLASSES);
                   break;
               case 3:
                   tableTitleBorder = "Panier";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(PurchaseManagementPanel.getCart() ,HEADER_CARTITEM,CARTITEM_COLUMN_CLASSES);
                   break;
               case 4:
                   tableTitleBorder = "Historique des achats";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(PurchasesList.getPurchases(),HEADER_PURCHASES,PURCHASE_COLUMN_CLASSES);
                   break;
               case 5:
                   tableTitleBorder = "Historique des prescriptions";
                   scrollPane.setBorder(BorderFactory.createTitledBorder(tableTitleBorder));
                   configTable(PrescriptionList.getPrescriptionList(),HEADER_PRESCRIPTIONS,PRESCRIPTION_COLUMN_CLASSES);
                   break;
               default:
                   break;
           }
       }catch(Exception e){
           System.out.println(e.getMessage());;
       }


    }


    public <T> void configTable(List<T> list, String[] headers, Class<?>[] columnClasses) {
        TableModel model = new TableModele<>(list, headers, columnClasses);
        this.table.setModel(model);
        this.table.revalidate();
        this.table.repaint();
    }

    public <T> void updateTable(List<T> filteredList, String[] headers, Class<?>[] columnClasses) {
        TableModel model = new TableModele<>(filteredList, headers, columnClasses);
        this.table.setModel(model);
        this.table.revalidate();
        this.table.repaint();
    }

    public JTable getTable(){
        return this.table;
    }

    public static <T> T getSelectedItem(JTable table, TableModele<T> model) {
        int row = table.getSelectedRow();
        if (row != -1) {
            return model.getData().get(row);
        }
        return null;
    }







}
