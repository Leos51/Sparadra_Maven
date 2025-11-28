package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.purchase.CartItem;
import fr.sparadrap.ecf.model.purchase.Purchase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO extends DAO<Purchase> implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseDAO.class);

    public PurchaseDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Permet de creer un objet dans la bdd
     *
     * @param purchase
     * @return boolean
     */
    @Override
    public boolean create(Purchase purchase) throws SQLException {
        boolean result = false;
        String sql = "CALL sp_create_purchase(?,?,?)";
        Connection conn = null;
        try{
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            try(CallableStatement stmt = conn.prepareCall(sql)) {

                //Param IN
                stmt.setInt(1, purchase.getCustomerID());
                stmt.setBoolean(2, purchase.isPrescriptionBased());

                //Param OUT
                stmt.registerOutParameter(3, Types.INTEGER);

                stmt.executeUpdate();

                int purchaseId = stmt.getInt(3);
                purchase.setId(purchaseId);

                // Insérer les items liés
                String itemSql = "CALL sp_add_purchase_item(?, ?, ?)";
                try (CallableStatement itemStmt = conn.prepareCall(itemSql)) {
                    for (CartItem item : purchase.getMedicines()) {
                        itemStmt.setInt(1, purchaseId);
                        itemStmt.setInt(2, item.getMedicine().getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }
                conn.commit();
                logger.info("Achat créé avec l'ID: {}", purchaseId);
                result = true;


            } catch (Exception e) {
                if(conn != null){
                    conn.rollback();
                }
                logger.error("Erreur lors de la création de l'achat: {}",e.getMessage(), e);
                throw new SQLException(e);
            }finally {
                if(conn != null){
                    conn.setAutoCommit(true);
                    conn.close();
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Permet de mettre a jour les données d'une entrée dasn la base
     *
     * @param purchase
     */
    @Override
    public boolean update(Purchase purchase) throws SQLException {
        String sql =  "UPDATE purchases SET is_prescription_based = ?, notes = ? WHERE id = ?";
        boolean result = false;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setBoolean(1, purchase.isPrescriptionBased());
            stmt.setString(2, null);
            stmt.setInt(3, purchase.getId());
            int updateCount = stmt.executeUpdate();
            if(updateCount > 0){
                logger.info("Achat mis à jour - ID: {}", purchase.getId());
                result = true;
            }
        }
        return  result;

    }

    /**
     * Supprime un achat par son ID
     * @param id L'ID de l'achat
     * @return true si succès
     */
    @Override
    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM purchases WHERE id = ?";
        boolean result = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            result = rowsAffected > 0;

            if (result) {
                logger.info("Achat supprimé - ID: {}", id);
            }
        }
        return result;
    }

    /**
     * Permet de recuperer tout les objets
     *
     * @return
     */
    @Override
    public List<Purchase> findAll() throws SQLException {
        String sql = "SELECT * FROM purchases  ORDER BY purchase_date DESC";
        List<Purchase> purchases = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setId(rs.getInt("id"));
                    purchase.setCustomer(rs.getInt("customer_id"));
                    purchase.setPrescriptionBased(rs.getBoolean("is_prescription_based"));
                    purchase.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
                    purchases.add(purchase);
                }
            }
        }
        return purchases;
    }

    /**
     * Récupère un achat par son ID
     * @param id L'ID de l'achat
     * @return L'achat trouvé ou null
     */
    @Override
    public Purchase findById(int id) throws SQLException {
        String sql = "SELECT * FROM purchases WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setId(rs.getInt("id"));
                    purchase.setCustomer(rs.getInt("customer_id"));
                    purchase.setPrescriptionBased(rs.getBoolean("is_prescription_based"));
                    purchase.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
                    loadPurchaseItems(conn, purchase);
                    return purchase;
                }
            }
        }
        return null;
    }

    private void loadPurchaseItems(Connection conn, Purchase purchase) {

    }

    /**
     * Methode de cloture de la connexion
     */
    @Override
    public void closeConnection() throws SQLException {

    }

    public List<Purchase> findByCustomerID(int p_id) {
        String sql = "SELECT * FROM purchases WHERE customer_id = ?";
        List<Purchase> purchases = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, p_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Purchase purchase = new Purchase();
                    purchase.setId(rs.getInt("id"));
                    purchase.setCustomer(rs.getInt("customer_id"));
                    purchase.setPrescriptionBased(rs.getBoolean("is_prescription_based"));
                    purchases.add(purchase);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return purchases;
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
    public void close() {
        try{
            super.closeConnection();
        }catch(SQLException e){
            logger.error("Erreur de fermeture connexion : {}", e.getMessage());
        }
    }
}
