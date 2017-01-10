package com.stg.imageconsumer.transformers;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Part;

import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.util.Assert;

import com.stg.imageconsumer.models.Email;

public class MailToEmailEntityTransformer extends AbstractMailMessageTransformer<Email> {
	
	private volatile String charset = "UTF-8";

	/**
	 * Specify the name of the Charset to use when converting from bytes.
	 * The default is UTF-8.
	 *
	 * @param charset The charset.
	 */
	public void setCharset(String charset) {
		Assert.notNull(charset, "charset must not be null");
		Assert.isTrue(Charset.isSupported(charset), "unsupported charset '" + charset + "'");
		this.charset = charset;
	}

	@Override
	protected AbstractIntegrationMessageBuilder<Email> doTransform(Message mailMessage) throws Exception {
		Email entity = new Email();
		entity.setSentDate(mailMessage.getSentDate());
		entity.setReceivedDate(mailMessage.getReceivedDate());
		entity.setFrom(addressesToString(mailMessage.getFrom()));
		entity.setTo(addressesToString(mailMessage.getRecipients(RecipientType.TO)));
		entity.setCC(addressesToString(mailMessage.getRecipients(RecipientType.CC)));
		entity.setBCC(addressesToString(mailMessage.getRecipients(RecipientType.BCC)));
		entity.setSubject(mailMessage.getSubject());
		Object content = mailMessage.getContent();
		if (content instanceof String) {
			entity.setBody((String) mailMessage.getContent());
		} else if (content instanceof Multipart) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			((Multipart) content).getBodyPart(0).writeTo(outputStream);
			entity.setBody((new String(outputStream.toByteArray(), this.charset)).trim());
		} else if (content instanceof Part) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			((Part) content).writeTo(outputStream);
			entity.setBody((new String(outputStream.toByteArray(), this.charset)).trim());
		}
		return this.getMessageBuilderFactory().withPayload(entity);
	}
	
	private String addressesToString(Address[] addresses) {
		if(addresses == null) {
			return null;
		}
		return Stream.of(addresses)
				.map(a -> a.toString())
				.collect(Collectors.joining(", "));
	}

}
