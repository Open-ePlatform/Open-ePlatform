-- MySQL dump 10.13  Distrib 5.1.73, for Win64 (unknown)
--
-- Host: localhost    Database: demo.oeplatform.org
-- ------------------------------------------------------
-- Server version	5.1.73-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `checkbox_queries`
--

DROP TABLE IF EXISTS `checkbox_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checkbox_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `minChecked` int(10) unsigned DEFAULT NULL,
  `maxChecked` int(10) unsigned DEFAULT NULL,
  `freeTextAlternative` varchar(255) DEFAULT NULL,
  `helpText` text,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkbox_queries`
--

LOCK TABLES `checkbox_queries` WRITE;
/*!40000 ALTER TABLE `checkbox_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkbox_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkbox_query_alternatives`
--

DROP TABLE IF EXISTS `checkbox_query_alternatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checkbox_query_alternatives` (
  `alternativeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`alternativeID`),
  KEY `FK_checkbox_query_alternatives_1` (`queryID`),
  CONSTRAINT `FK_checkbox_query_alternatives_1` FOREIGN KEY (`queryID`) REFERENCES `checkbox_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1318 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkbox_query_alternatives`
--

LOCK TABLES `checkbox_query_alternatives` WRITE;
/*!40000 ALTER TABLE `checkbox_query_alternatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkbox_query_alternatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkbox_query_instance_alternatives`
--

DROP TABLE IF EXISTS `checkbox_query_instance_alternatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checkbox_query_instance_alternatives` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `alternativeID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`queryInstanceID`,`alternativeID`),
  KEY `FK_checkbox_query_instance_alternatives_2` (`alternativeID`),
  CONSTRAINT `FK_checkbox_query_instance_alternatives_1` FOREIGN KEY (`queryInstanceID`) REFERENCES `checkbox_query_instances` (`queryInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_checkbox_query_instance_alternatives_2` FOREIGN KEY (`alternativeID`) REFERENCES `checkbox_query_alternatives` (`alternativeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkbox_query_instance_alternatives`
--

LOCK TABLES `checkbox_query_instance_alternatives` WRITE;
/*!40000 ALTER TABLE `checkbox_query_instance_alternatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkbox_query_instance_alternatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `checkbox_query_instances`
--

DROP TABLE IF EXISTS `checkbox_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checkbox_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  `freeTextAlternativeValue` varchar(255) DEFAULT NULL,
  `minChecked` int(10) unsigned DEFAULT NULL,
  `maxChecked` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_checkbox_query_instances_1` (`queryID`),
  CONSTRAINT `FK_checkbox_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `checkbox_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `checkbox_query_instances`
--

LOCK TABLES `checkbox_query_instances` WRITE;
/*!40000 ALTER TABLE `checkbox_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `checkbox_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_detail_queries`
--

DROP TABLE IF EXISTS `contact_detail_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_detail_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `helpText` text,
  `allowSMS` tinyint(1) unsigned NOT NULL,
  `requireAddress` tinyint(1) NOT NULL,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_detail_queries`
--

LOCK TABLES `contact_detail_queries` WRITE;
/*!40000 ALTER TABLE `contact_detail_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_detail_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contact_detail_query_instances`
--

DROP TABLE IF EXISTS `contact_detail_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contact_detail_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryID` int(10) unsigned NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zipCode` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobilePhone` varchar(255) DEFAULT NULL,
  `contactBySMS` tinyint(1) unsigned DEFAULT NULL,
  `persistUserProfile` tinyint(1) unsigned DEFAULT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_contact_detail_query_instances_1` (`queryID`),
  CONSTRAINT `FK_contact_detail_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `contact_detail_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19445 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contact_detail_query_instances`
--

LOCK TABLES `contact_detail_query_instances` WRITE;
/*!40000 ALTER TABLE `contact_detail_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `contact_detail_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drop_down_queries`
--

DROP TABLE IF EXISTS `drop_down_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drop_down_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `shortDescription` varchar(255) DEFAULT NULL,
  `freeTextAlternative` varchar(255) DEFAULT NULL,
  `helpText` text,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drop_down_queries`
--

LOCK TABLES `drop_down_queries` WRITE;
/*!40000 ALTER TABLE `drop_down_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `drop_down_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drop_down_query_alternatives`
--

DROP TABLE IF EXISTS `drop_down_query_alternatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drop_down_query_alternatives` (
  `alternativeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`alternativeID`),
  KEY `FK_drop_down_query_alternatives_1` (`queryID`),
  CONSTRAINT `FK_drop_down_query_alternatives_1` FOREIGN KEY (`queryID`) REFERENCES `drop_down_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drop_down_query_alternatives`
--

LOCK TABLES `drop_down_query_alternatives` WRITE;
/*!40000 ALTER TABLE `drop_down_query_alternatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `drop_down_query_alternatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `drop_down_query_instances`
--

DROP TABLE IF EXISTS `drop_down_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `drop_down_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  `alternativeID` int(10) unsigned DEFAULT NULL,
  `freeTextAlternativeValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_drop_down_query_instances_1` (`queryID`),
  KEY `FK_drop_down_query_instances_2` (`alternativeID`),
  CONSTRAINT `FK_drop_down_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `drop_down_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_drop_down_query_instances_2` FOREIGN KEY (`alternativeID`) REFERENCES `drop_down_query_alternatives` (`alternativeID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `drop_down_query_instances`
--

LOCK TABLES `drop_down_query_instances` WRITE;
/*!40000 ALTER TABLE `drop_down_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `drop_down_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feedback_flow_submit_surveys`
--

DROP TABLE IF EXISTS `feedback_flow_submit_surveys`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feedback_flow_submit_surveys` (
  `feedbackSurveyID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `flowID` int(10) unsigned NOT NULL,
  `flowInstanceID` int(10) unsigned NOT NULL,
  `added` datetime NOT NULL,
  `answer` varchar(45) NOT NULL,
  `comment` mediumtext,
  PRIMARY KEY (`feedbackSurveyID`),
  UNIQUE KEY `Index_3` (`flowID`,`flowInstanceID`),
  CONSTRAINT `FK_feedback_flow_submit_surveys_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feedback_flow_submit_surveys`
--

LOCK TABLES `feedback_flow_submit_surveys` WRITE;
/*!40000 ALTER TABLE `feedback_flow_submit_surveys` DISABLE KEYS */;
/*!40000 ALTER TABLE `feedback_flow_submit_surveys` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload_files`
--

DROP TABLE IF EXISTS `file_upload_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_upload_files` (
  `fileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `size` int(10) unsigned NOT NULL,
  `queryInstanceID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`fileID`),
  KEY `FK_file_upload_files_1` (`queryInstanceID`),
  CONSTRAINT `FK_file_upload_files_1` FOREIGN KEY (`queryInstanceID`) REFERENCES `file_upload_query_instances` (`queryInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1378 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload_files`
--

LOCK TABLES `file_upload_files` WRITE;
/*!40000 ALTER TABLE `file_upload_files` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_upload_files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload_queries`
--

DROP TABLE IF EXISTS `file_upload_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_upload_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `helpText` text,
  `maxFileCount` int(10) unsigned DEFAULT NULL,
  `maxFileSize` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload_queries`
--

LOCK TABLES `file_upload_queries` WRITE;
/*!40000 ALTER TABLE `file_upload_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_upload_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload_query_extensions`
--

DROP TABLE IF EXISTS `file_upload_query_extensions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_upload_query_extensions` (
  `queryID` int(10) unsigned NOT NULL,
  `extension` varchar(12) NOT NULL,
  PRIMARY KEY (`queryID`,`extension`),
  CONSTRAINT `FK_file_upload_query_extensions_1` FOREIGN KEY (`queryID`) REFERENCES `file_upload_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload_query_extensions`
--

LOCK TABLES `file_upload_query_extensions` WRITE;
/*!40000 ALTER TABLE `file_upload_query_extensions` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_upload_query_extensions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_upload_query_instances`
--

DROP TABLE IF EXISTS `file_upload_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `file_upload_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`queryInstanceID`) USING BTREE,
  KEY `FK_file_upload_query_instances_1` (`queryID`),
  CONSTRAINT `FK_file_upload_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `file_upload_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_upload_query_instances`
--

LOCK TABLES `file_upload_query_instances` WRITE;
/*!40000 ALTER TABLE `file_upload_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `file_upload_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flow_familiy_notification_settings`
--

DROP TABLE IF EXISTS `flow_familiy_notification_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flow_familiy_notification_settings` (
  `flowFamilyID` int(10) unsigned NOT NULL,
  `sendStatusChangedUserSMS` tinyint(1) NOT NULL,
  `sendExternalMessageReceivedUserSMS` tinyint(1) NOT NULL,
  `sendFlowInstanceSubmittedUserSMS` tinyint(1) NOT NULL,
  `sendFlowInstanceArchivedUserSMS` tinyint(1) NOT NULL,
  `sendStatusChangedUserEmail` tinyint(1) NOT NULL,
  `sendExternalMessageReceivedUserEmail` tinyint(1) NOT NULL,
  `sendFlowInstanceSubmittedUserEmail` tinyint(1) NOT NULL,
  `sendFlowInstanceArchivedUserEmail` tinyint(1) NOT NULL,
  `flowInstanceSubmittedUserEmailSubject` varchar(255) DEFAULT NULL,
  `flowInstanceSubmittedUserEmailMessage` text,
  `flowInstanceArchivedUserEmailSubject` varchar(255) DEFAULT NULL,
  `flowInstanceArchivedUserEmailMessage` text,
  `sendExternalMessageReceivedManagerEmail` tinyint(1) NOT NULL,
  `sendFlowInstanceAssignedManagerEmail` tinyint(1) NOT NULL,
  `sendStatusChangedManagerEmail` tinyint(1) NOT NULL,
  `sendFlowInstanceSubmittedManagerEmail` tinyint(1) NOT NULL,
  `sendFlowInstanceSubmittedGlobalEmail` tinyint(1) NOT NULL,
  `flowInstanceSubmittedGlobalEmailAddress` varchar(255) DEFAULT NULL,
  `flowInstanceSubmittedGlobalEmailAttachPDF` tinyint(1) NOT NULL,
  PRIMARY KEY (`flowFamilyID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flow_familiy_notification_settings`
--

LOCK TABLES `flow_familiy_notification_settings` WRITE;
/*!40000 ALTER TABLE `flow_familiy_notification_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `flow_familiy_notification_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_aborted_flow_instances`
--

DROP TABLE IF EXISTS `flowengine_aborted_flow_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_aborted_flow_instances` (
  `abortID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `flowFamilyID` int(10) unsigned NOT NULL,
  `flowID` int(10) unsigned NOT NULL,
  `stepID` int(10) unsigned NOT NULL,
  `added` datetime NOT NULL,
  PRIMARY KEY (`abortID`),
  KEY `FK_flowengine_aborted_flow_instances_1` (`flowID`),
  CONSTRAINT `FK_flowengine_aborted_flow_instances_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=166 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_aborted_flow_instances`
--

LOCK TABLES `flowengine_aborted_flow_instances` WRITE;
/*!40000 ALTER TABLE `flowengine_aborted_flow_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_aborted_flow_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_default_flow_statuses`
--

DROP TABLE IF EXISTS `flowengine_default_flow_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_default_flow_statuses` (
  `actionID` varchar(255) NOT NULL,
  `flowID` int(10) unsigned NOT NULL,
  `statusID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`actionID`,`flowID`),
  KEY `FK_flowengine_default_flow_states_2` (`flowID`),
  KEY `FK_flowengine_default_flow_states_3` (`statusID`) USING BTREE,
  CONSTRAINT `FK_flowengine_default_flow_states_1` FOREIGN KEY (`actionID`) REFERENCES `flowengine_flow_actions` (`actionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_flowengine_default_flow_states_2` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_flowengine_default_flow_statuses_3` FOREIGN KEY (`statusID`) REFERENCES `flowengine_flow_statuses` (`statusID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_default_flow_statuses`
--

LOCK TABLES `flowengine_default_flow_statuses` WRITE;
/*!40000 ALTER TABLE `flowengine_default_flow_statuses` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_default_flow_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_default_standard_statuses`
--

DROP TABLE IF EXISTS `flowengine_default_standard_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_default_standard_statuses` (
  `actionID` varchar(255) NOT NULL,
  `statusID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`actionID`,`statusID`),
  KEY `FK_flowengine_default_standard_statuses_2` (`statusID`),
  CONSTRAINT `FK_flowengine_default_standard_statuses_1` FOREIGN KEY (`actionID`) REFERENCES `flowengine_flow_actions` (`actionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_flowengine_default_standard_statuses_2` FOREIGN KEY (`statusID`) REFERENCES `flowengine_standard_statuses` (`statusID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_default_standard_statuses`
--

LOCK TABLES `flowengine_default_standard_statuses` WRITE;
/*!40000 ALTER TABLE `flowengine_default_standard_statuses` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_default_standard_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_evaluator_descriptors`
--

DROP TABLE IF EXISTS `flowengine_evaluator_descriptors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_evaluator_descriptors` (
  `evaluatorID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `evaluatorTypeID` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`evaluatorID`),
  KEY `FK_flowengine_evaluator_descriptors_1` (`queryID`),
  CONSTRAINT `FK_flowengine_evaluator_descriptors_1` FOREIGN KEY (`queryID`) REFERENCES `flowengine_query_descriptors` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1617 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_evaluator_descriptors`
--

LOCK TABLES `flowengine_evaluator_descriptors` WRITE;
/*!40000 ALTER TABLE `flowengine_evaluator_descriptors` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_evaluator_descriptors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_evaluators_target_queries`
--

DROP TABLE IF EXISTS `flowengine_evaluators_target_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_evaluators_target_queries` (
  `evaluatorID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`evaluatorID`,`queryID`),
  CONSTRAINT `FK_flowengine_evaluators_target_queries_1` FOREIGN KEY (`evaluatorID`) REFERENCES `flowengine_evaluator_descriptors` (`evaluatorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_evaluators_target_queries`
--

LOCK TABLES `flowengine_evaluators_target_queries` WRITE;
/*!40000 ALTER TABLE `flowengine_evaluators_target_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_evaluators_target_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_external_message_attachments`
--

DROP TABLE IF EXISTS `flowengine_external_message_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_external_message_attachments` (
  `attachmentID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) NOT NULL,
  `size` int(10) unsigned NOT NULL,
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `data` longblob NOT NULL,
  `messageID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`attachmentID`),
  KEY `FK_flowengine_external_message_attachments_1` (`messageID`),
  CONSTRAINT `FK_flowengine_external_message_attachments_1` FOREIGN KEY (`messageID`) REFERENCES `flowengine_external_messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_external_message_attachments`
--

LOCK TABLES `flowengine_external_message_attachments` WRITE;
/*!40000 ALTER TABLE `flowengine_external_message_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_external_message_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_external_message_read_receipts`
--

DROP TABLE IF EXISTS `flowengine_external_message_read_receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_external_message_read_receipts` (
  `userID` int(10) unsigned NOT NULL,
  `messageID` int(10) unsigned NOT NULL,
  `read` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`,`messageID`),
  KEY `FK_flowengine_external_message_read_receipts_1` (`messageID`),
  CONSTRAINT `FK_flowengine_external_message_read_receipts_1` FOREIGN KEY (`messageID`) REFERENCES `flowengine_external_messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_external_message_read_receipts`
--

LOCK TABLES `flowengine_external_message_read_receipts` WRITE;
/*!40000 ALTER TABLE `flowengine_external_message_read_receipts` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_external_message_read_receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_external_messages`
--

DROP TABLE IF EXISTS `flowengine_external_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_external_messages` (
  `messageID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `poster` int(10) unsigned NOT NULL,
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `editor` int(10) unsigned DEFAULT NULL,
  `updated` timestamp NULL DEFAULT NULL,
  `message` mediumtext NOT NULL,
  `flowInstanceID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`messageID`),
  KEY `FK_flowengine_external_messages_1` (`flowInstanceID`),
  CONSTRAINT `FK_flowengine_external_messages_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_external_messages`
--

LOCK TABLES `flowengine_external_messages` WRITE;
/*!40000 ALTER TABLE `flowengine_external_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_external_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_actions`
--

DROP TABLE IF EXISTS `flowengine_flow_actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_actions` (
  `actionID` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `required` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`actionID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_actions`
--

LOCK TABLES `flowengine_flow_actions` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_actions` DISABLE KEYS */;
INSERT INTO `flowengine_flow_actions` VALUES ('com.nordicpeak.flowengine.FlowBrowserModule.save','Användare sparar en ansökan utan att skicka den',1),('com.nordicpeak.flowengine.FlowBrowserModule.submit','Användare skickar in ansökan',1),('com.nordicpeak.flowengine.UserFlowInstanceModule.submitcompletion','Användare skickar in komplettering',0);
/*!40000 ALTER TABLE `flowengine_flow_actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_categories`
--

DROP TABLE IF EXISTS `flowengine_flow_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_categories` (
  `categoryID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `flowTypeID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`categoryID`) USING BTREE,
  KEY `FK_flowengine_flow_categories_1` (`flowTypeID`),
  CONSTRAINT `FK_flowengine_flow_categories_1` FOREIGN KEY (`flowTypeID`) REFERENCES `flowengine_flow_types` (`flowTypeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_categories`
--

LOCK TABLES `flowengine_flow_categories` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_categories` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_checks`
--

DROP TABLE IF EXISTS `flowengine_flow_checks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_checks` (
  `flowID` int(10) unsigned NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`flowID`,`value`) USING BTREE,
  CONSTRAINT `FK_flowengine_flow_checks_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_checks`
--

LOCK TABLES `flowengine_flow_checks` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_checks` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_checks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_families`
--

DROP TABLE IF EXISTS `flowengine_flow_families`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_families` (
  `flowFamilyID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `versionCount` int(10) unsigned NOT NULL,
  `contactName` varchar(255) DEFAULT NULL,
  `contactEmail` varchar(255) DEFAULT NULL,
  `contactPhone` varchar(255) DEFAULT NULL,
  `ownerName` varchar(255) DEFAULT NULL,
  `ownerEmail` varchar(255) DEFAULT NULL,
  `statisticsMode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`flowFamilyID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_families`
--

LOCK TABLES `flowengine_flow_families` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_families` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_families` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_family_favourites`
--

DROP TABLE IF EXISTS `flowengine_flow_family_favourites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_family_favourites` (
  `flowFamilyID` int(10) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowFamilyID`,`userID`),
  CONSTRAINT `FK_flowengine_flow_family_favourites_1` FOREIGN KEY (`flowFamilyID`) REFERENCES `flowengine_flow_families` (`flowFamilyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_family_favourites`
--

LOCK TABLES `flowengine_flow_family_favourites` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_family_favourites` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_family_favourites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_family_manager_groups`
--

DROP TABLE IF EXISTS `flowengine_flow_family_manager_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_family_manager_groups` (
  `flowFamilyID` int(10) unsigned NOT NULL,
  `groupID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowFamilyID`,`groupID`),
  CONSTRAINT `FK_flowengine_flow_family_manager_groups_1` FOREIGN KEY (`flowFamilyID`) REFERENCES `flowengine_flow_families` (`flowFamilyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_family_manager_groups`
--

LOCK TABLES `flowengine_flow_family_manager_groups` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_family_manager_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_family_manager_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_family_manager_users`
--

DROP TABLE IF EXISTS `flowengine_flow_family_manager_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_family_manager_users` (
  `flowFamilyID` int(10) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowFamilyID`,`userID`),
  CONSTRAINT `FK_flowengine_flow_family_manager_users_1` FOREIGN KEY (`flowFamilyID`) REFERENCES `flowengine_flow_families` (`flowFamilyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_family_manager_users`
--

LOCK TABLES `flowengine_flow_family_manager_users` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_family_manager_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_family_manager_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instance_attributes`
--

DROP TABLE IF EXISTS `flowengine_flow_instance_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instance_attributes` (
  `flowInstanceID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`flowInstanceID`,`name`),
  CONSTRAINT `FK_flowengine_flow_instance_attributes_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instance_attributes`
--

LOCK TABLES `flowengine_flow_instance_attributes` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instance_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instance_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instance_bookmarks`
--

DROP TABLE IF EXISTS `flowengine_flow_instance_bookmarks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instance_bookmarks` (
  `userID` int(10) unsigned NOT NULL,
  `flowInstanceID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`userID`,`flowInstanceID`),
  KEY `FK_flowengine_flow_instance_bookmarks_1` (`flowInstanceID`),
  CONSTRAINT `FK_flowengine_flow_instance_bookmarks_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instance_bookmarks`
--

LOCK TABLES `flowengine_flow_instance_bookmarks` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instance_bookmarks` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instance_bookmarks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instance_event_attributes`
--

DROP TABLE IF EXISTS `flowengine_flow_instance_event_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instance_event_attributes` (
  `eventID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`eventID`,`name`),
  CONSTRAINT `FK_flowengine_flow_instance_event_attributes_1` FOREIGN KEY (`eventID`) REFERENCES `flowengine_flow_instance_events` (`eventID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instance_event_attributes`
--

LOCK TABLES `flowengine_flow_instance_event_attributes` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instance_event_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instance_event_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instance_events`
--

DROP TABLE IF EXISTS `flowengine_flow_instance_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instance_events` (
  `eventID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `flowInstanceID` int(10) unsigned NOT NULL,
  `eventType` varchar(45) NOT NULL,
  `status` varchar(255) NOT NULL,
  `statusDescription` text,
  `details` varchar(255) DEFAULT NULL,
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `poster` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`eventID`),
  KEY `FK_flowengine_flow_instance_events_1` (`flowInstanceID`),
  CONSTRAINT `FK_flowengine_flow_instance_events_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=622 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instance_events`
--

LOCK TABLES `flowengine_flow_instance_events` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instance_events` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instance_events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instance_managers`
--

DROP TABLE IF EXISTS `flowengine_flow_instance_managers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instance_managers` (
  `flowInstanceID` int(10) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowInstanceID`,`userID`),
  CONSTRAINT `FK_flowengine_flow_instance_managers_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instance_managers`
--

LOCK TABLES `flowengine_flow_instance_managers` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instance_managers` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instance_managers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_instances`
--

DROP TABLE IF EXISTS `flowengine_flow_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_instances` (
  `flowInstanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `poster` int(10) unsigned DEFAULT NULL,
  `added` timestamp NULL DEFAULT NULL,
  `editor` int(10) unsigned DEFAULT NULL,
  `updated` timestamp NULL DEFAULT NULL,
  `fullyPopulated` tinyint(1) NOT NULL,
  `flowID` int(10) unsigned NOT NULL,
  `stepID` int(10) unsigned NOT NULL,
  `statusID` int(10) unsigned NOT NULL,
  `lastStatusChange` timestamp NULL DEFAULT NULL,
  `profileID` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`flowInstanceID`),
  KEY `FK_flowengine_flow_instances_1` (`flowID`),
  KEY `FK_flowengine_flow_instances_2` (`stepID`),
  KEY `FK_flowengine_flow_instances_3` (`statusID`) USING BTREE,
  CONSTRAINT `FK_flowengine_flow_instances_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`),
  CONSTRAINT `FK_flowengine_flow_instances_2` FOREIGN KEY (`stepID`) REFERENCES `flowengine_steps` (`stepID`),
  CONSTRAINT `FK_flowengine_flow_instances_3` FOREIGN KEY (`statusID`) REFERENCES `flowengine_flow_statuses` (`statusID`)
) ENGINE=InnoDB AUTO_INCREMENT=788 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_instances`
--

LOCK TABLES `flowengine_flow_instances` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_statuses`
--

DROP TABLE IF EXISTS `flowengine_flow_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_statuses` (
  `statusID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `managingTime` int(10) unsigned DEFAULT NULL,
  `isUserMutable` tinyint(1) NOT NULL,
  `isAdminMutable` tinyint(1) NOT NULL,
  `contentType` varchar(45) NOT NULL,
  `flowID` int(10) unsigned NOT NULL,
  `isUserDeletable` tinyint(1) NOT NULL,
  `isAdminDeletable` tinyint(1) NOT NULL,
  PRIMARY KEY (`statusID`) USING BTREE,
  KEY `FK_flowengine_flow_states_1` (`flowID`),
  CONSTRAINT `FK_flowengine_flow_statuses_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_statuses`
--

LOCK TABLES `flowengine_flow_statuses` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_statuses` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_tags`
--

DROP TABLE IF EXISTS `flowengine_flow_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_tags` (
  `flowID` int(10) unsigned NOT NULL,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`flowID`,`tag`),
  CONSTRAINT `FK_flowengine_flow_tags_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_tags`
--

LOCK TABLES `flowengine_flow_tags` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_type_admin_groups`
--

DROP TABLE IF EXISTS `flowengine_flow_type_admin_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_type_admin_groups` (
  `flowTypeID` int(10) unsigned NOT NULL,
  `groupID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowTypeID`,`groupID`),
  CONSTRAINT `FK_flowengine_flow_type_admin_groups_1` FOREIGN KEY (`flowTypeID`) REFERENCES `flowengine_flow_types` (`flowTypeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_type_admin_groups`
--

LOCK TABLES `flowengine_flow_type_admin_groups` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_type_admin_groups` DISABLE KEYS */;
INSERT INTO `flowengine_flow_type_admin_groups` VALUES (4,10);
/*!40000 ALTER TABLE `flowengine_flow_type_admin_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_type_admin_users`
--

DROP TABLE IF EXISTS `flowengine_flow_type_admin_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_type_admin_users` (
  `flowTypeID` int(10) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`flowTypeID`,`userID`),
  CONSTRAINT `FK_flowengine_flow_type_admin_users_1` FOREIGN KEY (`flowTypeID`) REFERENCES `flowengine_flow_types` (`flowTypeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_type_admin_users`
--

LOCK TABLES `flowengine_flow_type_admin_users` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_type_admin_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flow_type_admin_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_type_allowed_queries`
--

DROP TABLE IF EXISTS `flowengine_flow_type_allowed_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_type_allowed_queries` (
  `flowTypeID` int(10) unsigned NOT NULL,
  `queryTypeID` varchar(255) NOT NULL,
  PRIMARY KEY (`flowTypeID`,`queryTypeID`),
  CONSTRAINT `FK_flowengine_flow_type_allowed_queries_1` FOREIGN KEY (`flowTypeID`) REFERENCES `flowengine_flow_types` (`flowTypeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_type_allowed_queries`
--

LOCK TABLES `flowengine_flow_type_allowed_queries` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_type_allowed_queries` DISABLE KEYS */;
INSERT INTO `flowengine_flow_type_allowed_queries` VALUES (4,'com.nordicpeak.flowengine.queries.checkboxquery.CheckboxQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.contactdetailquery.ContactDetailQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.dropdownquery.DropDownQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.fileuploadquery.FileUploadQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.organizationdetailquery.OrganizationDetailQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.radiobuttonquery.RadioButtonQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.textareaquery.TextAreaQueryProviderModule'),(4,'com.nordicpeak.flowengine.queries.textfieldquery.TextFieldQueryProviderModule');
/*!40000 ALTER TABLE `flowengine_flow_type_allowed_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flow_types`
--

DROP TABLE IF EXISTS `flowengine_flow_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flow_types` (
  `flowTypeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`flowTypeID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flow_types`
--

LOCK TABLES `flowengine_flow_types` WRITE;
/*!40000 ALTER TABLE `flowengine_flow_types` DISABLE KEYS */;
INSERT INTO `flowengine_flow_types` VALUES (4,'Test');
/*!40000 ALTER TABLE `flowengine_flow_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_flows`
--

DROP TABLE IF EXISTS `flowengine_flows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_flows` (
  `flowID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `shortDescription` text NOT NULL,
  `longDescription` longtext,
  `submittedMessage` longtext,
  `iconFileName` varchar(255) DEFAULT NULL,
  `icon` blob,
  `publishDate` date DEFAULT NULL,
  `unPublishDate` date DEFAULT NULL,
  `flowTypeID` int(10) unsigned NOT NULL,
  `categoryID` int(10) unsigned DEFAULT NULL,
  `enabled` tinyint(1) NOT NULL,
  `usePreview` tinyint(1) NOT NULL,
  `flowFamilyID` int(10) unsigned NOT NULL,
  `version` int(10) unsigned NOT NULL,
  `requireAuthentication` tinyint(1) NOT NULL,
  `requireSigning` tinyint(1) unsigned NOT NULL,
  `externalLink` varchar(255) DEFAULT NULL,
  `showSubmitSurvey` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY (`flowID`),
  KEY `FK_flowengine_flows_1` (`flowTypeID`),
  KEY `FK_flowengine_flows_2` (`categoryID`) USING BTREE,
  KEY `FK_flowengine_flows_3` (`flowFamilyID`),
  CONSTRAINT `FK_flowengine_flows_1` FOREIGN KEY (`flowTypeID`) REFERENCES `flowengine_flow_types` (`flowTypeID`),
  CONSTRAINT `FK_flowengine_flows_2` FOREIGN KEY (`categoryID`) REFERENCES `flowengine_flow_categories` (`categoryID`) ON DELETE SET NULL ON UPDATE SET NULL,
  CONSTRAINT `FK_flowengine_flows_3` FOREIGN KEY (`flowFamilyID`) REFERENCES `flowengine_flow_families` (`flowFamilyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_flows`
--

LOCK TABLES `flowengine_flows` WRITE;
/*!40000 ALTER TABLE `flowengine_flows` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_flows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_internal_message_attachments`
--

DROP TABLE IF EXISTS `flowengine_internal_message_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_internal_message_attachments` (
  `attachmentID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) NOT NULL,
  `size` int(10) unsigned NOT NULL,
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `data` longblob NOT NULL,
  `messageID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`attachmentID`),
  KEY `FK_flowengine_internal_message_attachments_1` (`messageID`),
  CONSTRAINT `FK_flowengine_internal_message_attachments_1` FOREIGN KEY (`messageID`) REFERENCES `flowengine_internal_messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_internal_message_attachments`
--

LOCK TABLES `flowengine_internal_message_attachments` WRITE;
/*!40000 ALTER TABLE `flowengine_internal_message_attachments` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_internal_message_attachments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_internal_message_read_receipts`
--

DROP TABLE IF EXISTS `flowengine_internal_message_read_receipts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_internal_message_read_receipts` (
  `userID` int(10) unsigned NOT NULL,
  `messageID` int(10) unsigned NOT NULL,
  `read` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`userID`,`messageID`),
  KEY `FK_flowengine_internal_message_read_receipts_1` (`messageID`),
  CONSTRAINT `FK_flowengine_internal_message_read_receipts_1` FOREIGN KEY (`messageID`) REFERENCES `flowengine_internal_messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_internal_message_read_receipts`
--

LOCK TABLES `flowengine_internal_message_read_receipts` WRITE;
/*!40000 ALTER TABLE `flowengine_internal_message_read_receipts` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_internal_message_read_receipts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_internal_messages`
--

DROP TABLE IF EXISTS `flowengine_internal_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_internal_messages` (
  `messageID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `poster` int(10) unsigned NOT NULL,
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `editor` int(10) unsigned DEFAULT NULL,
  `updated` timestamp NULL DEFAULT NULL,
  `message` mediumtext NOT NULL,
  `flowInstanceID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`messageID`),
  KEY `FK_flowengine_internal_messages_1` (`flowInstanceID`),
  CONSTRAINT `FK_flowengine_internal_messages_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_internal_messages`
--

LOCK TABLES `flowengine_internal_messages` WRITE;
/*!40000 ALTER TABLE `flowengine_internal_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_internal_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_operating_message_flowfamilies`
--

DROP TABLE IF EXISTS `flowengine_operating_message_flowfamilies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_operating_message_flowfamilies` (
  `messageID` int(10) unsigned NOT NULL,
  `flowFamilyID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`messageID`,`flowFamilyID`),
  KEY `FK_flowengine_operating_message_flowfamilies_2` (`flowFamilyID`),
  CONSTRAINT `FK_flowengine_operating_message_flowfamilies_1` FOREIGN KEY (`messageID`) REFERENCES `flowengine_operating_messages` (`messageID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_flowengine_operating_message_flowfamilies_2` FOREIGN KEY (`flowFamilyID`) REFERENCES `flowengine_flow_families` (`flowFamilyID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_operating_message_flowfamilies`
--

LOCK TABLES `flowengine_operating_message_flowfamilies` WRITE;
/*!40000 ALTER TABLE `flowengine_operating_message_flowfamilies` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_operating_message_flowfamilies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_operating_messages`
--

DROP TABLE IF EXISTS `flowengine_operating_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_operating_messages` (
  `messageID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `message` varchar(255) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `disableFlows` tinyint(1) unsigned NOT NULL,
  `global` tinyint(1) unsigned NOT NULL,
  `posted` datetime NOT NULL,
  `poster` int(10) unsigned NOT NULL,
  `updated` datetime DEFAULT NULL,
  `editor` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`messageID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_operating_messages`
--

LOCK TABLES `flowengine_operating_messages` WRITE;
/*!40000 ALTER TABLE `flowengine_operating_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_operating_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_query_descriptors`
--

DROP TABLE IF EXISTS `flowengine_query_descriptors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_query_descriptors` (
  `queryID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `defaultQueryState` varchar(45) NOT NULL,
  `stepID` int(10) unsigned NOT NULL,
  `queryTypeID` varchar(255) NOT NULL,
  `exported` tinyint(1) NOT NULL,
  `xsdElementName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`queryID`),
  KEY `FK_flowengine_query_descriptors_1` (`stepID`),
  CONSTRAINT `FK_flowengine_query_descriptors_1` FOREIGN KEY (`stepID`) REFERENCES `flowengine_steps` (`stepID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3834 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_query_descriptors`
--

LOCK TABLES `flowengine_query_descriptors` WRITE;
/*!40000 ALTER TABLE `flowengine_query_descriptors` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_query_descriptors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_query_instance_descriptors`
--

DROP TABLE IF EXISTS `flowengine_query_instance_descriptors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_query_instance_descriptors` (
  `queryInstanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryState` varchar(45) NOT NULL,
  `populated` tinyint(1) NOT NULL,
  `flowInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_flowengine_query_instance_descriptors_1` (`flowInstanceID`),
  KEY `FK_flowengine_query_instance_descriptors_2` (`queryID`),
  CONSTRAINT `FK_flowengine_query_instance_descriptors_1` FOREIGN KEY (`flowInstanceID`) REFERENCES `flowengine_flow_instances` (`flowInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_flowengine_query_instance_descriptors_2` FOREIGN KEY (`queryID`) REFERENCES `flowengine_query_descriptors` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19450 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_query_instance_descriptors`
--

LOCK TABLES `flowengine_query_instance_descriptors` WRITE;
/*!40000 ALTER TABLE `flowengine_query_instance_descriptors` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_query_instance_descriptors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_standard_statuses`
--

DROP TABLE IF EXISTS `flowengine_standard_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_standard_statuses` (
  `statusID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `managingTime` int(10) unsigned DEFAULT NULL,
  `isUserMutable` tinyint(1) NOT NULL,
  `isUserDeletable` tinyint(1) NOT NULL,
  `isAdminMutable` tinyint(1) NOT NULL,
  `isAdminDeletable` tinyint(1) NOT NULL,
  `contentType` varchar(45) NOT NULL,
  PRIMARY KEY (`statusID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_standard_statuses`
--

LOCK TABLES `flowengine_standard_statuses` WRITE;
/*!40000 ALTER TABLE `flowengine_standard_statuses` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_standard_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_steps`
--

DROP TABLE IF EXISTS `flowengine_steps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_steps` (
  `stepID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `flowID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`stepID`),
  KEY `FK_flowengine_steps_1` (`flowID`),
  CONSTRAINT `FK_flowengine_steps_1` FOREIGN KEY (`flowID`) REFERENCES `flowengine_flows` (`flowID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=716 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_steps`
--

LOCK TABLES `flowengine_steps` WRITE;
/*!40000 ALTER TABLE `flowengine_steps` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_steps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_text_tags`
--

DROP TABLE IF EXISTS `flowengine_text_tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_text_tags` (
  `textTagID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `defaultValue` text,
  `type` varchar(10) NOT NULL,
  PRIMARY KEY (`textTagID`),
  UNIQUE KEY `Index_2` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_text_tags`
--

LOCK TABLES `flowengine_text_tags` WRITE;
/*!40000 ALTER TABLE `flowengine_text_tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_text_tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flowengine_user_organizations`
--

DROP TABLE IF EXISTS `flowengine_user_organizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flowengine_user_organizations` (
  `organizationID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `organizationNumber` varchar(16) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zipCode` varchar(255) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `mobilePhone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `contactBySMS` tinyint(1) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`organizationID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flowengine_user_organizations`
--

LOCK TABLES `flowengine_user_organizations` WRITE;
/*!40000 ALTER TABLE `flowengine_user_organizations` DISABLE KEYS */;
/*!40000 ALTER TABLE `flowengine_user_organizations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_aliases`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_aliases` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `alias` varchar(255) NOT NULL,
  `listIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`,`alias`),
  CONSTRAINT `FK_backgroundmodulealiases_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_aliases`
--

LOCK TABLES `openhierarchy_background_module_aliases` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_aliases` DISABLE KEYS */;
INSERT INTO `openhierarchy_background_module_aliases` VALUES (10,'*',0),(12,'*',10),(12,'exclude:administration*',0),(12,'exclude:flowadmin*',5),(12,'exclude:flowinstanceadmin*',6),(12,'exclude:minasidor*',2),(12,'exclude:myorganizations*',8),(12,'exclude:mysettings*',7),(12,'exclude:oversikt/flow*',3),(12,'exclude:oversikt/overview*',1),(12,'exclude:oversikt/submitted*',4),(12,'exclude:statistik*',9),(13,'*',0),(14,'*',10),(14,'exclude:administration*',0),(14,'exclude:flowadmin*',5),(14,'exclude:flowinstanceadmin*',6),(14,'exclude:minasidor*',2),(14,'exclude:myorganizations*',8),(14,'exclude:mysettings*',7),(14,'exclude:oversikt/flow*',3),(14,'exclude:oversikt/overview*',1),(14,'exclude:oversikt/submitted*',4),(14,'exclude:statistik*',9),(15,'exclude:minasidor*',3),(15,'exclude:minasidor/flowinstance*',0),(15,'exclude:minasidor/overview*',1),(15,'exclude:minasidor/preview*',2),(15,'exclude:statistik*',4),(16,'*',8),(16,'exclude:administration*',0),(16,'exclude:flowadmin*',5),(16,'exclude:flowinstanceadmin*',6),(16,'exclude:minasidor*',2),(16,'exclude:mysettings*',4),(16,'exclude:oversikt/flow*',3),(16,'exclude:oversikt/overview*',1),(16,'exclude:statistik*',7),(17,'*',0),(18,'*',0),(19,'exclude:minasidor/flowinstance*',0),(19,'exclude:minasidor/overview*',1),(19,'exclude:minasidor/preview*',2),(19,'minasidor*',5),(19,'myorganizations*',4),(19,'mysettings*',3),(20,'mysettings*',0);
/*!40000 ALTER TABLE `openhierarchy_background_module_aliases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_attributes`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_attributes` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`moduleID`,`name`),
  CONSTRAINT `FK_openhierarchy_background_module_attributes_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_attributes`
--

LOCK TABLES `openhierarchy_background_module_attributes` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_background_module_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_groups`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_groups` (
  `moduleID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`moduleID`,`groupID`),
  KEY `FK_backgroundmodulegroups_2` (`groupID`),
  CONSTRAINT `FK_backgroundmodulegroups_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_groups`
--

LOCK TABLES `openhierarchy_background_module_groups` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_background_module_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_settings`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_settings` (
  `counter` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `moduleID` int(10) unsigned NOT NULL,
  `id` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  PRIMARY KEY (`counter`),
  KEY `FK_backgroundmodulesettings_1` (`moduleID`),
  CONSTRAINT `FK_backgroundmodulesettings_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=300 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_settings`
--

LOCK TABLES `openhierarchy_background_module_settings` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_settings` DISABLE KEYS */;
INSERT INTO `openhierarchy_background_module_settings` VALUES (77,10,'profileSettingDescription','Sidhuvudet för den aktuella profilen'),(78,10,'profileSettingID','bgmodule-4'),(79,10,'profileSettingName','Sidhuvud'),(271,14,'flowCount','5'),(272,14,'interval','72'),(273,15,'mode','SHOW'),(274,16,'mode','SHOW'),(275,17,'editFavouritesAlias','/mysettings'),(276,17,'mode','SHOW'),(277,18,'nrOfEvents','5'),(278,19,'sectionID','5'),(279,20,'mode','EDIT'),(295,12,'adminGroups','10'),(296,12,'adminGroups','6'),(297,12,'cssClass','htmloutputmodule'),(298,12,'html','<section class=\"clearboth\">\r\n	<div class=\"center\">test</div>\r\n	<div class=\"center\">123</div>\r\n	<div class=\"current blue\">Om</div>\r\n</section>\r\n'),(299,12,'htmlRequired','true');
/*!40000 ALTER TABLE `openhierarchy_background_module_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_slots`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_slots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_slots` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `slot` varchar(255) NOT NULL,
  PRIMARY KEY (`moduleID`,`slot`),
  CONSTRAINT `FK_backgroundmoduleslots_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_slots`
--

LOCK TABLES `openhierarchy_background_module_slots` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_slots` DISABLE KEYS */;
INSERT INTO `openhierarchy_background_module_slots` VALUES (10,'header.logotype'),(12,'right-content-container.news'),(13,'top-content-container.info'),(14,'right-content-container.popular'),(15,'left-content-container.favourites'),(16,'right-content-container.favourites'),(17,'#menu-container.favourites'),(18,'sectionmenu-content-container.newevents'),(19,'top-content-container.mypagesmenu'),(20,'right-content-container.favourites');
/*!40000 ALTER TABLE `openhierarchy_background_module_slots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_module_users`
--

DROP TABLE IF EXISTS `openhierarchy_background_module_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_module_users` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`,`userID`),
  CONSTRAINT `FK_backgroundmoduleusers_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_background_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_module_users`
--

LOCK TABLES `openhierarchy_background_module_users` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_module_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_background_module_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_background_modules`
--

DROP TABLE IF EXISTS `openhierarchy_background_modules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_background_modules` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classname` varchar(255) NOT NULL DEFAULT '',
  `name` text NOT NULL,
  `xslPath` text,
  `xslPathType` varchar(255) DEFAULT NULL,
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `sectionID` int(10) unsigned NOT NULL DEFAULT '0',
  `dataSourceID` int(10) unsigned DEFAULT NULL,
  `staticContentPackage` varchar(255) DEFAULT NULL,
  `priority` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`),
  KEY `FK_backgroundmodules_1` (`sectionID`),
  KEY `FK_backgroundmodules_2` (`dataSourceID`),
  CONSTRAINT `FK_backgroundmodules_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_backgroundmodules_2` FOREIGN KEY (`dataSourceID`) REFERENCES `openhierarchy_data_sources` (`dataSourceID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_background_modules`
--

LOCK TABLES `openhierarchy_background_modules` WRITE;
/*!40000 ALTER TABLE `openhierarchy_background_modules` DISABLE KEYS */;
INSERT INTO `openhierarchy_background_modules` VALUES (10,'se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfileHTMLBackgroundModule','Sidhuvud','',NULL,1,1,1,1,1,NULL,'',1),(12,'se.unlogic.hierarchy.backgroundmodules.htmloutput.HTMLOutputModule','Aktuellt','HTMLOutputModule.sv.xsl','Classpath',1,1,1,1,1,NULL,'staticcontent',0),(13,'com.nordicpeak.flowengine.OperatingMessageBackgroundModule','Driftmeddelanden (global)','OperatingMessageBackgroundModuleTemplates.xsl','Classpath',1,1,1,1,1,NULL,'',0),(14,'com.nordicpeak.flowengine.PopularFlowFamiliesModule','Mest använda','PopularFlowFamiliesModule.sv.xsl','Classpath',1,1,1,1,1,NULL,'staticcontent',2),(15,'com.nordicpeak.flowengine.UserFavouriteBackgroundModule','Mina favoriter','UserFavouriteBackgroundModule.sv.xsl','Classpath',0,1,1,1,1,NULL,'staticcontent',1),(16,'com.nordicpeak.flowengine.UserFavouriteBackgroundModule','Mina favoriter','UserFavouriteBackgroundModule.sv.xsl','Classpath',0,1,1,1,1,NULL,'staticcontent',1),(17,'com.nordicpeak.flowengine.UserFavouriteBackgroundModule','Mina favoriter','UserFavouriteMenuModule.sv.xsl','Classpath',0,1,1,1,1,NULL,'staticcontent',0),(18,'com.nordicpeak.flowengine.NewEventsBackgroundModule','Mina meddelanden (bakgrund)','NewEventsBackgroundModule.sv.xsl','Classpath',0,1,1,1,1,NULL,'staticcontent',0),(19,'com.nordicpeak.flowengine.UserFlowInstanceMenuModule','Mina ärenden (Meny)','UserFlowInstanceMenuModuleTemplates.xsl','Classpath',0,1,1,1,1,NULL,'',0),(20,'com.nordicpeak.flowengine.UserFavouriteBackgroundModule','Redigera favoriter','UserFavouriteBackgroundModule.sv.xsl','Classpath',0,1,1,1,1,NULL,'staticcontent',1);
/*!40000 ALTER TABLE `openhierarchy_background_modules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_data_sources`
--

DROP TABLE IF EXISTS `openhierarchy_data_sources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_data_sources` (
  `dataSourceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `url` varchar(255) NOT NULL DEFAULT '',
  `type` varchar(45) NOT NULL DEFAULT '',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `driver` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `logAbandoned` tinyint(1) DEFAULT '0',
  `removeAbandoned` tinyint(1) DEFAULT '0',
  `removeTimeout` int(10) unsigned DEFAULT '30',
  `testOnBorrow` tinyint(1) DEFAULT '0',
  `validationQuery` varchar(255) DEFAULT 'SELECT 1',
  `maxActive` int(10) unsigned DEFAULT '30',
  `maxIdle` int(10) unsigned DEFAULT '8',
  `minIdle` int(10) unsigned DEFAULT '0',
  `maxWait` int(10) unsigned DEFAULT '0',
  `defaultCatalog` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`dataSourceID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_data_sources`
--

LOCK TABLES `openhierarchy_data_sources` WRITE;
/*!40000 ALTER TABLE `openhierarchy_data_sources` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_data_sources` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_module_aliases`
--

DROP TABLE IF EXISTS `openhierarchy_filter_module_aliases`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_module_aliases` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `alias` varchar(255) NOT NULL,
  `listIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`,`alias`),
  CONSTRAINT `FK_filtermodulealiases_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_filter_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_module_aliases`
--

LOCK TABLES `openhierarchy_filter_module_aliases` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_module_aliases` DISABLE KEYS */;
INSERT INTO `openhierarchy_filter_module_aliases` VALUES (2,'*',0);
/*!40000 ALTER TABLE `openhierarchy_filter_module_aliases` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_module_attributes`
--

DROP TABLE IF EXISTS `openhierarchy_filter_module_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_module_attributes` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`moduleID`,`name`),
  CONSTRAINT `FK_openhierarchy_filter_module_attributes_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_filter_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_module_attributes`
--

LOCK TABLES `openhierarchy_filter_module_attributes` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_module_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_filter_module_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_module_groups`
--

DROP TABLE IF EXISTS `openhierarchy_filter_module_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_module_groups` (
  `moduleID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`moduleID`,`groupID`),
  KEY `FK_filtermodulegroups_2` (`groupID`),
  CONSTRAINT `FK_filtermodulegroups_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_filter_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_module_groups`
--

LOCK TABLES `openhierarchy_filter_module_groups` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_module_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_filter_module_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_module_settings`
--

DROP TABLE IF EXISTS `openhierarchy_filter_module_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_module_settings` (
  `counter` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `moduleID` int(10) unsigned NOT NULL,
  `id` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  PRIMARY KEY (`counter`),
  KEY `FK_filtermodulesettings_1` (`moduleID`),
  CONSTRAINT `FK_filtermodulesettings_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_filter_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_module_settings`
--

LOCK TABLES `openhierarchy_filter_module_settings` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_module_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_filter_module_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_module_users`
--

DROP TABLE IF EXISTS `openhierarchy_filter_module_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_module_users` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`,`userID`),
  CONSTRAINT `FK_filtermoduleusers_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_filter_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_module_users`
--

LOCK TABLES `openhierarchy_filter_module_users` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_module_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_filter_module_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_filter_modules`
--

DROP TABLE IF EXISTS `openhierarchy_filter_modules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_filter_modules` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classname` varchar(255) NOT NULL DEFAULT '',
  `name` text NOT NULL,
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `dataSourceID` int(10) unsigned DEFAULT NULL,
  `priority` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`),
  KEY `FK_filtermodules_1` (`dataSourceID`),
  CONSTRAINT `FK_filtermodules_1` FOREIGN KEY (`dataSourceID`) REFERENCES `openhierarchy_data_sources` (`dataSourceID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_filter_modules`
--

LOCK TABLES `openhierarchy_filter_modules` WRITE;
/*!40000 ALTER TABLE `openhierarchy_filter_modules` DISABLE KEYS */;
INSERT INTO `openhierarchy_filter_modules` VALUES (2,'se.unlogic.hierarchy.filtermodules.login.LoginTriggerModule','LoginTriggerModule',1,0,0,1,NULL,0);
/*!40000 ALTER TABLE `openhierarchy_filter_modules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_foreground_module_attributes`
--

DROP TABLE IF EXISTS `openhierarchy_foreground_module_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_foreground_module_attributes` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`moduleID`,`name`),
  CONSTRAINT `FK_openhierarchy_foreground_module_attributes_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_foreground_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_foreground_module_attributes`
--

LOCK TABLES `openhierarchy_foreground_module_attributes` WRITE;
/*!40000 ALTER TABLE `openhierarchy_foreground_module_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_foreground_module_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_foreground_module_groups`
--

DROP TABLE IF EXISTS `openhierarchy_foreground_module_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_foreground_module_groups` (
  `moduleID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`moduleID`,`groupID`),
  KEY `FK_modulegroups_2` (`groupID`),
  CONSTRAINT `FK_modulegroups_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_foreground_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_foreground_module_groups`
--

LOCK TABLES `openhierarchy_foreground_module_groups` WRITE;
/*!40000 ALTER TABLE `openhierarchy_foreground_module_groups` DISABLE KEYS */;
INSERT INTO `openhierarchy_foreground_module_groups` VALUES (37,6),(39,6),(69,6),(70,6),(71,6),(95,6),(102,6),(109,6),(123,6),(133,6),(150,6),(160,6),(165,6),(176,6),(186,6),(123,10),(150,10),(160,10),(165,10),(133,11);
/*!40000 ALTER TABLE `openhierarchy_foreground_module_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_foreground_module_settings`
--

DROP TABLE IF EXISTS `openhierarchy_foreground_module_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_foreground_module_settings` (
  `counter` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `moduleID` int(10) unsigned NOT NULL,
  `id` varchar(255) NOT NULL,
  `value` mediumtext NOT NULL,
  PRIMARY KEY (`counter`),
  KEY `FK_modulesettings_1` (`moduleID`),
  CONSTRAINT `FK_modulesettings_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_foreground_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10393 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_foreground_module_settings`
--

LOCK TABLES `openhierarchy_foreground_module_settings` WRITE;
/*!40000 ALTER TABLE `openhierarchy_foreground_module_settings` DISABLE KEYS */;
INSERT INTO `openhierarchy_foreground_module_settings` VALUES (842,84,'menuItemType','MENUITEM'),(864,64,'menuItemType','MENUITEM'),(998,70,'menuItemType','MENUITEM'),(1002,37,'menuItemType','MENUITEM'),(1320,102,'cssPath','/css/fck.css'),(1321,102,'menuItemType','MENUITEM'),(3102,9,'menuItemType','SECTION'),(3103,9,'redirectURL','/'),(3104,112,'menuItemType','MENUITEM'),(3199,125,'menuItemType','MENUITEM'),(4089,146,'diskThreshold','100'),(4091,146,'menuItemType','MENUITEM'),(4092,146,'ramThreshold','500'),(5638,113,'ckConnectorModuleAlias','/fileconnector'),(5639,113,'cssPath','/css/fck.css'),(5640,113,'includeDebugData','false'),(5641,113,'menuItemType','MENUITEM'),(5642,113,'pdfStyleSheet','CheckboxQueryPDF.sv.xsl'),(5643,113,'queryStyleSheet','CheckboxQuery.sv.xsl'),(5644,113,'useCKEditorForDescription','true'),(5683,115,'ckConnectorModuleAlias','/fileconnector'),(5684,115,'cssPath','/css/fck.css'),(5685,115,'includeDebugData','false'),(5686,115,'menuItemType','MENUITEM'),(5687,115,'pdfStyleSheet','RadioButtonQueryPDF.sv.xsl'),(5688,115,'queryStyleSheet','RadioButtonQuery.sv.xsl'),(5689,115,'useCKEditorForDescription','true'),(5726,69,'menuItemType','MENUITEM'),(5727,160,'cssPath','/css/fck.css'),(5728,160,'menuItemType','MENUITEM'),(5729,150,'menuItemType','MENUITEM'),(6162,165,'cssPath','/css/style.css'),(6163,165,'diskThreshold','100'),(6165,165,'menuItemType','MENUITEM'),(6166,165,'ramThreshold','500'),(6192,123,'adminGroupIDs','6'),(6193,123,'maxFlowIconHeight','65'),(6194,123,'maxFlowIconWidth','65'),(6195,123,'maxRequestSize','1000'),(6196,123,'menuItemType','SECTION'),(6197,123,'ramThreshold','500'),(6214,108,'menuItemType','MENUITEM'),(6215,61,'maxFileSize','1'),(6216,61,'maxRequestSize','5'),(6217,61,'menuItemType','MENUITEM'),(6218,61,'ramThreshold','500'),(6219,161,'defaultLogotype','classpath://com/nordicpeak/flowengine/pdf/staticcontent/pics/logo.png'),(6220,161,'includedFonts','/com/nordicpeak/flowengine/pdf/fonts/SourceSansPro-Bold.ttf\r\n/com/nordicpeak/flowengine/pdf/fonts/SourceSansPro-It.ttf\r\n/com/nordicpeak/flowengine/pdf/fonts/SourceSansPro-Regular.ttf\r\n/com/nordicpeak/flowengine/pdf/fonts/SourceSansPro-Semibold.ttf'),(6221,161,'menuItemType','MENUITEM'),(6223,161,'pdfStyleSheet','FlowInstancePDF.sv.xsl'),(6224,161,'supportedActionIDs','com.nordicpeak.flowengine.FlowBrowserModule.submit'),(6226,161,'xhtmlDebug','false'),(6228,161,'xmlDebug','false'),(6230,116,'ckConnectorModuleAlias','/fileconnector'),(6231,116,'cssPath','/css/fck.css'),(6232,116,'includeDebugData','false'),(6233,116,'menuItemType','MENUITEM'),(6234,116,'pdfStyleSheet','DropDownQueryPDF.sv.xsl'),(6235,116,'queryStyleSheet','DropDownQuery.sv.xsl'),(6236,116,'useCKEditorForDescription','true'),(6364,149,'ckConnectorModuleAlias','/fileconnector'),(6365,149,'cssPath','/css/fck.css'),(6366,149,'includeDebugData','false'),(6367,149,'menuItemType','MENUITEM'),(6368,149,'pdfStyleSheet','OrganizationDetailQueryPDF.sv.xsl'),(6369,149,'queryStyleSheet','OrganizationDetailQuery.sv.xsl'),(6370,149,'useCKEditorForDescription','true'),(6371,131,'ckConnectorModuleAlias','/fileconnector'),(6372,131,'cssPath','/css/fck.css'),(6373,131,'includeDebugData','false'),(6374,131,'menuItemType','MENUITEM'),(6375,131,'pdfStyleSheet','ContactDetailQueryPDF.sv.xsl'),(6376,131,'queryStyleSheet','ContactDetailQuery.sv.xsl'),(6377,131,'useCKEditorForDescription','true'),(6510,117,'ckConnectorModuleAlias','/fileconnector'),(6511,117,'cssPath','/css/style.css'),(6512,117,'includeDebugData','false'),(6513,117,'menuItemType','MENUITEM'),(6514,117,'pdfStyleSheet','TextAreaQueryPDF.sv.xsl'),(6515,117,'queryStyleSheet','TextAreaQuery.sv.xsl'),(6516,117,'useCKEditorForDescription','true'),(6523,118,'ckConnectorModuleAlias','/fileconnector'),(6524,118,'cssPath','/css/style.css'),(6525,118,'includeDebugData','false'),(6526,118,'menuItemType','MENUITEM'),(6527,118,'pdfStyleSheet','TextFieldQueryPDF.sv.xsl'),(6528,118,'queryStyleSheet','TextFieldQuery.sv.xsl'),(6529,118,'useCKEditorForDescription','true'),(6956,119,'ckConnectorModuleAlias','/fileconnector'),(6957,119,'cleanupInterval','10'),(6958,119,'cssPath','/css/style.css'),(6960,119,'includeDebugData','true'),(6961,119,'maxAllowedFileSize','50'),(6962,119,'menuItemType','MENUITEM'),(6963,119,'pdfStyleSheet','FileUploadQueryPDF.sv.xsl'),(6964,119,'queryStyleSheet','FileUploadQuery.sv.xsl'),(6966,119,'useCKEditorForDescription','true'),(7044,39,'csspath','/css/fck.css'),(7045,39,'diskThreshold','100'),(7047,39,'pageViewModuleAlias','page'),(7048,39,'pageViewModuleName','Sidvisare'),(7049,39,'pageViewModuleXSLPath','PageViewModule.sv.xsl'),(7050,39,'pageViewModuleXSLPathType','Classpath'),(8744,170,'menuItemType','MENUITEM'),(8745,172,'maxRequestSize','1000'),(8746,172,'menuItemType','SECTION'),(8747,172,'ramThreshold','500'),(8748,173,'allowPasswordChanging','false'),(8749,173,'cancelRedirectURI','flowinstances'),(8750,173,'emailFieldMode','REQUIRED'),(8751,173,'firstnameFieldMode','DISABLED'),(8752,173,'lastnameFieldMode','DISABLED'),(8753,173,'menuItemType','MENUITEM'),(8754,173,'supportedAttributes','address:Adress:50\n\n\n\nzipCode:Postnummer:5\n\n\n\npostalAddress:Ort:50\n\n\n\nmobilePhone:Mobiltelefon:15\n\n\n\nphone:Telefonnummer:15\n\n\n\ncontactByLetter\n\n\n\ncontactByEmail\n\n\n\ncontactBySMS\n\n\n\ncontactByPhone'),(8755,173,'usernameFieldMode','HIDDEN'),(8870,180,'menuItemType','MENUITEM'),(8871,180,'supportedActionIDs','com.nordicpeak.flowengine.FlowBrowserModule.submit'),(8920,109,'adminUserIDs','1'),(8921,109,'allowedSettings','recommendedTags'),(8922,109,'allowedSettings','maxHitCount'),(8923,109,'cssPath','/css/fck.css'),(8924,109,'menuItemType','MENUITEM'),(8925,109,'moduleID','168'),(8926,109,'moduleType','FOREGROUND'),(8967,133,'enableSiteProfileSupport','false'),(8968,133,'highPriorityThreshold','90'),(8969,133,'logFlowInstanceIndexing','false'),(8970,133,'maxHitCount','20'),(8971,133,'maxRequestSize','1000'),(8972,133,'maxUnfilteredHitCount','100'),(8973,133,'mediumPriorityThreshold','60'),(8974,133,'menuItemType','SECTION'),(8975,133,'ramThreshold','500'),(9564,71,'formStyleSheet','SimpleUserProviderForm.sv.xsl'),(9565,71,'includeDebugData','false'),(9566,71,'listAsAddableType','true'),(9567,71,'menuItemType','MENUITEM'),(9568,71,'passwordAlgorithm','SHA-1'),(9569,71,'priority','0'),(9570,71,'supportedAttributes','citizenIdentifier:Personnummer\r\nsmexID!:SMEX ID\r\nphone!:Telefon\r\norganizationID!:Organisations ID\r\norganization!:Organisation'),(9571,71,'userTypeName','Användare med separat fält för användarnamn och lösenord'),(9572,56,'menuItemType','MENUITEM'),(9573,174,'changeCheckInterval','*/5 * * * *'),(9574,174,'enableExportSupport','true'),(9575,174,'flowStatisticsMessage','<h1>$family.name</h1>\r\n<p>Nedan visas statistik f&ouml;r e-tj&auml;nsten $family.name.</p>\r\n'),(9576,174,'globalStatisticsMessage','<h1>Statistik f&ouml;r samtliga e-tj&auml;nster</h1>\r\n<p>H&auml;r kan du se statistik p&aring; anv&auml;ndningen av v&aring;ra e-tj&auml;nster. V&auml;lj en e-tj&auml;nst i menyn till v&auml;nster f&ouml;r att visa statistik om en specifik e-tj&auml;nst.</p>\r\n'),(9577,174,'internalGroups','6'),(9578,174,'menuItemType','SECTION'),(9579,174,'slot','left-content-container.favourites'),(9580,174,'weeksBackInTime','20'),(10306,8,'adminTimeout','60'),(10307,8,'default','true'),(10308,8,'loginLockoutActivated','true'),(10309,8,'loginLockoutTime','1800'),(10310,8,'loginRetries','10'),(10311,8,'loginRetryInterval','600'),(10312,8,'logoutModuleAliases','/logout\r\n/logout/logout'),(10313,8,'menuItemType','SECTION'),(10314,8,'priority','100'),(10315,8,'userTimeout','30'),(10332,185,'menuItemType','MENUITEM'),(10333,168,'listAllFlowTypes','true'),(10334,168,'maxHitCount','10'),(10335,168,'maxRequestSize','1000'),(10336,168,'menuItemType','SECTION'),(10337,168,'openExternalFlowsInNewWindow','true'),(10338,168,'popularFlowCount','5'),(10339,168,'popularInterval','72'),(10340,168,'ramThreshold','500'),(10341,168,'showRelatedFlows','false'),(10343,168,'useCategoryFilter','false'),(10344,168,'userFavouriteModuleAlias','/myfavourites'),(10388,95,'allowAdminAdministration','true'),(10389,95,'allowGroupAdministration','true'),(10390,95,'allowUserSwitching','false'),(10391,95,'filteringField','FIRSTNAME'),(10392,95,'menuItemType','MENUITEM');
/*!40000 ALTER TABLE `openhierarchy_foreground_module_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_foreground_module_users`
--

DROP TABLE IF EXISTS `openhierarchy_foreground_module_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_foreground_module_users` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`moduleID`,`userID`),
  CONSTRAINT `FK_moduleusers_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_foreground_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_foreground_module_users`
--

LOCK TABLES `openhierarchy_foreground_module_users` WRITE;
/*!40000 ALTER TABLE `openhierarchy_foreground_module_users` DISABLE KEYS */;
INSERT INTO `openhierarchy_foreground_module_users` VALUES (39,8),(61,3),(69,8),(108,2);
/*!40000 ALTER TABLE `openhierarchy_foreground_module_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_foreground_modules`
--

DROP TABLE IF EXISTS `openhierarchy_foreground_modules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_foreground_modules` (
  `moduleID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `classname` varchar(255) NOT NULL DEFAULT '',
  `name` text NOT NULL,
  `alias` varchar(45) NOT NULL DEFAULT '',
  `description` text NOT NULL,
  `xslPath` text,
  `xslPathType` varchar(255) DEFAULT NULL,
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `visibleInMenu` tinyint(1) NOT NULL DEFAULT '0',
  `sectionID` int(10) unsigned NOT NULL DEFAULT '0',
  `dataSourceID` int(10) unsigned DEFAULT NULL,
  `staticContentPackage` varchar(255) DEFAULT NULL,
  `requiredProtocol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`moduleID`),
  UNIQUE KEY `Index_3` (`sectionID`,`alias`),
  KEY `FK_modules_1` (`sectionID`),
  KEY `FK_modules_2` (`dataSourceID`),
  CONSTRAINT `FK_modules_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_modules_2` FOREIGN KEY (`dataSourceID`) REFERENCES `openhierarchy_data_sources` (`dataSourceID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_foreground_modules`
--

LOCK TABLES `openhierarchy_foreground_modules` WRITE;
/*!40000 ALTER TABLE `openhierarchy_foreground_modules` DISABLE KEYS */;
INSERT INTO `openhierarchy_foreground_modules` VALUES (8,'se.unlogic.hierarchy.foregroundmodules.login.UserProviderLoginModule','Logga in','login','Logga in','LoginModule.sv.xsl','Classpath',1,0,0,1,0,1,NULL,NULL,NULL),(9,'se.unlogic.hierarchy.foregroundmodules.logout.LogoutModule','Logga ut','logout','Logga ut','LogoutModule.sv.xsl','Classpath',0,1,1,1,0,1,NULL,NULL,NULL),(17,'se.unlogic.hierarchy.foregroundmodules.userprofile.UserProfileModule','Mina inställningar','userprofile','Modul för ändring av användaruppgifter','userprofile.xsl','Classpath',0,1,1,1,1,0,NULL,NULL,NULL),(37,'se.unlogic.hierarchy.foregroundmodules.menuadmin.MenuAdminModule','Menyer','menuadmin','Menyer','MenuAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(39,'se.unlogic.hierarchy.foregroundmodules.pagemodules.PageAdminModule','Sidor','pageadmin','Sidor','PageAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontentadmin',NULL),(56,'se.unlogic.hierarchy.foregroundmodules.runtimeinfo.RuntimeInfoModule','Systeminfo','runtimeinfo','Systeminfo','RuntimeInfoModule.en.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(61,'se.unlogic.hierarchy.foregroundmodules.systemadmin.SystemAdminModule','Moduler och sektioner','systemadmin','Moduler och sektioner','SystemAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(64,'se.unlogic.hierarchy.foregroundmodules.threadinfo.ThreadInfoModule','Trådinfo','threadinfo','Trådinfo','ThreadInfoModule.en.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(67,'se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule','Statiskt innehåll','static','Statiskt innehåll',NULL,NULL,1,1,1,1,0,1,NULL,NULL,NULL),(69,'se.unlogic.hierarchy.foregroundmodules.usersessionadmin.UserSessionAdminModule','Inloggade användare','sessionadmin','Inloggade användare','UserSessionAdminModule.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(70,'se.unlogic.hierarchy.foregroundmodules.groupadmin.GroupAdminModule','Grupper','groupadmin','Grupper','GroupAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(71,'se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUserProviderModule','SimpleUserProviderModule','userprovider','SimpleUserProviderModule',NULL,NULL,0,0,0,1,0,1,NULL,'/se/unlogic/hierarchy/foregroundmodules/useradmin/staticcontent',NULL),(84,'se.unlogic.hierarchy.foregroundmodules.datasourceadmin.DataSourceAdminModule','Datakällor','datasourceadmin','Datakällor','DataSourceAdminModule.en.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(95,'se.unlogic.hierarchy.foregroundmodules.useradmin.UserAdminModule','Användare','useradmin','Användare','UserAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(102,'se.unlogic.hierarchy.foregroundmodules.htmloutput.HTMLOutputAdminModule','Högerkolumn - Admin','htmloutputadmin','Högerkolumn - Admin','HTMLOutputAdminModule.sv.xsl','Classpath',0,0,1,1,0,4,NULL,'staticcontent',NULL),(108,'se.unlogic.hierarchy.foregroundmodules.test.EmailMailTestModule','E-post test modul','mailtest','Skickar ett test meddelande till den som klickar på länken',NULL,NULL,0,0,1,1,0,4,NULL,NULL,NULL),(109,'se.unlogic.hierarchy.foregroundmodules.modulesettings.ModuleSettingUpdateModule','Inställningar - Sök','settings','Inställningar - Rekommenderade sökningar','ModuleSettingUpdateModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(111,'se.unlogic.hierarchy.foregroundmodules.test.XSLReload','Ladda om stilmall','reloadxsl','Ladda om stilmall',NULL,NULL,0,0,1,1,0,4,NULL,NULL,NULL),(112,'com.nordicpeak.flowengine.QueryHandlerModule','QueryHandler','queryhandler','QueryHandler',NULL,NULL,0,0,1,1,0,1,NULL,NULL,NULL),(113,'com.nordicpeak.flowengine.queries.checkboxquery.CheckboxQueryProviderModule','CheckBoxQueryProvider','checkboxquery','CheckBoxQueryProvider','CheckboxQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(115,'com.nordicpeak.flowengine.queries.radiobuttonquery.RadioButtonQueryProviderModule','RadioButtonQueryProvider','radiobuttonquery','RadioButtonQueryProvider','RadioButtonQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(116,'com.nordicpeak.flowengine.queries.dropdownquery.DropDownQueryProviderModule','DropDownQueryProvider','dropdown','DropDownQueryProvider','DropDownQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(117,'com.nordicpeak.flowengine.queries.textareaquery.TextAreaQueryProviderModule','TextAreaQueryProvider','textarea','TextAreaQueryProvider','TextAreaQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(118,'com.nordicpeak.flowengine.queries.textfieldquery.TextFieldQueryProviderModule','TextFieldQueryProvider','textfieldprovider','TextFieldQueryProvider','TextFieldQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(119,'com.nordicpeak.flowengine.queries.fileuploadquery.FileUploadQueryProviderModule','FileUploadQueryProvider','fileuploadquery','FileUploadQueryProvider','FileUploadQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(123,'com.nordicpeak.flowengine.FlowAdminModule','Adm. e-tjänster','flowadmin','Administrera e-tjänster','FlowAdminModule.sv.xsl','Classpath',0,0,0,1,1,1,NULL,'staticcontent',NULL),(124,'com.nordicpeak.flowengine.EvaluationHandlerModule','EvaluationHandler','evaluationhandler','EvaluationHandler',NULL,NULL,1,1,1,1,0,1,NULL,NULL,NULL),(125,'com.nordicpeak.flowengine.evaluators.querystateevaluator.QueryStateEvaluationProviderModule','QueryStateEvaluationProvider','querystateevaluationprovider','QueryStateEvaluationProvider','QueryStateEvaluationProviderModule.sv.xsl','Classpath',1,1,1,1,0,1,NULL,NULL,NULL),(131,'com.nordicpeak.flowengine.queries.contactdetailquery.ContactDetailQueryProviderModule','Kontaktvägar','contactdetails','Kontaktvägar','ContactDetailQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(133,'com.nordicpeak.flowengine.FlowInstanceAdminModule','Adm. ärenden','flowinstanceadmin','Administrera ärenden','FlowInstanceAdminModule.sv.xsl','Classpath',0,0,1,1,1,1,NULL,'staticcontent',NULL),(144,'se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroupProviderModule','SimpleGroupProvider','simplegroupprovider','A group provider for simple groups',NULL,NULL,0,0,0,1,0,1,NULL,NULL,NULL),(146,'com.nordicpeak.flowengine.CKConnectorModule','CKConnector','fileconnector','CKConnector',NULL,NULL,1,1,1,1,0,1,NULL,'staticcontent',NULL),(149,'com.nordicpeak.flowengine.queries.organizationdetailquery.OrganizationDetailQueryProviderModule','Kontaktuppgiftsfråga (företag)','organizationdetails','Kontaktuppgiftsfråga (företag)','OrganizationDetailQueryAdmin.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(150,'se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfilesAdminModule','Profiler','profiler','Profiler','SiteProfilesAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(160,'com.nordicpeak.flowengine.TextTagAdminModule','Taggar','tagadmin','Administrera taggar','TextTagAdminModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(161,'com.nordicpeak.flowengine.pdf.PDFGeneratorModule','PDFGeneratorModule','pdfgen','PDFGeneratorModule',NULL,NULL,0,0,0,1,0,1,NULL,NULL,NULL),(165,'se.unlogic.hierarchy.foregroundmodules.htmloutput.HTMLOutputAdminModule','Administrera widget','widgetadmin','Administrera widget','HTMLOutputAdminModule.sv.xsl','Classpath',0,0,1,1,0,4,NULL,'staticcontent',NULL),(168,'com.nordicpeak.flowengine.FlowBrowserModule','E-tjänster','oversikt','E-tjänster','FlowInstanceBrowserModule.sv.xsl','Classpath',1,1,1,1,1,1,NULL,'staticcontent',NULL),(169,'com.nordicpeak.flowengine.flowsubmitsurveys.FeedbackFlowSubmitSurvey','Feedback','flowfeedback','Feedback','FeedbackFlowSubmitSurvey.sv.xsl','Classpath',1,1,1,1,0,1,NULL,'staticcontent',NULL),(170,'com.nordicpeak.flowengine.UserFavouriteForegroundModule','Mina favoriter','myfavourites','Mina favoriter','',NULL,0,1,1,1,0,1,NULL,'staticcontent',NULL),(171,'com.nordicpeak.flowengine.UserOrganizationsModule','Mina företag','myorganizations','Mina företag','UserOrganizationsModule.sv.xsl','Classpath',0,1,1,1,0,1,NULL,'staticcontent',NULL),(172,'com.nordicpeak.flowengine.UserFlowInstanceModule','Mina sidor','minasidor','Mina sidor','UserFlowInstanceModule.sv.xsl','Classpath',0,1,1,1,1,1,NULL,'staticcontent',NULL),(173,'com.nordicpeak.flowengine.UserProfileModule','Mina uppgifter','mysettings','Mina uppgifter','UserProfileModule.sv.xsl','Classpath',0,1,0,1,0,1,NULL,'/com/nordicpeak/flowengine/staticcontent',NULL),(174,'com.nordicpeak.flowengine.statistics.StatisticsModule','Statistik','statistik','Statistik','StatisticsModule.sv.xsl','Classpath',1,1,1,1,1,1,NULL,'staticcontent',NULL),(175,'com.nordicpeak.flowengine.AbortedFlowInstanceListenerModule','AbortedFlowInstanceListenerModule','abortlistener','AbortedFlowInstanceListenerModule','',NULL,0,0,0,1,0,4,NULL,'',NULL),(176,'com.nordicpeak.flowengine.OperatingMessageModule','Driftmeddelanden','operatingmessages','Driftmeddelanden','OperatingMessageModule.sv.xsl','Classpath',0,0,1,1,1,4,NULL,'staticcontent',NULL),(179,'com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler','StandardFlowNotificationHandler','notificationhandler','StandardFlowNotificationHandler','StandardFlowNotificationHandler.sv.xsl','Classpath',0,1,0,0,0,4,NULL,'staticcontent',NULL),(180,'com.nordicpeak.flowengine.XMLProviderModule','XMLProviderModule','xml','XMLProviderModule','',NULL,0,0,0,1,0,4,NULL,'',NULL),(185,'com.nordicpeak.flowengine.signingproviders.DummySigningProvider','Dummy signing','dummy-signing','Dummy signing',NULL,NULL,1,1,1,1,0,1,NULL,'/com/nordicpeak/authifyclient/staticcontent',NULL),(186,'se.unlogic.hierarchy.foregroundmodules.mailsenders.direct.DirectMailSender','E-post sändare','mailsender','E-post sändare',NULL,NULL,0,0,0,1,0,4,NULL,NULL,NULL);
/*!40000 ALTER TABLE `openhierarchy_foreground_modules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_menu_index`
--

DROP TABLE IF EXISTS `openhierarchy_menu_index`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_menu_index` (
  `menuIndexID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `sectionID` int(10) unsigned NOT NULL DEFAULT '0',
  `menuIndex` int(10) unsigned NOT NULL DEFAULT '0',
  `moduleID` int(10) unsigned DEFAULT NULL,
  `uniqueID` varchar(255) DEFAULT NULL,
  `subSectionID` int(10) unsigned DEFAULT NULL,
  `menuItemID` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`menuIndexID`),
  UNIQUE KEY `UniqueID / ModuleID` (`moduleID`,`uniqueID`,`sectionID`),
  UNIQUE KEY `Index_5` (`sectionID`,`subSectionID`),
  KEY `FK_menuindex_3` (`subSectionID`),
  KEY `FK_menuindex_4` (`menuItemID`),
  CONSTRAINT `FK_menuindex_1` FOREIGN KEY (`moduleID`) REFERENCES `openhierarchy_foreground_modules` (`moduleID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_menuindex_2` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_menuindex_3` FOREIGN KEY (`subSectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_menuindex_4` FOREIGN KEY (`menuItemID`) REFERENCES `openhierarchy_virtual_menu_items` (`menuItemID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=409 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 206848 kB; (`sectionID`) REFER `foraldramotet-o';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_menu_index`
--

LOCK TABLES `openhierarchy_menu_index` WRITE;
/*!40000 ALTER TABLE `openhierarchy_menu_index` DISABLE KEYS */;
INSERT INTO `openhierarchy_menu_index` VALUES (186,4,6,37,'se.unlogic.hierarchy.modules.menuadmin.MenuAdminModule',NULL,NULL),(188,4,26,39,'39',NULL,NULL),(190,1,53,NULL,NULL,4,NULL),(214,4,33,56,'56',NULL,NULL),(222,4,41,61,'61',NULL,NULL),(226,4,34,64,'64',NULL,NULL),(230,1,18,64,'64',NULL,NULL),(235,4,27,37,'37',NULL,NULL),(249,1,24,8,'8',NULL,NULL),(250,1,34,9,'9',NULL,NULL),(251,4,32,69,'69',NULL,NULL),(252,4,19,70,'70',NULL,NULL),(324,4,40,84,'84',NULL,NULL),(331,4,3,NULL,NULL,NULL,10),(346,4,10,95,'95',NULL,NULL),(347,1,37,39,'18',NULL,NULL),(348,1,38,39,'19',NULL,NULL),(373,4,25,109,'109',NULL,NULL),(375,1,43,116,'116',NULL,NULL),(377,1,52,123,'123',NULL,NULL),(382,1,47,NULL,NULL,NULL,14),(383,5,4,NULL,NULL,NULL,15),(386,1,51,133,'133',NULL,NULL),(387,4,43,150,'150',NULL,NULL),(389,4,44,160,'160',NULL,NULL),(391,5,5,NULL,NULL,NULL,18),(392,5,1,NULL,NULL,NULL,19),(393,1,49,39,'20',NULL,NULL),(400,1,45,168,'168',NULL,NULL),(401,1,46,172,'172',NULL,NULL),(402,1,48,174,'174',NULL,NULL),(403,4,18,176,'176',NULL,NULL);
/*!40000 ALTER TABLE `openhierarchy_menu_index` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_section_attributes`
--

DROP TABLE IF EXISTS `openhierarchy_section_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_section_attributes` (
  `sectionID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`sectionID`,`name`),
  CONSTRAINT `FK_openhierarchy_section_attributes_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_section_attributes`
--

LOCK TABLES `openhierarchy_section_attributes` WRITE;
/*!40000 ALTER TABLE `openhierarchy_section_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_section_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_section_groups`
--

DROP TABLE IF EXISTS `openhierarchy_section_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_section_groups` (
  `sectionID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`sectionID`,`groupID`),
  CONSTRAINT `FK_sectiongroups_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_section_groups`
--

LOCK TABLES `openhierarchy_section_groups` WRITE;
/*!40000 ALTER TABLE `openhierarchy_section_groups` DISABLE KEYS */;
INSERT INTO `openhierarchy_section_groups` VALUES (4,6),(4,10);
/*!40000 ALTER TABLE `openhierarchy_section_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_section_users`
--

DROP TABLE IF EXISTS `openhierarchy_section_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_section_users` (
  `sectionID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`sectionID`,`userID`),
  CONSTRAINT `FK_sectionusers_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_section_users`
--

LOCK TABLES `openhierarchy_section_users` WRITE;
/*!40000 ALTER TABLE `openhierarchy_section_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_section_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_sections`
--

DROP TABLE IF EXISTS `openhierarchy_sections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_sections` (
  `sectionID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `parentSectionID` int(10) unsigned DEFAULT NULL,
  `alias` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `visibleInMenu` tinyint(1) NOT NULL DEFAULT '0',
  `breadCrumb` tinyint(1) DEFAULT '1',
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `anonymousDefaultURI` varchar(255) DEFAULT NULL,
  `userDefaultURI` varchar(255) DEFAULT NULL,
  `requiredProtocol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sectionID`),
  UNIQUE KEY `Index_2` (`parentSectionID`,`alias`),
  CONSTRAINT `FK_sections_1` FOREIGN KEY (`parentSectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_sections`
--

LOCK TABLES `openhierarchy_sections` WRITE;
/*!40000 ALTER TABLE `openhierarchy_sections` DISABLE KEYS */;
INSERT INTO `openhierarchy_sections` VALUES (1,NULL,'home',1,1,1,1,0,1,'Hem','Hemmasektionen','/oversikt','/oversikt','HTTPS'),(4,1,'administration',1,0,0,1,1,1,'Systemadm.','Systemadministration och övervakning','/sessionadmin','/sessionadmin',NULL),(5,1,'mypages',1,0,1,1,0,1,'Mina ärenden','Mina ärenden','/notset','/notset',NULL);
/*!40000 ALTER TABLE `openhierarchy_sections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_virtual_menu_item_groups`
--

DROP TABLE IF EXISTS `openhierarchy_virtual_menu_item_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_virtual_menu_item_groups` (
  `menuItemID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`menuItemID`,`groupID`),
  CONSTRAINT `FK_virtualmenuitemgroups_1` FOREIGN KEY (`menuItemID`) REFERENCES `openhierarchy_virtual_menu_items` (`menuItemID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_virtual_menu_item_groups`
--

LOCK TABLES `openhierarchy_virtual_menu_item_groups` WRITE;
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_item_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_item_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_virtual_menu_item_users`
--

DROP TABLE IF EXISTS `openhierarchy_virtual_menu_item_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_virtual_menu_item_users` (
  `menuItemID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`menuItemID`,`userID`),
  CONSTRAINT `FK_virtualmenuitemusers_1` FOREIGN KEY (`menuItemID`) REFERENCES `openhierarchy_virtual_menu_items` (`menuItemID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_virtual_menu_item_users`
--

LOCK TABLES `openhierarchy_virtual_menu_item_users` WRITE;
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_item_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_item_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `openhierarchy_virtual_menu_items`
--

DROP TABLE IF EXISTS `openhierarchy_virtual_menu_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `openhierarchy_virtual_menu_items` (
  `menuItemID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `itemtype` varchar(20) NOT NULL DEFAULT '',
  `name` varchar(45) DEFAULT NULL,
  `description` text,
  `url` text,
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `sectionID` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`menuItemID`),
  KEY `FK_menuadmin_1` (`sectionID`),
  CONSTRAINT `FK_virtualmenuitems_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `openhierarchy_virtual_menu_items`
--

LOCK TABLES `openhierarchy_virtual_menu_items` WRITE;
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_items` DISABLE KEYS */;
INSERT INTO `openhierarchy_virtual_menu_items` VALUES (10,'TITLE','Administration','Administration',NULL,0,0,0,4),(14,'SECTION','Mina favoriter','Mina favoriter','#menu-container.favourites',0,1,0,1),(15,'MENUITEM','Mina uppgifter','Mina uppgifter','/mysettings',0,1,1,5),(18,'MENUITEM','Mina företag','Mina företag','/myorganizations',0,1,1,5),(19,'MENUITEM','Mina ärenden','Mina ärenden','/flowinstances',0,1,1,5);
/*!40000 ALTER TABLE `openhierarchy_virtual_menu_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_detail_queries`
--

DROP TABLE IF EXISTS `organization_detail_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_detail_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `helpText` text,
  `allowSMS` tinyint(1) unsigned NOT NULL,
  `requireAddress` tinyint(1) NOT NULL,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_detail_queries`
--

LOCK TABLES `organization_detail_queries` WRITE;
/*!40000 ALTER TABLE `organization_detail_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_detail_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization_detail_query_instances`
--

DROP TABLE IF EXISTS `organization_detail_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organization_detail_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryID` int(10) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `organizationNumber` varchar(16) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `zipCode` varchar(10) DEFAULT NULL,
  `postalAddress` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobilePhone` varchar(255) DEFAULT NULL,
  `contactBySMS` tinyint(1) unsigned DEFAULT NULL,
  `organizationID` int(10) unsigned DEFAULT NULL,
  `persistOrganization` tinyint(1) unsigned DEFAULT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_organization_detail_query_instances_1` (`queryID`),
  CONSTRAINT `FK_organization_detail_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `organization_detail_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19446 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization_detail_query_instances`
--

LOCK TABLES `organization_detail_query_instances` WRITE;
/*!40000 ALTER TABLE `organization_detail_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization_detail_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_groups`
--

DROP TABLE IF EXISTS `page_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page_groups` (
  `pageID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`pageID`,`groupID`),
  CONSTRAINT `FK_pagegroups_1` FOREIGN KEY (`pageID`) REFERENCES `pages` (`pageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_groups`
--

LOCK TABLES `page_groups` WRITE;
/*!40000 ALTER TABLE `page_groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `page_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `page_users`
--

DROP TABLE IF EXISTS `page_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `page_users` (
  `pageID` int(10) unsigned NOT NULL,
  `userID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`pageID`,`userID`),
  CONSTRAINT `FK_pageusers_1` FOREIGN KEY (`pageID`) REFERENCES `pages` (`pageID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `page_users`
--

LOCK TABLES `page_users` WRITE;
/*!40000 ALTER TABLE `page_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `page_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pages`
--

DROP TABLE IF EXISTS `pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pages` (
  `pageID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `text` mediumtext NOT NULL,
  `enabled` varchar(45) NOT NULL DEFAULT '',
  `visibleInMenu` tinyint(1) NOT NULL DEFAULT '0',
  `anonymousAccess` tinyint(1) NOT NULL DEFAULT '0',
  `userAccess` tinyint(1) NOT NULL DEFAULT '0',
  `adminAccess` tinyint(1) NOT NULL DEFAULT '0',
  `sectionID` int(10) unsigned NOT NULL DEFAULT '0',
  `alias` varchar(255) NOT NULL DEFAULT '',
  `breadCrumb` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`pageID`),
  UNIQUE KEY `Index_3` (`sectionID`,`alias`),
  KEY `FK_pages_1` (`sectionID`),
  CONSTRAINT `FK_pages_1` FOREIGN KEY (`sectionID`) REFERENCES `openhierarchy_sections` (`sectionID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1 COMMENT='InnoDB free: 191488 kB; (`sectionID`) REFER `fkdb-system/sec';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pages`
--

LOCK TABLES `pages` WRITE;
/*!40000 ALTER TABLE `pages` DISABLE KEYS */;
/*!40000 ALTER TABLE `pages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_state_evaluator_alternatives`
--

DROP TABLE IF EXISTS `query_state_evaluator_alternatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_state_evaluator_alternatives` (
  `evaluatorID` int(10) unsigned NOT NULL,
  `alternativeID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`evaluatorID`,`alternativeID`),
  CONSTRAINT `FK_query_state_evaluator_alternatives_1` FOREIGN KEY (`evaluatorID`) REFERENCES `query_state_evaluators` (`evaluatorID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_state_evaluator_alternatives`
--

LOCK TABLES `query_state_evaluator_alternatives` WRITE;
/*!40000 ALTER TABLE `query_state_evaluator_alternatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_state_evaluator_alternatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_state_evaluators`
--

DROP TABLE IF EXISTS `query_state_evaluators`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `query_state_evaluators` (
  `evaluatorID` int(10) unsigned NOT NULL,
  `selectionMode` varchar(45) NOT NULL,
  `queryState` varchar(45) NOT NULL,
  `doNotResetQueryState` tinyint(1) NOT NULL,
  PRIMARY KEY (`evaluatorID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_state_evaluators`
--

LOCK TABLES `query_state_evaluators` WRITE;
/*!40000 ALTER TABLE `query_state_evaluators` DISABLE KEYS */;
/*!40000 ALTER TABLE `query_state_evaluators` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radio_button_queries`
--

DROP TABLE IF EXISTS `radio_button_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radio_button_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `freeTextAlternative` varchar(255) DEFAULT NULL,
  `helpText` text,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radio_button_queries`
--

LOCK TABLES `radio_button_queries` WRITE;
/*!40000 ALTER TABLE `radio_button_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `radio_button_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radio_button_query_alternatives`
--

DROP TABLE IF EXISTS `radio_button_query_alternatives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radio_button_query_alternatives` (
  `alternativeID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  PRIMARY KEY (`alternativeID`),
  KEY `FK_radio_button_query_alternatives_1` (`queryID`),
  CONSTRAINT `FK_radio_button_query_alternatives_1` FOREIGN KEY (`queryID`) REFERENCES `radio_button_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3058 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radio_button_query_alternatives`
--

LOCK TABLES `radio_button_query_alternatives` WRITE;
/*!40000 ALTER TABLE `radio_button_query_alternatives` DISABLE KEYS */;
/*!40000 ALTER TABLE `radio_button_query_alternatives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `radio_button_query_instances`
--

DROP TABLE IF EXISTS `radio_button_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `radio_button_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  `alternativeID` int(10) unsigned DEFAULT NULL,
  `freeTextAlternativeValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_radio_button_query_instances_1` (`queryID`),
  KEY `FK_radio_button_query_instances_2` (`alternativeID`),
  CONSTRAINT `FK_radio_button_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `radio_button_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_radio_button_query_instances_2` FOREIGN KEY (`alternativeID`) REFERENCES `radio_button_query_alternatives` (`alternativeID`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `radio_button_query_instances`
--

LOCK TABLES `radio_button_query_instances` WRITE;
/*!40000 ALTER TABLE `radio_button_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `radio_button_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simple_group_attributes`
--

DROP TABLE IF EXISTS `simple_group_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simple_group_attributes` (
  `groupID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`groupID`,`name`),
  CONSTRAINT `FK_simple_group_attributes_1` FOREIGN KEY (`groupID`) REFERENCES `simple_groups` (`groupID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simple_group_attributes`
--

LOCK TABLES `simple_group_attributes` WRITE;
/*!40000 ALTER TABLE `simple_group_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `simple_group_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simple_groups`
--

DROP TABLE IF EXISTS `simple_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simple_groups` (
  `groupID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`groupID`)
) ENGINE=InnoDB AUTO_INCREMENT=806 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simple_groups`
--

LOCK TABLES `simple_groups` WRITE;
/*!40000 ALTER TABLE `simple_groups` DISABLE KEYS */;
INSERT INTO `simple_groups` VALUES (6,'Systemadministratörer','Administratörer',1),(9,'Medborgare','Grupp för medborgare',1),(10,'E-tjänst administratörer','Grupp för användare som ska få bygga e-tjänster',1),(11,'Handläggare','Handläggare',1);
/*!40000 ALTER TABLE `simple_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simple_user_attributes`
--

DROP TABLE IF EXISTS `simple_user_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simple_user_attributes` (
  `userID` int(10) unsigned NOT NULL,
  `name` varchar(255) NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`userID`,`name`) USING BTREE,
  KEY `Index_2` (`name`),
  CONSTRAINT `FK_simple_user_attributes_1` FOREIGN KEY (`userID`) REFERENCES `simple_users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simple_user_attributes`
--

LOCK TABLES `simple_user_attributes` WRITE;
/*!40000 ALTER TABLE `simple_user_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `simple_user_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simple_user_groups`
--

DROP TABLE IF EXISTS `simple_user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simple_user_groups` (
  `userID` int(10) unsigned NOT NULL,
  `groupID` int(10) NOT NULL,
  PRIMARY KEY (`userID`,`groupID`),
  KEY `FK_usergroups_2` (`groupID`),
  CONSTRAINT `FK_usergroups_1` FOREIGN KEY (`userID`) REFERENCES `simple_users` (`userID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simple_user_groups`
--

LOCK TABLES `simple_user_groups` WRITE;
/*!40000 ALTER TABLE `simple_user_groups` DISABLE KEYS */;
INSERT INTO `simple_user_groups` VALUES (1,6),(1,11);
/*!40000 ALTER TABLE `simple_user_groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `simple_users`
--

DROP TABLE IF EXISTS `simple_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simple_users` (
  `userID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(40) NOT NULL,
  `password` varchar(255) NOT NULL DEFAULT '',
  `firstname` varchar(30) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `email` varchar(255) NOT NULL DEFAULT '',
  `admin` tinyint(1) NOT NULL DEFAULT '0',
  `enabled` tinyint(1) NOT NULL DEFAULT '0',
  `added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastlogin` timestamp NULL DEFAULT '0000-00-00 00:00:00',
  `language` varchar(76) DEFAULT NULL,
  `preferedDesign` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `simple_users`
--

LOCK TABLES `simple_users` WRITE;
/*!40000 ALTER TABLE `simple_users` DISABLE KEYS */;
INSERT INTO `simple_users` VALUES (1,'admin','6e2b592b196249838b312cfa026ebe2146dfa6dc','John','Doe','john.doe@oeplatform.org',1,1,'2008-01-27 19:43:42','2015-05-15 14:33:46',NULL,NULL);
/*!40000 ALTER TABLE `simple_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_profile_domains`
--

DROP TABLE IF EXISTS `site_profile_domains`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_profile_domains` (
  `profileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `domain` varchar(255) NOT NULL,
  PRIMARY KEY (`profileID`,`domain`),
  CONSTRAINT `FK_site_profile_domains_1` FOREIGN KEY (`profileID`) REFERENCES `site_profiles` (`profileID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_profile_domains`
--

LOCK TABLES `site_profile_domains` WRITE;
/*!40000 ALTER TABLE `site_profile_domains` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_profile_domains` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_profile_global_settings`
--

DROP TABLE IF EXISTS `site_profile_global_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_profile_global_settings` (
  `settingID` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `value` varchar(4096) NOT NULL,
  PRIMARY KEY (`settingID`,`sortIndex`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_profile_global_settings`
--

LOCK TABLES `site_profile_global_settings` WRITE;
/*!40000 ALTER TABLE `site_profile_global_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_profile_global_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_profile_settings`
--

DROP TABLE IF EXISTS `site_profile_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_profile_settings` (
  `settingID` varchar(255) NOT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `value` varchar(4096) NOT NULL,
  `profileID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`settingID`,`sortIndex`,`profileID`) USING BTREE,
  KEY `FK_site_profile_settings_1` (`profileID`),
  CONSTRAINT `FK_site_profile_settings_1` FOREIGN KEY (`profileID`) REFERENCES `site_profiles` (`profileID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_profile_settings`
--

LOCK TABLES `site_profile_settings` WRITE;
/*!40000 ALTER TABLE `site_profile_settings` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_profile_settings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_profiles`
--

DROP TABLE IF EXISTS `site_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `site_profiles` (
  `profileID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `design` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`profileID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_profiles`
--

LOCK TABLES `site_profiles` WRITE;
/*!40000 ALTER TABLE `site_profiles` DISABLE KEYS */;
/*!40000 ALTER TABLE `site_profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `table_versions`
--

DROP TABLE IF EXISTS `table_versions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `table_versions` (
  `tableGroupName` varchar(255) NOT NULL,
  `version` int(10) unsigned NOT NULL,
  PRIMARY KEY (`tableGroupName`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `table_versions`
--

LOCK TABLES `table_versions` WRITE;
/*!40000 ALTER TABLE `table_versions` DISABLE KEYS */;
INSERT INTO `table_versions` VALUES ('com.nordicpeak.flowengine.dao.FlowEngineDAOFactory',52),('com.nordicpeak.flowengine.evaluators.querystateevaluator.QueryStateEvaluationProviderModule',4),('com.nordicpeak.flowengine.flowsubmitsurveys.FeedbackFlowSubmitSurvey',2),('com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler',3),('com.nordicpeak.flowengine.queries.checkboxquery.CheckboxQueryProviderModule',7),('com.nordicpeak.flowengine.queries.contactdetailquery.ContactDetailQueryProviderModule',5),('com.nordicpeak.flowengine.queries.dropdownquery.DropDownQueryProviderModule',5),('com.nordicpeak.flowengine.queries.fileuploadquery.FileUploadQueryProviderModule',1),('com.nordicpeak.flowengine.queries.organizationdetailquery.OrganizationDetailQueryProviderModule',4),('com.nordicpeak.flowengine.queries.radiobuttonquery.RadioButtonQueryProviderModule',4),('com.nordicpeak.flowengine.queries.textareaquery.TextAreaQueryProviderModule',1),('com.nordicpeak.flowengine.queries.textfieldquery.TextFieldQueryProviderModule',3),('se.unlogic.hierarchy.core.daos.implementations.mysql.MySQLCoreDAOFactory',34),('se.unlogic.hierarchy.foregroundmodules.groupproviders.SimpleGroupProviderModule',3),('se.unlogic.hierarchy.foregroundmodules.pagemodules.daos.annotated.AnnotatedPageDAOFactory',3),('se.unlogic.hierarchy.foregroundmodules.userproviders.SimpleUserProviderModule',5),('se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfilesAdminModule',4);
/*!40000 ALTER TABLE `table_versions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_area_queries`
--

DROP TABLE IF EXISTS `text_area_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_area_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `helpText` text,
  `maxLength` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_area_queries`
--

LOCK TABLES `text_area_queries` WRITE;
/*!40000 ALTER TABLE `text_area_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_area_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_area_query_instances`
--

DROP TABLE IF EXISTS `text_area_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_area_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  `value` text,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_text_area_query_instances_1` (`queryID`),
  CONSTRAINT `FK_text_area_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `text_area_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_area_query_instances`
--

LOCK TABLES `text_area_query_instances` WRITE;
/*!40000 ALTER TABLE `text_area_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_area_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_field_queries`
--

DROP TABLE IF EXISTS `text_field_queries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_field_queries` (
  `queryID` int(10) unsigned NOT NULL,
  `description` text,
  `helpText` text,
  `layout` varchar(45) NOT NULL,
  PRIMARY KEY (`queryID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_field_queries`
--

LOCK TABLES `text_field_queries` WRITE;
/*!40000 ALTER TABLE `text_field_queries` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_field_queries` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_field_query_instance_values`
--

DROP TABLE IF EXISTS `text_field_query_instance_values`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_field_query_instance_values` (
  `textFieldValueID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `queryInstanceID` int(10) unsigned NOT NULL,
  `textFieldID` int(10) unsigned NOT NULL,
  `value` varchar(255) NOT NULL,
  PRIMARY KEY (`textFieldValueID`),
  KEY `FK_text_field_query_instance_values_1` (`queryInstanceID`),
  KEY `FK_text_field_query_instance_values_2` (`textFieldID`),
  CONSTRAINT `FK_text_field_query_instance_values_1` FOREIGN KEY (`queryInstanceID`) REFERENCES `text_field_query_instances` (`queryInstanceID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_text_field_query_instance_values_2` FOREIGN KEY (`textFieldID`) REFERENCES `text_fields` (`textFieldID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2217 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_field_query_instance_values`
--

LOCK TABLES `text_field_query_instance_values` WRITE;
/*!40000 ALTER TABLE `text_field_query_instance_values` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_field_query_instance_values` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_field_query_instances`
--

DROP TABLE IF EXISTS `text_field_query_instances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_field_query_instances` (
  `queryInstanceID` int(10) unsigned NOT NULL,
  `queryID` int(10) unsigned NOT NULL,
  PRIMARY KEY (`queryInstanceID`),
  KEY `FK_text_field_query_instances_1` (`queryID`),
  CONSTRAINT `FK_text_field_query_instances_1` FOREIGN KEY (`queryID`) REFERENCES `text_field_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_field_query_instances`
--

LOCK TABLES `text_field_query_instances` WRITE;
/*!40000 ALTER TABLE `text_field_query_instances` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_field_query_instances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `text_fields`
--

DROP TABLE IF EXISTS `text_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `text_fields` (
  `textFieldID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `label` varchar(255) DEFAULT NULL,
  `required` tinyint(1) NOT NULL,
  `width` int(10) unsigned DEFAULT NULL,
  `sortIndex` int(10) unsigned NOT NULL,
  `formatValidator` varchar(255) DEFAULT NULL,
  `queryID` int(10) unsigned NOT NULL,
  `maxContentLength` int(10) unsigned DEFAULT NULL,
  `invalidFormatMessage` varchar(255) DEFAULT NULL,
  `setAsAttribute` tinyint(1) NOT NULL,
  `attributeName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`textFieldID`),
  KEY `FK_text_fields_1` (`queryID`),
  CONSTRAINT `FK_text_fields_1` FOREIGN KEY (`queryID`) REFERENCES `text_field_queries` (`queryID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2756 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `text_fields`
--

LOCK TABLES `text_fields` WRITE;
/*!40000 ALTER TABLE `text_fields` DISABLE KEYS */;
/*!40000 ALTER TABLE `text_fields` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-15 16:42:13
