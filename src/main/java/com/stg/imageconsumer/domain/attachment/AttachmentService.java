package com.stg.imageconsumer.domain.attachment;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface AttachmentService {
	
	void updateAttachments(Collection<Attachment> attachments);

	KeyedFile getFile(String id);

	Supplier<? extends String> saveAndGetKey(Attachment attachment);

	Stream<Attachment> findSimilarAttachments(Attachment attachment);
	
}
