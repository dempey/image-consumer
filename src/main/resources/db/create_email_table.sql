CREATE TABLE IF NOT EXISTS `email` (
  `id` varchar(36) NOT NULL,
  `from_addresses` varchar(100) DEFAULT NULL,
  `to_addresses` varchar(100) DEFAULT NULL,
  `cc_addresses` varchar(100) DEFAULT NULL,
  `bcc_addresses` varchar(100) DEFAULT NULL,
  `subject` varchar(100) DEFAULT NULL,
  `sent_date` datetime DEFAULT NULL,
  `received_date` datetime DEFAULT NULL,
  `body` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
