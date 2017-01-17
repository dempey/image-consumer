package com.stg.imageconsumer.local;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestDatabaseConfiguration.class})
public class EmailServiceImplTest {
	
	@Autowired
	private EmailService emailService;

	
	@Test
	public void smokeTest() {
		assertThat(emailService, notNullValue());
	}
	
	@Test
	public void testInsert() {
		Email email = new Email();
		email.setTo("bob@bob.com");
		email.setFrom("george@bob.com");
		email.setSubject("test");
		email.setBody("body");
		email.setSentDate(new Date());
		email.setReceivedDate(new Date());
		email.addAttachment(new Attachment("one", "one".getBytes()));
		Email saved = emailService.save(email);
		assertThat(saved.getId(), notNullValue());
		saved.getAttachments().stream().forEach(a -> assertThat(a.getId(), notNullValue()));
	}

}
