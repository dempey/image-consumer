------------------------------------------------------------------
-- The SQL below is for creating the required table in MySQL
-- This also supports unit testing.
------------------------------------------------------------------
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


------------------------------------------------------------------
-- The SQL below is for creating the required table in PostgreSQL
------------------------------------------------------------------
-- Table: public.email

-- DROP TABLE public.email;

--CREATE TABLE public.email
--(
--  id character varying(36) NOT NULL,
--  from_addresses character varying(100) DEFAULT NULL::character varying,
--  to_addresses character varying(100) DEFAULT NULL::character varying,
--  cc_addresses character varying(100) DEFAULT NULL::character varying,
--  bcc_addresses character varying(100) DEFAULT NULL::character varying,
--  subject character varying(100) DEFAULT NULL::character varying,
--  sent_date timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
--  received_date timestamp(0) without time zone DEFAULT NULL::timestamp without time zone,
--  body bytea,
--  CONSTRAINT email_pkey PRIMARY KEY (id)
--)
--WITH (
--  OIDS=FALSE
--);
--ALTER TABLE public.email
--  OWNER TO xjwcelyzzoynem;
