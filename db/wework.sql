
-- DROP DATABASE IF EXISTS `wework`;
-- CREATE DATABASE IF NOT EXISTS `wework`;
USE `wework`;

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `chat_data_content`;
CREATE TABLE `chat_data_content` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `history_id` int(11) NOT NULL,
  `msgid` varchar(100) NOT NULL COMMENT '消息id，消息的唯一标识，企业可以使用此字段进行消息去重。String类型',
  `action` varchar(20) DEFAULT NULL COMMENT '消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型。String类型',
  `from` varchar(100) DEFAULT NULL COMMENT '消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid。String类型',
  `tolist` varchar(1000) DEFAULT NULL COMMENT '消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组，内容为string类型',
  `roomid` varchar(100) DEFAULT NULL COMMENT '群聊消息的群id。如果是单聊则为空。String类型',
  `msgtime` datetime DEFAULT NULL COMMENT '消息发送时间戳，utc时间，ms单位。',
  `msgtype` varchar(50) DEFAULT NULL COMMENT '文本消息为：text。String类型',
  `content` longtext COMMENT '消息内容。String类型',
  PRIMARY KEY (`id`),
  KEY `index_cd_c_msgid` (`msgid`),
  KEY `index_cd_c_hid` (`history_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `chat_data_content_tolist`;
CREATE TABLE `chat_data_content_tolist` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `msgid` varchar(100) DEFAULT NULL,
  `from` varchar(100) DEFAULT NULL,
  `to` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_cd_c_t_msgid` (`msgid`),
  KEY `index_cd_c_t_f` (`from`),
  KEY `index_cd_c_t_t` (`to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COMMENT='从企业微信拉取回的原始数据';

DROP TABLE IF EXISTS `chat_data_parsed`;
CREATE TABLE `chat_data_parsed` (
  `history_id` int(11) unsigned NOT NULL,
  `msgid` varchar(100) DEFAULT NULL,
  `action` varchar(100) DEFAULT '',
  `msgtype` varchar(100) DEFAULT NULL,
  `content` longtext,
  KEY `index_cd_p_hid` (`history_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='从原始内容(chat_data_ori)解析出来的数据';

DROP TABLE IF EXISTS `chat_data_parsed_media`;
CREATE TABLE `chat_data_parsed_media` (
  `parse_id` int(11) DEFAULT NULL,
  `msgid` varchar(100) DEFAULT NULL,
  `msgtype` varchar(100) DEFAULT NULL,
  `media_path` text,
  KEY `index_cd_pm_pid` (`parse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(100) NOT NULL DEFAULT '',
  `value` text NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  PRIMARY KEY (`id`),
  KEY `index_c_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4;