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
	public static final String SAVE_ENTITY = "saveEntity";
	public static final String GET_ATTACHMENTS = "getAttachments";
	public static final String SAVE_ATTACHMENTS = "saveAttachments";
	public static final String UPDATE_ATTACHMENTS = "updateAttachments";

	private static final Logger logger = LoggerFactory.getLogger(Integrations.class.getName());

	private MailToEmailEntityTransformer mailToEmailEntityTransformer;

	private GetOrCreateKeyTransformer getOrCreateKeyTransformer;

	private EmailService emailService;

	private AttachmentService attachmentService;

	@Autowired
	public Integrations(MailToEmailEntityTransformer mailToEmailEntityTransformer,
			GetOrCreateKeyTransformer getOrCreateKeyTransformer, EmailService emailService,
			AttachmentService attachmentService) {
		this.mailToEmailEntityTransformer = mailToEmailEntityTransformer;
		this.getOrCreateKeyTransformer = getOrCreateKeyTransformer;
		this.emailService = emailService;
		this.attachmentService = attachmentService;
	}

	@Bean
	public MessageChannel receiveMail() {
		return MessageChannels.direct(RECEIVE_MAIL).get();
	}

	@Bean
	public MessageChannel saveEntity() {
		return MessageChannels.direct(SAVE_ENTITY).get();
	}

	@Bean
	public MessageChannel getAttachments() {
		return MessageChannels.direct(GET_ATTACHMENTS).get();
	}

	@Bean
	public MessageChannel saveAttachments() {
		return MessageChannels.direct(SAVE_ATTACHMENTS).get();
	}

	@Bean
	public MessageChannel updateAttachments() {
		return MessageChannels.direct(UPDATE_ATTACHMENTS).get();
	}

	@SuppressWarnings("unchecked")
	@Transformer(inputChannel = RECEIVE_MAIL, outputChannel = SAVE_ENTITY)
	public Message<Email> mailToEmailEntityTransformer(Message<MailMessage> mail) {
		return (Message<Email>) mailToEmailEntityTransformer.transform(mail);
	}

	@Transformer(inputChannel = SAVE_ENTITY, outputChannel = GET_ATTACHMENTS)
	public Email saveEmailInformation(Email email) {
		logger.debug("attempting to save email");
		Email saved = emailService.save(email);
		logger.debug(String.format("saved email %s", saved.getId()));
		return saved;
	}

	@Transformer(inputChannel = GET_ATTACHMENTS, outputChannel = SAVE_ATTACHMENTS)
	public Set<Attachment> separateAttachments(Email email) {
		return email.getAttachments();
	}

	@Transformer(inputChannel = SAVE_ATTACHMENTS, outputChannel = UPDATE_ATTACHMENTS)
	public Set<Attachment> saveAttachmentsTransformer(Set<Attachment> attachments) {
//		logger.debug("saving attachments");
//		Set<Attachment> savedAttachments = attachments.stream().map(getOrCreateKeyTransformer::doTransform)
//				.collect(Collectors.toSet());
//		logger.debug("{} attachments saved", savedAttachments.size());
//		return savedAttachments;
		return attachments;
	}

	@ServiceActivator(inputChannel = UPDATE_ATTACHMENTS)
	public void updateAttachmentInformations(Set<Attachment> attachments) {
		attachmentService.updateAttachments(attachments);
	}

}
