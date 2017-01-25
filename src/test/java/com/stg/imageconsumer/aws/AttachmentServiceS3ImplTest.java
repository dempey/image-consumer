package com.stg.imageconsumer.aws;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.junit.Ignore;
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
@SpringBootTest(classes={ImageConsumerApplicationTests.class, TestAmazonConfiguration.class})
public class AttachmentServiceS3ImplTest {

	@Autowired
	public AttachmentServiceS3Impl attachmentService;
	
	@Test
	public void smokeTest() {
		assertThat(attachmentService, notNullValue());
		assertThat(attachmentService.attachmentRepository.count(), is(0L));
	}
	
	@Ignore
	@Test
	public void testSaveFile() {
		fail("not written yet");
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
}
