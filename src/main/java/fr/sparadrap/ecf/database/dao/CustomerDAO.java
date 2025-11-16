package fr.sparadrap.ecf.database.dao;


import fr.sparadrap.ecf.database.dbConnection;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public void create(Customer customer)
    {

        PreparedStatement pstmt = null;

        String sql = "INSERT INTO customers (last_name, first_name, nir, birth_date, phone, email, " +
                "address, post_code, city, mutual_insurance_id, doctor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = dbConnection.getInstanceDB()){
            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, customer.getLastName());
            pstmt.setString(2, customer.getFirstName());
            pstmt.setString(3, customer.getNir());
            pstmt.setDate(4, Date.valueOf(customer.getBirthDate()));
            pstmt.setString(5, customer.getPhone());
            pstmt.setString(6, customer.getEmail());
            pstmt.setString(7, customer.getAddress());
            pstmt.setString(8, customer.getPostCode());
            pstmt.setString(9, customer.getCity());

            if (customer.getMutualInsurance() != null) {
                pstmt.setInt(10, customer.getMutualInsurance().getId());
            } else {
                pstmt.setNull(10, Types.INTEGER);
            }

            if (customer.getDoctor() != null) {
                pstmt.setInt(11, customer.getDoctor().getId());
            } else {
                pstmt.setNull(11, Types.INTEGER);
            }

            int rowsAffected = pstmt.executeUpdate();

            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        customer.setId(rs.getInt(1));
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Customer customer)
    {
        String sql = "UPDATE customers SET last_name=?, first_name=?, nir=?, birth_date=?, " +
                "phone=?, email=?, address=?, post_code=?, city=?, " +
                "mutual_insurance_id=?, doctor_id=? WHERE id=?";

        try (Connection conn = dbConnection.getInstanceDB()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, customer.getLastName());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getNir());
            stmt.setDate(4, Date.valueOf(customer.getBirthDate()));
            stmt.setString(5, customer.getPhone());
            stmt.setString(6, customer.getEmail());
            stmt.setString(7, customer.getAddress());
            stmt.setString(8, customer.getPostCode());
            stmt.setString(9, customer.getCity());

            if (customer.getMutualInsurance() != null) {
                stmt.setInt(10, customer.getMutualInsurance().getId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            if (customer.getDoctor() != null) {
                stmt.setInt(11, customer.getDoctor().getId());
            } else {
                stmt.setNull(11, Types.INTEGER);
            }

            stmt.setInt(12, customer.getId());

            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }
    /**
     * Supprimer un client
     */
    public void delete(int p_id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = dbConnection.getInstanceDB()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p_id);
            stmt.executeUpdate();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Customer findById(int p_id){
        Customer customer = new Customer();
        String sql = "SELECT c.*, m.company_name, m.reimbursement_rate, " +
                "d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, " +
                "d.license_number " +
                "FROM customers c " +
                "LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id " +
                "LEFT JOIN doctors d ON c.doctor_id = d.id " +
                "WHERE c.id = ?";
        try{
            Connection conn = dbConnection.getInstanceDB();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               customer = mapResultSetToCustomer(rs);
            }
            return customer;
        }catch(SQLException e){
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Récupérer tous les clients
     */
    public List<Customer> findAll() throws SQLException {

        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM v_customer_details ORDER BY last_name, first_name";

        try (Connection conn = dbConnection.getInstanceDB()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                try{
                    Customer customer = mapResultSetToCustomer(rs);
                    customers.add(customer);
                }catch(SaisieException e){
                    System.err.println("Client ignoré (NIR invalide) : " + e.getMessage());
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     * Rechercher des clients
     */
    public List<Customer> search(String searchTerm) throws SQLException {

        List<Customer> customers = new ArrayList<>();
        String sql = "CALL sp_search_customers(?)";

        try (Connection conn = dbConnection.getInstanceDB();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    /**
     * Mapper un ResultSet vers un objet Customer
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException, SaisieException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setLastName(rs.getString("last_name"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setNir(rs.getString("nir"));
        customer.setBirthDate(rs.getDate("birth_date").toLocalDate());
        customer.setPhone(rs.getString("phone"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setPostCode(rs.getString("post_code"));
        customer.setCity(rs.getString("city"));

        return customer;
    }


}