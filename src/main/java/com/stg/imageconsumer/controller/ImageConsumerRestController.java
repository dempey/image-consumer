package com.stg.imageconsumer.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@RestController
public class ImageConsumerRestController {

    private static final Logger logger = LoggerFactory.getLogger(ImageConsumerRestController.class);
    
    private EmailService emailService;
    
    private AttachmentService attachmentService;
    
    @Autowired
    public ImageConsumerRestController(EmailService emailService, AttachmentService attachmentService) {
    	this.emailService = emailService;
    	this.attachmentService = attachmentService;
    }

    @RequestMapping(path = "/email", method = RequestMethod.GET)
    public List<Email> getEmailData() throws IOException {
        logger.debug("Beginning call to get email data");
        List<Email> emails = emailService.getAll();
        emails.forEach(e -> {
        	e.getAttachments().forEach(a -> {
        		a.setUrl(attachmentService.getUrlFor(a.getKey()));
        	});
        });
        return emails;
    }

}
