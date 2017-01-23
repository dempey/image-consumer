package com.stg.imageconsumer.local;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;

@Service
public class AttachmentServiceLocalImpl implements AttachmentService {
	private AttachmentRepository attachmentRepository;
	
	@Autowired
	public AttachmentServiceLocalImpl(AttachmentRepository attachmentRepository) {
		this.attachmentRepository = attachmentRepository;
	}
	
	@Override
	public Attachment saveFile(Attachment attachment) {
		return attachment;
	}

	@Override
	public void updateAttachments(Collection<Attachment> attachments) {
		attachmentRepository.save(attachments);
	}
	
	@Repository
	public interface AttachmentRepository extends CrudRepository<Attachment, String> {
		Stream<Attachment> findByMd5AndLength(byte[] md5, int length);
	}

}
