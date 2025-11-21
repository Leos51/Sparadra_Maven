package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static fr.sparadrap.ecf.utils.DateFormat.dateToString;

public class MedicineDAO {


    private static final Logger log = LoggerFactory.getLogger(MedicineDAO.class);

    public void create(Medicine medicine) throws SQLException, IOException, ClassNotFoundException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO medicines");
        sql.append(" (");
        sql.append("medicine_name, category_id, price, stock, ");
        sql.append("manufacture_date, expiry_date, requires_prescription)");
        sql.append(") ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?)");


        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, medicine.getMedicineName());
            stmt.setString(2, medicine.getCategory());
            stmt.setDouble(3, medicine.getPrice());
            stmt.setInt(4, medicine.getStock());/*
            stmt.setDate(5, Date.valueOf(medicine.getManufactureDate()));
            stmt.setDate(6, Date.valueOf(medicine.getExpiryDate()));
            stmt.setBoolean(7, medicine.requiresPrescription());*/
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                medicine.setId(rs.getInt(1));
            }
        } catch (Exception e) {
            log.error("e: ", e);

        }
    }

    /**
     * Récupérer tous les médicaments
     */
    public List<Medicine> findAll() throws SQLException {
        List<Medicine> medicines = new ArrayList<>();

        String sql = "SELECT m.*, c.category_name FROM medicines m JOIN categories c ON m.category_id = c.id";

        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(2));
                medicines.add(mapResultSetToMedicine(rs));
            }
        } catch (SaisieException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return medicines;
    }


    /**
     * Rechercher des médicaments
     */
    public List<Medicine> search(String searchTerm, Integer categoryId) throws SQLException, SaisieException {
        List<Medicine> medicines = new ArrayList<>();
        Medicine medicine = new Medicine();
        String sql = "CALL sp_search_medicines(?, ?)";

        try (Connection conn = DatabaseConnection.getConnection() ;
             CallableStatement stmt = conn.prepareCall(sql)) {

            if (searchTerm != null && !searchTerm.isEmpty()) {
                stmt.setString(1, searchTerm);
            } else {
                stmt.setNull(1, Types.VARCHAR);
            }

            if (categoryId != null) {
                stmt.setInt(2, categoryId);
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    medicines.add(mapResultSetToMedicine(rs));
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return medicines;
    }


    /**
     * Mettre à jour le stock
     */
    public boolean updateStock(int medicineId, int quantity) throws SQLException {
        String sql = "UPDATE medicines SET stock = stock - ? WHERE id = ? ";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, quantity);
            stmt.setInt(2, medicineId);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("médicament introuvable");
            }

            return true;
        }
    }

    private Medicine mapResultSetToMedicine(ResultSet rs) throws SQLException, SaisieException, IOException, ClassNotFoundException {
        Medicine medicine = new Medicine();
        Date sqlDate = rs.getDate("manufacture_date");
        medicine.setReleaseDate(dateToString(sqlDate));



        medicine.setId(rs.getInt("id"));
        medicine.setMedicineName(rs.getString("medicine_name"));
        int categoryId = rs.getInt("category_id");
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = categoryDAO.findById(categoryId);
        medicine.setCategory(category.getCategoryName());
        medicine.setPrice(rs.getDouble("price"));
        medicine.setStock(rs.getInt("stock"));



        return medicine;
    }

}

