package com.stg.imageconsumer.integrations;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import com.stg.imageconsumer.models.Email;
import com.stg.imageconsumer.repository.EmailRepository;

public class IntegrationsTest {
	
	private Integrations integrations;
	
	private MailToEmailEntityTransformer mailToEmailEntityTransformer;
	
	private EmailRepository emailRepository;
	
	@Before
	public void setup() {
		mailToEmailEntityTransformer = mock(MailToEmailEntityTransformer.class);
		emailRepository = mock(EmailRepository.class);
		when(emailRepository.save(any(Email.class))).thenAnswer(new Answer<Email>() {
			public Email answer(InvocationOnMock invocation) {
				Email email = invocation.getArgumentAt(0, Email.class);
				email.setId(-1L);
				return email;
			}
		});
		integrations = new Integrations(mailToEmailEntityTransformer, emailRepository);
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
		MessageChannel transformedEntity = integrations.saveEntity();
		assertThat(transformedEntity, notNullValue());
		assertThat(((AbstractMessageChannel) transformedEntity).getFullChannelName(), is(Integrations.SAVE_ENTITY));
	}

	@Test
	public void testMailToEmailEntityTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("mailToEmailEntityTransformer", Message.class).isAnnotationPresent(Transformer.class), is(true));
		integrations.mailToEmailEntityTransformer(null);
		verify(mailToEmailEntityTransformer).transform(any());
	}

	@Test
	public void testHandleEmailEntity() throws NoSuchMethodException, SecurityException {
		assertThat(Integrations.class.getDeclaredMethod("handleEmailEntity", Email.class).isAnnotationPresent(ServiceActivator.class), is(true));
		integrations.handleEmailEntity(new Email());
		verify(emailRepository).save(any(Email.class));
	}

}
