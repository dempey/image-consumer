package com.stg.imageconsumer.integration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@ImportResource("classpath:/META-INF/spring/emailConfiguration.xml")
@IntegrationComponentScan
@ComponentScan
public class IntegrationConfiguration {
	
}
