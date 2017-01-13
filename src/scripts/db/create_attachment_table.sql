CREATE TABLE `attachment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_id` int(11) DEFAULT NULL,
  `filename` varchar(45) DEFAULT NULL,
  `length` varchar(45) DEFAULT NULL,
  `md5` varchar(45) DEFAULT NULL,
  `url` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ATTAHMENT_FK_idx` (`email_id`),
  CONSTRAINT `fk_attachment_email_email_id` FOREIGN KEY (`email_id`) REFERENCES `email` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
