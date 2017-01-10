package com.stg.imageconsumer;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.mail.MailMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.stg.imageconsumer.models.Email;
import com.stg.imageconsumer.transformers.MailToEmailEntityTransformer;

@SpringBootApplication
@IntegrationComponentScan
@ImportResource("classpath:/META-INF/spring/emailConfiguration.xml")
public class ImageConsumerApplication {
	
	public static final String RECEIVE_MAIL = "receiveMail";
	private static final String TRANSFORMED_ENTITY = "transformedEntity";

	public static void main(String[] args) throws IOException {
		ConfigurableApplicationContext ctx = SpringApplication.run(ImageConsumerApplication.class, args);
		System.out.println("Hit Enter to terminate");
		System.in.read();
		ctx.close();
	}
	
	@Bean
	public MessageChannel receiveMail() {
		return MessageChannels.direct(RECEIVE_MAIL).get();
	}
	
	@Bean
	public MessageChannel transformedEntity() {
		return MessageChannels.direct(TRANSFORMED_ENTITY).get();
	}
	
	@SuppressWarnings("unchecked")
	@Transformer(inputChannel=RECEIVE_MAIL, outputChannel=TRANSFORMED_ENTITY)
	public Message<Email> mailToEmailEntityTransformer(Message<MailMessage> mail) {
		return (Message<Email>) new MailToEmailEntityTransformer().transform(mail);
	}
	
	@ServiceActivator(inputChannel=TRANSFORMED_ENTITY)
	public void handleEmailEntity(Message<Email> emailMessage) {
		System.out.println(Transformers.toMap().transform(emailMessage));
	}
	
}
