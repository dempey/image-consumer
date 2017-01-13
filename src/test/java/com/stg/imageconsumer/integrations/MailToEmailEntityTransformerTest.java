package com.stg.imageconsumer.integrations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.junit.Before;
import org.junit.Test;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;

import com.stg.imageconsumer.integrations.MailToEmailEntityTransformer;
import com.stg.imageconsumer.models.Email;

public class MailToEmailEntityTransformerTest {
	
	private MailToEmailEntityTransformer transformer;

	@Before
	public void setup() {
		transformer = new MailToEmailEntityTransformer();
	}
	
	@Test
	public void testDoTransformString() throws Exception {
		Date sentDate = new Date(0L);
		Date receivedDate = new Date(13000000L);
		Message msg1 = mock(Message.class);
		when(msg1.getContent()).thenReturn("hello");
		when(msg1.getSentDate()).thenReturn(sentDate);
		when(msg1.getFrom()).thenReturn(new Address[]{new InternetAddress("bob@bob.com")});
		when(msg1.getSubject()).thenReturn("re: subject");
		when(msg1.getReceivedDate()).thenReturn(receivedDate);
		when(msg1.getRecipients(eq(RecipientType.TO))).thenReturn(new Address[]{new InternetAddress("to@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.CC))).thenReturn(new Address[]{new InternetAddress("cc@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.BCC))).thenReturn(new Address[]{new InternetAddress("bcc@bob.com")});
		AbstractIntegrationMessageBuilder<Email> transformed = transformer.doTransform(msg1);
		assertThat(transformed, notNullValue());
		Email entity = transformed.getPayload();
		assertThat(entity.getBody(), is("hello"));
		assertThat(entity.getSentDate(), is(sentDate));
		assertThat(entity.getFrom(), is("bob@bob.com"));
		assertThat(entity.getSubject(), is("re: subject"));
		assertThat(entity.getReceivedDate(), is(receivedDate));
		assertThat(entity.getTo(), is("to@bob.com"));
		assertThat(entity.getCC(), is("cc@bob.com"));
		assertThat(entity.getBCC(), is("bcc@bob.com"));
	}
	
	@Test
	public void testDoTransformPart() throws Exception {
		Date sentDate = new Date(12000000L);
		Date receivedDate = new Date(1600000L);
		Part content = new MimeBodyPart();
		content.setContent("part hello", "text/plain");
		Message msg1 = mock(Message.class);
		when(msg1.getContent()).thenReturn(content);
		when(msg1.getSentDate()).thenReturn(sentDate);
		when(msg1.getFrom()).thenReturn(new Address[]{new InternetAddress("bob@bob.com")});
		when(msg1.getSubject()).thenReturn("re: subject");
		when(msg1.getReceivedDate()).thenReturn(receivedDate);
		when(msg1.getRecipients(eq(RecipientType.TO))).thenReturn(new Address[]{new InternetAddress("to@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.CC))).thenReturn(new Address[]{new InternetAddress("cc@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.BCC))).thenReturn(new Address[]{new InternetAddress("bcc@bob.com")});
		AbstractIntegrationMessageBuilder<Email> transformed = transformer.doTransform(msg1);
		assertThat(transformed, notNullValue());
		Email entity = transformed.getPayload();
		assertThat(entity.getBody(), is("part hello"));
		assertThat(entity.getSentDate(), is(sentDate));
		assertThat(entity.getFrom(), is("bob@bob.com"));
		assertThat(entity.getSubject(), is("re: subject"));
		assertThat(entity.getReceivedDate(), is(receivedDate));
		assertThat(entity.getTo(), is("to@bob.com"));
		assertThat(entity.getCC(), is("cc@bob.com"));
		assertThat(entity.getBCC(), is("bcc@bob.com"));
	}
	
	@Test
	public void testDoTransformMultipart() throws Exception {
		Date sentDate = new Date(12000000L);
		Date receivedDate = new Date(1600000L);
		Multipart content = new MimeMultipart();
		BodyPart bodypart = new MimeBodyPart();
		bodypart.setContent("multipart hello", "text/plain"); 
		content.addBodyPart(bodypart);
		Message msg1 = mock(Message.class);
		when(msg1.getContent()).thenReturn(content);
		when(msg1.getSentDate()).thenReturn(sentDate);
		when(msg1.getFrom()).thenReturn(new Address[]{new InternetAddress("bob@bob.com")});
		when(msg1.getSubject()).thenReturn("re: subject");
		when(msg1.getReceivedDate()).thenReturn(receivedDate);
		when(msg1.getRecipients(eq(RecipientType.TO))).thenReturn(new Address[]{new InternetAddress("to@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.CC))).thenReturn(new Address[]{new InternetAddress("cc@bob.com")});
		when(msg1.getRecipients(eq(RecipientType.BCC))).thenReturn(new Address[]{new InternetAddress("bcc@bob.com")});
		AbstractIntegrationMessageBuilder<Email> transformed = transformer.doTransform(msg1);
		assertThat(transformed, notNullValue());
		Email entity = transformed.getPayload();
		assertThat(entity.getBody(), is("multipart hello"));
		assertThat(entity.getSentDate(), is(sentDate));
		assertThat(entity.getFrom(), is("bob@bob.com"));
		assertThat(entity.getSubject(), is("re: subject"));
		assertThat(entity.getReceivedDate(), is(receivedDate));
		assertThat(entity.getTo(), is("to@bob.com"));
		assertThat(entity.getCC(), is("cc@bob.com"));
		assertThat(entity.getBCC(), is("bcc@bob.com"));
	}
	
}
