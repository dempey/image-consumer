package com.stg.imageconsumer.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DatabaseTest {
	
	@Configuration
	@EnableAutoConfiguration(excludeName={"org.springframework.integration.mail.ImapIdleChannelAdapter"})
	@ComponentScan(basePackages="com.stg.imageconsumer.domain")
	public static class TestConfiguration {
		@Bean
		public DataSource dataSource() {
			EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
			return builder
					.setType(EmbeddedDatabaseType.H2)
					.addScript("/db/set_mode.sql")
					.addScripts("/db/create_email_table.sql", "/db/create_attachment_table.sql")
					.build();
		}
		
		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setDataSource(dataSource());
			factory.setPackagesToScan(new String[] {"com.stg.imageconsumer.domain"});
			HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
			adapter.setGenerateDdl(false);
			factory.setJpaVendorAdapter(adapter);
			factory.setJpaProperties(additionalProperties());
			return factory;
		}
		
		private Properties additionalProperties() {
			Properties props = new Properties();
			return props;
		}
	}
	
	@Autowired
	private EmailRepository emailRepository;

	
	@Test
	public void smokeTest() {
		assertThat(emailRepository, notNullValue());
		assertThat(emailRepository.count(), is(0L));
	}
	
}
