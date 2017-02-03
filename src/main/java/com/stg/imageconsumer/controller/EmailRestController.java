package com.stg.imageconsumer.controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@RestController
@RequestMapping("/emails")
public class EmailRestController {

    private static final Logger logger = LoggerFactory.getLogger(EmailRestController.class);
    
    private EmailService emailService;
    
    private AttachmentService attachmentService;
    
    @Autowired
    public EmailRestController(EmailService emailService, AttachmentService attachmentService) {
    	this.emailService = emailService;
    	this.attachmentService = attachmentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Email> getEmailData() throws IOException {
        logger.debug("Beginning call to get email data");
        List<Email> emails = emailService.getAll();
        for(Email email:emails) {
        	for(Attachment attachment:email.getAttachments()) {
        		String extension = FilenameUtils.getExtension(attachment.getFilename());
        		byte[] rawdata = IOUtils.toByteArray(attachmentService.getFile(attachment.getKey()).getInputStream());
        		String src = String.format("data:image/%s;base64,%s", extension, Base64Utils.encodeToString(rawdata));
        		logger.debug("src: {}", src);
        		attachment.setSrc(src);
        	}
        }
        return emails;
    }

}
