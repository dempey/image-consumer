package com.stg.imageconsumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import com.stg.imageconsumer.aws.AmazonConfiguration;
import com.stg.imageconsumer.integration.IntegrationConfiguration;
import com.stg.imageconsumer.local.DatabaseConfiguration;

@SpringBootApplication(scanBasePackages={"com.stg.imageconsumer.domain"})
@Import({ AmazonConfiguration.class, DatabaseConfiguration.class, IntegrationConfiguration.class })
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
