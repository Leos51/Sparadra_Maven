package fr.sparadrap.ecf.database.dao;


import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO extends DAO<Category> implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDAO.class);

    public CategoryDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    public void createCategory(Category c) {

    }

    /**
     * Permet de creer un objet dans la bdd
     * @param c
     * @return boolean
     */
    @Override
    public boolean create(Category c) throws SQLException {
        boolean result = false;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try(Connection connection = DatabaseConnection.getConnection()) {
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, c.getCategoryName());
            pstmt.setString(2, c.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        c.setId(rs.getInt(1));
                    }
                }
            }
            result = true;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally{
            return result;
        }
    }

    /**
     * Permet de mettre a jour les données d'une entrée dasn la base
     *
     * @param obj
     */
    @Override
    public boolean update(Category obj) {
        return false;
    }

    /**
     * Permet de supprimer un objet de la bdd
     *
     * @param id
     */
    @Override
    public boolean deleteById(int id) {
        return false;
    }

    @Override
    public List<Category> findAll() {

        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try(Connection conn = DatabaseConnection.getConnection()){
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(mapResultSetToCategories(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return categories;
    }

    /**
     * Permet de recuperer un objet via son id
     *
     * @param id
     * @return
     */
    @Override
    public Category findById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategories(rs);
                }
            } catch (SaisieException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return null; // si aucune catégorie trouvée
    }

    /**
     * Methode de cloture de la connexion
     */
    @Override
    public void closeConnection() throws SQLException {

    }


    /*public Category findById(int id) {
        PreparedStatement pstmt = null;
        Category c = null;
        String sql = "SELECT * FROM categories WHERE id = ?";
        return c;
    }*/

    private Category mapResultSetToCategories(ResultSet rs) throws SQLException, SaisieException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setCategoryName(rs.getString("category_name"));
        c.setDescription(rs.getString("description"));

        return c;

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
        }catch(Exception e){
            logger.error("Erreur close : " + e.getMessage());
        }
    }
}
