package com.stg.imageconsumer.integration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.utils.IntegrationUtils;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.integration.transformer.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.attachment.KeyedFile;

@Component
public class GetOrCreateKeyTransformer implements Transformer {

	private static final Logger logger = Logger.getLogger(GetOrCreateKeyTransformer.class.getName());

	private AttachmentService attachmentService;

	@Autowired
	public GetOrCreateKeyTransformer(AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Override
	public Message<?> transform(Message<?> message) {
		Object payload = message.getPayload();
		if (!(payload instanceof Attachment)) {
			throw new MessageTransformationException(message, this.getClass().getSimpleName()
					+ " requires a com.stg.imageconsumer.domain.attachment.Attachment payload");
		}
		Attachment attachment = (Attachment) payload;
		AbstractIntegrationMessageBuilder<Attachment> builder = null;
		try {
			builder = IntegrationUtils.getMessageBuilderFactory(null).withPayload(attachment);
		} catch (Exception e) {
			throw new MessageTransformationException(message, "failed to transform mail message", e);
		}
		if (builder == null) {
			throw new MessageTransformationException(message, "failed to transform mail message");
		}
		builder.copyHeaders(message.getHeaders());
		return builder.build();
	}

	public Attachment doTransform(Attachment attachment) {
//		try {
//			String key = attachmentService.findSimilarAttachments(attachment)
//					.map(Attachment::getKey)
//					.filter(k -> k != null).map(id -> attachmentService.getFile(id)).filter(kf -> {
//						try {
//							return IOUtils.contentEquals(kf.getInputStream(),
//									new ByteArrayInputStream(attachment.getData()));
//						} catch (IOException e) {
//							logger.error("problem comparing files", e);
//						}
//						return false;
//					}).findAny()
//					.map(KeyedFile::getKey)
//					.orElseGet(attachmentService.saveAndGetKey(attachment));
//			logger.debug("saved key {}", key);
//			attachment.setKey(key);
//		} catch (Exception e) {
//			logger.error("Error saving attachment " + attachment.getId(), e);
//		}
		return attachment;
	}

}
