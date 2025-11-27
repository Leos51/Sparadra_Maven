package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.model.person.MutualInsurance;
import fr.sparadrap.ecf.utils.exception.DAOException;
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

        String insert_sql = "INSERT INTO customers (last_name, first_name, nir, birth_date, phone, email, " +
                "address, post_code, city, mutual_insurance_id, doctor_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(insert_sql, PreparedStatement.RETURN_GENERATED_KEYS)){

            mapCustomerToStatement(pstmt, customer);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        customer.setId(rs.getInt(1));
                        logger.info("Client créé avec l'ID: {}", customer.getId());
                    }
                }
                result = true;
            }

        } catch (SQLException e) {
            logger.error("Erreur lors de la création du client: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * Permet de mettre a jour les données d'un client dasn la base
     * @param customer le client à mettre à jour
     */
    @Override
    public boolean update(Customer customer){
        boolean result = false;
        String sql = "UPDATE customers SET last_name=?, first_name=?, nir=?, birth_date=?, " +
                "phone=?, email=?, address=?, post_code=?, city=?, " +
                "mutual_insurance_id=?, doctor_id=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            mapCustomerToStatement(stmt, customer);
            stmt.setInt(12, customer.getId());
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Client mis à jour - ID: {}", customer.getId());
                result = true;
            }
            logger.warn("Aucun client trouvé avec l'ID: {}", customer.getId());
        } catch (SQLException e) {;
            logger.error("Erreur lors de la mise à jour du client ID {}: {}", customer.getId(), e.getMessage(), e);
        }
        return result;
    }



    /**
     * Supprime un client par son ID.
     * @param p_id identifiant du client
     * @return true si la suppression a réussi
     */
    @Override
    public boolean deleteById(int p_id)  {
        String sql = "DELETE FROM customers WHERE id = ?";
        boolean result = false;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            ;
            stmt.setInt(1, p_id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Client supprimé - ID: {}", p_id);
                result = true;
            }
            logger.warn("Aucun client trouvé avec l'ID: {}", p_id);
        }catch (SQLException e){
            logger.error("Erreur lors de la suppression du client ID {}: {}", p_id, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Recherche un client par son ID.
     * @param p_id identifiant du client
     * @return le client trouvé ou null
     */
    @Override
    public Customer findById(int p_id){
        logger.debug("Recherche du client avec l'ID: {}", p_id);
        Customer customer = new Customer();
        StringBuilder sql = new StringBuilder(
                "SELECT c.*, m.company_name, m.reimbursement_rate, ");
        sql.append("d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number ");
        sql.append("FROM customers c ");
        sql.append("LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id ");
        sql.append("LEFT JOIN doctors d ON c.doctor_ID = d.id ");
        sql.append("WHERE c.id = ?");

        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(String.valueOf(sql));){

            stmt.setInt(1, p_id);
            try(ResultSet rs = stmt.executeQuery()){
                while(rs.next()){
                    customer = mapResultSetToCustomer(rs);
                    logger.info("Client trouvé : {}", customer.getFullName());
                    return customer;
                }
            }
            logger.debug("Aucun client trouvé avec l'ID: {}", p_id);

        }catch(SQLException | SaisieException e){
            logger.error("Erreur lors de la recherche du client ID : {}", p_id, e);
        }

        return null;
    }


    /**
     * Récupère tous les clients.
     * @return liste des clients
     */
    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM v_customer_details ORDER BY last_name, first_name";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
            logger.debug("{} clients récupérés", customers.size());
        } catch (SQLException | SaisieException e) {
            logger.error("Erreur lors de la récupération des clients: {}", e.getMessage(), e);
        }
        return customers;
    }

    /**
     * Recherche les clients d'un médecin.
     * @param doctorId identifiant du médecin
     * @return
     */
    public List<Customer> findCustomersByDoctorID(int doctorId) {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM v_customer_details WHERE doctor_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();){
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, doctorId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
            logger.debug("{} clients trouvés pour le médecin ID: {}", customers.size(), doctorId);
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche des clients du médecin ID {}: {}", doctorId, e.getMessage(), e);
        }

        return customers;
    }

    /**
     * Recherche des clients par terme de recherche.
     * @param searchTerm terme de recherche
     * @return liste des clients correspondants
     */
    public List<Customer> search(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }
        List<Customer> customers = new ArrayList<>();

        String sql = "CALL sp_search_customers(?)";

        try (Connection conn = DatabaseConnection.getConnection();
                CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, searchTerm.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            logger.debug("{} clients trouvés pour la recherche: '{}'", customers.size(), searchTerm);
        }catch (SQLException e) {
            logger.error("Erreur lors de la recherche '{}': {}", searchTerm, e.getMessage(), e);
        }

        return customers;
    }


    /**
     * Remplit un PreparedStatement avec les données d'un Customer.
     * Utilisé pour INSERT et UPDATE.
     * @param stmt le PreparedStatement à remplir
     * @param customer le client source
     * @throws SQLException en cas d'erreur SQL
     */
    private void mapCustomerToStatement(PreparedStatement stmt, Customer customer) throws SQLException {
            int index = 1;
            // Champs principaux
            stmt.setString(index++, customer.getLastName());
            stmt.setString(index++, customer.getFirstName());
            stmt.setString(index++, customer.getNir());
            stmt.setDate(index++, customer.getBirthDate() != null? Date.valueOf(customer.getBirthDate()) : null);
            stmt.setString(index++, customer.getPhone());
            stmt.setString(index++, customer.getEmail());
            stmt.setString(index++, customer.getAddress());
            stmt.setString(index++, customer.getPostCode());
            stmt.setString(index++, customer.getCity());

            // Mutuelle (optionnelle)
            if (customer.getMutualInsurance() != null) {
                stmt.setInt(index++, customer.getMutualInsurance().getId());
            } else {
                stmt.setNull(index++, Types.INTEGER);
            }
            // Médecin (optionnel)
            if (customer.getDoctor() != null) {
                stmt.setInt(index++, customer.getDoctor().getId());
            } else {
                stmt.setNull(index++, Types.INTEGER);
            }
    }

    /**
     * Mappe un ResultSet vers un Customer (champs de base uniquement)
     * @param rs le ResultSet à mapper
     * @return le Customer créé
     * @throws SaisieException,SQLException en cas d'erreur SQL
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SaisieException, SQLException {
        Customer customer = new Customer();
            customer.setId(rs.getInt("id"));
            customer.setLastName(rs.getString("last_name"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setNir(rs.getString("nir"));

            Date birthDate = rs.getDate("birth_date");
            if (birthDate != null) {
                customer.setBirthDate(birthDate.toLocalDate());
            }

            customer.setPhone(rs.getString("phone"));
            customer.setEmail(rs.getString("email"));
            customer.setAddress(rs.getString("address"));
            customer.setPostCode(rs.getString("post_code"));
            customer.setCity(rs.getString("city"));

        int doctorId = rs.getInt("doctor_id");
        if (!rs.wasNull() && doctorId > 0) {
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            customer.setDoctor(doctor);
        }
        // Récupérer mutual_insurance_id si présent
        try {
            int mutualId = rs.getInt("mutual_insurance_id");
            if (!rs.wasNull() && mutualId > 0) {
                MutualInsurance mutual = new MutualInsurance();
                mutual.setId(mutualId);
                customer.setMutualInsurance(mutual);
            }
        } catch (SQLException ignored) {
            // Colonne non présente dans ce ResultSet
        }

        return customer;
    }

    /**
     * Mappe un ResultSet vers un Customer avec les détails complets
     * (médecin et mutuelle inclus).
     *
     * @param rs le ResultSet à mapper
     * @return le Customer créé avec tous les détails
     * @throws SQLException en cas d'erreur SQL
     */
    private Customer mapResultSeyToCustomerFull(ResultSet rs) throws SaisieException, SQLException {
        Customer customer = mapResultSetToCustomer(rs);
        // Charger les détails du médecin

            String doctorLastName = rs.getString("doctor_last_name");
            if (doctorLastName != null) {
                Doctor doctor = customer.getDoctor();
                if (doctor == null) {
                    doctor = new Doctor();
                    customer.setDoctor(doctor);
                }
                doctor.setLastName(doctorLastName);
                doctor.setFirstName(rs.getString("doctor_first_name"));
                doctor.setRpps(rs.getString("license_number"));
            }

        String companyName = rs.getString("company_name");
        if (companyName != null) {
            MutualInsurance mutual = customer.getMutualInsurance();
            if (mutual == null) {
                mutual = new MutualInsurance();
                customer.setMutualInsurance(mutual);
            }
            mutual.setCompagnyName(companyName);
            mutual.setReimbursementRate(rs.getDouble("reimbursement_rate"));
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
    /**
     * Valide les données obligatoires d'un client.
     *
     * @param customer le client à valider
     * @throws DAOException si les données sont invalides
     */
    private void validateCustomer(Customer customer) throws DAOException {
        if (customer == null) {
            throw new DAOException("Le client ne peut pas être null");
        }
        if (customer.getLastName() == null || customer.getLastName().trim().isEmpty()) {
            throw new DAOException("Le nom du client est obligatoire");
        }
        if (customer.getFirstName() == null || customer.getFirstName().trim().isEmpty()) {
            throw new DAOException("Le prénom du client est obligatoire");
        }
        if (customer.getNir() == null || customer.getNir().trim().isEmpty()) {
            throw new DAOException("Le NIR du client est obligatoire");
        }
    }
}