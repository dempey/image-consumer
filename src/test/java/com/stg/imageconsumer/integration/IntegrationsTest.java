package com.stg.imageconsumer.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

public class IntegrationsTest {
	
	private Integrations integrations;
	
	private MailToEmailEntityTransformer mailToEmailEntityTransformer;
	
	private GetOrCreateKeyTransformer getOrCreateKeyTransformer;
	
	private EmailService emailService;
	
	private AttachmentService attachmentService;
	
	@Before
	public void setup() {
		mailToEmailEntityTransformer = mock(MailToEmailEntityTransformer.class);
		getOrCreateKeyTransformer = mock(GetOrCreateKeyTransformer.class);
		emailService = mock(EmailService.class);
		attachmentService = mock(AttachmentService.class);
		when(emailService.save(any(Email.class))).thenAnswer(new Answer<Email>() {
			public Email answer(InvocationOnMock invocation) {
				Email email = invocation.getArgumentAt(0, Email.class);
				email.setId(UUID.randomUUID().toString());
				email.setAttachments(email.getAttachments().stream()
						.map(a -> {
							a.setId(UUID.randomUUID().toString());
							return a;
						})
						.collect(Collectors.toSet()));
				return email;
			}
		});
		when(getOrCreateKeyTransformer.doTransform(any(Attachment.class))).thenAnswer(new Answer<Attachment>() {
			public Attachment answer(InvocationOnMock invocation) throws Throwable {
				Attachment attachment = invocation.getArgumentAt(0, Attachment.class);
				attachment.setId(UUID.randomUUID().toString());
				attachment.setKey(UUID.randomUUID().toString());
				return null;
			}});
		integrations = new Integrations(mailToEmailEntityTransformer, getOrCreateKeyTransformer, emailService, attachmentService);
	}
	
	@Test
	public void smokeTest() {
		assertThat(Integrations.RECEIVE_MAIL, notNullValue());
		assertThat(Integrations.RECEIVE_MAIL, is("receiveMail"));
		assertThat(integrations, notNullValue());
	}

	@Test
	public void testReceiveMail() {
		MessageChannel receiveMail = integrations.receiveMail();
		assertThat(receiveMail, notNullValue());
		assertThat(((AbstractMessageChannel) receiveMail).getFullChannelName(), is(Integrations.RECEIVE_MAIL));
	}

	@Test
	public void testSaveEntity() {
		MessageChannel saveEntity = integrations.saveEntity();
		assertThat(saveEntity, notNullValue());
		assertThat(((AbstractMessageChannel) saveEntity).getFullChannelName(), is(Integrations.SAVE_ENTITY));
	}

	@Test
	public void testGetAttachments() {
		MessageChannel getAttachments = integrations.getAttachments();
		assertThat(getAttachments, notNullValue());
		assertThat(((AbstractMessageChannel) getAttachments).getFullChannelName(), is(Integrations.GET_ATTACHMENTS));
	}

	@Test
	public void testSaveAttachments() {
		MessageChannel saveAttachments = integrations.saveAttachments();
		assertThat(saveAttachments, notNullValue());
		assertThat(((AbstractMessageChannel) saveAttachments).getFullChannelName(), is(Integrations.SAVE_ATTACHMENTS));
	}

	@Test
	public void testUpdateAttachments() {
		MessageChannel updateAttachments = integrations.updateAttachments();
		assertThat(updateAttachments, notNullValue());
		assertThat(((AbstractMessageChannel) updateAttachments).getFullChannelName(), is(Integrations.UPDATE_ATTACHMENTS));
	}

	@Test
	public void testMailToEmailEntityTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("mailToEmailEntityTransformer", Message.class).isAnnotationPresent(Transformer.class), is(true));
		integrations.mailToEmailEntityTransformer(null);
		verify(mailToEmailEntityTransformer).transform(any());
	}

	@Test
	public void testSaveEmailInformation() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("saveEmailInformation", Email.class).isAnnotationPresent(Transformer.class), is(true));
		integrations.saveEmailInformation(new Email());
		verify(emailService).save(any(Email.class));
	}

	@Test
	public void testSeparateAttachments() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("separateAttachments", Email.class).isAnnotationPresent(Transformer.class), is(true));
		Email email = new Email();
		email.addAttachment(new Attachment("one", "one".getBytes()));
		email.addAttachment(new Attachment("two", "two".getBytes()));
		Set<Attachment> attachments = integrations.separateAttachments(email);
		assertThat(attachments.size(), is(2));
	}
	
	@Test
	public void testSaveAttachmentsTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("saveAttachmentsTransformer", Set.class).isAnnotationPresent(Transformer.class), is(true));
		Set<Attachment> attachments = new HashSet<>(Arrays.asList(new Attachment("one", "123".getBytes()), new Attachment("two", "321".getBytes())));
		integrations.saveAttachmentsTransformer(attachments);
		verify(getOrCreateKeyTransformer, times(2)).doTransform(any(Attachment.class));
	}
	
	@Test
	public void testUpdateAttachmentInformations() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("updateAttachmentInformations", Set.class).isAnnotationPresent(ServiceActivator.class), is(true));
		Set<Attachment> attachments = Collections.singleton(new Attachment("one", "one".getBytes()));
		integrations.updateAttachmentInformations(attachments);
		verify(attachmentService).updateAttachments(eq(attachments));
	}

}
