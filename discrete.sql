/*
Navicat MySQL Data Transfer

Source Server         : aliyun
Source Server Version : 80025
Source Host           : rm-bp17pz93qi4altl751o.mysql.rds.aliyuncs.com:3306
Source Database       : discrete

Target Server Type    : MYSQL
Target Server Version : 80025
File Encoding         : 65001

Date: 2023-03-16 16:25:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(300) NOT NULL COMMENT '密码',
  `avatar` varchar(300) DEFAULT NULL COMMENT '头像',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `question_id` char(19) NOT NULL COMMENT '问题id',
  `parent_id` char(19) NOT NULL DEFAULT '0' COMMENT '父id，如果是回答的话就是0',
  `reply_id` char(19) NOT NULL DEFAULT '0' COMMENT '楼中楼回复id，回复某个回答的id，没有回复就是0',
  `content` text COMMENT '回答内容',
  `user_id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '回答人id',
  `avatar` varchar(300) DEFAULT NULL COMMENT '回答人头像',
  `username` varchar(10) NOT NULL COMMENT '回答人用户名',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `id` char(19) NOT NULL COMMENT '轮播图id',
  `title` varchar(20) DEFAULT NULL COMMENT '轮播图标题',
  `image_url` varchar(300) NOT NULL COMMENT '轮播图图片url地址',
  `link_url` varchar(300) DEFAULT NULL COMMENT '轮播图图片点击链接地址',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，1（true）已删除，0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for chapter
-- ----------------------------
DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `id` char(19) NOT NULL COMMENT '章节id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `title` varchar(60) DEFAULT NULL COMMENT '章节标题',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，1（true）已删除，0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `id` char(19) NOT NULL COMMENT '课程id',
  `teacher_id` char(19) NOT NULL COMMENT '讲师id',
  `title` varchar(50) NOT NULL COMMENT '课程标题',
  `lesson_num` int unsigned NOT NULL COMMENT '课时数',
  `cover` varchar(200) NOT NULL COMMENT '课程封面图片url',
  `view_count` int unsigned DEFAULT '0' COMMENT '浏览量',
  `learning_num` int unsigned DEFAULT '0' COMMENT '课程参与人数',
  `comment_num` int DEFAULT '0' COMMENT '评论数',
  `status` varchar(20) NOT NULL DEFAULT 'Provisional' COMMENT '课程状态，Draft已保存未发布，Provisional未保存临时数据，Normal已发布',
  `del_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除，1（true）已删除，0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `description` text NOT NULL COMMENT '课程简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` char(19) NOT NULL COMMENT '主键id',
  `title` varchar(50) NOT NULL COMMENT '问题标题',
  `content` text COMMENT '问题内容（描述）',
  `user_id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '提问人id',
  `avatar` varchar(300) DEFAULT NULL COMMENT '提问人头像',
  `username` varchar(10) NOT NULL COMMENT '提问人用户名',
  `answer_num` int DEFAULT '0' COMMENT '回答数',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` char(19) NOT NULL,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(300) NOT NULL COMMENT '密码',
  `mail` varchar(50) NOT NULL COMMENT '邮箱',
  `gender` tinyint NOT NULL DEFAULT '1' COMMENT '性别 1男，2女',
  `avatar` varchar(300) DEFAULT NULL COMMENT '用户头像',
  `introduce` varchar(100) DEFAULT NULL COMMENT '个人介绍',
  `permission` int NOT NULL DEFAULT '0' COMMENT '权限，0是普通用户，1是教师',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`) USING BTREE,
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_course
-- ----------------------------
DROP TABLE IF EXISTS `user_course`;
CREATE TABLE `user_course` (
  `id` char(19) NOT NULL COMMENT '职工课程学习情况id',
  `user_id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职工id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `accomplish_flag` tinyint NOT NULL DEFAULT '0' COMMENT '是否完成 1（true）已完成， 0（false）未完成，默认0',
  `learning_video_num` int NOT NULL DEFAULT '0' COMMENT '已学习小节数目，默认0',
  `all_video_num` int NOT NULL COMMENT '课程小节总数',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `last_video_id` char(19) DEFAULT NULL COMMENT '上次学习的小节id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for user_video
-- ----------------------------
DROP TABLE IF EXISTS `user_video`;
CREATE TABLE `user_video` (
  `id` char(19) NOT NULL COMMENT '职工小节表id',
  `user_id` char(19) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '职工id',
  `video_id` char(19) NOT NULL COMMENT '小节id',
  `learning_time` float NOT NULL DEFAULT '0' COMMENT '已学习时长（s）',
  `duration` float NOT NULL COMMENT '小节总时长（s）',
  `accomplish_flag` tinyint NOT NULL DEFAULT '0' COMMENT '小节是否学习完成，1（true）已完成， 0（false）未完成，默认0',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `last_time` float NOT NULL DEFAULT '0' COMMENT '上次观看视频时长记录（s）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for video
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` char(19) NOT NULL COMMENT '小节id',
  `course_id` char(19) NOT NULL COMMENT '课程id',
  `chapter_id` char(19) NOT NULL COMMENT '章节id',
  `title` varchar(60) NOT NULL COMMENT '小节标题',
  `video_source_id` varchar(100) NOT NULL COMMENT '小节视频id（阿里云视频id）',
  `video_original_name` varchar(200) NOT NULL COMMENT '原始文件名称（用户上传文件时的视频名称）',
  `play_count` int unsigned DEFAULT '0' COMMENT '播放次数',
  `duration` float DEFAULT NULL COMMENT '视频时长（秒）',
  `size` bigint unsigned DEFAULT NULL COMMENT '视频大小（字节）',
  `del_flag` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，1（true）已删除，0（false）未删除',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
