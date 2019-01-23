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
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

/*Data for the table `menu` */

insert  into `menu`(`menu_Id`,`menu_Name`,`menu_Url`,`menu_Pid`,`menu_Target`) values (1,'管理中心',NULL,NULL,'&#xe674;'),(2,'公告','',NULL,'&#xe667;'),(3,'账户管理',NULL,NULL,'&#xe673;'),(4,'财务管理',NULL,NULL,'&#xe735;'),(5,'结算管理',NULL,NULL,'&#xe659;'),(6,'订单管理',NULL,NULL,'&#xe63c;'),(7,'API管理',NULL,NULL,'&#xe60b;'),(8,'结算申请','merchant/settle/order/pre',5,'myframe'),(9,'代付申请','merchant/place/order/pre',5,'myframe'),(10,'结算记录','view/merchantsettleList',5,'myframe'),(11,'代付记录','view/merchantplaceList',5,'myframe'),(12,'通道费率','view/paypageList',7,'myframe'),(13,'API开发文档','merchant/api',7,'myframe'),(14,'充值订单','view/merchantRechargeList',6,'myframe'),(17,'资金记录','merchantfinance/amount',4,'myframe'),(18,'对账单','view/merchantbill',4,'myframe'),(19,'基本信息','merchant/accountinfo',3,NULL),(20,'银行卡管理','view/merchantbindbankList',3,NULL),(21,'认证信息','view/merchantaudit',3,NULL),(22,'登录密码','view/merchantloginpwd',3,NULL),(23,'支付密码','view/merchantpaypwd',3,NULL),(24,'站内公告','notice/list',2,NULL),(26,'保证金明细','view/merchantfrezzList',4,NULL),(27,'代付银行卡管理','view/merchantplacebindbankList',3,NULL),(28,'商户管理',NULL,NULL,NULL),(29,'我的商户','view/merchantproxy',28,NULL),(30,'登录日志','view/loginlog',1,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_account_bindbank` */

insert  into `merchant_account_bindbank`(`id`,`merchant_id`,`bank_name`,`branch_name`,`account_name`,`account_number`,`province`,`default_status`,`remark`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52','中国银行','怀化分行','麻辣烫','6227002930070614333',NULL,1,NULL),(2,'141b6ccb8bde4b10b1d0c4a5db91cf52','中国银行','怀化分行','伍佰','6227002930070614332',NULL,1,NULL),(3,'141b6ccb8bde4b10b1d0c4a5db91cf52','建设银行','长沙市支行','刘德华','6227 0029 3007 0614 333',NULL,1,NULL),(4,'141b6ccb8bde4b10b1d0c4a5db91cf52','建设银行','怀化支行','张学友','6227002930070614333',NULL,1,NULL),(5,'141b6ccb8bde4b10b1d0c4a5db91cf52','建设银行','怀化支行','张韶涵','6227002930070614333',NULL,0,NULL),(6,'141b6ccb8bde4b10b1d0c4a5db91cf52','建设银行','湖南省支行','黎明','6227002930070614333',NULL,1,NULL),(7,'141b6ccb8bde4b10b1d0c4a5db91cf52','建设银行','怀化支行','董垂瑞','6227002930070614333',NULL,1,NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_account_info` */

insert  into `merchant_account_info`(`id`,`uuid`,`proxy_id`,`account_name`,`mobile_number`,`pass_word`,`create_time`,`status`,`login_ip`,`helppay_status`,`pay_level`,`role_id`) values (1,'2222',NULL,'mwj','17666126557','mwj',NULL,0,NULL,0,1,1),(5,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL,'mwj666788','17666126558','c4ca4238a0b923820dcc509a6f75849b','2019-01-08 11:58:30',0,NULL,0,1,1),(6,'501b880cbaef4247b17e72f625c55ea7',NULL,NULL,'17666126559','c4ca4238a0b923820dcc509a6f75849b',NULL,1,NULL,1,1,2),(7,'a3565fbfb32b4da7a7cf580c2dbe4da5',NULL,NULL,'17666126555','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL),(8,'646d165011ea4c6d8a7aeeb714f85735',NULL,NULL,'17666126330','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL),(9,'c5621042d2e74ffd885a86cdc4b146fe',NULL,NULL,'17666126510','2ff09cb743b3398510935a2bc3c003de',NULL,1,NULL,1,1,NULL);

/*Table structure for table `merchant_api` */

DROP TABLE IF EXISTS `merchant_api`;

CREATE TABLE `merchant_api` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `md5_key` varchar(32) DEFAULT NULL,
  `api_doc_url` varchar(200) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_api` */

insert  into `merchant_api`(`id`,`merchant_id`,`secret_key`,`md5_key`,`api_doc_url`,`create_time`,`update_time`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52','1a280dcd6d354a3b80dffad647100c74','dsf2514sd','https://dev.mysql.com/get/Downloads/MySQL-8.0/mysql-8.0.13-winx64-debug-test.zip','2018-12-20 09:50:48',NULL),(2,'501b880cbaef4247b17e72f625c55ea7','10f4a5534852475da415c1eec12bbfbc',NULL,NULL,'2019-01-02 17:20:56',NULL),(3,'a3565fbfb32b4da7a7cf580c2dbe4da5','98b926f89f824397bcaf5da68f476db8',NULL,NULL,'2019-01-02 17:35:40',NULL),(4,'646d165011ea4c6d8a7aeeb714f85735','816cc564e7274880a0e819b4994bf36d',NULL,NULL,'2019-01-03 11:12:17',NULL),(5,'c5621042d2e74ffd885a86cdc4b146fe','7dbb61498e1f485795e2e982206a6478',NULL,NULL,'2019-01-21 09:48:49',NULL);

/*Table structure for table `merchant_bill` */

DROP TABLE IF EXISTS `merchant_bill`;

CREATE TABLE `merchant_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL,
  `bill_name` varchar(20) DEFAULT NULL,
  `recharge_amount` decimal(30,4) DEFAULT NULL,
  `recharge_number` int(11) DEFAULT NULL,
  `recharge_service_amount` decimal(30,4) DEFAULT NULL,
  `settle_amount` decimal(30,4) DEFAULT NULL,
  `settle_number` int(11) DEFAULT NULL,
  `settle_service_amount` decimal(30,4) DEFAULT NULL,
  `place_amount` decimal(30,4) DEFAULT NULL,
  `place_number` int(11) DEFAULT NULL,
  `place_service_amount` decimal(30,4) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_bill` */

insert  into `merchant_bill`(`id`,`merchant_id`,`bill_name`,`recharge_amount`,`recharge_number`,`recharge_service_amount`,`settle_amount`,`settle_number`,`settle_service_amount`,`place_amount`,`place_number`,`place_service_amount`,`create_time`) values (34,'141b6ccb8bde4b10b1d0c4a5db91cf52','2018年12月账单','23522.0001',10,'470.0200','1.0000',1,NULL,'56.0000',1,NULL,'2019-01-17 17:04:05');

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_finance` */

insert  into `merchant_finance`(`id`,`blance_amount`,`fronze_amount`,`merchant_id`,`pay_password`,`create_time`,`status`) values (2,'10000.0000','0.0000','141b6ccb8bde4b10b1d0c4a5db91cf52','f624b2750e8a5ae77a86d074209a70e8','2019-01-18 15:19:33',0),(3,'200.0000','0.0000','501b880cbaef4247b17e72f625c55ea7','e10adc3949ba59abbe56e057f20f883e','2019-01-16 14:58:28',-1),(4,'0.0000','0.0000','a3565fbfb32b4da7a7cf580c2dbe4da5','5cf9cd00a0c3448002b452177b765859','2019-01-02 17:35:40',-1),(5,'0.0000','0.0000','646d165011ea4c6d8a7aeeb714f85735','a01610228fe998f515a72dd730294d87','2019-01-03 11:12:17',-1),(6,'0.0000','0.0000','c5621042d2e74ffd885a86cdc4b146fe','202cb962ac59075b964b07152d234b70','2019-01-21 09:48:49',-1);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_frezzon` */

insert  into `merchant_frezzon`(`id`,`merchant_id`,`order_number`,`amount`,`arrival_time`,`frezz_time`,`really_arrival_time`,`status`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52','12121212122','100.0000','2019-01-10 17:41:09','2019-01-22 17:41:12','2019-01-22 17:41:12',0),(2,'141b6ccb8bde4b10b1d0c4a5db91cf52','1212234343','100.0000','2019-01-10 17:41:09','2019-01-22 17:41:12','2019-01-18 15:13:00',0),(3,'141b6ccb8bde4b10b1d0c4a5db91cf52','1','10000.0000','2019-01-18 15:20:58','2019-01-18 15:20:03','2019-01-18 15:21:00',0);

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
) ENGINE=InnoDB AUTO_INCREMENT=331 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_login_log` */

insert  into `merchant_login_log`(`id`,`login_time`,`login_address`,`IP`,`merchant_id`) values (1,'2018-12-19 17:57:27','XX-XX-内网IP','127.0.0.1','2222'),(2,'2018-12-20 10:13:15','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(3,'2018-12-20 10:15:05','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(4,'2018-12-20 10:15:14','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(5,'2018-12-20 10:15:18','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(6,'2018-12-20 10:15:19','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(7,'2018-12-20 10:15:20','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(8,'2018-12-20 10:15:21','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(9,'2018-12-20 10:15:21','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(10,'2018-12-21 15:38:28',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(11,'2018-12-21 15:45:34','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(12,'2018-12-21 16:10:09','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(13,'2018-12-26 11:00:15','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(14,'2018-12-26 11:05:35',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(15,'2018-12-26 11:09:42','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(16,'2018-12-26 11:12:01',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(17,'2018-12-26 11:16:41',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(18,'2018-12-26 11:22:57','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(19,'2018-12-26 11:27:00',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(20,'2018-12-26 11:30:49','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(21,'2018-12-26 11:35:43','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(22,'2018-12-26 11:40:13','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(23,'2018-12-26 11:40:25',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(24,'2018-12-26 16:03:00','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(25,'2018-12-26 16:48:44',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(26,'2018-12-28 16:11:27',NULL,'127.0.0.1','2222'),(27,'2018-12-28 16:11:44',NULL,'127.0.0.1','2222'),(28,'2018-12-28 16:13:33','XX-XX-内网IP','127.0.0.1','2222'),(29,'2019-01-03 14:36:54','XX-XX-内网IP','127.0.0.1','2222'),(30,'2019-01-03 14:37:34','XX-XX-内网IP','127.0.0.1','2222'),(31,'2019-01-03 14:37:44',NULL,'127.0.0.1','2222'),(32,'2019-01-03 15:39:24',NULL,'127.0.0.1','2222'),(33,'2019-01-03 15:53:02','XX-XX-内网IP','127.0.0.1','2222'),(34,'2019-01-03 15:54:54',NULL,'127.0.0.1','2222'),(35,'2019-01-03 15:55:21','XX-XX-内网IP','127.0.0.1','2222'),(36,'2019-01-03 15:59:18','XX-XX-内网IP','127.0.0.1','2222'),(37,'2019-01-03 16:00:34','XX-XX-内网IP','127.0.0.1','2222'),(38,'2019-01-03 16:00:34','XX-XX-内网IP','127.0.0.1','2222'),(39,'2019-01-03 16:02:46','XX-XX-内网IP','127.0.0.1','2222'),(40,'2019-01-04 20:46:16',NULL,'127.0.0.1','2222'),(41,'2019-01-05 12:08:39','XX-XX-内网IP','127.0.0.1','2222'),(42,'2019-01-05 16:45:11','XX-XX-内网IP','127.0.0.1','2222'),(43,'2019-01-05 16:46:43','XX-XX-内网IP','127.0.0.1','2222'),(44,'2019-01-05 17:14:44','XX-XX-内网IP','127.0.0.1','2222'),(45,'2019-01-05 17:33:41','XX-XX-内网IP','127.0.0.1','2222'),(46,'2019-01-05 17:33:41',NULL,'127.0.0.1','2222'),(47,'2019-01-05 17:40:03','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(48,'2019-01-05 17:40:27','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(49,'2019-01-05 17:54:33',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(50,'2019-01-05 17:58:41','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(51,'2019-01-05 17:58:37','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(52,'2019-01-05 18:06:36',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(53,'2019-01-05 18:06:34','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(54,'2019-01-05 18:09:11','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(55,'2019-01-05 18:19:14','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(56,'2019-01-05 18:19:18','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(57,'2019-01-05 19:21:08',NULL,'127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(58,'2019-01-06 23:48:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(59,'2019-01-06 23:48:09',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(60,'2019-01-06 23:50:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(61,'2019-01-06 23:51:35',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(62,'2019-01-08 09:37:45',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(63,'2019-01-08 11:12:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(64,'2019-01-08 13:21:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(65,'2019-01-08 13:33:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(66,'2019-01-08 13:39:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(67,'2019-01-08 13:42:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(68,'2019-01-08 14:03:16',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(69,'2019-01-08 14:27:12',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(70,'2019-01-08 15:36:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(71,'2019-01-08 16:19:30',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(72,'2019-01-08 16:45:38',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(73,'2019-01-08 17:14:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(74,'2019-01-08 17:19:14',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(75,'2019-01-08 17:25:50',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(76,'2019-01-08 17:30:12',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(77,'2019-01-08 17:31:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(78,'2019-01-08 17:50:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(79,'2019-01-08 17:57:41',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(80,'2019-01-08 17:58:22',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(81,'2019-01-08 18:03:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(82,'2019-01-08 18:06:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(83,'2019-01-08 18:07:12',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(84,'2019-01-08 18:10:00',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(85,'2019-01-09 09:17:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(86,'2019-01-09 11:04:37',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(87,'2019-01-09 11:18:47',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(88,'2019-01-09 11:49:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(89,'2019-01-09 13:00:30',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(90,'2019-01-09 13:02:54',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(91,'2019-01-09 13:03:40',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(92,'2019-01-09 14:31:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(93,'2019-01-09 14:55:47',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(94,'2019-01-09 14:57:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(95,'2019-01-09 14:59:31',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(96,'2019-01-09 14:59:54',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(97,'2019-01-09 15:07:57',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(98,'2019-01-09 15:20:15',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(99,'2019-01-09 15:55:50',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(100,'2019-01-09 15:59:16',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(101,'2019-01-09 16:02:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(102,'2019-01-09 16:05:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(103,'2019-01-09 16:24:54',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(104,'2019-01-09 16:26:02',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(105,'2019-01-09 16:38:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(106,'2019-01-09 16:42:54',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(107,'2019-01-09 17:04:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(108,'2019-01-09 17:06:57',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(109,'2019-01-09 17:08:37',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(110,'2019-01-09 17:37:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(111,'2019-01-10 10:03:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(112,'2019-01-10 10:10:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(113,'2019-01-10 10:10:44',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(114,'2019-01-10 11:05:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(115,'2019-01-10 13:19:03',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(116,'2019-01-10 13:25:58',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(117,'2019-01-10 13:28:45',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(118,'2019-01-10 14:11:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(119,'2019-01-10 14:17:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(120,'2019-01-10 14:49:19',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(121,'2019-01-10 14:59:06',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(122,'2019-01-10 15:10:29',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(123,'2019-01-10 15:48:48',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(124,'2019-01-10 16:04:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(125,'2019-01-10 16:56:46',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(126,'2019-01-10 17:11:31',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(127,'2019-01-10 17:30:19',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(128,'2019-01-10 17:36:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(129,'2019-01-10 17:50:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(130,'2019-01-10 18:04:49',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(131,'2019-01-11 09:30:14',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(132,'2019-01-11 09:35:00',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(133,'2019-01-11 09:39:57',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(134,'2019-01-11 09:41:16',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(135,'2019-01-11 09:44:18',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(136,'2019-01-11 10:18:45',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(137,'2019-01-11 10:47:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(138,'2019-01-11 11:36:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(139,'2019-01-11 11:57:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(140,'2019-01-11 13:55:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(141,'2019-01-11 13:55:58',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(142,'2019-01-11 13:56:38',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(143,'2019-01-11 13:56:58',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(144,'2019-01-11 14:00:43',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(145,'2019-01-11 14:00:55',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(146,'2019-01-11 14:01:18',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(147,'2019-01-11 14:17:29',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(148,'2019-01-11 14:17:35',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(149,'2019-01-11 14:17:41',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(150,'2019-01-11 15:22:11',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(151,'2019-01-11 15:24:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(152,'2019-01-11 15:50:30',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(153,'2019-01-11 15:54:48',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(154,'2019-01-11 15:55:44',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(155,'2019-01-11 16:21:26',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(156,'2019-01-11 16:21:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(157,'2019-01-11 16:24:15',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(158,'2019-01-11 16:32:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(159,'2019-01-11 16:36:47',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(160,'2019-01-11 17:28:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(161,'2019-01-11 18:14:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(162,'2019-01-11 18:17:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(163,'2019-01-11 18:21:07',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(164,'2019-01-11 18:26:19',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(165,'2019-01-11 18:34:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(166,'2019-01-14 09:18:41',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(167,'2019-01-14 11:42:06',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(168,'2019-01-14 11:45:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(169,'2019-01-14 12:00:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(170,'2019-01-14 12:10:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(171,'2019-01-14 12:15:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(172,'2019-01-14 12:28:09',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(173,'2019-01-14 12:28:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(174,'2019-01-14 12:32:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(175,'2019-01-14 14:41:21',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(176,'2019-01-14 14:41:43',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(177,'2019-01-14 14:41:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(178,'2019-01-14 14:45:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(179,'2019-01-14 14:48:49',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(180,'2019-01-14 14:49:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(181,'2019-01-14 14:57:48',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(182,'2019-01-14 15:25:15',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(183,'2019-01-14 16:39:07',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(184,'2019-01-14 16:39:28',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(185,'2019-01-14 16:41:09',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(186,'2019-01-14 16:43:01',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(187,'2019-01-14 16:44:07',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(188,'2019-01-14 16:54:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(189,'2019-01-14 17:01:28',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(190,'2019-01-14 17:23:26',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(191,'2019-01-14 17:24:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(192,'2019-01-14 17:25:57',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(193,'2019-01-14 17:28:27',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(194,'2019-01-14 17:30:30',NULL,'192.168.0.114','141b6ccb8bde4b10b1d0c4a5db91cf52'),(195,'2019-01-14 18:10:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(196,'2019-01-14 18:13:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(197,'2019-01-14 18:14:37',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(198,'2019-01-15 09:24:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(199,'2019-01-15 09:53:18',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(200,'2019-01-15 09:59:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(201,'2019-01-15 11:04:58',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(202,'2019-01-15 11:05:43',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(203,'2019-01-15 11:20:07',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(204,'2019-01-15 11:23:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(205,'2019-01-15 11:26:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(206,'2019-01-15 13:43:47',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(207,'2019-01-15 14:06:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(208,'2019-01-15 15:10:49',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(209,'2019-01-15 15:28:18',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(210,'2019-01-15 15:49:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(211,'2019-01-15 16:26:52',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(212,'2019-01-15 17:10:00',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(213,'2019-01-15 17:12:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(214,'2019-01-15 17:28:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(215,'2019-01-15 17:34:03',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(216,'2019-01-15 17:34:19',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(217,'2019-01-16 09:26:02',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(218,'2019-01-16 09:54:40',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(219,'2019-01-16 11:01:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(220,'2019-01-16 11:13:04',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(221,'2019-01-16 11:18:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(222,'2019-01-16 11:23:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(223,'2019-01-16 11:24:41',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(224,'2019-01-16 13:34:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(225,'2019-01-16 13:34:59',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(226,'2019-01-16 13:39:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(227,'2019-01-16 13:42:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(228,'2019-01-16 13:54:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(229,'2019-01-16 14:12:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(230,'2019-01-16 14:15:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(231,'2019-01-16 14:19:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(232,'2019-01-16 14:27:37',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(233,'2019-01-16 14:36:59',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(234,'2019-01-16 14:40:04',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(235,'2019-01-16 14:41:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(236,'2019-01-16 14:43:46',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(237,'2019-01-16 14:49:27',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(238,'2019-01-16 14:53:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(239,'2019-01-16 14:57:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(240,'2019-01-16 15:00:58',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(241,'2019-01-16 15:05:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(242,'2019-01-16 15:21:26',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(243,'2019-01-16 15:21:44',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(244,'2019-01-16 15:35:21',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(245,'2019-01-16 16:51:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(246,'2019-01-17 10:58:16',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(247,'2019-01-17 10:58:26',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(248,'2019-01-17 11:04:29',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(249,'2019-01-17 11:28:23',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(250,'2019-01-17 12:21:55',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(251,'2019-01-17 13:38:27',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(252,'2019-01-17 14:59:04',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(253,'2019-01-17 17:14:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(254,'2019-01-17 17:21:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(255,'2019-01-17 17:22:12',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(256,'2019-01-18 09:13:30',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(257,'2019-01-18 09:38:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(258,'2019-01-18 10:02:38',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(259,'2019-01-18 10:03:35',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(260,'2019-01-18 14:24:06',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(261,'2019-01-18 14:41:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(262,'2019-01-18 15:14:19',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(263,'2019-01-18 15:19:49',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(264,'2019-01-18 15:43:46',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(265,'2019-01-18 15:56:15',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(266,'2019-01-18 16:06:53',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(267,'2019-01-18 16:12:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(268,'2019-01-18 16:13:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(269,'2019-01-18 16:16:00',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(270,'2019-01-18 17:09:34',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(271,'2019-01-21 09:42:20',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(272,'2019-01-21 09:48:58',NULL,NULL,'c5621042d2e74ffd885a86cdc4b146fe'),(273,'2019-01-21 09:49:18',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(274,'2019-01-21 10:06:33',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(275,'2019-01-21 10:10:49',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(276,'2019-01-21 10:11:27',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(277,'2019-01-21 10:12:23',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(278,'2019-01-21 10:14:38',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(279,'2019-01-21 10:15:15',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(280,'2019-01-21 10:15:48',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(281,'2019-01-21 10:15:56',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(282,'2019-01-21 10:19:08',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(283,'2019-01-21 10:19:15',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(284,'2019-01-21 10:20:01',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(285,'2019-01-21 10:23:52',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(286,'2019-01-21 10:27:22',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(287,'2019-01-21 10:32:25',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(288,'2019-01-21 10:33:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(289,'2019-01-21 10:40:39',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(290,'2019-01-21 10:57:47',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(291,'2019-01-21 10:58:06',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(292,'2019-01-21 11:25:05',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(293,'2019-01-21 11:36:28',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(294,'2019-01-21 13:55:07',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(295,'2019-01-21 14:31:27',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(296,'2019-01-21 14:57:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(297,'2019-01-21 15:02:32',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(298,'2019-01-21 15:06:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(299,'2019-01-21 15:27:42',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(300,'2019-01-21 16:18:29',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(301,'2019-01-21 16:20:54',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(302,'2019-01-21 16:22:37',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(303,'2019-01-21 16:37:56',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(304,'2019-01-21 18:07:36','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(305,'2019-01-22 09:52:16',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(306,'2019-01-22 10:25:24',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(307,'2019-01-22 11:56:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(308,'2019-01-22 15:59:43',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(309,'2019-01-22 16:15:39',NULL,'0:0:0:0:0:0:0:1','501b880cbaef4247b17e72f625c55ea7'),(310,'2019-01-22 16:18:34',NULL,NULL,'501b880cbaef4247b17e72f625c55ea7'),(311,'2019-01-22 16:19:13',NULL,'0:0:0:0:0:0:0:1','501b880cbaef4247b17e72f625c55ea7'),(312,'2019-01-22 16:19:48',NULL,'0:0:0:0:0:0:0:1','501b880cbaef4247b17e72f625c55ea7'),(313,'2019-01-22 16:23:04',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(314,'2019-01-22 17:54:30',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(315,'2019-01-22 18:04:32','XX-XX-内网IP','127.0.0.1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(316,'2019-01-23 13:47:10',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(317,'2019-01-23 13:48:59',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(318,'2019-01-23 13:54:55',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(319,'2019-01-23 14:01:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(320,'2019-01-23 14:42:56',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(321,'2019-01-23 14:43:57',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(322,'2019-01-23 14:44:44',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52'),(323,'2019-01-23 15:00:36',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(324,'2019-01-23 15:16:13',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(325,'2019-01-23 15:55:51',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(326,'2019-01-23 15:58:05',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(327,'2019-01-23 16:23:33',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(328,'2019-01-23 16:42:56',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(329,'2019-01-23 17:18:09',NULL,'0:0:0:0:0:0:0:1','141b6ccb8bde4b10b1d0c4a5db91cf52'),(330,'2019-01-23 17:30:35',NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52');

/*Table structure for table `merchant_place_account_bindbank` */

DROP TABLE IF EXISTS `merchant_place_account_bindbank`;

CREATE TABLE `merchant_place_account_bindbank` (
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
  UNIQUE KEY `merchant_place_account_bindbank_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_place_account_bindbank` */

insert  into `merchant_place_account_bindbank`(`id`,`merchant_id`,`bank_name`,`branch_name`,`account_name`,`account_number`,`province`,`default_status`,`remark`) values (1,'141b6ccb8bde4b10b1d0c4a5db91cf52','东莞市商','北京市永德路141号','张新发','6223330166210041333',NULL,0,NULL);

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
  `trade_type` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_place_order_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_place_order` */

insert  into `merchant_place_order`(`id`,`order_number`,`merchant_id`,`pay_amount`,`service_amount`,`arrive_amount`,`bank_name`,`branch_name`,`bank_number`,`province`,`city`,`create_time`,`handler_time`,`handler_name`,`status`,`type`,`descreption`,`trade_type`) values (1,'P20190116141549076585423','141b6ccb8bde4b10b1d0c4a5db91cf52','56',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2018-12-13 14:15:49',NULL,NULL,0,2,NULL,NULL),(2,'P20190116142342460252841','141b6ccb8bde4b10b1d0c4a5db91cf52','100',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:23:42',NULL,NULL,1,2,NULL,NULL),(3,'P20190116142425020932154','141b6ccb8bde4b10b1d0c4a5db91cf52','100',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:24:25',NULL,NULL,1,2,NULL,NULL),(4,'P20190116144355497930314','141b6ccb8bde4b10b1d0c4a5db91cf52','200',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:43:56',NULL,NULL,1,2,NULL,NULL),(5,'P20190116144959007826352','141b6ccb8bde4b10b1d0c4a5db91cf52','500',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:49:59',NULL,NULL,1,2,NULL,NULL),(6,'P20190116145449019114958','141b6ccb8bde4b10b1d0c4a5db91cf52','2000',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:54:49',NULL,NULL,1,2,NULL,NULL),(7,'P20190116145727738779285','141b6ccb8bde4b10b1d0c4a5db91cf52','3000',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:57:28',NULL,NULL,1,2,NULL,NULL),(8,'P20190116145845979227433','141b6ccb8bde4b10b1d0c4a5db91cf52','100',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:58:46',NULL,NULL,1,2,NULL,NULL),(9,'P20190116145918675220863','141b6ccb8bde4b10b1d0c4a5db91cf52','100',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 14:59:19',NULL,NULL,1,2,NULL,NULL),(10,'P20190116150158491556220','141b6ccb8bde4b10b1d0c4a5db91cf52','2000',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 15:01:58','2019-01-16 15:02:04',NULL,-1,2,'订单提交操作异常！',NULL),(11,'P20190116151000001292322','141b6ccb8bde4b10b1d0c4a5db91cf52','2800',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 15:10:00','2019-01-16 15:10:05','系统',-1,2,'订单提交操作异常！',NULL),(12,'P20190116153845917246691','141b6ccb8bde4b10b1d0c4a5db91cf52','2800',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-16 15:38:46','2019-01-16 15:38:51','系统',-1,2,'订单提交操作异常！',NULL),(13,'P20190117111054635471332','141b6ccb8bde4b10b1d0c4a5db91cf52','1000',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-17 11:10:55','2019-01-17 11:11:00','系统',-1,2,'订单提交操作异常！',NULL),(14,'P20190121104845478297826','141b6ccb8bde4b10b1d0c4a5db91cf52','10000',NULL,NULL,'东莞市商','北京市永德路141号','6223330166210041333',NULL,NULL,'2019-01-21 10:48:45','2019-01-21 10:48:51','系统',-1,2,'订单提交操作异常！',NULL);

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
  `trade_type` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_recharge_order_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_recharge_order` */

insert  into `merchant_recharge_order`(`id`,`order_number`,`merchant_order_number`,`bank_order_number`,`merchant_id`,`type`,`pay_amount`,`service_amount`,`arrival_amount`,`create_time`,`success_time`,`pay_channel`,`pay_bank`,`descreption`,`status`,`trade_type`) values (1,'2018122110:44:04:273851084','',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000',NULL,NULL,'2018-12-21 10:44:04',NULL,'WY','ICBC',NULL,-1,NULL),(2,'2018122110:46:32:397935523','1',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000',NULL,NULL,'2018-12-21 10:46:32',NULL,'WY','ICBC',NULL,-1,NULL),(3,'2018122113:27:04:795981986','12',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000','0.0000','10.0000','2018-12-21 13:27:05','2018-12-21 13:31:31','WY','ICBC',NULL,0,NULL),(4,'2018122113:39:07:379276853','2',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10.0000','0.0000','10.0000','2018-12-21 13:39:07','2018-12-21 13:41:21','WY','ICBC',NULL,0,NULL),(5,'2018122113:46:38:125141528','3',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0001','20.0000','980.0000','2018-12-21 13:46:38','2018-12-21 13:46:58','WY','ICBC',NULL,0,NULL),(6,'2018122113:49:35:640461572','4',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10000.0000','200.0000','9800.0000','2018-12-21 13:49:36','2018-12-21 13:51:39','WY','ICBC',NULL,0,NULL),(7,'2018122113:52:49:671208416','5',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'10000.0000','200.0000','9800.0000','2018-12-21 13:52:50','2018-12-21 13:53:11','WY','ICBC',NULL,0,NULL),(8,'2018122113:54:50:883772530','6',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1.0000','0.0000','1.0000','2018-12-21 13:54:51','2018-12-21 13:55:03','WY','ICBC',NULL,0,NULL),(9,'2018122113:56:15:039235359','7',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'500.0000','10.0000','490.0000','2018-12-23 13:56:15','2018-12-21 13:56:33','WY','ICBC',NULL,0,NULL),(10,'2018122115:34:18:801288552','8',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0000','20.0000','980.0000','2018-12-23 15:34:19','2018-12-21 15:34:35','WY','ICBC',NULL,0,NULL),(11,'2018122116:04:11:315140933','111222333',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1000.0000','20.0000','980.0000','2018-12-22 16:04:11','2018-12-21 16:04:35','WY','ICBC',NULL,0,NULL),(12,'2018122116:09:42:000816829','20',NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',1,'1.0000','0.0200','0.9800','2018-12-22 16:09:42','2018-12-21 16:10:30','WY','ICBC',NULL,0,NULL);

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
  `trade_type` varchar(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_settle_order_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_settle_order` */

insert  into `merchant_settle_order`(`id`,`order_number`,`postal_amount`,`service_amount`,`arrival_amount`,`merchant_bind_bank_id`,`create_time`,`pay_time`,`status`,`type`,`descreption`,`handle_people`,`merchant_id`,`trade_type`) values (46,'S2018122611:40:26:885604882','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:27',NULL,0,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(47,'S2018122611:40:27:820540578','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(48,'S2018122611:40:28:600671258','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(49,'S2018122611:40:29:331690839','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(50,'S2018122611:40:30:063650034','1.0000',NULL,NULL,NULL,'2018-12-26 11:40:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(51,'S2018122611:41:30:289627120','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(52,'S2018122611:41:31:206574381','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:31',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(53,'S2018122611:41:32:256633999','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:32',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(54,'S2018122611:41:33:028805995','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:33',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(55,'S2018122611:41:33:718421647','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:34',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(56,'S2018122611:41:35:678842461','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:36',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(57,'S2018122611:41:40:852450151','10.0000',NULL,NULL,NULL,'2018-12-26 11:41:41',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(58,'S2018122616:03:03:350764830','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:03',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(59,'S2018122616:03:05:440255356','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:05',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(60,'S2018122616:03:06:339266813','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:06',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(61,'S2018122616:03:06:944718566','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(62,'S2018122616:03:07:607880287','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(63,'S2018122616:03:08:872377045','10.0000',NULL,NULL,NULL,'2018-12-26 16:03:09',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(64,'S2018122616:04:23:732747632','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(65,'S2018122616:04:24:051911397','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(66,'S2018122616:04:24:389832110','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:24',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(67,'S2018122616:04:27:667223892','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(68,'S2018122616:04:28:223156830','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:28',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(69,'S2018122616:04:28:626221644','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(70,'S2018122616:04:28:977233162','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(71,'S2018122616:04:29:322435337','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:29',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(72,'S2018122616:04:29:681303051','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(73,'S2018122616:04:30:014729675','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(74,'S2018122616:04:30:381339455','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:30',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(75,'S2018122616:04:30:750631621','5.0000',NULL,NULL,NULL,'2018-12-26 16:04:31',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(76,'S2018122616:48:47:895852844','5.0000',NULL,NULL,1,'2018-12-26 16:48:48',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(77,'S2018122616:48:53:911396620','5.0000',NULL,NULL,1,'2018-12-26 16:48:54',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(78,'S2018122616:48:54:775343478','5.0000',NULL,NULL,1,'2018-12-26 16:48:55',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(79,'S2018122616:48:55:356519393','5.0000',NULL,NULL,1,'2018-12-26 16:48:55',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(80,'S2018122616:48:55:861533230','5.0000',NULL,NULL,1,'2018-12-26 16:48:56',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(81,'S2018122616:48:56:396532381','5.0000',NULL,NULL,1,'2018-12-26 16:48:56',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(82,'S2018122616:48:56:943707683','5.0000',NULL,NULL,1,'2018-12-26 16:48:57',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(83,'S2018122616:49:06:308906437','5.0000',NULL,NULL,2,'2018-12-26 16:49:06',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(84,'S2018122616:49:06:906111835','5.0000',NULL,NULL,2,'2018-12-26 16:49:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(85,'S2018122616:49:07:379122830','5.0000',NULL,NULL,2,'2018-12-26 16:49:07',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(86,'S2018122616:49:07:789850098','5.0000',NULL,NULL,2,'2018-12-26 16:49:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(87,'S2018122616:49:08:141841260','5.0000',NULL,NULL,2,'2018-12-26 16:49:08',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(88,'S2018122616:48:53:911396620','5.0000',NULL,NULL,1,'2018-12-26 16:48:54',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(89,'S20190115175936922956637','55.8820',NULL,NULL,7,'2019-01-15 17:59:37',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(90,'S20190116142333315885895','100.0000',NULL,NULL,7,'2019-01-16 14:23:33',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(91,'S20190116143108809298226','100.0000',NULL,NULL,7,'2019-01-16 14:31:09',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(92,'S20190116143138216609580','100.0000',NULL,NULL,7,'2019-01-16 14:31:38',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(93,'S20190116143226638290136','100.0000',NULL,NULL,7,'2019-01-16 14:32:27',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(94,'S20190116154902388934004','2800.0000',NULL,NULL,7,'2019-01-16 15:49:02',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL),(95,'S20190117105921593846612','2000.0000',NULL,NULL,7,'2019-01-17 10:59:22',NULL,-1,3,NULL,NULL,'141b6ccb8bde4b10b1d0c4a5db91cf52',NULL);

/*Table structure for table `merchant_verify` */

DROP TABLE IF EXISTS `merchant_verify`;

CREATE TABLE `merchant_verify` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile_number` varchar(11) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `code` varchar(6) DEFAULT NULL,
  `type` varchar(15) DEFAULT NULL,
  `avaliable_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `merchant_verify_PK` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

/*Data for the table `merchant_verify` */

insert  into `merchant_verify`(`id`,`mobile_number`,`email`,`code`,`type`,`avaliable_time`) values (1,'17666126557',NULL,'405445','LOGIN','2018-12-19 18:36:30'),(2,'17666126558',NULL,'219350','REGISTER','2018-12-20 09:51:00'),(3,'17666126558',NULL,'928908','LOGIN','2018-12-20 10:20:05'),(4,'17666126559',NULL,'344646','REGISTER','2019-01-02 17:22:53'),(5,'17666126880',NULL,'556832','REGISTER','2019-01-02 17:33:31'),(6,'17666411125',NULL,'931396','REGISTER','2019-01-02 17:35:31'),(7,'17666126555',NULL,'334452','REGISTER','2019-01-02 17:40:30'),(8,'17666126560',NULL,'730336','REGISTER','2019-01-03 10:34:26'),(9,'17666126330',NULL,'882286','REGISTER','2019-01-03 11:17:05'),(10,'17666126510',NULL,'383901','REGISTER','2019-01-21 09:53:21'),(11,'17666126558',NULL,'433659','UPDATE_PWD','2019-01-21 15:08:40'),(12,'17666126558',NULL,'449391','UPDATE_PAY_PWD','2019-01-21 14:47:55');

/*Table structure for table `mid_role_menu` */

DROP TABLE IF EXISTS `mid_role_menu`;

CREATE TABLE `mid_role_menu` (
  `role_id` int(11) DEFAULT NULL,
  `menu_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `mid_role_menu` */

insert  into `mid_role_menu`(`role_id`,`menu_id`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,26),(1,27),(2,28),(2,29),(1,30);

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `notice` */

insert  into `notice`(`id`,`content`,`create_time`,`title`,`status`) values (1,'2019-02-05 晚上18：00 -次日早上 9:00 停机','2019-01-11 10:18:26','停机版本发布',NULL),(2,'2019-02-01 晚上18：00 -次日早上 9:00 停机','2019-01-06 10:18:26','停机版本发布',NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Data for the table `pay_type` */

insert  into `pay_type`(`id`,`name`,`rate`,`status`,`merchant_level`) values (1,'银联','12',0,1),(2,'支付宝','10',0,1),(3,'微信','10',0,1);

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `role_Id` int(11) NOT NULL AUTO_INCREMENT,
  `role_Name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`role_Id`,`role_Name`) values (1,'商户'),(2,'代理商');

/*Table structure for table `system_consts` */

DROP TABLE IF EXISTS `system_consts`;

CREATE TABLE `system_consts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `consts_key` varchar(30) DEFAULT NULL,
  `consts_value` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `system_consts` */

insert  into `system_consts`(`id`,`consts_key`,`consts_value`,`create_time`) values (1,'SMART_RECHARGE_REST_URL','http://www.baidu.com','2019-01-21 17:12:54');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
