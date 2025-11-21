package fr.sparadrap.ecf.database.dao;


import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO extends DAO<Category> {


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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Category category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setCategoryName(rs.getString("category_name"));
                    category.setDescription(rs.getString("description"));
                    return category;
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
        System.out.println("Closing connection : CatDAO");
        if (connection != null) {
            connection.close();
        }
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

}
