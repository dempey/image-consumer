package com.stg.imageconsumer.aws;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

@Service
public class AttachmentServiceS3Impl implements AttachmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);
	
	public static final String bucketName = "imageconsumer";

	@Override
	public Attachment save(Attachment attachment) {
		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials("AKIAI6K7HBM4NIWREROQ", "pszNOqJFRHGky8cBwpODLglaf/YTJTVpCToOEb86"));
		if(!s3client.doesBucketExist(bucketName)) {
			s3client.createBucket(bucketName);
		}
		s3client.listBuckets().stream()
			.map(Bucket::getName)
			.map(s3client::listObjects)
			.map(ObjectListing::getObjectSummaries)
			.flatMap(List::stream)
			.forEach(os -> logger.debug("bucket: {}, key: {}", os.getBucketName(), os.getKey()));

		try {
			logger.debug("tyring to write file to s3");
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(attachment.getLength());
			metadata.setContentMD5(attachment.getMd5());
			s3client.putObject("imageconsumer", attachment.getFilename(), new ByteArrayInputStream(attachment.getData()), metadata);
			attachment.setUrl(s3client.getUrl("imageconsumer", attachment.getFilename()).toExternalForm());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachment;
	}
	
}
