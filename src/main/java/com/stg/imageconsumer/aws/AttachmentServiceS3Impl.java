package com.stg.imageconsumer.aws;

import com.amazonaws.services.s3.model.*;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

@Service
public class AttachmentServiceS3Impl implements AttachmentService {
	
	private ResourceLoader resourceLoader;
	
	@Autowired
	public AttachmentServiceS3Impl(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public Attachment save(Attachment attachment) {
		String url = getUrlFor(attachment);
		WritableResource writableResource = (WritableResource) resourceLoader.getResource(url);
		try(OutputStream outputStream = writableResource.getOutputStream()) {
			outputStream.write(attachment.getData());
			attachment.setUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return attachment;
	}
	
	public String getUrlFor(Attachment attachment) {
		return String.format("s3://image.consumer/%s", attachment.getMd5());
	}

}
