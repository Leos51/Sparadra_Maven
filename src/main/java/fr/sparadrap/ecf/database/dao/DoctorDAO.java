package fr.sparadrap.ecf.database.dao;
import fr.sparadrap.ecf.model.person.Doctor;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class DoctorDAO extends DAO<Doctor> {

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
        String query = "INSERT INTO Doctor (last_name, first_name, license_number, phone, email, address, post_code, city ) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
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
        return false;
    }

    /**
     * Permet de supprimer un médecin de la bdd
     * @param id
     */
    @Override
    public boolean deleteById(int id) throws SQLException {
        return false;
    }

    /**
     * Permet de recuperer tout les médecins
     * @return
     */
    @Override
    public List<Doctor> findAll() throws SQLException {
        return List.of();
    }

    /**
     * Permet de recuperer un médecin via son id
     *
     * @param id
     * @return
     */
    @Override
    public Doctor findById(int id) {
        return null;
    }

    /**
     * Methode de cloture de la connexion
     */
    @Override
    public void closeConnection() throws SQLException {

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
