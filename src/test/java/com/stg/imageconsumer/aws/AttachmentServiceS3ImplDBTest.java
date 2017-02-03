package com.stg.imageconsumer.aws;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;

import com.stg.imageconsumer.ImageConsumerApplicationTests;
import com.stg.imageconsumer.domain.attachment.Attachment;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { ImageConsumerApplicationTests.class, TestAmazonConfiguration.class })
//@EnableJpaRepositories(considerNestedRepositories=true)
public class AttachmentServiceS3ImplDBTest {

	@Autowired
	public AttachmentServiceS3Impl attachmentService;

	@Test
	public void smokeTest() {
		assertThat(attachmentService, notNullValue());
		assertThat(attachmentService.attachmentRepository.count(), is(0L));
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void testUpdateAttachments() {
		Attachment saved = attachmentService.attachmentRepository.save(new Attachment("one", "one".getBytes()));
		saved.setKey("two");
		attachmentService.updateAttachments(Collections.singleton(saved));
		Attachment updated = attachmentService.attachmentRepository.findOne(saved.getId());
		assertThat(updated.getKey(), is("two"));
	}

	@Test
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void testFindSimilarAttachments() {
		Attachment saved = attachmentService.attachmentRepository.save(new Attachment("one", "one".getBytes()));
		Attachment two = new Attachment("one", saved.getData());
		two.setId("two");
		Attachment similar = attachmentService.findSimilarAttachments(two).findFirst().get();
		assertThat(similar.getId(), is(saved.getId()));
	}

	@Test(expected = NoSuchElementException.class)
	@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
	public void testFindNoSimilarAttachments() {
		attachmentService.attachmentRepository.save(new Attachment("one", "one".getBytes()));
		Attachment two = new Attachment("one", "two".getBytes());
		two.setId("two");
		attachmentService.findSimilarAttachments(two).findFirst().get();
	}

}