-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: sparadrah_db
-- ------------------------------------------------------
-- Server version	8.4.7

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'Analgésique','Médicaments contre la douleur','2025-11-12 16:58:00'),(2,'Antibiotique','Médicaments contre les infections bactériennes','2025-11-12 16:58:00'),(3,'Anti-inflammatoire','Médicaments réduisant l\'inflammation','2025-11-12 16:58:00'),(4,'Antihistaminique','Médicaments contre les allergies','2025-11-12 16:58:00'),(5,'Antitussif','Médicaments contre la toux','2025-11-12 16:58:00'),(6,'Vitamines','Compléments vitaminiques','2025-11-12 16:58:00'),(7,'Dermatologie','Médicaments pour la peau','2025-11-12 16:58:00');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nir` varchar(15) COLLATE utf8mb4_unicode_ci NOT NULL,
  `birth_date` date NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_code` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mutual_insurance_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nir` (`nir`),
  KEY `mutual_insurance_id` (`mutual_insurance_id`),
  KEY `doctor_id` (`doctor_id`),
  CONSTRAINT `customers_ibfk_1` FOREIGN KEY (`mutual_insurance_id`) REFERENCES `mutual_insurances` (`id`) ON DELETE SET NULL,
  CONSTRAINT `customers_ibfk_2` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Leroy','Jean','175028912345678','1975-02-28','0612345678','jean.leroy@email.fr','10 Rue des Lilas','75015','Paris',1,1,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(2,'Bernard','Claire','285057823456789','1985-05-07','0623456789','claire.bernard@email.fr','25 Avenue Victor Hugo','69002','Lyon',2,2,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(3,'Petit','Marc','190103934567890','1990-10-03','0634567890','marc.petit@email.fr','8 Place de la République','13002','Marseille',3,3,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(4,'Moreau','Julie','295126745678901','1995-12-06','0645678901','julie.moreau@email.fr','33 Rue du Commerce','31000','Toulouse',1,1,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(5,'Laurent','Paul','188034556789012','1988-03-04','0656789012','paul.laurent@email.fr','17 Boulevard Saint-Michel','44000','Nantes',4,NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctors` (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `first_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `license_number` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `post_code` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `city` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `speciality` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_number` (`license_number`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,'Dupont','Marie','DR001234','0601020304','marie.dupont@medical.fr','15 Rue de la Santé','75014','Paris','Médecine Générale','2025-11-12 16:58:00','2025-11-12 16:58:00'),(2,'Martin','Pierre','DR005678','0605060708','pierre.martin@medical.fr','28 Avenue des Soins','69001','Lyon','Pédiatrie','2025-11-12 16:58:00','2025-11-12 16:58:00'),(3,'Dubois','Sophie','DR009012','0609101112','sophie.dubois@medical.fr','42 Boulevard Medical','13001','Marseille','Dermatologie','2025-11-12 16:58:00','2025-11-12 16:58:00');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medicines`
--

DROP TABLE IF EXISTS `medicines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medicines` (
  `id` int NOT NULL AUTO_INCREMENT,
  `medicine_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `category_id` int NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `stock` int NOT NULL DEFAULT '0',
  `manufacture_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `description` text COLLATE utf8mb4_unicode_ci,
  `requires_prescription` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `medicines_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medicines`
--

LOCK TABLES `medicines` WRITE;
/*!40000 ALTER TABLE `medicines` DISABLE KEYS */;
INSERT INTO `medicines` VALUES (1,'Paracétamol 500mg',1,3.50,150,'2024-01-15','2026-01-15',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(2,'Doliprane 1000mg',1,4.20,100,'2024-02-01','2026-02-01',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(3,'Amoxicilline 500mg',2,8.90,80,'2024-01-10','2025-07-10',NULL,1,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(4,'Ibuprofène 400mg',3,5.50,120,'2024-03-01','2026-03-01',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(5,'Cétirizine 10mg',4,6.80,90,'2024-02-15','2026-02-15',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(6,'Sirop Contre la Toux',5,7.50,60,'2024-01-20','2025-06-20',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(7,'Vitamine C 1000mg',6,9.90,200,'2024-04-01','2027-04-01',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(8,'Crème Hydratante',7,12.50,75,'2024-03-10','2026-03-10',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(9,'Aspirine 500mg',1,3.20,140,'2024-01-05','2026-01-05',NULL,0,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(10,'Augmentin 1g',2,15.60,50,'2024-02-20','2025-08-20',NULL,1,'2025-11-12 16:58:00','2025-11-12 16:58:00');
/*!40000 ALTER TABLE `medicines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mutual_insurances`
--

DROP TABLE IF EXISTS `mutual_insurances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `mutual_insurances` (
  `id` int NOT NULL AUTO_INCREMENT,
  `company_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `reimbursement_rate` decimal(5,4) NOT NULL,
  `phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `company_name` (`company_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mutual_insurances`
--

LOCK TABLES `mutual_insurances` WRITE;
/*!40000 ALTER TABLE `mutual_insurances` DISABLE KEYS */;
INSERT INTO `mutual_insurances` VALUES (1,'Mutuelle Générale',0.7500,'0140506070','contact@mutuelle-generale.fr',NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(2,'Harmonie Mutuelle',0.8000,'0141516171','info@harmonie.fr',NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(3,'MGEN',0.7000,'0142526272','service@mgen.fr',NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(4,'Axa Santé',0.6500,'0143536373','contact@axa-sante.fr',NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00'),(5,'Pas de mutuelle',0.0000,NULL,NULL,NULL,'2025-11-12 16:58:00','2025-11-12 16:58:00');
/*!40000 ALTER TABLE `mutual_insurances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_items`
--

DROP TABLE IF EXISTS `purchase_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `purchase_id` int NOT NULL,
  `medicine_id` int NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(10,2) NOT NULL,
  `line_total` decimal(10,2) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `purchase_id` (`purchase_id`),
  KEY `medicine_id` (`medicine_id`),
  CONSTRAINT `purchase_items_ibfk_1` FOREIGN KEY (`purchase_id`) REFERENCES `purchases` (`id`) ON DELETE CASCADE,
  CONSTRAINT `purchase_items_ibfk_2` FOREIGN KEY (`medicine_id`) REFERENCES `medicines` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_items`
--

LOCK TABLES `purchase_items` WRITE;
/*!40000 ALTER TABLE `purchase_items` DISABLE KEYS */;
INSERT INTO `purchase_items` VALUES (1,1,3,2,8.90,17.80,'2025-11-12 16:58:00'),(2,1,5,1,6.80,6.80,'2025-11-12 16:58:00'),(3,2,1,2,3.50,7.00,'2025-11-12 16:58:00'),(4,2,9,2,3.20,6.40,'2025-11-12 16:58:00'),(5,3,10,1,15.60,15.60,'2025-11-12 16:58:00'),(6,4,6,1,7.50,7.50,'2025-11-12 16:58:00'),(7,5,3,2,8.90,17.80,'2025-11-12 16:58:00'),(8,5,4,2,5.50,11.00,'2025-11-12 16:58:00');
/*!40000 ALTER TABLE `purchase_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchases`
--

DROP TABLE IF EXISTS `purchases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchases` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `purchase_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_prescription_based` tinyint(1) DEFAULT '0',
  `total_amount` decimal(10,2) NOT NULL,
  `reimbursement_amount` decimal(10,2) DEFAULT '0.00',
  `final_amount` decimal(10,2) NOT NULL,
  `notes` text COLLATE utf8mb4_unicode_ci,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `purchases_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchases`
--

LOCK TABLES `purchases` WRITE;
/*!40000 ALTER TABLE `purchases` DISABLE KEYS */;
INSERT INTO `purchases` VALUES (1,1,'2024-11-01 10:30:00',1,24.40,18.30,6.10,NULL,'2025-11-12 16:58:00'),(2,2,'2024-11-05 14:15:00',0,13.30,0.00,13.30,NULL,'2025-11-12 16:58:00'),(3,3,'2024-11-08 09:45:00',1,15.60,10.92,4.68,NULL,'2025-11-12 16:58:00'),(4,1,'2024-11-10 16:20:00',0,7.50,0.00,7.50,NULL,'2025-11-12 16:58:00'),(5,4,'2024-11-12 11:00:00',1,28.80,21.60,7.20,NULL,'2025-11-12 16:58:00');
/*!40000 ALTER TABLE `purchases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_customer_details`
--

DROP TABLE IF EXISTS `v_customer_details`;
/*!50001 DROP VIEW IF EXISTS `v_customer_details`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_customer_details` AS SELECT 
 1 AS `id`,
 1 AS `last_name`,
 1 AS `first_name`,
 1 AS `nir`,
 1 AS `birth_date`,
 1 AS `phone`,
 1 AS `email`,
 1 AS `address`,
 1 AS `post_code`,
 1 AS `city`,
 1 AS `mutual_insurance_name`,
 1 AS `reimbursement_rate`,
 1 AS `doctor_last_name`,
 1 AS `doctor_first_name`,
 1 AS `doctor_license`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_medicine_stock`
--

DROP TABLE IF EXISTS `v_medicine_stock`;
/*!50001 DROP VIEW IF EXISTS `v_medicine_stock`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_medicine_stock` AS SELECT 
 1 AS `id`,
 1 AS `medicine_name`,
 1 AS `category_name`,
 1 AS `price`,
 1 AS `stock`,
 1 AS `expiry_date`,
 1 AS `stock_status`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `v_purchase_history`
--

DROP TABLE IF EXISTS `v_purchase_history`;
/*!50001 DROP VIEW IF EXISTS `v_purchase_history`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `v_purchase_history` AS SELECT 
 1 AS `purchase_id`,
 1 AS `purchase_date`,
 1 AS `is_prescription_based`,
 1 AS `customer_last_name`,
 1 AS `customer_first_name`,
 1 AS `customer_nir`,
 1 AS `total_amount`,
 1 AS `reimbursement_amount`,
 1 AS `final_amount`,
 1 AS `item_count`*/;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `v_customer_details`
--

/*!50001 DROP VIEW IF EXISTS `v_customer_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_customer_details` AS select `c`.`id` AS `id`,`c`.`last_name` AS `last_name`,`c`.`first_name` AS `first_name`,`c`.`nir` AS `nir`,`c`.`birth_date` AS `birth_date`,`c`.`phone` AS `phone`,`c`.`email` AS `email`,`c`.`address` AS `address`,`c`.`post_code` AS `post_code`,`c`.`city` AS `city`,`m`.`company_name` AS `mutual_insurance_name`,`m`.`reimbursement_rate` AS `reimbursement_rate`,`d`.`last_name` AS `doctor_last_name`,`d`.`first_name` AS `doctor_first_name`,`d`.`license_number` AS `doctor_license` from ((`customers` `c` left join `mutual_insurances` `m` on((`c`.`mutual_insurance_id` = `m`.`id`))) left join `doctors` `d` on((`c`.`doctor_id` = `d`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_medicine_stock`
--

/*!50001 DROP VIEW IF EXISTS `v_medicine_stock`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_medicine_stock` AS select `m`.`id` AS `id`,`m`.`medicine_name` AS `medicine_name`,`cat`.`category_name` AS `category_name`,`m`.`price` AS `price`,`m`.`stock` AS `stock`,`m`.`expiry_date` AS `expiry_date`,(case when (`m`.`stock` = 0) then 'RUPTURE' when (`m`.`stock` < 20) then 'STOCK_BAS' when (`m`.`expiry_date` < (now() + interval 3 month)) then 'EXPIRE_BIENTOT' else 'OK' end) AS `stock_status` from (`medicines` `m` join `categories` `cat` on((`m`.`category_id` = `cat`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `v_purchase_history`
--

/*!50001 DROP VIEW IF EXISTS `v_purchase_history`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `v_purchase_history` AS select `p`.`id` AS `purchase_id`,`p`.`purchase_date` AS `purchase_date`,`p`.`is_prescription_based` AS `is_prescription_based`,`c`.`last_name` AS `customer_last_name`,`c`.`first_name` AS `customer_first_name`,`c`.`nir` AS `customer_nir`,`p`.`total_amount` AS `total_amount`,`p`.`reimbursement_amount` AS `reimbursement_amount`,`p`.`final_amount` AS `final_amount`,count(`pi`.`id`) AS `item_count` from ((`purchases` `p` join `customers` `c` on((`p`.`customer_id` = `c`.`id`))) left join `purchase_items` `pi` on((`p`.`id` = `pi`.`purchase_id`))) group by `p`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-12 18:39:30
