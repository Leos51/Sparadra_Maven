package fr.sparadrap.ecf.database.dao;
import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO extends DAO<Doctor> implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDAO.class);

    public DoctorDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Permet de creer un médecin dans la bdd
     *
     * @param doctor
     * @return boolean
     */
    @Override
    public boolean create(Doctor doctor) throws SQLException {
        boolean result = false;
        String query = "INSERT INTO doctors (last_name, first_name, license_number, phone, email, address, post_code, city ) VALUES (?,?,?,?,?,?,?,?)";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
            mapDoctorToStatement(pstmt, doctor);

            int rowsAffected = pstmt.executeUpdate();

            // Récupérer l'ID généré
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        doctor.setId(rs.getInt(1));
                    }
                }
                result = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Permet de mettre a jour les données d'une entrée dasn la base
     *
     * @param doctor
     */
    @Override
    public boolean update(Doctor doctor) throws SQLException {
        boolean result = false;
        String sql = "UPDATE doctors SET last_name=?, first_name=?, license_number=?, " +
                "phone=?, email=?, address=?, post_code=?, city=? WHERE id="+doctor.getId();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            mapDoctorToStatement(stmt, doctor);
            stmt.executeUpdate();
            result = true;
        } catch (SQLException e) {;
            logger.error("Erreur de connexion -update doctor: {}", e.getMessage());
        }
        return result;
    }

    /**
     * Permet de supprimer un médecin de la bdd
     * @param p_id
     */
    @Override
    public boolean deleteById(int p_id) throws SQLException {
        String sql = "DELETE FROM doctors WHERE id = ?";
        boolean result = false;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p_id);
            int affectedRows = stmt.executeUpdate();
            result = (affectedRows > 0);
            if (result) {
                logger.info("Suppression du docteur avec ID {}", p_id);
            }else{
                logger.warn("Erreur suppression docteur avec ID {}", p_id);
            }

        }

        return result;
    }

    /**
     * Permet de recuperer tout les médecins
     * @return
     */
    @Override
    public List<Doctor> findAll() throws SQLException {

        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors ORDER BY last_name, first_name";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);){
            ResultSet rs = stmt.executeQuery();
            System.out.println(rs);
            while (rs.next()) {
                Doctor doctor = mapResultSetToDoctor(rs);
                doctors.add(doctor);
            }
        } catch (SaisieException e) {
            System.out.println(e.getMessage());;
        }
        return doctors;
    }

    public Doctor findDoctorById(int p_id) throws SQLException {
        logger.debug("Recherche du client avec l'ID: {}", p_id);
        Doctor doctor = new Doctor();
        String query = "SELECT * FROM doctors WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, p_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctor =  mapResultSetToDoctor(rs);
                System.out.println(rs.getInt("id"));

            }
            logger.info("Médecin trouvé : {}", doctor.getFullName());
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return doctor;
    }

    /**
     * Rechercher des medecins
     */
    public List<Doctor> search(String p_search_term) throws SQLException {

        List<Doctor> doctors = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM doctors ");
        sql.append("WHERE last_name LIKE CONCAT('%', ?, '%')");
        sql.append("OR first_name LIKE CONCAT('%', ?, '%')");
        sql.append("OR city LIKE CONCAT('%', ?, '%')");
        sql.append("OR license_number LIKE CONCAT('%', ?, '%');");

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            stmt.setString(1, p_search_term);
            stmt.setString(2, p_search_term);
            stmt.setString(3, p_search_term);
            stmt.setString(4, p_search_term);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    doctors.add(mapResultSetToDoctor(rs));
                }
            } catch (SaisieException e) {
                System.out.println(e.getMessage());
            }
        }
        return doctors;
    }

    private Doctor mapResultSetToDoctor(ResultSet rs) throws SQLException, SaisieException {
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("id"));

        doctor.setLastName(rs.getString("last_name"));
        doctor.setFirstName(rs.getString("first_name"));
        doctor.setRpps(rs.getString("license_number"));
        doctor.setPhone(rs.getString("phone"));
        doctor.setEmail(rs.getString("email"));
        doctor.setAddress(rs.getString("address"));
        doctor.setPostCode(rs.getString("post_code"));
        doctor.setCity(rs.getString("city"));

        return doctor;


    }

    /**
     * Permet de recuperer un médecin via son id
     *
     * @param id
     * @return
     */
    @Override
    public Doctor findById(int id) {
        logger.debug("Recherche du médecin avec l'ID: {}", id);
        Doctor doctor = new Doctor();
        String query = "SELECT * FROM doctors WHERE id = ?";
        try(Connection conn = DatabaseConnection.getConnection();
                PreparedStatement statement = conn.prepareStatement(query)){
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                doctor =  mapResultSetToDoctor(rs);
            }
        }catch(Exception e){
            logger.error("Erreur searchDoctors : " + e.getMessage());
        }
        return doctor;
    }


    private void mapDoctorToStatement(PreparedStatement stmt, Doctor doctor) throws SQLException {
        int index = 1;
        // Champs principaux
        stmt.setString(index++, doctor.getLastName());
        stmt.setString(index++, doctor.getFirstName());
        stmt.setString(index++, doctor.getRpps());
        stmt.setString(index++, doctor.getPhone());
        stmt.setString(index++, doctor.getEmail());
        stmt.setString(index++, doctor.getAddress());
        stmt.setString(index++, doctor.getPostCode());
        stmt.setString(index++, doctor.getCity());

    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     *
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
