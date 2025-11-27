package fr.sparadrap.ecf.view.swingview.tablemodele;

import fr.sparadrap.ecf.database.dao.CustomerDAO;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.model.purchase.CartItem;
import fr.sparadrap.ecf.model.purchase.Purchase;
import fr.sparadrap.ecf.utils.DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.AbstractTableModel;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TableModele<T> extends AbstractTableModel {
    private static final Logger log = LoggerFactory.getLogger(TableModele.class);
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final List<T> data;
    private final String[] columnNames;
    private final Class<?>[] columnClasses;
    private final DateTimeFormatter FORMATTER_DATE_FRENCH = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TableModele(List<T> data, String[] columnNames, Class<?>[] columnClasses) {
        this.data = data;
        this.columnNames = columnNames;
        this.columnClasses = columnClasses;
    }


    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    public List<T> getData() {
        return data;
    }


    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T item = data.get(rowIndex);
        // Add custom logic here to return the value based on the column index and the item type
        if (item instanceof Customer user) {
            return switch (columnIndex) {
                case 0 -> user.getFullName();
                case 1 -> user.getEmail();
                case 2 -> user.getPhone();
                case 3 -> user.getCity();
                case 4 -> user.getNir();
                case 5 -> {Doctor doctor = user.getDoctor();
                    yield doctor == null ? "Pas de medecin" : doctor.getFullName();
                }
                default -> null;
            };
        } else if (item instanceof Doctor doctor) {
            return switch (columnIndex) {
                case 0 -> doctor.getFullName();
                case 1 -> doctor.getEmail();
                case 2 -> doctor.getPhone();
                case 3 -> doctor.getCity();
                case 4 -> doctor.getRpps();

                default -> null;
            };
        } else if (item instanceof Medicine medicine) {
            return switch (columnIndex) {
                case 0 -> medicine.getMedicineName();
                case 1 -> medicine.getCategory();
                case 2 -> medicine.getPrice();
                case 3 -> medicine.getStock();
                case 4 -> DateFormat.formatDate(medicine.getReleaseDate(), "dd/MM/yyyy");
                default -> null;

            };
        }else if (item instanceof CartItem pm) {
            return switch (columnIndex) {
                case 0 -> pm.getMedicine().getMedicineName(); // Nom du médicament
                case 1 -> pm.getQuantity();                   // Quantité achetée
                case 2 -> pm.getPrice();        // Prix unitaire
                case 3 -> pm.getLinePrice(); // Prix total
                default -> null;
            };
        }else if (item instanceof Purchase purchase) {

            int customerId = purchase.getCustomerID();
                try {
                    CustomerDAO customerDAO = new CustomerDAO();
                    Customer user = customerDAO.findById(customerId);
                    return switch (columnIndex) {
                        case 0 -> DateFormat.formatDate(purchase.getPurchaseDate(), "dd/MM/yyyy");
                        case 1 -> (user != null) ? user.getFullName() : "Client inconnu";
                        case 2 -> purchase.isPrescriptionBased()? "Avec ordonnance" : "Direct";
                        case 3 -> String.format("%.2f €",purchase.getTotal());
                        default -> null;
                    };
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }




        }else if (item instanceof Prescription prescription) {
            return switch (columnIndex) {
                case 0 -> DateFormat.formatDate(prescription.getPrescriptingDate(), "dd/MM/yyyy");
                case 1 -> prescription.getDoctor().getFullName();
                case 2 -> prescription.getCustomer().getFullName();
                case 3 -> prescription.getPrescriptedMedicines().size();
                default -> null;
            };
        }
        return null;
    }



}
