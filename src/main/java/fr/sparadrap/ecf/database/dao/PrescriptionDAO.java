package fr.sparadrap.ecf.database.dao;

import fr.sparadrap.ecf.database.DatabaseConnection;
import fr.sparadrap.ecf.model.medicine.Medicine;
import fr.sparadrap.ecf.model.medicine.Prescription;
import fr.sparadrap.ecf.model.person.Customer;
import fr.sparadrap.ecf.model.person.Doctor;
import fr.sparadrap.ecf.utils.DateFormat;
import fr.sparadrap.ecf.utils.exception.SaisieException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO extends DAO<Prescription> implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionDAO.class);

    public PrescriptionDAO() throws SQLException, IOException, ClassNotFoundException {
    }

    /**
     * Crée une nouvelle ordonnance en base de données
     * @param prescription L'ordonnance à créer
     * @return true si succès
     */
    @Override
    public boolean create(Prescription prescription) throws SQLException {
        boolean result = false;
        String sqlPrescription = "INSERT INTO prescriptions (prescription_date, doctor_id, customer_id) VALUES (?, ?, ?)";
        String sqlPrescriptionItem = "INSERT INTO prescription_items (prescription_id, medicine_id) VALUES (?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insérer l'ordonnance
            try (PreparedStatement stmtPrescription = conn.prepareStatement(sqlPrescription, Statement.RETURN_GENERATED_KEYS)) {
                stmtPrescription.setDate(1, Date.valueOf(prescription.getPrescriptingDate()));
                stmtPrescription.setInt(2, prescription.getDoctor().getId());
                stmtPrescription.setInt(3, prescription.getCustomer().getId());

                int rowsAffected = stmtPrescription.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet rs = stmtPrescription.getGeneratedKeys()) {
                        if (rs.next()) {
                            int prescriptionId = rs.getInt(1);

                            // Insérer les médicaments prescrits
                            try (PreparedStatement stmtItem = conn.prepareStatement(sqlPrescriptionItem)) {
                                for (Medicine medicine : prescription.getPrescriptedMedicines()) {
                                    stmtItem.setInt(1, prescriptionId);
                                    stmtItem.setInt(2, medicine.getId());
                                    stmtItem.addBatch();
                                }
                                stmtItem.executeBatch();
                            }

                            conn.commit();
                            logger.info("Ordonnance créée avec l'ID: {}", prescriptionId);
                            result = true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            logger.error("Erreur lors de la création de l'ordonnance: {}", e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return result;
    }

    /**
     * Met à jour une ordonnance existante
     * @param prescription L'ordonnance à mettre à jour
     * @return true si succès
     */
    @Override
    public boolean update(Prescription prescription) throws SQLException {
        // Les ordonnances ne sont généralement pas modifiées une fois créées
        logger.warn("La mise à jour des ordonnances n'est pas supportée");
        return false;
    }

    /**
     * Supprime une ordonnance par son ID
     * @param id L'ID de l'ordonnance
     * @return true si succès
     */
    @Override
    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM prescriptions WHERE id = ?";
        boolean result = false;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Ordonnance supprimée - ID: {}", id);
                result = true;
            }
        }
        return result;
    }

    /**
     * Récupère toutes les ordonnances
     * @return Liste des ordonnances
     */
    @Override
    public List<Prescription> findAll() throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, ");
        sql.append("d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, ");
        sql.append("c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir ");
        sql.append("FROM prescriptions p ");
        sql.append("JOIN doctors d ON p.doctor_id = d.id ");
        sql.append("JOIN customers c ON p.customer_id = c.id ");
        sql.append("ORDER BY p.prescription_date DESC;");



        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString());
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Prescription prescription = mapResultSetToPrescription(rs);
                if (prescription != null) {
                    // Charger les médicaments de l'ordonnance
                    loadPrescriptionMedicines(conn, prescription, rs.getInt("id"));
                    prescriptions.add(prescription);
                }
            }
            logger.debug("{} ordonnances récupérées", prescriptions.size());
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération des ordonnances: {}", e.getMessage(), e);
        }
        return prescriptions;
    }

    /**
     * Récupère une ordonnance par son ID
     * @param id L'ID de l'ordonnance
     * @return L'ordonnance trouvée ou null
     */
    @Override
    public Prescription findById(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.*, ");
        sb.append("d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, ");
        sb.append("c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir ");
        sb.append("FROM prescriptions p ");
        sb.append("JOIN doctors d ON p.doctor_id = d.id ");
        sb.append("JOIN customers c ON p.customer_id = c.id ");
        sb.append("WHERE p.id = ?");

        String sql = sb.toString();



        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Prescription prescription = mapResultSetToPrescription(rs);
                    if (prescription != null) {
                        loadPrescriptionMedicines(conn, prescription, id);
                    }
                    return prescription;
                }
            }
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération de l'ordonnance: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * Récupère les ordonnances d'un client
     * @param customerId L'ID du client
     * @return Liste des ordonnances du client
     */
    public List<Prescription> findByCustomerId(int customerId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.*, ");
        sb.append("d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, ");
        sb.append("c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir ");
        sb.append("FROM prescriptions p ");
        sb.append("JOIN doctors d ON p.doctor_id = d.id ");
        sb.append("JOIN customers c ON p.customer_id = c.id ");
        sb.append("WHERE p.customer_id = ? ");
        sb.append("ORDER BY p.prescription_date DESC");

        String sql = sb.toString();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Prescription prescription = mapResultSetToPrescription(rs);
                    if (prescription != null) {
                        loadPrescriptionMedicines(conn, prescription, rs.getInt("id"));
                        prescriptions.add(prescription);
                    }
                }
            }
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération des ordonnances du client: {}", e.getMessage(), e);
        }
        return prescriptions;
    }

    /**
     * Récupère les ordonnances récentes d'un client (moins de 3 mois)
     * @param customerId L'ID du client
     * @return Liste des ordonnances récentes
     */
    public List<Prescription> findRecentByCustomerId(int customerId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT p.*, ");
        sb.append("d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, ");
        sb.append("c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir ");
        sb.append("FROM prescriptions p ");
        sb.append("JOIN doctors d ON p.doctor_id = d.id ");
        sb.append("JOIN customers c ON p.customer_id = c.id ");
        sb.append("WHERE p.customer_id = ? ");
        sb.append("AND p.prescription_date >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) ");
        sb.append("ORDER BY p.prescription_date DESC");

        String sql = sb.toString();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Prescription prescription = mapResultSetToPrescription(rs);
                    if (prescription != null) {
                        loadPrescriptionMedicines(conn, prescription, rs.getInt("id"));
                        prescriptions.add(prescription);
                    }
                }
            }
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération des ordonnances récentes: {}", e.getMessage(), e);
        }
        return prescriptions;
    }

    /**
     * Récupère les ordonnances d'un médecin
     * @param doctorId L'ID du médecin
     * @return Liste des ordonnances du médecin
     */
    public List<Prescription> findByDoctorId(int doctorId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT p.*, " +
                "d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, " +
                "c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.id " +
                "JOIN customers c ON p.customer_id = c.id " +
                "WHERE p.doctor_id = ? " +
                "ORDER BY p.prescription_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, doctorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Prescription prescription = mapResultSetToPrescription(rs);
                    if (prescription != null) {
                        loadPrescriptionMedicines(conn, prescription, rs.getInt("id"));
                        prescriptions.add(prescription);
                    }
                }
            }
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération des ordonnances du médecin: {}", e.getMessage(), e);
        }
        return prescriptions;
    }

    /**
     * Récupère les ordonnances par date
     * @param date La date de prescription
     * @return Liste des ordonnances de cette date
     */
    public List<Prescription> findByDate(LocalDate date) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT p.*, " +
                "d.id AS doctor_id, d.last_name AS doctor_last_name, d.first_name AS doctor_first_name, d.license_number, " +
                "c.id AS customer_id, c.last_name AS customer_last_name, c.first_name AS customer_first_name, c.nir " +
                "FROM prescriptions p " +
                "JOIN doctors d ON p.doctor_id = d.id " +
                "JOIN customers c ON p.customer_id = c.id " +
                "WHERE DATE(p.prescription_date) = ? " +
                "ORDER BY p.prescription_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Prescription prescription = mapResultSetToPrescription(rs);
                    if (prescription != null) {
                        loadPrescriptionMedicines(conn, prescription, rs.getInt("id"));
                        prescriptions.add(prescription);
                    }
                }
            }
        } catch (SaisieException e) {
            logger.error("Erreur lors de la récupération des ordonnances par date: {}", e.getMessage(), e);
        }
        return prescriptions;
    }

    /**
     * Vérifie si une ordonnance est encore valide (moins de 3 mois)
     * @param prescriptionId L'ID de l'ordonnance
     * @return true si valide
     */
    public boolean isPrescriptionValid(int prescriptionId) throws SQLException {
        String sql = "SELECT prescription_date FROM prescriptions WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, prescriptionId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate prescriptionDate = rs.getDate("prescription_date").toLocalDate();
                    LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);
                    return prescriptionDate.isAfter(threeMonthsAgo);
                }
            }
        }
        return false;
    }

    /**
     * Charge les médicaments d'une ordonnance
     */
    private void loadPrescriptionMedicines(Connection conn, Prescription prescription, int prescriptionId)
            throws SQLException, SaisieException {
        String sql = "SELECT m.* FROM medicines m " +
                "JOIN prescription_items pi ON m.id = pi.medicine_id " +
                "WHERE pi.prescription_id = ?";

        List<Medicine> medicines = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescriptionId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medicine medicine = new Medicine();
                    medicine.setId(rs.getInt("id"));
                    medicine.setMedicineName(rs.getString("medicine_name"));
                    medicine.setPrice(rs.getDouble("price"));
                    medicine.setStock(rs.getInt("stock"));

                    Date manufactureDate = rs.getDate("manufacture_date");
                    if (manufactureDate != null) {
                        medicine.setReleaseDate(DateFormat.formatDate(manufactureDate.toLocalDate(), "dd/MM/yyyy"));
                    }

                    medicines.add(medicine);
                }
            }
        }

        if (!medicines.isEmpty()) {
            prescription.setPrescriptedMedicines(medicines);
        }
    }

    /**
     * Mappe un ResultSet vers un objet Prescription
     */
    private Prescription mapResultSetToPrescription(ResultSet rs) throws SQLException, SaisieException {
        // Créer le médecin
        Doctor doctor = new Doctor();
        doctor.setId(rs.getInt("doctor_id"));
        doctor.setLastName(rs.getString("doctor_last_name"));
        doctor.setFirstName(rs.getString("doctor_first_name"));
        doctor.setRpps(rs.getString("license_number"));

        // Créer le client
        Customer customer = new Customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setLastName(rs.getString("customer_last_name"));
        customer.setFirstName(rs.getString("customer_first_name"));
        customer.setNir(rs.getString("nir"));

        // Récupérer la date
        Date prescriptionDate = rs.getDate("prescription_date");
        String dateStr = DateFormat.formatDate(prescriptionDate.toLocalDate(), "dd/MM/yyyy");

        // Créer une liste de médicaments vide (sera remplie après)
        List<Medicine> emptyMedicines = new ArrayList<>();
        // On ajoute un médicament temporaire pour éviter l'exception
        // Il sera remplacé par loadPrescriptionMedicines
        Medicine tempMedicine = new Medicine();
        tempMedicine.setMedicineName("temp");
        tempMedicine.setCategory("temp");
        tempMedicine.setPrice(0.01);
        tempMedicine.setReleaseDate("01/01/2000");
        tempMedicine.setStock(1);
        emptyMedicines.add(tempMedicine);

        // Créer l'ordonnance
        Prescription prescription = new Prescription(dateStr, doctor, customer, emptyMedicines);

        return prescription;
    }

    @Override
    public void close() throws Exception {
        try {
            super.closeConnection();
        } catch (SQLException e) {
            logger.error("Erreur de fermeture connexion PrescriptionDAO: {}", e.getMessage());
        }
    }
}