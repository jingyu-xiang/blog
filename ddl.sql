/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TABLE IF EXISTS `ms_article`;
CREATE TABLE `ms_article` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `comment_counts` int DEFAULT NULL COMMENT '评论数量',
                              `create_date` bigint DEFAULT NULL COMMENT '创建时间',
                              `summary` varchar(255) DEFAULT NULL COMMENT '简介',
                              `title` varchar(64) DEFAULT NULL COMMENT '标题',
                              `view_counts` int DEFAULT NULL COMMENT '浏览数量',
                              `author_id` bigint DEFAULT NULL COMMENT '作者id',
                              `body_id` bigint DEFAULT NULL COMMENT '内容id',
                              `category_id` bigint DEFAULT NULL COMMENT '类别id',
                              `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT 'logical delete',
                              PRIMARY KEY (`id`),
                              FULLTEXT KEY `idx_title_summary` (`title`,`summary`)
) ENGINE=InnoDB AUTO_INCREMENT=1583832213484265474 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_article_body`;
CREATE TABLE `ms_article_body` (
                                   `id` bigint NOT NULL AUTO_INCREMENT,
                                   `content` longtext,
                                   `content_html` longtext,
                                   `article_id` bigint NOT NULL,
                                   PRIMARY KEY (`id`),
                                   KEY `article_id` (`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1583832213685592066 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_article_tag`;
CREATE TABLE `ms_article_tag` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `article_id` bigint NOT NULL,
                                  `tag_id` bigint NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `article_id` (`article_id`),
                                  KEY `tag_id` (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1583831359620440068 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_category`;
CREATE TABLE `ms_category` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                               `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                               `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                               `deleted` bit(1) NOT NULL DEFAULT b'0',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1586426897343754242 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_comment`;
CREATE TABLE `ms_comment` (
                              `id` bigint NOT NULL AUTO_INCREMENT,
                              `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
                              `create_date` bigint NOT NULL,
                              `article_id` bigint NOT NULL,
                              `author_id` bigint NOT NULL,
                              `parent_id` bigint DEFAULT NULL,
                              `to_uid` bigint NOT NULL,
                              `level` int NOT NULL,
                              `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT 'logical delete',
                              PRIMARY KEY (`id`),
                              KEY `article_id` (`article_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1583837204194361346 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_confirmation`;
CREATE TABLE `ms_confirmation` (
                                   `id` bigint NOT NULL,
                                   `user_id` bigint NOT NULL,
                                   `confirmed` tinyint(1) NOT NULL DEFAULT '0',
                                   `code` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `ms_sys_user`;
CREATE TABLE `ms_sys_user` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `account` varchar(64) DEFAULT NULL COMMENT '账号',
                               `admin` bit(1) DEFAULT NULL COMMENT '是否管理员',
                               `create_date` bigint DEFAULT NULL COMMENT '注册时间',
                               `deleted` bit(1) DEFAULT NULL COMMENT '是否删除',
                               `github` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '邮箱',
                               `last_login` bigint DEFAULT NULL COMMENT '最后登录时间',
                               `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
                               `password` varchar(64) DEFAULT NULL COMMENT '密码',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1587268349938491395 DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `ms_tag`;
CREATE TABLE `ms_tag` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                          `tag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                          `deleted` bit(1) NOT NULL DEFAULT b'0',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1586425824122023938 DEFAULT CHARSET=utf8mb3;



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;