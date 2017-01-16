package com.stg.imageconsumer.domain;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={DatabaseTestConfiguration.class})
public class EmailRepositoryTest {
	
	@Autowired
	private EmailRepository emailRepository;

	
	@Test
	public void smokeTest() {
		assertThat(emailRepository, notNullValue());
		assertThat(emailRepository.count(), is(0L));
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
		Email save = emailRepository.save(email);
		assertThat(save.getId(), notNullValue());
	}

}
