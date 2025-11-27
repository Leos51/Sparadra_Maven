package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.model.person.MutualInsurance;
import fr.sparadrap.ecf.utils.exception.SaisieException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class CustomerDAO extends DAO<Customer> implements AutoCloseable {
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

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){
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

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
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
     */
    @Override
    public boolean deleteById(int p_id)  {
        String sql = "DELETE FROM customers WHERE id = ?";
        boolean result = false;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ;
            stmt.setInt(1, p_id);
            stmt.executeUpdate();
            result = true;
        }catch (SQLException e){
            logger.error("Erreur de connexion -delete customer : {}", e.getMessage());
        }
        return result;
    }


    @Override
    public Customer findById(int p_id){
        logger.debug("Recherche du client avec l'ID: {}", p_id);
        Customer customer = new Customer();
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, m.company_name, m.reimbursement_rate, ");
        sql.append("d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number ");
        sql.append("FROM customers c ");
        sql.append("LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id ");
        sql.append("LEFT JOIN doctors d ON c.id = d.id ");
        sql.append("WHERE c.id = ?");

        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(String.valueOf(sql));){

            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
               customer = mapResultSetToCustomer(rs);
            }
            logger.info("Client trouvé : {}", customer.getFullName());
            return customer;
        }catch(SQLException e){
            logger.error("Erreur lors de la recherche du client ID : {}", p_id, e);
        }

        return null;
    }



    /**
     * Récupérer tous les clients
     */
    @Override
    public List<Customer> findAll() {

        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM v_customer_details ORDER BY last_name, first_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = mapResultSetToCustomer(rs);
                customers.add(customer);
            }
        } catch (SQLException e) {
            logger.error("Erreur de connexion -findAll : {}", e.getMessage());
        }
        return customers;
    }

    public List<Customer> findCustomersByDoctorID(int p_id) {
        String sql = "SELECT * FROM customers WHERE doctor_id = ?";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, p_id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        } catch (Exception e) {
            logger.error("Erreur de connexion -findCustomersByDoctorID : {}", e.getMessage());
        }

        return customers;
    }

    /**
     * Rechercher des clients
     */
    public List<Customer> search(String searchTerm) {

        List<Customer> customers = new ArrayList<>();
        String sql = "CALL sp_search_customers(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }catch (SQLException e) {
            logger.error("Erreur de connexion -search : {}", e.getMessage());
        }

        return customers;
    }


    /**
     * Remplit un PreparedStatement avec les données d'un Customer
     * Utilisable pour INSERT ou UPDATE selon la requête SQL
     */
    private void mapCustomerToStatement(PreparedStatement stmt, Customer customer) {
        try{
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
        } catch (SQLException e) {
            logger.error("Erreur de connexion -mapCustomerToStatement : {}", e.getMessage());
        }
    }

    /**
     * Mapper un ResultSet vers un objet Customer
     */
    private Customer mapResultSetToCustomer(ResultSet rs)  {
        Customer customer = new Customer();
        try{
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
            customer.setDoctorByID(rs.getInt("doctor_id"));
        } catch (SQLException | SaisieException e) {
            logger.error("Erreur de connexion -mapResultSetToCustomer : {}", e.getMessage());
        }

        return customer;
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
        } catch (SQLException e) {
            logger.error("Erreur de closeConnection CustomerDAO : {}", e.getMessage());
        }

    }
}