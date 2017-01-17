package com.stg.imageconsumer.integration;

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

import com.stg.imageconsumer.domain.Email;
import com.stg.imageconsumer.domain.EmailRepository;
import com.stg.imageconsumer.integration.IntegrationConfiguration;
import com.stg.imageconsumer.integration.MailToEmailEntityTransformer;

public class IntegrationConfigurationTest {
	
	private IntegrationConfiguration integrations;
	
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
		integrations = new IntegrationConfiguration(mailToEmailEntityTransformer, emailRepository);
	}
	
	@Test
	public void smokeTest() {
		assertThat(IntegrationConfiguration.RECEIVE_MAIL, notNullValue());
		assertThat(IntegrationConfiguration.RECEIVE_MAIL, is("receiveMail"));
		assertThat(integrations, notNullValue());
	}

	@Test
	public void testReceiveMail() {
		MessageChannel receiveMail = integrations.receiveMail();
		assertThat(receiveMail, notNullValue());
		assertThat(((AbstractMessageChannel) receiveMail).getFullChannelName(), is(IntegrationConfiguration.RECEIVE_MAIL));
	}

	@Test
	public void testSaveEntity() {
		MessageChannel transformedEntity = integrations.saveEntity();
		assertThat(transformedEntity, notNullValue());
		assertThat(((AbstractMessageChannel) transformedEntity).getFullChannelName(), is(IntegrationConfiguration.SAVE_ENTITY));
	}

	@Test
	public void testMailToEmailEntityTransformer() throws NoSuchMethodException, SecurityException {
		assertThat(IntegrationConfiguration.class.getDeclaredMethod("mailToEmailEntityTransformer", Message.class).isAnnotationPresent(Transformer.class), is(true));
		integrations.mailToEmailEntityTransformer(null);
		verify(mailToEmailEntityTransformer).transform(any());
	}

	@Test
	public void testHandleEmailEntity() throws NoSuchMethodException, SecurityException {
		assertThat(IntegrationConfiguration.class.getDeclaredMethod("handleEmailEntity", Email.class).isAnnotationPresent(ServiceActivator.class), is(true));
		integrations.handleEmailEntity(new Email());
		verify(emailRepository).save(any(Email.class));
	}

}
