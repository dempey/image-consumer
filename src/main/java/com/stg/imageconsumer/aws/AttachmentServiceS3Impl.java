package com.stg.imageconsumer.aws;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

public class AttachmentServiceS3Impl implements AttachmentService {

	@Override
	public Attachment save(Attachment attachment) {
		return attachment;
	}

}
