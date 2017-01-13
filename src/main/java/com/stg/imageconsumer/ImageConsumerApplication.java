package com.stg.imageconsumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;

@SpringBootApplication
@IntegrationComponentScan("com.stg.imageconsumer.integrations")
@ImportResource("classpath:/META-INF/spring/emailConfiguration.xml")
public class ImageConsumerApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ImageConsumerApplication.class);

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = SpringApplication.run(ImageConsumerApplication.class, args);
		logger.debug("started");
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();
	}
	
}
