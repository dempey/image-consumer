package com.stg.imageconsumer.local;

import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

@Service
public class AttachmentServiceLocalImpl implements AttachmentService {

	@Override
	public Attachment save(Attachment attachment) {
		return attachment;
	}

}
