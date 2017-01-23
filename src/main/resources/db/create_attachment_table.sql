CREATE TABLE IF NOT EXISTS `ATTACHMENT` (
  `id` varchar(36) NOT NULL,
  `email_id` varchar(36) DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `md5` binary(16) DEFAULT NULL,
  `s3_key` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ATTAHMENT_FK_idx` (`email_id`),
  CONSTRAINT `fk_attachment_email_email_id` FOREIGN KEY (`email_id`) REFERENCES `EMAIL` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
