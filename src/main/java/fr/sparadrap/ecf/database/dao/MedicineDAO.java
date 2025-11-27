package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static fr.sparadrap.ecf.utils.DateFormat.dateToString;

public class MedicineDAO extends DAO<Medicine> implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(MedicineDAO.class);

    public MedicineDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    public boolean create(Medicine medicine) throws SQLException {
        Boolean result = false;
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
            result = true;
        } catch (Exception e) {
            log.error("e: ", e);

        }
        return result;
    }

    /**
     * Permet de mettre a jour les données d'une entrée dasn la base
     *
     * @param obj
     */
    @Override
    public boolean update(Medicine obj) throws SQLException {
        return false;
    }

    /**
     * Permet de supprimer un objet de la bdd
     *
     * @param id
     */
    @Override
    public boolean deleteById(int id) throws SQLException {
        return false;
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
     * Permet de recuperer un objet via son id
     *
     * @param id
     * @return
     */
    @Override
    public Medicine findById(int id) throws SQLException {
        return null;
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
        try(CategoryDAO categoryDAO = new CategoryDAO()){
            Category category = categoryDAO.findById(categoryId);
            medicine.setCategory(category.getCategoryName());
            medicine.setPrice(rs.getDouble("price"));
            medicine.setStock(rs.getInt("stock"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ;




        return medicine;
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
     * @throws Exception if this resource cannot be closed
     * @apiNote While this interface method is declared to throw {@code
     * Exception}, implementers are <em>strongly</em> encouraged to
     * declare concrete implementations of the {@code close} method to
     * throw more specific exceptions, or to throw no exception at all
     * if the close operation cannot fail.
     *
     * <p> Cases where the close operation may fail require careful
     * attention by implementers. It is strongly advised to relinquish
     * the underlying resources and to internally <em>mark</em> the
     * resource as closed, prior to throwing the exception. The {@code
     * close} method is unlikely to be invoked more than once and so
     * this ensures that the resources are released in a timely manner.
     * Furthermore it reduces problems that could arise when the resource
     * wraps, or is wrapped, by another resource.
     *
     * <p><em>Implementers of this interface are also strongly advised
     * to not have the {@code close} method throw {@link
     * InterruptedException}.</em>
     * <p>
     * This exception interacts with a thread's interrupted status,
     * and runtime misbehavior is likely to occur if an {@code
     * InterruptedException} is {@linkplain Throwable#addSuppressed
     * suppressed}.
     * <p>
     * More generally, if it would cause problems for an
     * exception to be suppressed, the {@code AutoCloseable.close}
     * method should not throw it.
     *
     * <p>Note that unlike the {@link Closeable#close close}
     * method of {@link Closeable}, this {@code close} method
     * is <em>not</em> required to be idempotent.  In other words,
     * calling this {@code close} method more than once may have some
     * visible side effect, unlike {@code Closeable.close} which is
     * required to have no effect if called more than once.
     * <p>
     * However, implementers of this interface are strongly encouraged
     * to make their {@code close} methods idempotent.
     */
    @Override
    public void close(){
        try{
            super.closeConnection();
        } catch (SQLException e) {
            log.error("Erreur de fermeture connexion : {}", e.getMessage());
        }

    }
}

