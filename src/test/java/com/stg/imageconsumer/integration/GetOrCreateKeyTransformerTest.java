package com.stg.imageconsumer.integration;

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.attachment.KeyedFile;

public class GetOrCreateKeyTransformerTest {
	
	private AttachmentService attachmentService;
	
	private GetOrCreateKeyTransformer getOrCreateKeyTransformer;
	
	private Attachment newAttachment;
	
	private Attachment newAttachmentWithKey;
	
	private Attachment oldAttachment;
	
	private Attachment otherOldAttachment;
	
	private static final String newId = "one";
	private static final String oldId = "two";
	private static final String otherId = "three";
	private static final byte[] contents = "bytes".getBytes();
	private static final byte[] different = "different".getBytes();
	
	@Before
	public void setup() {
		newAttachment = new Attachment(newId, contents);
		newAttachment.setId(newId);
		newAttachmentWithKey = new Attachment(newId, contents);
		newAttachmentWithKey.setId(newId);
		newAttachmentWithKey.setKey(newId);
		oldAttachment = new Attachment(oldId, contents);
		oldAttachment.setId(oldId);
		otherOldAttachment = new Attachment(otherId, different);
		otherOldAttachment.setId(otherId);
		attachmentService = mock(AttachmentService.class);
		when(attachmentService.findSimilarAttachments(eq(newAttachment))).thenReturn(Stream.empty());
		when(attachmentService.findSimilarAttachments(eq(oldAttachment))).thenReturn(Stream.of(newAttachmentWithKey));
		when(attachmentService.findSimilarAttachments(eq(otherOldAttachment))).thenReturn(Stream.of(newAttachmentWithKey));
		when(attachmentService.saveAndGetKey(any(Attachment.class))).thenAnswer(new Answer<Supplier<? extends String>>() {
			public Supplier<? extends String> answer(InvocationOnMock invocation) throws Throwable {
				Attachment attachment = invocation.getArgumentAt(0, Attachment.class);
				return () -> attachment.getId();
			}});
		when(attachmentService.getFile(eq(newId))).thenReturn(new KeyedFile(newId, new ByteArrayInputStream(contents)));
		getOrCreateKeyTransformer = new GetOrCreateKeyTransformer(attachmentService);
	}
	
	@Test(expected = MessageTransformationException.class)
	public void testTransformNotAttachment() {
		Message<String> messageString = MessageBuilder.createMessage("hello", new MessageHeaders(null));
		getOrCreateKeyTransformer.transform(messageString);
	}
	
	@Test
	public void testTransform() {
		Message<Attachment> messageAttachment = MessageBuilder.createMessage(newAttachment, new MessageHeaders(null));
		Message<?> returnedMessage = getOrCreateKeyTransformer.transform(messageAttachment);
		assertThat(returnedMessage.getPayload(), instanceOf(Attachment.class));
		Attachment savedAttachment = (Attachment) returnedMessage.getPayload();
		assertThat(savedAttachment, hasProperty("id", is(newId)));
		assertThat(savedAttachment, hasProperty("key", is(newId)));
	}
	
	@Test
	public void testDoTransformDifferent() {
		Attachment savedAttachment = getOrCreateKeyTransformer.doTransform(newAttachment);
		assertThat(savedAttachment, hasProperty("id", is(newId)));
		assertThat(savedAttachment, hasProperty("key", is(newId)));
	}
	
	@Test
	public void testDoTransformSame() {
		Attachment savedAttachment = getOrCreateKeyTransformer.doTransform(oldAttachment);
		assertThat(savedAttachment, hasProperty("id", is(oldId)));
		assertThat(savedAttachment, hasProperty("key", is(newId)));
	}
	
	@Test
	public void testDoTransformSimilarButDifferent() {
		Attachment savedAttachment = getOrCreateKeyTransformer.doTransform(otherOldAttachment);
		assertThat(savedAttachment, hasProperty("id", is(otherId)));
		assertThat(savedAttachment, hasProperty("key", is(otherId)));
	}

}
