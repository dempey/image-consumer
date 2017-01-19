package com.stg.imageconsumer.aws;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableContextResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.stg.imageconsumer.local.DatabaseConfiguration;

@Configuration
@Profile("prod")
@EnableJpaRepositories(considerNestedRepositories=true)
//@EnableContextRegion(region="us-west-2")
//@EnableContextCredentials(accessKey="AKIAI6K7HBM4NIWREROQ", secretKey="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")
//@EnableContextResourceLoader
@ComponentScan
public class AmazonConfiguration {
	
	private Logger logger = LoggerFactory.getLogger(AmazonConfiguration.class);

	@Bean
	public DataSource dataSource() {
		logger.debug("RDS dataSource");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://imageconsumer.crj31z9rwl7x.us-west-2.rds.amazonaws.com:3306/mydb?useSSL=false");
		dataSource.setUsername("root");
		dataSource.setPassword("STGrocks!");
		return dataSource;
	}
	
	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource());
		factory.setPackagesToScan("com.stg.imageconsumer.domain", "com.stg.imageconsumer.aws");
		factory.setJpaVendorAdapter(adapter);
		factory.setJpaPropertyMap(DatabaseConfiguration.jpaProperties());

		factory.afterPropertiesSet();
		return factory.getObject();
	}
}
