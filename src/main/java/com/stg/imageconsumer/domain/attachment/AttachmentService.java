package com.stg.imageconsumer.domain.attachment;

import java.util.Collection;

public interface AttachmentService {

	Attachment saveFile(Attachment attachment);
	
	void updateAttachments(Collection<Attachment> attachments);
	
}
