# ************************************************************
# Sequel Pro SQL dump
# Version 5446
#
# https://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Database: wework
# Generation Time: 2021-08-12 08:30:51 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `account`;

CREATE TABLE `account` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT,
  `employee_id` int(11) NOT NULL,
  `token` varchar(100) NOT NULL DEFAULT '',
  `password` varchar(100) NOT NULL,
  `authority` varchar(200) NOT NULL DEFAULT '',
  `creation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `account_employee_id` (`employee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table chat_data_contact_ship
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_data_contact_ship`;

CREATE TABLE `chat_data_contact_ship` (
  `history_id` int(11) DEFAULT NULL,
  `msgid` varchar(100) NOT NULL DEFAULT '',
  `user_id` varchar(50) DEFAULT NULL,
  `action` varchar(10) DEFAULT NULL COMMENT '1: sender(发送者); 2: receiver(消息接收者)',
  `user_type` tinyint(4) DEFAULT NULL COMMENT '1: 公司员工; 2: 客户',
  `user_name` varchar(50) DEFAULT NULL,
  `user_avatar` text,
  `user_position` varchar(50) DEFAULT NULL,
  `user_gender` tinyint(11) DEFAULT NULL COMMENT '性别 0-未知 1-男性 2-女性'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table chat_data_history
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_data_history`;

CREATE TABLE `chat_data_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `seq` int(11) DEFAULT NULL,
  `msgid` varchar(100) DEFAULT NULL,
  `public_key_ver` int(11) DEFAULT NULL,
  `encrypt_random_key` text,
  `encrypt_chat_msg` longtext,
  `creation_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='从企业微信拉取回的原始数据';



# Dump of table chat_data_parsed
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_data_parsed`;

CREATE TABLE `chat_data_parsed` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `history_id` int(11) unsigned NOT NULL,
  `msgid` varchar(100) DEFAULT NULL,
  `action` varchar(100) DEFAULT '',
  `msgtype` varchar(100) DEFAULT NULL,
  `roomid` varchar(100) DEFAULT NULL,
  `msgtime` datetime DEFAULT NULL,
  `msg` text,
  `media_url` text,
  `extra_media` tinyint(4) NOT NULL DEFAULT '0',
  `content` longtext,
  `sender` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_cd_p_hid` (`history_id`),
  KEY `index_c_d_p_roomid` (`roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='从原始内容(chat_data_ori)解析出来的数据';



# Dump of table chat_data_room
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_data_room`;

CREATE TABLE `chat_data_room` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `history_id` int(11) DEFAULT NULL,
  `from` varchar(64) DEFAULT '',
  `to` varchar(64) DEFAULT '',
  `roomid` varchar(64) DEFAULT NULL,
  `room_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_c_d_h_id` (`history_id`),
  KEY `index_c_d_r_roomid` (`roomid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table chat_data_room_user
# ------------------------------------------------------------

DROP TABLE IF EXISTS `chat_data_room_user`;

CREATE TABLE `chat_data_room_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `history_id` int(11) DEFAULT NULL,
  `room_user` varchar(64) DEFAULT '',
  `room_id` varchar(64) DEFAULT NULL,
  `room_name` varchar(100) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_c_d_r_u_n` (`room_user`,`room_id`),
  KEY `index_c_d_h_id` (`history_id`),
  KEY `index_c_d_r_roomid` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table config
# ------------------------------------------------------------

DROP TABLE IF EXISTS `config`;

CREATE TABLE `config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `module` varchar(100) NOT NULL DEFAULT '',
  `code` varchar(100) NOT NULL DEFAULT '',
  `value` text NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `index_c_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table contact_external
# ------------------------------------------------------------

DROP TABLE IF EXISTS `contact_external`;

CREATE TABLE `contact_external` (
  `external_userid` varchar(50) NOT NULL DEFAULT '' COMMENT '外部联系人的userid',
  `name` varchar(100) DEFAULT NULL,
  `avatar` text,
  `type` tinyint(4) DEFAULT NULL COMMENT '外部联系人的类型，1表示该外部联系人是微信用户，2表示该外部联系人是企业微信用户',
  `gender` tinyint(4) DEFAULT NULL COMMENT '外部联系人性别 0-未知 1-男性 2-女性',
  `position` varchar(50) DEFAULT NULL COMMENT '外部联系人的职位，如果外部企业或用户选择隐藏职位，则不返回，仅当联系人类型是企业微信用户时有此字段',
  `corp_name` varchar(50) DEFAULT NULL COMMENT '外部联系人所在企业的简称，仅当联系人类型是企业微信用户时有此字段',
  `corp_full_name` varchar(100) DEFAULT NULL COMMENT '外部联系人所在企业的主体名称，仅当联系人类型是企业微信用户时有此字段',
  `context` longtext,
  UNIQUE KEY `idx_contact_external_euid` (`external_userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `employee`;

CREATE TABLE `employee` (
  `id` int(9) unsigned NOT NULL AUTO_INCREMENT,
  `userid` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `mobile` varchar(20) DEFAULT '',
  `thumb_avatar` text,
  `avatar` text,
  `position` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `department` varchar(100) DEFAULT NULL,
  `creation_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `cp_userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



# Dump of table u_contact
# ------------------------------------------------------------

DROP VIEW IF EXISTS `u_contact`;

CREATE TABLE `u_contact` (
   `uid` VARCHAR(100) NOT NULL DEFAULT '',
   `uname` VARCHAR(100) NULL DEFAULT NULL,
   `uposition` VARCHAR(50) NULL DEFAULT NULL,
   `thumb_avatar` TEXT NULL DEFAULT NULL,
   `avatar` TEXT NULL DEFAULT NULL,
   `utype` BIGINT(20) NOT NULL DEFAULT '0'
) ENGINE=MyISAM;





# Replace placeholder table for u_contact with correct view syntax
# ------------------------------------------------------------

DROP TABLE `u_contact`;

CREATE ALGORITHM=UNDEFINED DEFINER=`temp_account`@`%` SQL SECURITY INVOKER VIEW `u_contact`
AS SELECT
   `employee`.`userid` AS `uid`,
   `employee`.`name` AS `uname`,
   `employee`.`position` AS `uposition`,
   `employee`.`thumb_avatar` AS `thumb_avatar`,
   `employee`.`avatar` AS `avatar`,1 AS `utype`
FROM `employee` union select `contact_external`.`external_userid` AS `uid`,`contact_external`.`name` AS `uname`,`contact_external`.`position` AS `uposition`,`contact_external`.`avatar` AS `thumb_avatar`,`contact_external`.`avatar` AS `avatar`,2 AS `utype` from `contact_external`;

/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
