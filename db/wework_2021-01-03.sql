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

INSERT INTO `account` (`id`, `employee_id`, `token`, `password`, `authority`, `creation_time`, `update_time`)
VALUES
	(1,30,'eb7706db-cde3-4804-af79-155e3dacf392','123','admin','2020-12-26 20:28:29','2020-12-26 20:28:29');

DROP TABLE IF EXISTS `chat_data_content`;

CREATE TABLE `chat_data_content` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `msgid` varchar(100) DEFAULT NULL COMMENT '消息id，消息的唯一标识，企业可以使用此字段进行消息去重。String类型',
  `action` varchar(20) DEFAULT NULL COMMENT '消息动作，目前有send(发送消息)/recall(撤回消息)/switch(切换企业日志)三种类型。String类型',
  `from` varchar(100) DEFAULT NULL COMMENT '消息发送方id。同一企业内容为userid，非相同企业为external_userid。消息如果是机器人发出，也为external_userid。String类型',
  `tolist` varchar(1000) DEFAULT NULL COMMENT '消息接收方列表，可能是多个，同一个企业内容为userid，非相同企业为external_userid。数组，内容为string类型',
  `roomid` varchar(100) DEFAULT NULL COMMENT '群聊消息的群id。如果是单聊则为空。String类型',
  `msgtime` timestamp NULL DEFAULT NULL COMMENT '消息发送时间戳，utc时间，ms单位。',
  `msgtype` varchar(50) DEFAULT NULL COMMENT '文本消息为：text。String类型',
  `content` text COMMENT '消息内容。String类型',
  PRIMARY KEY (`id`),
  KEY `index_cd_c_msgid` (`msgid`)
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


DROP TABLE IF EXISTS `chat_data_item`;

CREATE TABLE `chat_data_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `seq` int(11) DEFAULT NULL,
  `msg_id` varchar(100) NOT NULL DEFAULT '',
  `public_key_ver` int(11) DEFAULT NULL,
  `encrypt_random_key` text,
  `encrypt_chat_msg` text,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_seq` (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='从原始内容(chat_data_ori)解析出来的数据';


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


INSERT INTO `employee` (`id`, `userid`, `name`, `mobile`, `thumb_avatar`, `avatar`, `position`, `email`, `department`, `creation_time`, `update_time`)
VALUES
	(1,'XiuZhenLong','修振龙','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(2,'krisGaoXianSen','高松','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(3,'luker','徐星','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(4,'leon','刘效陈-急事电话联系','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(5,'ShangShanRuoShui','王娟','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(6,'GeYuanChun','戈园春  急事电话联系','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(7,'flora','胡悦余','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(8,'Guo','郭付凯','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(9,'DuYiYun','杜依芸','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(10,'june.','邹珺','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(11,'QiQi','丁琪-急事电话联系','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(12,'ustinian_zl','高正强','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(13,'petkit-XiaoXiao','萧萧','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(14,'LiuLiXia','刘丽霞','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(15,'sean','刘立业','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(16,'XieChao','谢超','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(17,'z','张贝贝','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(18,'JinXiang','金翔','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(19,'lexie','杨博文-平面','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(20,'ZhangYi','张怡','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(21,'DingWenJie','丁文佶','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(22,'WangYaWen','王亚雯','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(23,'YangLuLu','杨露露','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(24,'WuHongFu','吴洪甫','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(25,'HeWenQi','贺文琪','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(26,'LuYiZhao','室内设计-陆一曌','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(27,'ZhangTao','张涛','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(28,'JiangKangLuan','江康栾','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(29,'LiQingRu','李青茹','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(30,'WeiFeng','韦峰','13795358757',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(31,'ZhuXuan','朱璇','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(32,'GaoHuiYing','高慧莹','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(33,'WangWeiDong','王维栋-急事电话联系13222894750','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(34,'WangYu','王宇','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(35,'HuaChao','华超','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(36,'BianJie','卞杰','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(37,'ZhangYuZhen','张玉珍','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(38,'YuJin','于金-急事电话联系','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(39,'LiuLiXia_1','刘丽霞','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(40,'ZhangYuanJun','张元隽','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(41,'ZhuMaoZhou','朱茂舟','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(42,'LvMing','闾茗','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(43,'HangXinTong','杭心童','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(44,'WangMengCheng','王梦成','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(45,'ZhouWei','周维','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(46,'ZhangNing','张宁','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(47,'YuJie','俞杰','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(48,'DuHui','杜惠','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(49,'XuRuoNan','徐若男','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(50,'DongWenShu','董文舒','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(51,'WangJingLan','王静岚','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(52,'PanFuRong','潘芙蓉','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(53,'TuoXueHua','庹雪华','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(54,'XuBo','徐波','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(55,'LiXiang','李翔 平面设计师','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(56,'GuBin','顾斌','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(57,'LiuYanYan','刘艳燕','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(58,'LiTianYu','李天宇','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(59,'CaiTingTing','蔡婷婷','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(60,'LuoQian','罗倩 急事电话联系(罗倩-督导)','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(61,'ZhouDong','周栋','',NULL,NULL,NULL,NULL,NULL,'2020-12-22 19:36:12','2020-12-22 19:36:12'),
	(62,'1','3','',NULL,NULL,NULL,NULL,NULL,'2020-12-23 11:25:44','2020-12-23 11:25:44');

