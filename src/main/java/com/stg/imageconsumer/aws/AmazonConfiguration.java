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
//@EnableRdsInstance(dbInstanceIdentifier="imageconsumer", password="STGrocks!", username="root")
@EnableJpaRepositories(considerNestedRepositories=true)
//@EnableContextRegion(region="us-west-2")
//@EnableContextCredentials
//@EnableContextResourceLoader
@ComponentScan
public class AmazonConfiguration {
	
	public static final String BUCKET = "imageconsumer";
	
	@Bean
	public RdsInstanceConfigurer rdsInstanceConfigurer() {
		return new RdsInstanceConfigurer() {

			@Override
			public DataSourceFactory getDataSourceFactory() {
				TomcatJdbcDataSourceFactory dataSourceFactory = new TomcatJdbcDataSourceFactory();
				dataSourceFactory.setConnectionProperties("useSSL=false");
				dataSourceFactory.setDefaultCatalog("mydb");
				dataSourceFactory.setTestOnBorrow(true);
				dataSourceFactory.setValidationQuery("SELECT 1");
				return dataSourceFactory;
			}
			
		};
	}
}
