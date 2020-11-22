/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50725
Source Host           : localhost:3306
Source Database       : test-sql

Target Server Type    : MYSQL
Target Server Version : 50725
File Encoding         : 65001

Date: 2020-11-22 12:59:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `carousel`
-- ----------------------------
DROP TABLE IF EXISTS `carousel`;
CREATE TABLE `carousel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `carousel_id` varchar(32) NOT NULL,
  `label` varchar(255) NOT NULL,
  `note` varchar(255) NOT NULL,
  `sub_date` varchar(255) NOT NULL,
  `text` text NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `carousel_index` (`carousel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of carousel
-- ----------------------------
INSERT INTO `carousel` VALUES ('6', '2020-11-05 20:42:43', 'manager1', '2020-11-05 20:42:43', 'manager1', '202011052042427600592910142020', '其他', '相关情报将有巩某某为您追踪报道', '2020-11-5', '某大三xx博学生竟然为了玩lol竟然扔掉了代码！', '震惊，某大三学生竟然！');
INSERT INTO `carousel` VALUES ('7', '2020-11-05 20:43:58', 'manager1', '2020-11-05 20:43:58', 'manager1', '202011052043572226693410142020', '其他', '相关情报将由本记者巩某某追踪报道', '2020-11-5', '某大三学生xx博竟然为了玩lol，还把下属给（哔！！！！）了！惨无人道', '震惊，某大三学生竟然！（续）');
INSERT INTO `carousel` VALUES ('8', '2020-11-05 20:44:58', 'manager1', '2020-11-05 21:28:27', 'manager1', '202011052044579768850810142020', '考试相关', '来自前端博老板的鼓励', '2020-11-5', '把考试扔了吧', '这是考试相关的消息');
INSERT INTO `carousel` VALUES ('9', '2020-11-05 20:45:32', 'manager1', '2020-11-05 21:26:46', 'manager1', '202011052045310025990710142020', '网站相关', '来自后端大佬对游客投向同情的眼神', '2020-11-5', '从此之后可以看网站消息了，游客再也不是老five了', '网站消息开启啦！');
INSERT INTO `carousel` VALUES ('10', '2020-11-05 20:46:46', 'manager1', '2020-11-05 21:28:47', 'manager1', '202011052046450481330710142020', '其他', '测试', '2020-11-5', '其他的消息', '其他');
INSERT INTO `carousel` VALUES ('11', '2020-11-06 13:42:00', 'manager1', '2020-11-06 13:42:00', 'manager1', '202011061341598175334310142020', '网站相关', '无', '2020-11-6', '主页已进行更新', '主页更新');
INSERT INTO `carousel` VALUES ('12', '2020-11-08 13:42:12', 'manager1', '2020-11-08 13:42:12', 'manager1', '202011081342111492702810142020', '网站相关', '来自后端大佬的关怀', '2020-11-8', '新增图片功能，网页准考证图片，网页账户图片，增加自己账户的乐趣！', '网页更新');
INSERT INTO `carousel` VALUES ('13', '2020-11-08 15:41:26', 'manager1', '2020-11-08 15:42:12', 'manager1', '202011081541262593178710142020', '其他', '无', '2020-11-8', '我觉得主页消息里还可以品论会更有趣，但这会增加一个页面，大大加大了前端工作人员的负担（尤其是xx博还在一边A他A他）\n但是如果做出来了我绝对会把加大前端工作人员负担的那个后端程序员给沙了。', '主页评论');
INSERT INTO `carousel` VALUES ('14', '2020-11-08 18:00:49', 'huang', '2020-11-08 18:01:51', 'huang', '202011081800498288308310142020', '其他', '无', '2020-11-8', '压迫前端工作人员，欺压百姓，bug太多，前端开发人员都，，都，，都要把眼泪射出来了啦', '举报后端开发大佬');
INSERT INTO `carousel` VALUES ('15', '2020-11-10 14:40:08', 'manager1', '2020-11-10 14:40:08', 'manager1', '202011101440088172118710142020', '网站相关', '无', '2020-11-10', '路由更新，拆分原本manager变为manager和admin', '前端路由更新');
INSERT INTO `carousel` VALUES ('16', '2020-11-16 10:56:20', 'manager1', '2020-11-20 11:25:32', 'kana', '202011161056198863752510142020', '网站相关', '无', '2020-11-16', '更新用户可以百度账号登陆功能', '更改测试');

-- ----------------------------
-- Table structure for `channel`
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `channel` varchar(255) NOT NULL,
  `channel_id` varchar(32) NOT NULL,
  `exam_type_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `channel_index` (`channel`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES ('1', '2020-10-15 11:36:45', null, '2020-10-15 11:36:45', null, 'CET', '202010151136458576119010052020', '202010150930332534995510022020');
INSERT INTO `channel` VALUES ('2', '2020-10-15 11:36:45', null, '2020-10-15 11:36:45', null, 'MBA', '202010151136455246169010052020', '202010102110496884566610022020');
INSERT INTO `channel` VALUES ('3', '2020-10-15 11:36:45', null, '2020-10-15 11:36:45', null, 'NCRE', '202010151136450501477010052020', '202010102110496607127210022020');
INSERT INTO `channel` VALUES ('4', '2020-10-15 11:36:45', null, '2020-10-15 11:36:45', null, 'ACCA/CAT', '202010151136457384137710052020', '202010102110498701543610022020');
INSERT INTO `channel` VALUES ('5', '2020-10-15 11:36:46', null, '2020-10-15 11:36:46', null, 'ARE', '202010151136455791503710052020', '202010102110497754888710022020');
INSERT INTO `channel` VALUES ('7', '2020-10-16 15:29:20', 'kana', '2020-10-16 15:29:20', 'kana', '网课频道', '202010161529200852206610052020', '202010161053091840244710022020');
INSERT INTO `channel` VALUES ('9', '2020-10-23 17:45:07', 'manager1', '2020-10-23 21:36:14', 'manager1', '学校频道', '202010231745075565020110052020', '202010231640241316026910022020');
INSERT INTO `channel` VALUES ('14', '2020-10-28 18:33:37', 'huang', '2020-10-28 18:33:37', 'huang', '日语系', '202010281833372669384610052020', '202010221839293882392510022020');
INSERT INTO `channel` VALUES ('15', '2020-11-06 21:35:15', 'manager1', '2020-11-06 21:35:15', 'manager1', 'bilibili', '202011062135156856025510052020', '202011061406107455240810022020');

-- ----------------------------
-- Table structure for `exam_entry`
-- ----------------------------
DROP TABLE IF EXISTS `exam_entry`;
CREATE TABLE `exam_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `contact` varchar(32) NOT NULL,
  `exam_detail_id` varchar(32) NOT NULL,
  `exam_entry_id` varchar(32) NOT NULL,
  `note` varchar(32) NOT NULL,
  `number` int(11) NOT NULL,
  `state` varchar(32) NOT NULL,
  `term` varchar(32) NOT NULL,
  `version` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `exam_entry_id` (`exam_entry_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_entry
-- ----------------------------
INSERT INTO `exam_entry` VALUES ('8', '2020-10-31 19:32:12', 'manager1', '2020-10-31 20:45:41', 'manager1', 'manager1', '202010311930230864211510032020', '202010311932125419362010082020', '考试报名未发布', '700', 'PREPARE', '2020FH', '0');
INSERT INTO `exam_entry` VALUES ('14', '2020-11-03 21:59:42', 'manager1', '2020-11-03 21:59:42', 'manager1', 'manager1', '202010291618393222725510032020', '202011032159429200644410082020', '考试开始报名', '4', 'START', '2020SH', '6');
INSERT INTO `exam_entry` VALUES ('15', '2020-11-05 19:06:06', 'manager1', '2020-11-05 19:06:06', 'manager1', 'manager1', '202011051905265989989910032020', '202011051906051399599710082020', '考试开始报名', '1200', 'START', '2020FH', '0');

-- ----------------------------
-- Table structure for `exam_entry_record`
-- ----------------------------
DROP TABLE IF EXISTS `exam_entry_record`;
CREATE TABLE `exam_entry_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_entry_id` varchar(32) NOT NULL,
  `exam_entry_record_id` varchar(32) NOT NULL,
  `note` varchar(32) NOT NULL,
  `state` varchar(32) NOT NULL,
  `exam_detail_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `exam_entry_record_id` (`exam_entry_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_entry_record
-- ----------------------------
INSERT INTO `exam_entry_record` VALUES ('1', '2020-11-01 02:00:00', 'manager1', '2020-11-02 02:00:00', 'life', '202010311926106917249010082020', '202011010200006201341510092020', '考试开始报名', 'FINISH', '202010311019337592289710032020');
INSERT INTO `exam_entry_record` VALUES ('2', '2020-11-01 02:00:00', 'manager1', '2020-11-01 02:00:00', 'manager1', '202010311847481966207310082020', '202011010200008321134510092020', '考试报名取消', 'CANCEL', '202010311930230864211510032020');
INSERT INTO `exam_entry_record` VALUES ('3', '2020-11-02 02:00:00', 'manager1', '2020-11-02 02:00:00', 'manager1', '202011011611048338789810082020', '202011020200005119074910092020', '考试报名完成', 'FINISH', '202010311931296408846910032020');
INSERT INTO `exam_entry_record` VALUES ('4', '2020-11-04 02:00:00', 'bozi', '2020-11-04 02:00:00', 'manager1', '202011032127223523090310082020', '202011040200007412266710092020', '考试报名完成', 'FINISH', '202010311931296408846910032020');

-- ----------------------------
-- Table structure for `exam_location`
-- ----------------------------
DROP TABLE IF EXISTS `exam_location`;
CREATE TABLE `exam_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_detail_id` varchar(32) NOT NULL,
  `exam_location_id` varchar(32) NOT NULL,
  `location` varchar(32) NOT NULL,
  `teacher` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  `user_exam_entry_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_location_id_index` (`exam_location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_location
-- ----------------------------

-- ----------------------------
-- Table structure for `exam_record`
-- ----------------------------
DROP TABLE IF EXISTS `exam_record`;
CREATE TABLE `exam_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_description` varchar(32) NOT NULL,
  `exam_end_time` varchar(32) NOT NULL,
  `exam_location` varchar(32) NOT NULL,
  `exam_record_id` varchar(32) NOT NULL,
  `exam_start_time` varchar(32) NOT NULL,
  `exam_type_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_record_id_index` (`exam_record_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_record
-- ----------------------------
INSERT INTO `exam_record` VALUES ('3', '2020-10-18 15:03:18', 'kana', '2020-10-18 15:03:18', 'kana', '英语六级', '2020-09-18 15:00', '格致楼106', '202010181503181345742110042020', '2020-09-18 13:00', '202010150930332534995510022020');
INSERT INTO `exam_record` VALUES ('6', '2020-10-18 15:11:33', 'kana', '2020-10-18 15:11:33', 'kana', '英语四级', '2020-09-05 11:00', '格致楼211', '202010181511331022003010042020', '2020-09-05 9:00', '202010150930332534995510022020');
INSERT INTO `exam_record` VALUES ('7', '2020-10-18 15:11:34', 'kana', '2020-10-18 15:11:34', 'kana', '英语四级', '2020-09-05 11:00', '格致楼207', '202010181511337618937910042020', '2020-09-05 9:00', '202010150930332534995510022020');

-- ----------------------------
-- Table structure for `exam_score`
-- ----------------------------
DROP TABLE IF EXISTS `exam_score`;
CREATE TABLE `exam_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_detail_id` varchar(32) NOT NULL,
  `exam_score` varchar(32) NOT NULL,
  `exam_score_id` varchar(32) NOT NULL,
  `stu_no` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_score_id_index` (`exam_score_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_score
-- ----------------------------

-- ----------------------------
-- Table structure for `exam_type`
-- ----------------------------
DROP TABLE IF EXISTS `exam_type`;
CREATE TABLE `exam_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_limit` varchar(32) NOT NULL,
  `exam_type_description` varchar(32) NOT NULL,
  `exam_type_id` varchar(32) NOT NULL,
  `exam_type_name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_type_id_index` (`exam_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_type
-- ----------------------------
INSERT INTO `exam_type` VALUES ('2', '2020-10-10 21:10:50', null, '2020-10-10 21:10:50', null, '本校大一至大四学生', '美国工商管理人才考试', '202010102110496884566610022020', 'MBA');
INSERT INTO `exam_type` VALUES ('3', '2020-10-10 21:10:50', null, '2020-10-10 21:10:50', null, '本校大一至大四学生', '计算机等级考试', '202010102110496607127210022020', 'NCRE');
INSERT INTO `exam_type` VALUES ('4', '2020-10-10 21:10:50', null, '2020-10-10 21:10:50', null, '本校大一至大四学生', '财会类考试', '202010102110498701543610022020', 'ACCA/CAT');
INSERT INTO `exam_type` VALUES ('5', '2020-10-10 21:10:50', null, '2020-10-10 21:10:50', null, '本校大一至大四学生', '建筑类考试', '202010102110497754888710022020', 'ARE');
INSERT INTO `exam_type` VALUES ('8', '2020-10-15 09:30:34', null, '2020-10-15 09:30:34', null, '本校大一至大四学生', '英语四六级考试', '202010150930332534995510022020', 'CET');
INSERT INTO `exam_type` VALUES ('9', '2020-10-16 10:53:10', 'kana', '2020-10-16 10:53:10', 'kana', '慕课用户', '慕课测试', '202010161053091840244710022020', 'Mooc-exam');
INSERT INTO `exam_type` VALUES ('10', '2020-10-22 18:39:29', 'manager1', '2020-10-23 16:14:21', 'manager1', '无限制', '日语N2', '202010221839293882392510022020', 'JLPT');
INSERT INTO `exam_type` VALUES ('13', '2020-10-23 16:40:25', 'manager1', '2020-11-06 21:33:25', 'manager1', '大一新生', '学生手册考试', '202010231640241316026910022020', 'SchoolTest');
INSERT INTO `exam_type` VALUES ('16', '2020-11-06 14:05:43', 'manager1', '2020-11-06 14:05:43', 'manager1', 'bilibili大会员', 'Redis什么的', '202011061405431441895210022020', '我不知道啊');
INSERT INTO `exam_type` VALUES ('17', '2020-11-06 14:06:11', 'life', '2020-11-06 21:34:50', 'manager1', '热爱bilibili的学生们', 'bilibili 1024 CTF挑战赛', '202011061406107455240810022020', 'bilibili 1024');

-- ----------------------------
-- Table structure for `exam_type_detail`
-- ----------------------------
DROP TABLE IF EXISTS `exam_type_detail`;
CREATE TABLE `exam_type_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_announce` varchar(32) NOT NULL,
  `exam_description` varchar(32) NOT NULL,
  `exam_detail_id` varchar(32) NOT NULL,
  `exam_end_time` varchar(32) NOT NULL,
  `exam_location` varchar(32) NOT NULL,
  `exam_start_time` varchar(32) NOT NULL,
  `exam_type_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_type_detail_id_index` (`exam_detail_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of exam_type_detail
-- ----------------------------
INSERT INTO `exam_type_detail` VALUES ('6', '2020-10-18 15:05:28', 'kana', '2020-10-18 15:05:28', 'kana', '请等待发布', '英语四级', '202010181505285583262110032020', '2020-12-05 11:00', '明德楼205', '2020-12-05 9:00', '202010150930332534995510022020');
INSERT INTO `exam_type_detail` VALUES ('7', '2020-10-29 16:18:40', 'manager1', '2020-10-30 14:12:51', 'manager1', '考试结束后一周内在个人中心中通知', '学生手册考试', '202010291618393222725510032020', '2020-10-29 20:30', '另行通知', '2020-10-29 19:00', '202010231640241316026910022020');
INSERT INTO `exam_type_detail` VALUES ('8', '2020-10-31 10:19:33', 'manager1', '2020-11-06 15:04:00', 'life', '考试结束后即可打开观看分数', '学生手册考试', '202010311019337592289710032020', '2020-10-31 10:19', '网上', '2020-10-31 10:19', '202010231640241316026910022020');
INSERT INTO `exam_type_detail` VALUES ('9', '2020-10-31 19:30:23', 'manager1', '2020-10-31 19:30:23', 'manager1', '三个月后', '计算机等级考试', '202010311930230864211510032020', '2020-11-11 20:30', '仁义楼', '2020-11-11 18:30', '202010102110496607127210022020');
INSERT INTO `exam_type_detail` VALUES ('10', '2020-10-31 19:31:30', 'manager1', '2020-10-31 19:31:30', 'manager1', '五个工作日内', '美国工商管理人才考试', '202010311931296408846910032020', '2020-12-03 12:00', 'C楼', '2020-12-02 12:00', '202010102110496884566610022020');
INSERT INTO `exam_type_detail` VALUES ('11', '2020-11-05 19:05:26', 'manager1', '2020-11-05 19:05:26', 'manager1', '新的考试已经出现，怎么能够停滞不前?', '学生手册考试', '202011051905265989989910032020', '2020-11-05 19:05', '梦里', '2020-11-05 19:05', '202010231640241316026910022020');
INSERT INTO `exam_type_detail` VALUES ('12', '2020-11-08 16:24:39', 'manager1', '2020-11-08 16:24:39', 'manager1', '考完后页面查看，系统评分', 'bilibili 1024 CTF挑战赛', '202011081624394168434310032020', '2020-12-08 16:24', 'bilibili', '2020-11-08 12:00', '202011061406107455240810022020');

-- ----------------------------
-- Table structure for `image`
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `image_id` varchar(32) NOT NULL,
  `image_name` varchar(255) NOT NULL,
  `tag` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `image_id` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of image
-- ----------------------------

-- ----------------------------
-- Table structure for `log`
-- ----------------------------
DROP TABLE IF EXISTS `log`;
CREATE TABLE `log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `action` varchar(255) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `message` varchar(255) NOT NULL,
  `result` tinyint(1) DEFAULT '1',
  `time` varchar(255) NOT NULL,
  `transfer_user_name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `transfer_user` (`transfer_user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of log
-- ----------------------------

-- ----------------------------
-- Table structure for `major`
-- ----------------------------
DROP TABLE IF EXISTS `major`;
CREATE TABLE `major` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `class_name` varchar(32) NOT NULL,
  `class_number` varchar(32) NOT NULL,
  `discipline` varchar(32) NOT NULL,
  `major` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `major_name_index` (`major`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of major
-- ----------------------------
INSERT INTO `major` VALUES ('2', '2020-11-01 11:54:26', 'life', '2020-11-01 11:54:26', 'life', '软工三班', '50', '软件工程', '计算机');
INSERT INTO `major` VALUES ('3', '2020-11-01 14:29:59', 'manager1', '2020-11-01 14:29:59', 'manager1', '计科八班', '40', '计算机科学与技术', '计算机');
INSERT INTO `major` VALUES ('5', '2020-11-01 14:35:48', 'manager1', '2020-11-01 14:35:48', 'manager1', '财经二班', '52', '财经管理', '财政');
INSERT INTO `major` VALUES ('8', '2020-11-02 19:25:07', 'manager1', '2020-11-02 19:25:07', 'manager1', '软工二班', '38', '软件工程', '计算机');
INSERT INTO `major` VALUES ('9', '2020-11-02 19:45:43', 'manager1', '2020-11-02 19:45:43', 'manager1', '软工一班', '35', '软件工程', '计算机');
INSERT INTO `major` VALUES ('10', '2020-11-02 19:46:42', 'manager1', '2020-11-02 19:46:42', 'manager1', '计科七班', '42', '计算机科学与技术', '计算机');
INSERT INTO `major` VALUES ('11', '2020-11-02 19:47:35', 'manager1', '2020-11-02 19:47:35', 'manager1', '英语一班', '37', '英语专业', '英语系');
INSERT INTO `major` VALUES ('13', '2020-11-02 19:48:56', 'manager1', '2020-11-02 19:48:56', 'manager1', '自然一班', '10', '超自然专业', '自然系');
INSERT INTO `major` VALUES ('14', '2020-11-02 19:49:01', 'manager1', '2020-11-02 19:49:01', 'manager1', '自然二班', '10', '超自然专业', '自然系');
INSERT INTO `major` VALUES ('15', '2020-11-02 19:49:09', 'manager1', '2020-11-02 19:49:09', 'manager1', '自然三班', '12', '超自然专业', '自然系');
INSERT INTO `major` VALUES ('16', '2020-11-02 19:49:17', 'manager1', '2020-11-02 19:49:17', 'manager1', '自然四班', '17', '超自然专业', '自然系');

-- ----------------------------
-- Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `channel` varchar(32) NOT NULL,
  `content` text NOT NULL,
  `exam_description` varchar(32) NOT NULL,
  `exam_type_id` varchar(32) NOT NULL,
  `publisher` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `publisher_index` (`publisher`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES ('54', '2020-10-28 19:57:48', 'kana', '2020-10-28 19:57:48', 'kana', '日语系', '删除用1', '日语N2', '202010221839293882392510022020', 'kana');
INSERT INTO `message` VALUES ('56', '2020-10-28 20:01:48', 'kana', '2020-10-28 20:01:48', 'kana', 'CET', 'CET消息', '英语四六级考试', '202010150930332534995510022020', 'kana');
INSERT INTO `message` VALUES ('57', '2020-10-30 13:50:18', 'manager1', '2020-10-30 13:50:18', 'manager1', 'CET', 'hhh', '英语四六级考试', '202010150930332534995510022020', 'manager1');
INSERT INTO `message` VALUES ('58', '2020-10-30 13:50:32', 'manager1', '2020-10-30 13:50:32', 'manager1', '学校频道', '给我好好学习', '学生手册考试', '202010231640241316026910022020', 'manager1');
INSERT INTO `message` VALUES ('59', '2020-11-01 18:35:01', 'life', '2020-11-01 18:35:01', 'life', 'NCRE', 'hellowolrd', '计算机等级考试', '202010102110496607127210022020', 'life');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `exam_role_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2020-10-08 12:51:33', null, '2020-10-08 12:51:33', null, '用户', 'USER');
INSERT INTO `role` VALUES ('2', '2020-10-08 12:51:33', null, '2020-10-08 12:51:33', null, '临时用户', 'TEMP_USER');
INSERT INTO `role` VALUES ('3', '2020-10-08 12:51:34', null, '2020-11-09 21:53:53', 'manager1', '教师', 'MANAGER');
INSERT INTO `role` VALUES ('4', '2020-10-08 12:51:34', null, '2020-11-10 11:43:35', 'manager1', '管理员', 'ADMIN');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(32) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(32) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `full_name` varchar(32) NOT NULL,
  `password` varchar(255) NOT NULL,
  `user_name` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `exam_user_name_id` (`user_name`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `user_exam_entry`
-- ----------------------------
DROP TABLE IF EXISTS `user_exam_entry`;
CREATE TABLE `user_exam_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_entry_id` varchar(32) NOT NULL,
  `user_exam_entry_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_exam_entry_index` (`exam_entry_id`,`user_id`) USING BTREE,
  KEY `user_exam_entry_id_index` (`user_exam_entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_exam_entry
-- ----------------------------

-- ----------------------------
-- Table structure for `user_exam_entry_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_exam_entry_record`;
CREATE TABLE `user_exam_entry_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `exam_entry_id` varchar(32) NOT NULL,
  `if_attend` bit(1) NOT NULL,
  `user_exam_entry_record_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `exam_record_id_index` (`user_exam_entry_record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_exam_entry_record
-- ----------------------------

-- ----------------------------
-- Table structure for `user_info`
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `class_name` varchar(32) NOT NULL,
  `identification_number` varchar(32) NOT NULL,
  `major` varchar(32) NOT NULL,
  `real_name` varchar(32) NOT NULL,
  `stu_no` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_info` (`user_id`) USING BTREE,
  KEY `user_id_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  KEY `FK859n2jvi8ivhui0rl0esws6o` (`user_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------

-- ----------------------------
-- Table structure for `user_sub`
-- ----------------------------
DROP TABLE IF EXISTS `user_sub`;
CREATE TABLE `user_sub` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `channel_id` varchar(32) NOT NULL,
  `user_channel_id` varchar(32) NOT NULL,
  `user_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `channel_index` (`channel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_sub
-- ----------------------------
