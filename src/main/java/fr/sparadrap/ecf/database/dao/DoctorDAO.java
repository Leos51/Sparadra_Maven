package fr.sparadrap.ecf.database.dao;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO extends DAO<Doctor> {
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
        try(PreparedStatement pstmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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
        try(PreparedStatement stmt = connection.prepareStatement(query);){
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
        try(PreparedStatement stmt = connection.prepareStatement(query)) {
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

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
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
        try(PreparedStatement statement = connection.prepareStatement(query)){
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

    /**
     * Methode de cloture de la connexion
     */
    @Override
    public void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
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
}
