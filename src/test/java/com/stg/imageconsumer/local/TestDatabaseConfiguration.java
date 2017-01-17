package com.stg.imageconsumer.local;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableAutoConfiguration(excludeName = { "org.springframework.integration.mail.ImapIdleChannelAdapter" })
@ComponentScan(basePackages = { "com.stg.imageconsumer.local", "com.stg.imageconsumer.domain" })
public class TestDatabaseConfiguration {

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2)
				.addScript("/db/set_mode.sql")
				.addScripts("/db/create_email_table.sql", "/db/create_attachment_table.sql")
				.build();
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setGenerateDdl(false);
		return adapter;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(dataSource())
				.packages(new String[] { "com.stg.imageconsumer.local", "com.stg.imageconsumer.domain" })
				.properties(com.stg.imageconsumer.local.DatabaseConfiguration.jpaProperties())
				.build();
	}


}
