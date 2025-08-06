-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: erb
-- ------------------------------------------------------
-- Server version	8.0.42

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `loyalty_points` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'ayman aljamal','ayman.aljamal2017@gmail.com','0599123456','Ramallah, CityCenter, Palestine','$2a$10$5F.7JrRAnCqpc2NGu7ZvU.k5j714c1CTvnk5P3dWjIJtM4kZhk7f6',0),(2,'baha jaghoob','bahajaghoob10@gmail.com','0599988776','Nablus, CityCenter, Palestine','$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW',60),(3,'Yazan','yazan@example.com','0599999999','Ramallah, CityCenter, Palestine','$2a$10$QTzXS.Zq.y41uOqCRUjMa.Vnz1OllrXjI8DN5tG8/Ln2I8xuq0/Ku',0),(4,'Sara Ali','sara.ali@example.com','0599123456','Ramallah, CityCenter, Palestine','$2a$10$77uqPYV1fKtHE4r7DQ3k1OyfKpKbQJ0TfqAFLwkAClnCXdtUD33..',0),(5,'Khaled Nasser','khaled.nasser@example.com','0599777666','Nablus, International','$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW',0),(6,'John Doe','newcustomer@example.com',NULL,'123 Main St, International','$2a$10$gq8KaaOUu5Y6gKzuWY0qDuCJ114HWX6w6.HGYLER2wn702dk/6nRS',0);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `delivery`
--

DROP TABLE IF EXISTS `delivery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `delivery` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `delivered_at` datetime DEFAULT NULL,
  `order_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`),
  KEY `employee_id` (`employee_id`),
  CONSTRAINT `delivery_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `delivery_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `delivery`
--

LOCK TABLES `delivery` WRITE;
/*!40000 ALTER TABLE `delivery` DISABLE KEYS */;
INSERT INTO `delivery` VALUES (1,'2025-07-15 12:52:24',2,3),(2,'2025-07-17 11:23:15',4,3),(42,NULL,5,2),(43,'2025-07-22 11:45:00',6,3),(44,NULL,7,2),(45,NULL,1,3),(46,NULL,3,3),(47,'2025-07-27 13:30:42',8,3);
/*!40000 ALTER TABLE `delivery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `salary` double NOT NULL,
  `work_hours` int DEFAULT NULL,
  `rank` varchar(50) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `warehouse_id` bigint DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `FKm7i6oxl8m436bvfrsmleyjdwc` (`warehouse_id`),
  CONSTRAINT `FKm7i6oxl8m436bvfrsmleyjdwc` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `employee_chk_1` CHECK ((`rank` in (_utf8mb4'SUPER_ADMIN',_utf8mb4'DELIVERY',_utf8mb4'STAFF')))
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (1,'Mona Khalil','mona.khalil@erp.com',6500,40,'STAFF','https://example.com/img1.jpg','0599123456',1,'$2a$10$QTzXS.Zq.y41uOqCRUjMa.Vnz1OllrXjI8DN5tG8/Ln2I8xuq0/Ku'),(2,'Sami Odeh','sami.odeh@erp.com',4200,40,'DELIVERY','https://example.com/img2.jpg','0599988776',1,'$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW'),(3,'Hanan Naser','hanan.naser@erp.com',4000,35,'DELIVERY','default.jpg','0599000000',1,'$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW'),(4,'Ahmad Saeed','ahmad.saeed@example.com',3500,40,'SUPER_ADMIN','ahmad.jpg','0599123456',1,'$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW'),(5,'Mona Khalil','mona.khalil@example.com',2200,38,'DELIVERY','mona.jpg','0599765432',2,'$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW'),(6,'Omar Abbas','omar.abbas@example.com',2500,40,'STAFF','omar.jpg','0599234567',1,'$2a$10$ZZECW8R11hw.7CtDHHFR1um0pL76TQpLsiZYTo7DwjUEM3uxDlzUW'),(11,'Employee One','employee1@example.com',0,0,'STAFF',NULL,NULL,NULL,'$2a$10$yMH1OCLD.iV.61ccWDK.wuAAmD2WVk5ziWeVHBWJZsBJc08gGej8u');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,2,2),(2,1,3,1),(3,2,1,1),(4,NULL,NULL,1),(5,1,1,2),(6,1,3,1),(7,2,2,1),(8,3,1,1),(9,3,2,2),(10,8,1,1),(11,8,3,2);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `delivered_at` datetime DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  KEY `fk_orders_employee` (`employee_id`),
  CONSTRAINT `fk_orders_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'PROCESSING','2025-07-15 12:52:24',NULL,1,1),(2,'DELIVERED','2025-07-15 12:52:24','2025-07-15 12:52:24',2,6),(3,'DELIVERED','2025-07-17 11:14:19','2025-07-31 13:38:36',2,6),(4,'DELIVERED','2025-07-17 11:23:15','2025-07-17 11:23:15',1,11),(5,'DELIVERED','2025-07-20 10:00:00','2025-08-03 13:03:52',1,11),(6,'DELIVERED','2025-07-21 11:00:00','2025-07-22 11:45:00',2,6),(7,'PROCESSING','2025-07-21 12:00:00',NULL,1,1),(8,'DELIVERED','2025-07-27 12:09:01','2025-07-27 13:30:42',1,1);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `expiry_date` datetime(6) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `customer_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKpgtfnsxqulg2y6etps4s3cv22` (`customer_id`),
  CONSTRAINT `FKvqclwhibqf2q7nr8el7y3tfs` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_tokens`
--

LOCK TABLES `password_reset_tokens` WRITE;
/*!40000 ALTER TABLE `password_reset_tokens` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_tokens` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'Laptop Lenovo ThinkPad','14-inch business laptop with Intel i7',3200),(2,'Wireless Mouse Logitech','Ergonomic wireless mouse',85),(3,'Mechanical Keyboard','RGB Backlit Gaming Keyboard',290),(4,'Laptop','Dell XPS 15',3000),(5,'Laptop HP','Laptop 15 inch, 8GB RAM',800),(6,'Smartphone Samsung','Samsung Galaxy S21',600),(7,'Headphones Sony','Noise cancelling headphones',120),(8,'Laptop HP','Laptop 15 inch, 8GB RAM',800),(9,'Smartphone Samsung','Samsung Galaxy S21',600),(10,'Headphones Sony','Noise cancelling headphones',120);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_receipt`
--

DROP TABLE IF EXISTS `stock_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stock_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `received_at` datetime DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `employee_id` bigint DEFAULT NULL,
  `warehouse_id` bigint DEFAULT NULL,
  `order_item_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_id` (`employee_id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `fk_order_item` (`order_item_id`),
  CONSTRAINT `fk_order_item` FOREIGN KEY (`order_item_id`) REFERENCES `order_item` (`id`),
  CONSTRAINT `stock_receipt_ibfk_2` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`),
  CONSTRAINT `stock_receipt_ibfk_3` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_receipt`
--

LOCK TABLES `stock_receipt` WRITE;
/*!40000 ALTER TABLE `stock_receipt` DISABLE KEYS */;
INSERT INTO `stock_receipt` VALUES (1,'2025-07-15 12:52:33',10,3,1,2),(2,'2025-07-15 12:52:33',15,2,2,1),(3,'2025-07-18 08:00:00',20,3,1,3),(4,'2025-07-19 11:30:00',50,3,1,1),(5,'2025-07-20 15:00:00',30,3,2,2),(6,'2025-07-18 08:00:00',20,3,1,3),(7,'2025-07-19 11:30:00',50,3,1,1),(8,'2025-07-20 15:00:00',30,3,2,2);
/*!40000 ALTER TABLE `stock_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse`
--

DROP TABLE IF EXISTS `warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `location` varchar(255) DEFAULT NULL,
  `capacity` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `country_code` varchar(2) NOT NULL,
  `created_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse`
--

LOCK TABLES `warehouse` WRITE;
/*!40000 ALTER TABLE `warehouse` DISABLE KEYS */;
INSERT INTO `warehouse` VALUES (1,'Central Warehouse - Ramallah',500,'Central Warehouse','PS','2025-07-01 10:00:00'),(2,'Secondary Warehouse - Hebron',300,'Secondary Warehouse','PS','2025-07-12 15:30:00');
/*!40000 ALTER TABLE `warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `warehouse_product`
--

DROP TABLE IF EXISTS `warehouse_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `warehouse_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `warehouse_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `warehouse_id` (`warehouse_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `warehouse_product_ibfk_1` FOREIGN KEY (`warehouse_id`) REFERENCES `warehouse` (`id`),
  CONSTRAINT `warehouse_product_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `warehouse_product`
--

LOCK TABLES `warehouse_product` WRITE;
/*!40000 ALTER TABLE `warehouse_product` DISABLE KEYS */;
INSERT INTO `warehouse_product` VALUES (1,1,1,25),(2,1,2,100),(3,2,1,20),(4,2,3,58);
/*!40000 ALTER TABLE `warehouse_product` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-06 13:47:14
