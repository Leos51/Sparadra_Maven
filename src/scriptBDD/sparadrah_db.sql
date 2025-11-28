
DROP DATABASE IF EXISTS sparadrah_db;
CREATE DATABASE sparadrah_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sparadrah_db;

-- ============================================
-- TABLE: MUTUELLES
-- ============================================
CREATE TABLE mutual_insurances (
                                   id INT PRIMARY KEY AUTO_INCREMENT,
                                   company_name VARCHAR(100) NOT NULL UNIQUE,
                                   reimbursement_rate DECIMAL(5,4) NOT NULL,
                                   phone VARCHAR(20),
                                   email VARCHAR(100),
                                   address VARCHAR(255),
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- TABLE: MEDECINS
-- ============================================
CREATE TABLE doctors (
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         last_name VARCHAR(100) NOT NULL,
                         first_name VARCHAR(100) NOT NULL,
                         license_number VARCHAR(50) NOT NULL UNIQUE,
                         phone VARCHAR(20),
                         email VARCHAR(100),
                         address VARCHAR(255),
                         post_code VARCHAR(10),
                         city VARCHAR(100),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- TABLE: CLIENTS
-- ============================================
CREATE TABLE customers (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           last_name VARCHAR(100) NOT NULL,
                           first_name VARCHAR(100) NOT NULL,
                           nir VARCHAR(15) NOT NULL UNIQUE,
                           birth_date DATE NOT NULL,
                           phone VARCHAR(20) NOT NULL,
                           email VARCHAR(100) NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           post_code VARCHAR(10) NOT NULL,
                           city VARCHAR(100) NOT NULL,
                           mutual_insurance_id INT,
                           doctor_id INT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (mutual_insurance_id) REFERENCES mutual_insurances(id) ON DELETE SET NULL,
                           FOREIGN KEY (doctor_id) REFERENCES doctors(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================
-- TABLE: CATEGORIES DE MEDICAMENTS
-- ============================================
CREATE TABLE categories (
                            id INT PRIMARY KEY AUTO_INCREMENT,
                            category_name VARCHAR(100) NOT NULL UNIQUE,
                            description TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ============================================
-- TABLE: MEDICAMENTS
-- ============================================
CREATE TABLE medicines (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           medicine_name VARCHAR(200) NOT NULL,
                           category_id INT NOT NULL,
                           price DECIMAL(10,2) NOT NULL,
                           stock INT NOT NULL DEFAULT 0,
                           manufacture_date DATE,
                           expiry_date DATE,
                           description TEXT,
                           requires_prescription BOOLEAN DEFAULT FALSE,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================
-- TABLE: ACHATS (FACTURES)
-- ============================================
CREATE TABLE purchases (
                           id INT PRIMARY KEY AUTO_INCREMENT,
                           customer_id INT NOT NULL,
                           purchase_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           is_prescription_based BOOLEAN DEFAULT FALSE,
                           total_amount DECIMAL(10,2) NOT NULL,
                           reimbursement_amount DECIMAL(10,2) DEFAULT 0,
                           final_amount DECIMAL(10,2) NOT NULL,
                           notes TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================
-- TABLE: LIGNES D'ACHAT (ITEMS DU PANIER)
-- ============================================
CREATE TABLE purchase_items (
                                id INT PRIMARY KEY AUTO_INCREMENT,
                                purchase_id INT NOT NULL,
                                medicine_id INT NOT NULL,
                                quantity INT NOT NULL,
                                unit_price DECIMAL(10,2) NOT NULL,
                                line_total decimal(10,2) NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE,
                                FOREIGN KEY (medicine_id) REFERENCES medicines(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ============================================
-- DONNEES DE TEST
-- ============================================

-- Insertion des mutuelles
INSERT INTO mutual_insurances (company_name, reimbursement_rate, phone, email) VALUES
                                                                                   ('Mutuelle Générale', 0.7500, '0140506070', 'contact@mutuelle-generale.fr'),
                                                                                   ('Harmonie Mutuelle', 0.8000, '0141516171', 'info@harmonie.fr'),
                                                                                   ('MGEN', 0.7000, '0142526272', 'service@mgen.fr'),
                                                                                   ('Axa Santé', 0.6500, '0143536373', 'contact@axa-sante.fr'),
                                                                                   ('Pas de mutuelle', 0.0000, NULL, NULL);

-- Insertion des catégories
INSERT INTO categories (category_name, description) VALUES
                                                        ('Analgésique', 'Médicaments contre la douleur'),
                                                        ('Antibiotique', 'Médicaments contre les infections bactériennes'),
                                                        ('Anti-inflammatoire', 'Médicaments réduisant l''inflammation'),
                                                        ('Antihistaminique', 'Médicaments contre les allergies'),
                                                        ('Antitussif', 'Médicaments contre la toux'),
                                                        ('Vitamines', 'Compléments vitaminiques'),
                                                        ('Dermatologie', 'Médicaments pour la peau');

-- Insertion des médecins
INSERT INTO doctors (last_name, first_name, license_number, phone, email, address, post_code, city) VALUES
                                                                                                        ('Dupont', 'Marie', '10123456789', '0601020304', 'marie.dupont@medical.fr', '15 Rue de la Santé', '75014', 'Paris'),
                                                                                                        ('Martin', 'Pierre', '10234567890', '0605060708', 'pierre.martin@medical.fr', '28 Avenue des Soins', '69001', 'Lyon'),
                                                                                                        ('Dubois', 'Sophie', '10345678901', '0609101112', 'sophie.dubois@medical.fr', '42 Boulevard Medical', '13001', 'Marseille'),
                                                                                                        ('Bernard', 'Luc', '10456789012', '0611223344', 'luc.bernard@medical.fr', '10 Rue des Lilas', '31000', 'Toulouse'),
                                                                                                        ('Petit', 'Claire', '10567890123', '0622334455', 'claire.petit@medical.fr', '5 Place de la République', '44000', 'Nantes'),
                                                                                                        ('Robert', 'Jean', '10678901234', '0633445566', 'jean.robert@medical.fr', '18 Rue Victor Hugo', '67000', 'Strasbourg'),
                                                                                                        ('Moreau', 'Isabelle', '10789012345', '0644556677', 'isabelle.moreau@medical.fr', '22 Avenue de la Liberté', '06000', 'Nice'),
                                                                                                        ('Laurent', 'Thomas', '10890123456', '0655667788', 'thomas.laurent@medical.fr', '7 Boulevard des Sciences', '33000', 'Bordeaux'),
                                                                                                        ('Simon', 'Julie', '10901234567', '0666778899', 'julie.simon@medical.fr', '3 Rue des Médecins', '34000', 'Montpellier'),
                                                                                                        ('Michel', 'Antoine', '11012345678', '0677889900', 'antoine.michel@medical.fr', '12 Rue Pasteur', '35000', 'Rennes'),
                                                                                                        ('Lefevre', 'Emma', '11123456789', '0688990011', 'emma.lefevre@medical.fr', '25 Rue de l’Hôpital', '80000', 'Amiens'),
                                                                                                        ('Garcia', 'Paul', '11234567890', '0699001122', 'paul.garcia@medical.fr', '8 Rue des Jardins', '72000', 'Le Mans');

-- Insertion des médicaments
INSERT INTO medicines (medicine_name, category_id, price, stock, manufacture_date, expiry_date, requires_prescription) VALUES
                                                                                                                           ('Paracétamol 500mg', 1, 3.50, 150, '2024-01-15', '2026-01-15', FALSE),
                                                                                                                           ('Doliprane 1000mg', 1, 4.20, 100, '2024-02-01', '2026-02-01', FALSE),
                                                                                                                           ('Amoxicilline 500mg', 2, 8.90, 80, '2024-01-10', '2025-07-10', TRUE),
                                                                                                                           ('Ibuprofène 400mg', 3, 5.50, 120, '2024-03-01', '2026-03-01', FALSE),
                                                                                                                           ('Cétirizine 10mg', 4, 6.80, 90, '2024-02-15', '2026-02-15', FALSE),
                                                                                                                           ('Sirop Contre la Toux', 5, 7.50, 60, '2024-01-20', '2025-06-20', FALSE),
                                                                                                                           ('Vitamine C 1000mg', 6, 9.90, 200, '2024-04-01', '2027-04-01', FALSE),
                                                                                                                           ('Crème Hydratante', 7, 12.50, 75, '2024-03-10', '2026-03-10', FALSE),
                                                                                                                           ('Aspirine 500mg', 1, 3.20, 140, '2024-01-05', '2026-01-05', FALSE),
                                                                                                                           ('Augmentin 1g', 2, 15.60, 50, '2024-02-20', '2025-08-20', TRUE);

-- Insertion des clients
INSERT INTO customers (last_name, first_name, nir, birth_date, phone, email, address, post_code, city, mutual_insurance_id, doctor_id) VALUES
                                                                                                                                           ('Leroy', 'Jean', '175028912345678', '1975-02-28', '0612345678', 'jean.leroy@email.fr', '10 Rue des Lilas', '75015', 'Paris', 1, 1),
                                                                                                                                           ('Bernard', 'Claire', '285057823456789', '1985-05-07', '0623456789', 'claire.bernard@email.fr', '25 Avenue Victor Hugo', '69002', 'Lyon', 2, 2),
                                                                                                                                           ('Petit', 'Marc', '190103934567890', '1990-10-03', '0634567890', 'marc.petit@email.fr', '8 Place de la République', '13002', 'Marseille', 3, 3),
                                                                                                                                           ('Moreau', 'Julie', '295126745678901', '1995-12-06', '0645678901', 'julie.moreau@email.fr', '33 Rue du Commerce', '31000', 'Toulouse', 1, 1),
                                                                                                                                           ('Laurent', 'Paul', '188034556789012', '1988-03-04', '0656789012', 'paul.laurent@email.fr', '17 Boulevard Saint-Michel', '44000', 'Nantes', 4, NULL);

-- Insertion d'achats exemples
INSERT INTO purchases (customer_id, purchase_date, is_prescription_based, total_amount, reimbursement_amount, final_amount) VALUES
                                                                                                                                (1, '2024-11-01 10:30:00', TRUE, 24.40, 18.30, 6.10),
                                                                                                                                (2, '2024-11-05 14:15:00', FALSE, 13.30, 0.00, 13.30),
                                                                                                                                (3, '2024-11-08 09:45:00', TRUE, 15.60, 10.92, 4.68),
                                                                                                                                (1, '2024-11-10 16:20:00', FALSE, 7.50, 0.00, 7.50),
                                                                                                                                (4, '2024-11-12 11:00:00', TRUE, 28.80, 21.60, 7.20);

-- Insertion des lignes d'achat
INSERT INTO purchase_items (purchase_id, medicine_id, quantity, unit_price, line_total) VALUES
-- Achat 1 (client 1, avec ordonnance)
(1, 3, 2, 8.90, 17.80),
(1, 5, 1, 6.80, 6.80),
-- Achat 2 (client 2, sans ordonnance)
(2, 1, 2, 3.50, 7.00),
(2, 9, 2, 3.20, 6.40),
-- Achat 3 (client 3, avec ordonnance)
(3, 10, 1, 15.60, 15.60),
-- Achat 4 (client 1, sans ordonnance)
(4, 6, 1, 7.50, 7.50),
-- Achat 5 (client 4, avec ordonnance)
(5, 3, 2, 8.90, 17.80),
(5, 4, 2, 5.50, 11.00);

-- ============================================
-- VUES UTILES
-- ============================================

-- Vue: Informations complètes des clients
CREATE VIEW v_customer_details AS
SELECT
    c.id,
    c.last_name,
    c.first_name,
    c.nir,
    c.birth_date,
    c.phone,
    c.email,
    c.address,
    c.post_code,
    c.city,
    m.id AS mutual_insurance_id,
    m.company_name AS mutual_insurance_name,
    m.reimbursement_rate,
    d.id AS doctor_ID,
    d.last_name AS doctor_last_name,
    d.first_name AS doctor_first_name,
    d.license_number AS doctor_license
FROM customers c
         LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id
         LEFT JOIN doctors d ON c.doctor_id = d.id;

-- Vue: Historique complet des achats
CREATE VIEW v_purchase_history AS
SELECT
    p.id AS purchase_id,
    p.purchase_date,
    p.is_prescription_based,
    c.last_name AS customer_last_name,
    c.first_name AS customer_first_name,
    c.nir AS customer_nir,
    p.total_amount,
    p.reimbursement_amount,
    p.final_amount,
    COUNT(pi.id) AS item_count
FROM purchases p
         JOIN customers c ON p.customer_id = c.id
         LEFT JOIN purchase_items pi ON p.id = pi.purchase_id
GROUP BY p.id;

-- Vue: Stock des médicaments avec alertes
CREATE VIEW v_medicine_stock AS
SELECT
    m.id,
    m.medicine_name,
    cat.category_name,
    m.price,
    m.stock,
    m.expiry_date,
    CASE
        WHEN m.stock = 0 THEN 'RUPTURE'
        WHEN m.stock < 20 THEN 'STOCK_BAS'
        WHEN m.expiry_date < DATE_ADD(NOW(), INTERVAL 3 MONTH) THEN 'EXPIRE_BIENTOT'
        ELSE 'OK'
        END AS stock_status
FROM medicines m
         JOIN categories cat ON m.category_id = cat.id;

-- ============================================
-- PROCEDURES STOCKEES
-- ============================================

DELIMITER //

-- Procédure: Créer un nouvel achat
CREATE PROCEDURE sp_create_purchase(
    IN p_customer_id INT,
    IN p_is_prescription BOOLEAN,
    OUT p_purchase_id INT
)
BEGIN
    DECLARE v_reimbursement_rate DECIMAL(5,4);

    -- Récupérer le taux de remboursement du client
SELECT COALESCE(m.reimbursement_rate, 0) INTO v_reimbursement_rate
FROM customers c
         LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id
WHERE c.id = p_customer_id;

-- Créer l'achat
INSERT INTO purchases (customer_id, is_prescription_based, total_amount, reimbursement_amount, final_amount)
VALUES (p_customer_id, p_is_prescription, 0, 0, 0);

SET p_purchase_id = LAST_INSERT_ID();
END //

-- Procédure: Ajouter un article à un achat
CREATE PROCEDURE sp_add_purchase_item(
    IN p_purchase_id INT,
    IN p_medicine_id INT,
    IN p_quantity INT
)
BEGIN
    DECLARE v_price DECIMAL(10,2);
    DECLARE v_line_total DECIMAL(10,2);
    DECLARE v_stock INT;
    DECLARE v_is_prescription BOOLEAN;
    DECLARE v_reimbursement_rate DECIMAL(5,4);

    -- Vérifier le stock
SELECT stock INTO v_stock FROM medicines WHERE id = p_medicine_id;
IF v_stock < p_quantity THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Stock insuffisant';
END IF;

    -- Récupérer le prix
SELECT price INTO v_price FROM medicines WHERE id = p_medicine_id;
SET v_line_total = v_price * p_quantity;

    -- Ajouter la ligne d'achat
INSERT INTO purchase_items (purchase_id, medicine_id, quantity, unit_price, line_total)
VALUES (p_purchase_id, p_medicine_id, p_quantity, v_price, v_line_total);

-- Mettre à jour le stock
UPDATE medicines
SET stock = stock - p_quantity
WHERE id = p_medicine_id;

-- Mettre à jour les totaux de l'achat
SELECT is_prescription_based INTO v_is_prescription
FROM purchases WHERE id = p_purchase_id;

SELECT COALESCE(m.reimbursement_rate, 0) INTO v_reimbursement_rate
FROM purchases p
         JOIN customers c ON p.customer_id = c.id
         LEFT JOIN mutual_insurances m ON c.mutual_insurance_id = m.id
WHERE p.id = p_purchase_id;

UPDATE purchases p
SET
    total_amount = (SELECT COALESCE(SUM(line_total), 0) FROM purchase_items WHERE purchase_id = p.id),
    reimbursement_amount = CASE
                               WHEN v_is_prescription THEN (SELECT COALESCE(SUM(line_total), 0) FROM purchase_items WHERE purchase_id = p.id) * v_reimbursement_rate
                               ELSE 0
        END,
    final_amount = total_amount - reimbursement_amount
WHERE id = p_purchase_id;
END //

-- Procédure: Rechercher des clients
CREATE PROCEDURE sp_search_customers(
    IN p_search_term VARCHAR(100)
)
BEGIN
SELECT * FROM v_customer_details
WHERE
    last_name LIKE CONCAT('%', p_search_term, '%')
   OR first_name LIKE CONCAT('%', p_search_term, '%')
   OR city LIKE CONCAT('%', p_search_term, '%')
   OR nir LIKE CONCAT('%', p_search_term, '%');
END //

-- Procédure: Rechercher des médicaments
CREATE PROCEDURE sp_search_medicines(
    IN p_search_term VARCHAR(100),
    IN p_category_id INT
)
BEGIN
SELECT
    m.*,
    c.category_name
FROM medicines m
         JOIN categories c ON m.category_id = c.id
WHERE
    (p_search_term IS NULL OR m.medicine_name LIKE CONCAT('%', p_search_term, '%'))
  AND (p_category_id IS NULL OR m.category_id = p_category_id)
  AND m.stock > 0;
END //

DELIMITER ;

