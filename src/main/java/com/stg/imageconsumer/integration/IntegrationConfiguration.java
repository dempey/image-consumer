package com.stg.imageconsumer.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
@ImportResource("classpath:/META-INF/spring/emailConfiguration.xml")
@IntegrationComponentScan
@ComponentScan
public class IntegrationConfiguration {
	
	public static final String RECEIVE_MAIL = "receiveMail";
	public static final String SAVE_ATTACHMENTS = "saveAttachments";
	public static final String SAVE_ENTITY = "saveEntity";
	

	@Bean
	public MessageChannel receiveMail() {
		return MessageChannels.direct(RECEIVE_MAIL).get();
	}
	
	@Bean
	public MessageChannel saveEntity() {
		return MessageChannels.direct(SAVE_ENTITY).get();
	}
	
}
