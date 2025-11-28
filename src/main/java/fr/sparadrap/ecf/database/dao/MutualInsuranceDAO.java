package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.person.MutualInsurance;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MutualInsuranceDAO extends DAO<MutualInsurance> {
    private static final Logger logger = LoggerFactory.getLogger(MutualInsuranceDAO.class);
    public MutualInsuranceDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Permet de creer un objet dans la bdd
     *
     * @param mutualInsurance
     * @return boolean
     */
    @Override
    public boolean create(MutualInsurance mutualInsurance) throws SQLException {
        boolean result = false;
        String sql = "INSERT INTO mutual_insurances (company_name, reimbursement_rate, phone, email, address) " +
                "VALUES (?, ?, ?, ?, ?)";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mutualInsurance.getCompagnyName());
            stmt.setDouble(2, mutualInsurance.getReimbursementRate());
            stmt.setString(3, mutualInsurance.getPhone());
            stmt.setString(4, mutualInsurance.getEmail());
            stmt.setString(5, mutualInsurance.getAddress());

            int row = stmt.executeUpdate();
            if (row > 0) {
                try(ResultSet resultSet = stmt.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        mutualInsurance.setId(resultSet.getInt(1));
                        logger.info("Mutuelle créée avec l'ID: {}", mutualInsurance.getId());
                    }
                }
                result = true;
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la création de la mutuelle: {}", e.getMessage(), e);
        }
        return  result;
    }

    /**
     * Met à jour une mutuelle existante
     * @param mutualInsurance La mutuelle à mettre à jour
     * @return true si succès
     */
    @Override
    public boolean update(MutualInsurance mutualInsurance) {
        boolean result = false;
        String sql = "UPDATE mutual_insurances SET company_name=?, reimbursement_rate=?, phone=?, email=?, address=? WHERE id=?";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, mutualInsurance.getCompagnyName());
            stmt.setDouble(2, mutualInsurance.getReimbursementRate());
            stmt.setString(3, mutualInsurance.getPhone());
            stmt.setString(4, mutualInsurance.getEmail());
            stmt.setString(5, mutualInsurance.getAddress());
            stmt.setInt(6, mutualInsurance.getId());

            int row = stmt.executeUpdate();
            if (row > 0) {
                logger.info("Mutuelle mise à jour - ID: {}", mutualInsurance.getId());
                result = true;
            }else {
                logger.warn("Aucune mutuelle trouvée avec l'ID: {}", mutualInsurance.getId());
            }
        }catch (SQLException e) {
            logger.error("Erreur lors de la mise à jour de la mutuelle ID {}: {}",
                    mutualInsurance.getId(), e.getMessage(), e);
        }
        return  result;
    }

    /**
     * Supprime une mutuelle par son ID
     * @param id L'ID de la mutuelle
     * @return true si succès
     */
    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM mutual_insurances WHERE id = ?";
        boolean result = false;

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int row = stmt.executeUpdate();
            if (row > 0) {
                logger.info("Mutuelle supprimée - ID: {}", id);
                result = true;
            }else{
                logger.warn("Aucune mutuelle trouvé avec l'ID: {}", id);
            }
        }catch (SQLException e) {
            logger.error("Erreur lors de la suppression de la mutuelle ID {}: {}", id, e.getMessage(), e);
        }
        return result;
    }

    /**
     * Récupère toutes les mutuelles
     * @return Liste des mutuelles
     */
    @Override
    public List<MutualInsurance> findAll() throws SQLException {
        List<MutualInsurance> mutualInsurances = new ArrayList<>();
        String sql = "SELECT * FROM mutual_insurances ORDER BY company_name";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){

            while (rs.next()) {
                mutualInsurances.add(mapResultSetToMutualInsurance(rs));
            }
            logger.debug("{} mutuelles récupérées",  mutualInsurances.size());
        }catch (SQLException e) {
            logger.error("Erreur lors de la récupration des mutuelles");
        } catch (SaisieException e) {
            throw new RuntimeException(e);
        }
        return mutualInsurances;
    }


    /**
     * Récupère une mutuelle par son ID
     * @param id L'ID de la mutuelle
     * @return La mutuelle trouvée ou null
     */
    @Override
    public MutualInsurance findById(int id) {
        String sql = "SELECT * FROM mutual_insurances WHERE id = ?";

        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    MutualInsurance mutualInsurance = mapResultSetToMutualInsurance(rs);
                    logger.debug("Mutuelle trouvée: {}", mutualInsurance.getCompagnyName());
                    return mutualInsurance;
                }
            }
            logger.debug("Aucune mutuelle trouvé avec l'ID: {}", id);
        }catch (SQLException | SaisieException e) {
            logger.error("Erreur lors du mapping de la mutuelle : {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Recherche une mutuelle par son nom
     * @param name Le nom de la mutuelle
     * @return La mutuelle trouvée ou null
     */
    public MutualInsurance findByName(String name) {
        String sql = "SELECT * FROM mutual_insurances WHERE company_name = ?";

        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);

            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()) {
                    return mapResultSetToMutualInsurance(rs);
                }
            }
        } catch (SQLException | SaisieException e) {
            logger.error("Erreur lors de la recherche de la mutuelle par nom: {}", e.getMessage(), e);
        }

        return  null;
    }

    /**
     * Recherche des mutuelles par terme de recherche
     * @param searchTerm Le terme de recherche
     * @return Liste des mutuelles correspondantes
     */
    public List<MutualInsurance> search(String searchTerm) throws SQLException {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findAll();
        }

        List<MutualInsurance> mutualInsurances = new ArrayList<>();
        String sql = "SELECT * FROM mutual_insurances WHERE company_name LIKE ? ORDER BY company_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + searchTerm.trim() + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    mutualInsurances.add(mapResultSetToMutualInsurance(rs));
                }
            } catch (SaisieException e) {
                throw new RuntimeException(e);
            }
            logger.debug("{} mutuelles trouvées pour la recherche: '{}'", mutualInsurances.size(), searchTerm);
        }catch (SQLException e) {
            logger.error("Erreur lors de la recherche '{}': {}", searchTerm, e.getMessage(), e);
        }
        return mutualInsurances;
    }

    /**
     * Mappe un ResultSet vers un objet MutualInsurance
     * @param rs Le ResultSet
     * @return L'objet MutualInsurance
     */
    private MutualInsurance mapResultSetToMutualInsurance(ResultSet rs) throws SQLException, SaisieException {
        MutualInsurance mutualInsurance = new MutualInsurance();
        mutualInsurance.setId(rs.getInt("id"));
        mutualInsurance.setCompagnyName(rs.getString("company_name"));
        mutualInsurance.setReimbursementRate(rs.getDouble("reimbursement_rate"));

        String phone = rs.getString("phone");
        if (phone != null && !phone.isEmpty()) {
            mutualInsurance.setPhone(phone);
        }

        String email = rs.getString("email");
        if (email != null && !email.isEmpty()) {
            mutualInsurance.setEmail(email);
        }

        String address = rs.getString("address");
        if (address != null) {
            mutualInsurance.setAddress(address);
        }

        return mutualInsurance;
    }

    @Override
    public void close() {
        try {
            super.closeConnection();
        } catch (SQLException e) {
            logger.error("Erreur de fermeture connexion MutualInsuranceDAO: {}", e.getMessage());
        }
    }

}
