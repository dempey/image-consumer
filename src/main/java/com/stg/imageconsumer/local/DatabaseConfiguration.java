package com.stg.imageconsumer.local;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@Profile("!prod")
@EnableJpaRepositories(considerNestedRepositories=true)
@EnableTransactionManagement
@ComponentScan
public class DatabaseConfiguration {

	private static final String USER = System.getenv("DB_USERNAME");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");

	protected Logger logger = LoggerFactory.getLogger(DatabaseConfiguration.class);
	
	@Configuration
	@ConditionalOnClass(name="org.postgresql.Driver")
	@Profile("dev")
	public class MySQLDatabaseConfiguration {
		@Bean
		public DataSource dataSource() {
			logger.debug("MySQLDatabaseConfiguration dataSource");
			DriverManagerDataSource dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName("org.postgresql.Driver");
			dataSource.setUrl("jdbc:postgresql://ec2-23-21-227-73.compute-1.amazonaws.com:5432/d2th8fddh1b6p4?sslmode=require");
			dataSource.setUsername(USER);
			dataSource.setPassword(PASSWORD);
			return dataSource;
		}
	
		@Bean
		public EntityManagerFactory entityManagerFactory() {
			HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
			adapter.setShowSql(false);
			adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");

			LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
			factory.setDataSource(dataSource());
			factory.setPackagesToScan("com.stg.imageconsumer.domain", "com.stg.imageconsumer.local");
			factory.setJpaVendorAdapter(adapter);
			factory.setJpaPropertyMap(jpaProperties());
	
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		
	}
	
	@Configuration
	@ConditionalOnMissingBean(DataSource.class)
	public class EmbeddedDatabaseConfiguration {
		@Bean
		public DataSource dataSource() {
			logger.debug("EmbeddedDatabaseConfiguration dataSource");
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
					.packages("com.stg.imageconsumer.local", "com.stg.imageconsumer.domain")
					.properties(com.stg.imageconsumer.local.DatabaseConfiguration.jpaProperties())
					.build();
		}
	}

	public static Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<>();
		props.put("hibernate.physical_naming_strategy", PhysicalNamingStrategyImpl.INSTANCE);
		return props;
	}

	public static class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl implements Serializable {

		private static final long serialVersionUID = 1L;

		public static final PhysicalNamingStrategyImpl INSTANCE = new PhysicalNamingStrategyImpl();

		@Override
		public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
			return new Identifier(addUnderscores(name.getText()), name.isQuoted());
		}

		@Override
		public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
			return new Identifier(addUnderscores(name.getText()), name.isQuoted());
		}


		protected static String addUnderscores(String name) {
			final StringBuilder buf = new StringBuilder( name.replace('.', '_') );
			for (int i=1; i<buf.length()-1; i++) {
				if (
						Character.isLowerCase( buf.charAt(i-1) ) &&
						Character.isUpperCase( buf.charAt(i) ) &&
						Character.isLowerCase( buf.charAt(i+1) )
						) {
					buf.insert(i++, '_');
				}
			}
			return buf.toString();
		}
	}
	
}
