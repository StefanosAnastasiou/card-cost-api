--
-- Table structure for table `Cost`
--

DROP TABLE IF EXISTS `Cost`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Cost` (
  `id` int NOT NULL AUTO_INCREMENT,
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cost` DECIMAL(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=314 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Initial data for table `Cost`
--

LOCK TABLES `Cost` WRITE;
/*!40000 ALTER TABLE `Cost` DISABLE KEYS */;
INSERT INTO `Cost` VALUES (1,'US',5),(2,'GR',15),(3,'Others',10);
/*!40000 ALTER TABLE `Cost` ENABLE KEYS */;
UNLOCK TABLES;
