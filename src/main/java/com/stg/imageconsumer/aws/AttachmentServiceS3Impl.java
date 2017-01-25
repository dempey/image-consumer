package com.stg.imageconsumer.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.attachment.KeyedFile;

@Service
public class AttachmentServiceS3Impl implements AttachmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);
	
	public static final String bucketName = "imageconsumer";
	
	AttachmentRepository attachmentRepository;
	
	ResourceLoader resourceLoader;
	
	@Autowired
	public AttachmentServiceS3Impl(AttachmentRepository attachmentRepository, ResourceLoader resourceLoader) {
		this.attachmentRepository = attachmentRepository;
		this.resourceLoader = resourceLoader;
	}

	private Resource getResource(String id) {
		return this.resourceLoader.getResource(String.format("s3://%s/%s", bucketName, id));
	}

	@Override
	public KeyedFile getFile(String id) {
		try {
			return new KeyedFile(id, getResource(id).getInputStream());
		} catch (IOException e) {
			logger.error("Error opening the file " + id, e);
		}
		return null;
	}

	@Override
	public Supplier<? extends String> saveAndGetKey(Attachment attachment) {
		return () -> {
			logger.debug("trying to write file to s3");
			WritableResource resource = (WritableResource) getResource(attachment.getId());
			try (OutputStream output = resource.getOutputStream()) {
				IOUtils.copy(new ByteArrayInputStream(attachment.getData()), output);
			} catch (IOException e) {
				logger.error("Error saving the file " + attachment.getId(), e);
			}
			return attachment.getId();
		};
	}

	@Override
	public Stream<Attachment> findSimilarAttachments(Attachment attachment) {
		return attachmentRepository.findByIdNotAndMd5AndLength(attachment.getId(), attachment.getMd5(), attachment.getLength());
	}

	@Override
	public void updateAttachments(Collection<Attachment> attachments) {
		attachmentRepository.save(attachments);
	}
	
	@Repository
	public interface AttachmentRepository extends CrudRepository<Attachment, String> {
		Stream<Attachment> findByIdNotAndMd5AndLength(String id, byte[] md5, int length);
	}
}
