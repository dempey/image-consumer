package com.stg.imageconsumer.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
	
	private EmailService emailService;
	
	private AttachmentService attachmentService;
	
	@Before
	public void setup() {
		mailToEmailEntityTransformer = mock(MailToEmailEntityTransformer.class);
		emailService = mock(EmailService.class);
		attachmentService = mock(AttachmentService.class);
		when(emailService.save(any(Email.class))).thenAnswer(new Answer<Email>() {
			public Email answer(InvocationOnMock invocation) {
				Email email = invocation.getArgumentAt(0, Email.class);
				email.setId(-1L);
				return email;
			}
		});
		integrations = new Integrations(mailToEmailEntityTransformer, emailService, attachmentService);
	}
	
	@Test
	public void smokeTest() {
		assertThat(IntegrationConfiguration.RECEIVE_MAIL, notNullValue());
		assertThat(IntegrationConfiguration.RECEIVE_MAIL, is("receiveMail"));
		assertThat(integrations, notNullValue());
	}

//	@Test
//	public void testReceiveMail() {
//		MessageChannel receiveMail = integrations.receiveMail();
//		assertThat(receiveMail, notNullValue());
//		assertThat(((AbstractMessageChannel) receiveMail).getFullChannelName(), is(IntegrationConfiguration.RECEIVE_MAIL));
//	}
//
//	@Test
//	public void testSaveEntity() {
//		MessageChannel transformedEntity = integrations.saveEntity();
//		assertThat(transformedEntity, notNullValue());
//		assertThat(((AbstractMessageChannel) transformedEntity).getFullChannelName(), is(IntegrationConfiguration.SAVE_ENTITY));
//	}

	@Test
	public void testMailToEmailEntityTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("mailToEmailEntityTransformer", Message.class).isAnnotationPresent(Transformer.class), is(true));
		integrations.mailToEmailEntityTransformer(null);
		verify(mailToEmailEntityTransformer).transform(any());
	}

	@Test
	public void testSaveEmailInformation() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("saveEmailInformation", Email.class).isAnnotationPresent(ServiceActivator.class), is(true));
		integrations.saveEmailInformation(new Email());
		verify(emailService).save(any(Email.class));
	}
	
	@Test
	public void testSaveAttachmentsTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("saveAttachmentsTransformer", Email.class).isAnnotationPresent(Transformer.class), is(true));
		Email fakeEmail = new Email();
		fakeEmail.addAttachment(new Attachment("one", "123".getBytes()));
		fakeEmail.addAttachment(new Attachment("two", "321".getBytes()));
		integrations.saveAttachmentsTransformer(fakeEmail);
		verify(attachmentService, times(2)).save(any(Attachment.class));
	}

}
