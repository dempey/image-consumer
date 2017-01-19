package com.stg.imageconsumer.integration;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.mail.MailMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@Component
public class Integrations {
	
	public static final String RECEIVE_MAIL = "receiveMail";
	public static final String SAVE_ATTACHMENTS = "saveAttachments";
	public static final String SAVE_ENTITY = "saveEntity";
	
	private static final Logger logger = LoggerFactory.getLogger(Integrations.class);
	
	private MailToEmailEntityTransformer mailToEmailEntityTransformer;
	
	private EmailService emailService;
	
	private AttachmentService attachmentService;
	
	@Autowired
	public Integrations(MailToEmailEntityTransformer mailToEmailEntityTransformer, EmailService emailService, AttachmentService attachmentService) {
		this.mailToEmailEntityTransformer = mailToEmailEntityTransformer;
		this.emailService = emailService;
		this.attachmentService = attachmentService;
	}
	

	@Bean
	public MessageChannel receiveMail() {
		return MessageChannels.direct(RECEIVE_MAIL).get();
	}
	
	@Bean
	public MessageChannel sadeAttachments() {
		return MessageChannels.direct(SAVE_ATTACHMENTS).get();
	}
	
	@Bean
	public MessageChannel saveEntity() {
		return MessageChannels.direct(SAVE_ENTITY).get();
	}
	
	@SuppressWarnings("unchecked")
	@Transformer(inputChannel = RECEIVE_MAIL, outputChannel = SAVE_ATTACHMENTS)
	public Message<Email> mailToEmailEntityTransformer(Message<MailMessage> mail) {
		return (Message<Email>) mailToEmailEntityTransformer.transform(mail);
	}
	
	@Transformer(inputChannel = SAVE_ATTACHMENTS, outputChannel = SAVE_ENTITY)
	public Email saveAttachmentsTransformer(Email email) {
		logger.debug("saving attachments");
		Set<Attachment> savedAttachments = email.getAttachments().stream().map(attachmentService::save).collect(Collectors.toSet());
		email.setAttachments(savedAttachments);
		logger.debug(String.format("%d attachments saved", savedAttachments.size()));
		return email;
	}	
	
	@ServiceActivator(inputChannel=SAVE_ENTITY)
	public void saveEmailInformation(Email email) {
		logger.debug("attempting to save email");
		Email saved = emailService.save(email);
		logger.debug(String.format("saved email #%d", saved.getId()));
	}

}
