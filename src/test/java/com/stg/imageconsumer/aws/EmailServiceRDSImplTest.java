package com.stg.imageconsumer.aws;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.stg.imageconsumer.ImageConsumerApplicationTests;
import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.email.Email;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ImageConsumerApplicationTests.class, TestAmazonConfiguration.class})
public class EmailServiceRDSImplTest {
	
	@Autowired
	private EmailServiceRDSImpl emailService;
	
	@Test
	public void smokeTest() {
		assertThat(emailService, notNullValue());
		assertThat(emailService.count(), is(0L));
	}
	
	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void testSave() {
		Email email = new Email();
		email.setTo("bob@bob.com");
		email.setFrom("george@bob.com");
		email.setSubject("test");
		email.setBody("body".getBytes());
		email.setSentDate(new Date());
		email.setReceivedDate(new Date());
		email.addAttachment(new Attachment("one", "one".getBytes()));
		Email saved = emailService.save(email);
		assertThat(saved.getId(), notNullValue());
		saved.getAttachments().stream().forEach(a -> assertThat(a.getId(), notNullValue()));
	}

}
