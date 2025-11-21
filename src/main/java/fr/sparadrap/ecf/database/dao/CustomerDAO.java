package fr.sparadrap.ecf.database.dao;


import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CustomerDAO extends DAO<Customer>  {
    private static final Logger logger = LoggerFactory.getLogger(CustomerDAO.class);

    public CustomerDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Permet de creer un client dans la bdd
     * @param customer
     * @return boolean
     */
    @Override
    public boolean create(Customer customer)
    {
        boolean result = false;

        String sql = "INSERT INTO customers (last_name, first_name, nir, birth_date, phone, email, " +
                "address, post_code, city, mutual_insurance_id, doctor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
            mapCustomerToStatement(pstmt, customer);

            int rowsAffected = pstmt.executeUpdate();

            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        customer.setId(rs.getInt(1));
                    }
                }
                result = true;
            }

        } catch (Exception e) {
            logger.error("Erreur de connexion - create customer : {}", e.getMessage());
        }
        return result;
    }

    /**
     * Permet de mettre a jour les données d'un client dasn la base
     * @param customer
     */
    @Override
    public boolean update(Customer customer){
        boolean result = false;
        String sql = "UPDATE customers SET last_name=?, first_name=?, nir=?, birth_date=?, " +
                "phone=?, email=?, address=?, post_code=?, city=?, " +
                "mutual_insurance_id=?, doctor_id=? WHERE id="+customer.getId();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            mapCustomerToStatement(stmt, customer);
            stmt.executeUpdate();
            result = true;
        } catch (SQLException e) {;
            logger.error("Erreur de connexion -update customer : {}", e.getMessage());
        }
        return result;


    }



    /**
     * Permet de supprimer un client de la bdd via son id
     * @param p_id
     * @return
     * @throws SQLException
     */
    @Override
    public boolean deleteById(int p_id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        boolean result = false;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ;
            stmt.setInt(1, p_id);
            stmt.executeUpdate();
            result = true;
        }
        return result;
    }


    @Override
    public Customer findById(int p_id){
        logger.debug("Recherche du client avec l'ID: {}", p_id);
        Customer customer = new Customer();
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, m.company_name, m.reimbursement_rate, ");
        sql.append("d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, ");
        sql.append("d.birth_date AS doctor_birth_date, d.license_number ,");
        sql.append("FROM customers c ");
        sql.append("LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id ");
        sql.append("LEFT JOIN doctors d ON c.id = doctors.id ");
        sql.append("WHERE c.id = ?");

        try(PreparedStatement stmt = connection.prepareStatement(String.valueOf(sql));){

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               customer = mapResultSetToCustomer(rs);
            }
            logger.info("Client trouvé : {}", customer.getFullName());
            return customer;
        }catch(SQLException e){
            logger.error("Erreur lors de la recherche du client ID : {}", p_id, e);
        } catch (SaisieException e) {
            System.out.println("Erreur de saisie -findById : " + e.getMessage());
        }

        return null;
    }



    /**
     * Récupérer tous les clients
     */
    @Override
    public List<Customer> findAll() throws SQLException {

        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM v_customer_details ORDER BY last_name, first_name";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                try{
                    Customer customer = mapResultSetToCustomer(rs);
                    customers.add(customer);
                }catch(SaisieException e){
                    System.err.println("Client ignoré (NIR invalide) : " + e.getMessage());
                }

            }
        }
        return customers;
    }

    public List<Customer> findCustomersByDoctorID(int p_id) {
        String sql = "SELECT * FROM customers WHERE doctor_id = ?";
        List<Customer> customers = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, p_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (Exception e) {
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
        }
        return customers;
    }

    /**
     * Methode de cloture de la connexion
     */
    @Override
    public void closeConnection() throws SQLException {
        System.out.println("Closing connection : CustomerDAO");
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }



    /**
     * Remplit un PreparedStatement avec les données d'un Customer
     * Utilisable pour INSERT ou UPDATE selon la requête SQL
     */
    private void mapCustomerToStatement(PreparedStatement stmt, Customer customer) throws SQLException {
        int index = 1;
        // Champs principaux
        stmt.setString(index++, customer.getLastName());
        stmt.setString(index++, customer.getFirstName());
        stmt.setString(index++, customer.getNir());
        stmt.setDate(index++, Date.valueOf(customer.getBirthDate()));
        stmt.setString(index++, customer.getPhone());
        stmt.setString(index++, customer.getEmail());
        stmt.setString(index++, customer.getAddress());
        stmt.setString(index++, customer.getPostCode());
        stmt.setString(index++, customer.getCity());

        // Champs optionnels (Mutuelle, Médecin)
        if (customer.getMutualInsurance() != null) {
            stmt.setInt(index++, customer.getMutualInsurance().getId());
        } else {
            stmt.setNull(index++, Types.INTEGER);
        }

        if (customer.getDoctor() != null) {
            stmt.setInt(index++, customer.getDoctor().getId());
        } else {
            stmt.setNull(index++, Types.INTEGER);
        }
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