package com.stg.imageconsumer.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.mail.MailMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.stg.imageconsumer.models.Email;
import com.stg.imageconsumer.repository.EmailRepository;

@MessageEndpoint
public class Integrations {
	private static final Logger logger = LoggerFactory.getLogger(Integrations.class);
	
	public static final String RECEIVE_MAIL = "receiveMail";
	private static final String SAVE_ATTACHMENTS = "saveAttachments";
	private static final String SAVE_ENTITY = "saveEntity";
	
	@Autowired
	private EmailRepository emailRepository;
	
	@Bean
	public MessageChannel receiveMail() {
		return MessageChannels.direct(RECEIVE_MAIL).get();
	}
	
	@Bean
	public MessageChannel transformedEntity() {
		return MessageChannels.direct(SAVE_ENTITY).get();
	}
	
	@SuppressWarnings("unchecked")
	@Transformer(inputChannel = RECEIVE_MAIL, outputChannel = SAVE_ENTITY)
	public Message<Email> mailToEmailEntityTransformer(Message<MailMessage> mail) {
		return (Message<Email>) new MailToEmailEntityTransformer().transform(mail);
	}
	
	@ServiceActivator(inputChannel=SAVE_ENTITY)
	public void handleEmailEntity(Email email) {
		logger.debug("attempting to save email");
		logger.debug(email.getBody());
		logger.debug("attachments " + email.getAttachments().size());
		Email saved = emailRepository.save(email);
		logger.debug(String.format("saved email #%d", saved.getId()));
	}
}
