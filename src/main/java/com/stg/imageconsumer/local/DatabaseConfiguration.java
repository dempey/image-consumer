package com.stg.imageconsumer.local;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(considerNestedRepositories=true)
@EnableTransactionManagement
public class DatabaseConfiguration {
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/imageconsumer?useSSL=false");;
		dataSource.setUsername("root");
		dataSource.setPassword("password");
		return dataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPackagesToScan("com.stg.imageconsumer.domain");
		factory.setJpaVendorAdapter(adapter);
		factory.setJpaPropertyMap(jpaProperties());

		factory.afterPropertiesSet();
		return factory.getObject();

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
			return buf.toString().toLowerCase(Locale.ROOT);
		}
	}
}
