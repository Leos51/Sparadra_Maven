package fr.sparadrap.ecf.database.dao;


import fr.sparadrap.ecf.database.dbConnection;
import fr.sparadrap.ecf.model.medicine.Category;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryDAO {

    public void createCategory(Category c) {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try(Connection connection = dbConnection.getInstanceDB()) {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<Category> findAll() throws SQLException, SaisieException {

        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";

        try(Connection conn = dbConnection.getInstanceDB()){
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
