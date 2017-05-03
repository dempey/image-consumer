------------------------------------------------------------------
-- The SQL below is for creating the required table in MySQL
-- This also supports unit testing.
------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `attachment` (
  `id` varchar(36) NOT NULL,
  `email_id` varchar(36) DEFAULT NULL,
  `filename` varchar(100) DEFAULT NULL,
  `length` int(11) DEFAULT NULL,
  `md5` binary(16) DEFAULT NULL,
  `s3_key` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `ATTAHMENT_FK_idx` (`email_id`),
  CONSTRAINT `fk_attachment_email_email_id` FOREIGN KEY (`email_id`) REFERENCES `email` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

------------------------------------------------------------------
-- The SQL below is for creating the required table in PostgreSQL
------------------------------------------------------------------
-- Table: public.attachment

-- DROP TABLE public.attachment;

--CREATE TABLE public.attachment
--(
--  id character varying(36) NOT NULL,
--  email_id character varying(36) DEFAULT NULL::character varying,
--  filename character varying(100) DEFAULT NULL::character varying,
--  length integer,
--  image bytea,
--  md5 bytea,
--  s3_key character varying(36) DEFAULT NULL::character varying,
--  CONSTRAINT attachment_pkey PRIMARY KEY (id),
--  CONSTRAINT fk_attachment_email_email_id FOREIGN KEY (email_id)
--      REFERENCES public.email (id) MATCH SIMPLE
--      ON UPDATE NO ACTION ON DELETE NO ACTION
--)
--WITH (
--  OIDS=FALSE
--);
--ALTER TABLE public.attachment
--  OWNER TO xjwcelyzzoynem;

-- Index: public.attachment_fk_idx

-- DROP INDEX public.attachment_fk_idx;

--CREATE INDEX attachment_fk_idx
--  ON public.attachment
--  USING btree
--  (email_id COLLATE pg_catalog."default");