package com.stg.imageconsumer.aws;

import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.context.config.annotation.EnableContextResourceLoader;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.cloud.aws.jdbc.config.annotation.RdsInstanceConfigurer;
import org.springframework.cloud.aws.jdbc.datasource.DataSourceFactory;
import org.springframework.cloud.aws.jdbc.datasource.TomcatJdbcDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("prod")
@EnableRdsInstance(dbInstanceIdentifier="imageconsumer", password="STGrocks!", username="root")
@EnableJpaRepositories(considerNestedRepositories=true)
@EnableContextRegion(region="us-west-2")
@EnableContextCredentials
@EnableContextResourceLoader
@ComponentScan
public class AmazonConfiguration {
	
	@Bean
	public RdsInstanceConfigurer rdsInstanceConfigurer() {
		return new RdsInstanceConfigurer() {

			@Override
			public DataSourceFactory getDataSourceFactory() {
				TomcatJdbcDataSourceFactory dataSourceFactory = new TomcatJdbcDataSourceFactory();
				dataSourceFactory.setConnectionProperties("useSSL=false");
				dataSourceFactory.setDefaultCatalog("mydb");
				return dataSourceFactory;
			}
			
		};
	}

//	@Bean
//	public DataSource dataSource() {
//		logger.debug("RDS dataSource");
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://imageconsumer.crj31z9rwl7x.us-west-2.rds.amazonaws.com:3306/mydb?useSSL=false");
//		dataSource.setUsername("root");
//		dataSource.setPassword("STGrocks!");
//		return dataSource;
//	}
//	
//	@Bean
//	public EntityManagerFactory entityManagerFactory() {
//		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//		adapter.setShowSql(false);
//		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
//
//		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
//		factory.setDataSource(dataSource());
//		factory.setPackagesToScan("com.stg.imageconsumer.domain", "com.stg.imageconsumer.aws");
//		factory.setJpaVendorAdapter(adapter);
//		factory.setJpaPropertyMap(DatabaseConfiguration.jpaProperties());
//
//		factory.afterPropertiesSet();
//		return factory.getObject();
//	}
}
