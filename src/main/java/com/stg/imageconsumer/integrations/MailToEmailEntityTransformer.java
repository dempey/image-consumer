package com.stg.imageconsumer.integrations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

import org.apache.commons.io.IOUtils;
import org.springframework.integration.mail.transformer.AbstractMailMessageTransformer;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.stg.imageconsumer.models.Attachment;
import com.stg.imageconsumer.models.Email;

@Component
public class MailToEmailEntityTransformer extends AbstractMailMessageTransformer<Email> {

	private volatile String charset = "UTF-8";

	/**
	 * Specify the name of the Charset to use when converting from bytes.
	 * The default is UTF-8.
	 *
	 * @param charset
	 *            The charset.
	 */
	public void setCharset(String charset) {
		Assert.notNull(charset, "charset must not be null");
		Assert.isTrue(Charset.isSupported(charset), "unsupported charset '" + charset + "'");
		this.charset = charset;
	}

	@Override
	protected AbstractIntegrationMessageBuilder<Email> doTransform(javax.mail.Message mailMessage)
			throws Exception {
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
			handleMultipart((Multipart) content, entity);
		} else if (content instanceof Part) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			((Part) content).writeTo(outputStream);
			entity.setBody((new String(outputStream.toByteArray(), this.charset)).trim());
		}
		return this.getMessageBuilderFactory().withPayload(entity);
	}
	
	private void handleMultipart(Multipart part, Email email) throws MessagingException, IOException {
		for(int i = 0; i < part.getCount(); i++) {
			BodyPart bodyPart = part.getBodyPart(i);
			Object content = bodyPart.getContent();
			if(content instanceof String) {
				String stringContent = (String) content;
				if(Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
					email.addAttachment(new Attachment(bodyPart.getFileName(), stringContent.getBytes()));
				} else if(!email.hasBody()) {
					email.setBody(stringContent);
				} else {
					System.out.println("new string " + stringContent);
				}
			} else if(content instanceof InputStream) {
				byte[] bytes = IOUtils.toByteArray((InputStream) content);
				email.addAttachment(new Attachment(bodyPart.getFileName(), bytes));
			} else if(content instanceof Multipart) {
				handleMultipart((Multipart) content, email);
			}
		}
	}

	private String addressesToString(Address[] addresses) {
		if (addresses == null) {
			return null;
		}
		return Stream.of(addresses).map(a -> a.toString()).collect(Collectors.joining(", "));
	}

}