package com.stg.imageconsumer.aws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.Base64;
import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

@Service
public class AttachmentServiceS3Impl implements AttachmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);
	
	public static final String bucketName = "imageconsumer";
	
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	public AttachmentServiceS3Impl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}

	@Override
	public Attachment saveFile(Attachment attachment) {
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials("AKIAI6K7HBM4NIWREROQ", "pszNOqJFRHGky8cBwpODLglaf/YTJTVpCToOEb86"));
		if(!s3client.doesBucketExist(bucketName)) {
			s3client.createBucket(bucketName, Region.US_West_2);
		}

		try {
			String key = findSimilarAttachments(attachment)
				.map(Attachment::getKey)
				.filter(k -> k != null)
				.map(id -> s3client.getObject(bucketName, id))
				.filter(s3o -> {
					try {
						return Arrays.equals(IOUtils.toByteArray(s3o.getObjectContent()), attachment.getData());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return false;
				})
				.findAny()
				.map(S3Object::getKey)
				.orElseGet(saveToS3AndGetKey(s3client, attachment));
			logger.debug("saved key {}", key);
			attachment.setKey(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}

	private Supplier<? extends String> saveToS3AndGetKey(AmazonS3 s3client, Attachment attachment) {
		return () -> {
			logger.debug("trying to write file to s3");
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(attachment.getLength());
			metadata.setContentMD5(Base64.encodeAsString(attachment.getMd5()));
			s3client.putObject(bucketName, attachment.getId(), new ByteArrayInputStream(attachment.getData()), metadata);
			return attachment.getId();
		};
	}

	private Stream<Attachment> findSimilarAttachments(Attachment attachment) {
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
