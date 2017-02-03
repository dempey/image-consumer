package com.stg.imageconsumer.aws;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.attachment.KeyedFile;

public class AttachmentServiceS3ImplTest {
	
	private AttachmentServiceS3Impl.AttachmentRepository attachmentRepository;
	
	private ResourceLoader resourceLoader;
	
	private AttachmentService attachmentService;
	
	@Before
	public void setup() {
		attachmentRepository = mock(AttachmentServiceS3Impl.AttachmentRepository.class);
		resourceLoader = mock(ResourceLoader.class);
		attachmentService = new AttachmentServiceS3Impl(attachmentRepository, resourceLoader);
	}

	@Test
	public void testGetFile() throws IOException {
		when(resourceLoader.getResource(anyString())).thenReturn(new ByteArrayResource("hello there".getBytes()));
		KeyedFile one = attachmentService.getFile("one");
		assertThat(IOUtils.toByteArray(one.getInputStream()), is("hello there".getBytes()));
		verify(resourceLoader).getResource(eq("s3://" + AmazonConfiguration.BUCKET + "/one"));
	}

	@Test
	public void testSaveAndGetKey() throws IOException {
		WritableResource resource = mock(WritableResource.class);
		when(resource.getOutputStream()).thenReturn(new ByteArrayOutputStream());
		when(resourceLoader.getResource(anyString())).thenReturn(resource);
		Attachment attachment = new Attachment("one", "one".getBytes());
		attachment.setId("id");
		Supplier<? extends String> sup = attachmentService.saveAndGetKey(attachment);
		assertThat(sup.get(), is("id"));
	}
}
