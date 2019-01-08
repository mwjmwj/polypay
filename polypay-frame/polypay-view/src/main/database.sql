/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.24 : Database - polypay
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`polypay` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `polypay`;

/*Table structure for table `menu` */

DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `menu_Id` int(11) NOT NULL AUTO_INCREMENT,
  `menu_Name` varchar(255) DEFAULT NULL,
  `menu_Url` varchar(255) DEFAULT NULL,
  `menu_Pid` int(11) DEFAULT NULL,
  `menu_Target` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menu_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

/*Data for the table `menu` */

insert  into `menu`(`menu_Id`,`menu_Name`,`menu_Url`,`menu_Pid`,`menu_Target`) values (1,'管理中心',NULL,NULL,NULL),(2,'公告',NULL,NULL,NULL),(3,'账户管理',NULL,NULL,NULL),(4,'财务管理',NULL,NULL,NULL),(5,'结算管理',NULL,NULL,NULL),(6,'订单管理',NULL,NULL,NULL),(7,'API管理',NULL,NULL,NULL),(8,'结算申请',NULL,5,'myframe'),(9,'代付申请',NULL,5,'myframe'),(10,'结算记录','merchant/settle/order/list',5,'myframe'),(11,'代付记录','merchant/place/order/list',5,'myframe'),(12,'通道费率','',7,'myframe'),(13,'API开发文档',NULL,7,'myframe'),(14,'充值订单','view/merchantRechargeList',6,'myframe'),(15,'成功订单',NULL,6,'myframe'),(16,'失败订单',NULL,6,'myframe'),(17,'资金记录',NULL,4,'myframe'),(18,'对账单',NULL,4,'myframe'),(19,'基本信息',NULL,3,NULL),(20,'银行卡管理',NULL,3,NULL),(21,'认证信息',NULL,3,NULL),(22,'登录密码',NULL,3,NULL),(23,'支付密码',NULL,3,NULL),(24,'站内公告',NULL,2,NULL),(25,'控制面板',NULL,1,NULL),(26,'顶顶顶顶',NULL,3,NULL);

/*Table structure for table `merchant_account_bindbank` */

DROP TABLE IF EXISTS `merchant_account_bindbank`;

CREATE TABLE `merchant_account_bindbank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL,
  `bank_name` varchar(30) DEFAULT NULL,
  `branch_name` varchar(50) DEFAULT NULL,
  `account_name` varchar(10) DEFAULT NULL,
  `account_number` varchar(50) DEFAULT NULL,
  `province` varchar(10) DEFAULT NULL,
  `default_status` int(11) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_account_bindbank_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_account_bindbank` */

insert  into `merchant_account_bindbank`(`id`,`merchant_id`,`bank_name`,`branch_name`,`account_name`,`account_number`,`province`,`default_status`,`remark`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL,NULL,NULL,'6227002930070614333',NULL,NULL,NULL),(2,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL,NULL,NULL,'6227002930070614333',NULL,NULL,NULL);

/*Table structure for table `merchant_account_info` */

DROP TABLE IF EXISTS `merchant_account_info`;

CREATE TABLE `merchant_account_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(32) DEFAULT NULL,
  `proxy_id` varchar(32) DEFAULT NULL,
  `account_name` varchar(50) DEFAULT NULL,
  `mobile_number` varchar(11) DEFAULT NULL,
  `pass_word` varchar(64) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `login_ip` varchar(200) DEFAULT NULL,
  `helppay_status` int(11) DEFAULT NULL,
  `pay_level` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_account_info` */

insert  into `merchant_account_info`(`id`,`uuid`,`proxy_id`,`account_name`,`mobile_number`,`pass_word`,`create_time`,`status`,`login_ip`,`helppay_status`,`pay_level`,`role_id`) values (1,'2222',NULL,'mwj','17666126557','mwj',NULL,0,NULL,0,1,1),(5,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL,'mwj666788','17666126558','123456',NULL,0,NULL,NULL,NULL,1),(6,'501b880cbaef4247b17e72f625c55ea7',NULL,NULL,'17666126559','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL),(7,'a3565fbfb32b4da7a7cf580c2dbe4da5',NULL,NULL,'17666126555','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL),(8,'646d165011ea4c6d8a7aeeb714f85735',NULL,NULL,'17666126330','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL);

/*Table structure for table `merchant_api` */

DROP TABLE IF EXISTS `merchant_api`;

CREATE TABLE `merchant_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `md5_key` varchar(32) DEFAULT NULL,
  `api_doc_url` varchar(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_api` */

insert  into `merchant_api`(`id`,`merchant_id`,`secret_key`,`md5_key`,`api_doc_url`,`create_time`,`update_time`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52','1a280dcd6d354a3b80dffad647100c74','qqq',NULL,'2018-12-20 09:50:48',NULL),(2,'501b880cbaef4247b17e72f625c55ea7','10f4a5534852475da415c1eec12bbfbc',NULL,NULL,'2019-01-02 17:20:56',NULL),(3,'a3565fbfb32b4da7a7cf580c2dbe4da5','98b926f89f824397bcaf5da68f476db8',NULL,NULL,'2019-01-02 17:35:40',NULL),(4,'646d165011ea4c6d8a7aeeb714f85735','816cc564e7274880a0e819b4994bf36d',NULL,NULL,'2019-01-03 11:12:17',NULL);

/*Table structure for table `merchant_finance` */

DROP TABLE IF EXISTS `merchant_finance`;

CREATE TABLE `merchant_finance` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `blance_amount` decimal(30,4) DEFAULT '0.0000',
  `fronze_amount` decimal(30,4) DEFAULT '0.0000',
  `merchant_id` varchar(32) NOT NULL,
  `pay_password` varchar(32) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_finance` */

insert  into `merchant_finance`(`id`,`blance_amount`,`fronze_amount`,`merchant_id`,`pay_password`,`create_time`,`status`) values (2,'55.8820','98.0980','141b6ccb8bde4b10b1d0c4a5db91cf52','e10adc3949ba59abbe56e057f20f883e','2018-12-21 15:35:36',0),(3,'0.0000','0.0000','501b880cbaef4247b17e72f625c55ea7','e10adc3949ba59abbe56e057f20f883e','2019-01-02 17:20:56',-1),(4,'0.0000','0.0000','a3565fbfb32b4da7a7cf580c2dbe4da5','5cf9cd00a0c3448002b452177b765859','2019-01-02 17:35:40',-1),(5,'0.0000','0.0000','646d165011ea4c6d8a7aeeb714f85735','a01610228fe998f515a72dd730294d87','2019-01-03 11:12:17',-1);

/*Table structure for table `merchant_frezzon` */

DROP TABLE IF EXISTS `merchant_frezzon`;

CREATE TABLE `merchant_frezzon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL,
  `order_number` varchar(40) DEFAULT NULL,
  `amount` decimal(20,4) DEFAULT NULL,
  `arrival_time` timestamp NULL DEFAULT NULL,
  `frezz_time` timestamp NULL DEFAULT NULL,
  `really_arrival_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `merchant_frezzon` */

/*Table structure for table `merchant_login_log` */

DROP TABLE IF EXISTS `merchant_login_log`;

CREATE TABLE `merchant_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_time` timestamp NULL DEFAULT NULL,
  `login_address` varchar(20) DEFAULT NULL,
  `IP` varchar(15) DEFAULT NULL,
  `merchant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_login_log_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_login_log` */

insert  into `merchant_login_log`(`id`,`login_time`,`login_address`,`IP`,`merchant_id`) values (1,'2018-12-19 17:57:27','XX-XX-内网IP','127.0.0.1','2222'),(2,'2018-12-20 10:13:15','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(3,'2018-12-20 10:15:05','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(4,'2018-12-20 10:15:14','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(5,'2018-12-20 10:15:18','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(6,'2018-12-20 10:15:19','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(7,'2018-12-20 10:15:20','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(8,'2018-12-20 10:15:21','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(9,'2018-12-20 10:15:21','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(10,'2018-12-21 15:38:28',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(11,'2018-12-21 15:45:34','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(12,'2018-12-21 16:10:09','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(13,'2018-12-26 11:00:15','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(14,'2018-12-26 11:05:35',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(15,'2018-12-26 11:09:42','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(16,'2018-12-26 11:12:01',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(17,'2018-12-26 11:16:41',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(18,'2018-12-26 11:22:57','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(19,'2018-12-26 11:27:00',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(20,'2018-12-26 11:30:49','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(21,'2018-12-26 11:35:43','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(22,'2018-12-26 11:40:13','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(23,'2018-12-26 11:40:25',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(24,'2018-12-26 16:03:00','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(25,'2018-12-26 16:48:44',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(26,'2018-12-28 16:11:27',NULL,'127.0.0.1','2222'),(27,'2018-12-28 16:11:44',NULL,'127.0.0.1','2222'),(28,'2018-12-28 16:13:33','XX-XX-内网IP','127.0.0.1','2222'),(29,'2019-01-03 14:36:54','XX-XX-内网IP','127.0.0.1','2222'),(30,'2019-01-03 14:37:34','XX-XX-内网IP','127.0.0.1','2222'),(31,'2019-01-03 14:37:44',NULL,'127.0.0.1','2222'),(32,'2019-01-03 15:39:24',NULL,'127.0.0.1','2222'),(33,'2019-01-03 15:53:02','XX-XX-内网IP','127.0.0.1','2222'),(34,'2019-01-03 15:54:54',NULL,'127.0.0.1','2222'),(35,'2019-01-03 15:55:21','XX-XX-内网IP','127.0.0.1','2222'),(36,'2019-01-03 15:59:18','XX-XX-内网IP','127.0.0.1','2222'),(37,'2019-01-03 16:00:34','XX-XX-内网IP','127.0.0.1','2222'),(38,'2019-01-03 16:00:34','XX-XX-内网IP','127.0.0.1','2222'),(39,'2019-01-03 16:02:46','XX-XX-内网IP','127.0.0.1','2222'),(40,'2019-01-04 20:46:16',NULL,'127.0.0.1','2222'),(41,'2019-01-05 12:08:39','XX-XX-内网IP','127.0.0.1','2222'),(42,'2019-01-05 16:45:11','XX-XX-内网IP','127.0.0.1','2222'),(43,'2019-01-05 16:46:43','XX-XX-内网IP','127.0.0.1','2222'),(44,'2019-01-05 17:14:44','XX-XX-内网IP','127.0.0.1','2222'),(45,'2019-01-05 17:33:41','XX-XX-内网IP','127.0.0.1','2222'),(46,'2019-01-05 17:33:41',NULL,'127.0.0.1','2222'),(47,'2019-01-05 17:40:03','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(48,'2019-01-05 17:40:27','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(49,'2019-01-05 17:54:33',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(50,'2019-01-05 17:58:41','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(51,'2019-01-05 17:58:37','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(52,'2019-01-05 18:06:36',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(53,'2019-01-05 18:06:34','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(54,'2019-01-05 18:09:11','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(55,'2019-01-05 18:19:14','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(56,'2019-01-05 18:19:18','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(57,'2019-01-05 19:21:08',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(58,'2019-01-06 23:48:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(59,'2019-01-06 23:48:09',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(60,'2019-01-06 23:50:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(61,'2019-01-06 23:51:35',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52');

/*Table structure for table `merchant_place_order` */

DROP TABLE IF EXISTS `merchant_place_order`;

CREATE TABLE `merchant_place_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(32) DEFAULT NULL,
  `merchant_id` char(32) DEFAULT NULL,
  `pay_amount` decimal(20,0) DEFAULT NULL,
  `service_amount` decimal(20,0) DEFAULT NULL,
  `arrive_amount` decimal(20,0) DEFAULT NULL,
  `bank_name` varchar(20) DEFAULT NULL,
  `branch_name` varchar(50) DEFAULT NULL,
  `bank_number` varchar(50) DEFAULT NULL,
  `province` varchar(10) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `handler_time` timestamp NULL DEFAULT NULL,
  `handler_name` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `descreption` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_place_order_PK` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `merchant_place_order` */

/*Table structure for table `merchant_recharge_order` */

DROP TABLE IF EXISTS `merchant_recharge_order`;

CREATE TABLE `merchant_recharge_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(32) DEFAULT NULL,
  `merchant_order_number` varchar(32) DEFAULT NULL,
  `bank_order_number` varchar(32) DEFAULT NULL,
  `merchant_id` varchar(32) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `pay_amount` decimal(20,4) DEFAULT NULL,
  `service_amount` decimal(20,4) DEFAULT NULL,
  `arrival_amount` decimal(30,4) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `success_time` timestamp NULL DEFAULT NULL,
  `pay_channel` varchar(10) DEFAULT NULL,
  `pay_bank` varchar(20) DEFAULT NULL,
  `descreption` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_recharge_order_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_recharge_order` */

insert  into `merchant_recharge_order`(`id`,`order_number`,`merchant_order_number`,`bank_order_number`,`merchant_id`,`type`,`pay_amount`,`service_amount`,`arrival_amount`,`create_time`,`success_time`,`pay_channel`,`pay_bank`,`descreption`,`status`) values (1,'2018122110:44:04:273851084','',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000',NULL,NULL,'2018-12-21 10:44:04',NULL,'WY','ICBC',NULL,1),(2,'2018122110:46:32:397935523','1',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000',NULL,NULL,'2018-12-21 10:46:32',NULL,'WY','ICBC',NULL,1),(3,'2018122113:27:04:795981986','12',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000','0.0000','10.0000','2018-12-21 13:27:05','2018-12-21 13:31:31','WY','ICBC',NULL,0),(4,'2018122113:39:07:379276853','2',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000','0.0000','10.0000','2018-12-21 13:39:07','2018-12-21 13:41:21','WY','ICBC',NULL,0),(5,'2018122113:46:38:125141528','3',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0000','20.0000','980.0000','2018-12-21 13:46:38','2018-12-21 13:46:58','WY','ICBC',NULL,0),(6,'2018122113:49:35:640461572','4',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10000.0000','200.0000','9800.0000','2018-12-21 13:49:36','2018-12-21 13:51:39','WY','ICBC',NULL,0),(7,'2018122113:52:49:671208416','5',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10000.0000','200.0000','9800.0000','2018-12-21 13:52:50','2018-12-21 13:53:11','WY','ICBC',NULL,0),(8,'2018122113:54:50:883772530','6',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1.0000','0.0000','1.0000','2018-12-21 13:54:51','2018-12-21 13:55:03','WY','ICBC',NULL,0),(9,'2018122113:56:15:039235359','7',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'5000000.0000','100000.0000','4900000.0000','2018-12-21 13:56:15','2018-12-21 13:56:33','WY','ICBC',NULL,0),(10,'2018122115:34:18:801288552','8',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0000','20.0000','980.0000','2018-12-21 15:34:19','2018-12-21 15:34:35','WY','ICBC',NULL,0),(11,'2018122116:04:11:315140933','111222333',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0000','20.0000','980.0000','2018-12-21 16:04:11','2018-12-21 16:04:35','WY','ICBC',NULL,0),(12,'2018122116:09:42:000816829','20',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1.0000','0.0200','0.9800','2018-12-21 16:09:42','2018-12-21 16:10:30','WY','ICBC',NULL,0);

/*Table structure for table `merchant_settle_order` */

DROP TABLE IF EXISTS `merchant_settle_order`;

CREATE TABLE `merchant_settle_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_number` varchar(32) DEFAULT NULL,
  `postal_amount` decimal(20,4) DEFAULT NULL,
  `service_amount` decimal(20,4) DEFAULT NULL,
  `arrival_amount` decimal(20,4) DEFAULT NULL,
  `merchant_bind_bank_id` int(11) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `pay_time` timestamp NULL DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `descreption` int(11) DEFAULT NULL,
  `handle_people` varchar(20) DEFAULT NULL,
  `merchant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_settle_order_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_settle_order` */

insert  into `merchant_settle_order`(`id`,`order_number`,`postal_amount`,`service_amount`,`arrival_amount`,`merchant_bind_bank_id`,`create_time`,`pay_time`,`status`,`type`,`descreption`,`handle_people`,`merchant_id`) values (46,'S2018122611:40:26:885604882','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:27',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(47,'S2018122611:40:27:820540578','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(48,'S2018122611:40:28:600671258','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(49,'S2018122611:40:29:331690839','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(50,'S2018122611:40:30:063650034','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(51,'S2018122611:41:30:289627120','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(52,'S2018122611:41:31:206574381','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:31',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(53,'S2018122611:41:32:256633999','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:32',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(54,'S2018122611:41:33:028805995','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:33',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(55,'S2018122611:41:33:718421647','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:34',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(56,'S2018122611:41:35:678842461','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:36',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(57,'S2018122611:41:40:852450151','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:41',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(58,'S2018122616:03:03:350764830','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:03',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(59,'S2018122616:03:05:440255356','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:05',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(60,'S2018122616:03:06:339266813','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:06',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(61,'S2018122616:03:06:944718566','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(62,'S2018122616:03:07:607880287','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(63,'S2018122616:03:08:872377045','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:09',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(64,'S2018122616:04:23:732747632','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(65,'S2018122616:04:24:051911397','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(66,'S2018122616:04:24:389832110','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(67,'S2018122616:04:27:667223892','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(68,'S2018122616:04:28:223156830','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(69,'S2018122616:04:28:626221644','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(70,'S2018122616:04:28:977233162','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(71,'S2018122616:04:29:322435337','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(72,'S2018122616:04:29:681303051','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(73,'S2018122616:04:30:014729675','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(74,'S2018122616:04:30:381339455','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(75,'S2018122616:04:30:750631621','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:31',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(76,'S2018122616:48:47:895852844','5.0000',NULL,NULL,1,'2018-12-26 16:48:48',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(77,'S2018122616:48:53:911396620','5.0000',NULL,NULL,1,'2018-12-26 16:48:54',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(78,'S2018122616:48:54:775343478','5.0000',NULL,NULL,1,'2018-12-26 16:48:55',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(79,'S2018122616:48:55:356519393','5.0000',NULL,NULL,1,'2018-12-26 16:48:55',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(80,'S2018122616:48:55:861533230','5.0000',NULL,NULL,1,'2018-12-26 16:48:56',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(81,'S2018122616:48:56:396532381','5.0000',NULL,NULL,1,'2018-12-26 16:48:56',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(82,'S2018122616:48:56:943707683','5.0000',NULL,NULL,1,'2018-12-26 16:48:57',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(83,'S2018122616:49:06:308906437','5.0000',NULL,NULL,2,'2018-12-26 16:49:06',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(84,'S2018122616:49:06:906111835','5.0000',NULL,NULL,2,'2018-12-26 16:49:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(85,'S2018122616:49:07:379122830','5.0000',NULL,NULL,2,'2018-12-26 16:49:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(86,'S2018122616:49:07:789850098','5.0000',NULL,NULL,2,'2018-12-26 16:49:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(87,'S2018122616:49:08:141841260','5.0000',NULL,NULL,2,'2018-12-26 16:49:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(88,'S2018122616:48:53:911396620','5.0000',NULL,NULL,1,'2018-12-26 16:48:54',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52');

/*Table structure for table `merchant_verify` */

DROP TABLE IF EXISTS `merchant_verify`;

CREATE TABLE `merchant_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(11) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `code` varchar(6) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `avaliable_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_verify_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_verify` */

insert  into `merchant_verify`(`id`,`mobile_number`,`email`,`code`,`type`,`avaliable_time`) values (1,'17666126557',NULL,'405445','LOGIN','2018-12-19 18:36:30'),(2,'17666126558',NULL,'219350','REGISTER','2018-12-20 09:51:00'),(3,'17666126558',NULL,'928908','LOGIN','2018-12-20 10:20:05'),(4,'17666126559',NULL,'344646','REGISTER','2019-01-02 17:22:53'),(5,'17666126880',NULL,'556832','REGISTER','2019-01-02 17:33:31'),(6,'17666411125',NULL,'931396','REGISTER','2019-01-02 17:35:31'),(7,'17666126555',NULL,'334452','REGISTER','2019-01-02 17:40:30'),(8,'17666126560',NULL,'730336','REGISTER','2019-01-03 10:34:26'),(9,'17666126330',NULL,'882286','REGISTER','2019-01-03 11:17:05');

/*Table structure for table `mid_role_menu` */

DROP TABLE IF EXISTS `mid_role_menu`;

CREATE TABLE `mid_role_menu` (
  `role_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `mid_role_menu` */

insert  into `mid_role_menu`(`role_id`,`menu_id`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26);

/*Table structure for table `notice` */

DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(2000) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Notice_PK` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `notice` */

/*Table structure for table `pay_type` */

DROP TABLE IF EXISTS `pay_type`;

CREATE TABLE `pay_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) DEFAULT NULL,
  `rate` decimal(10,0) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `merchant_level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pay_type_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `pay_type` */

insert  into `pay_type`(`id`,`name`,`rate`,`status`,`merchant_level`) values (1,'银联','10',0,1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_Id` int(11) NOT NULL AUTO_INCREMENT,
  `role_Name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`role_Id`,`role_Name`) values (1,'商户');

/*Table structure for table `system_consts` */

DROP TABLE IF EXISTS `system_consts`;

CREATE TABLE `system_consts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `consts_key` varchar(20) DEFAULT NULL,
  `consts_value` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `system_consts` */

insert  into `system_consts`(`id`,`consts_key`,`consts_value`,`create_time`) values (1,'RECHARGE_REST_URL','http://www.baidu.com','2018-12-21 09:47:53');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
