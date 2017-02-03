package com.stg.imageconsumer.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
public class EmailRestController {

    private static final Logger logger = LoggerFactory.getLogger(EmailRestController.class);

    //TODO This should return JSON
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getEmailData() {
        logger.debug("Beginning call to get email data");

        return new ResponseEntity("Not yet implemented", HttpStatus.NOT_IMPLEMENTED);
    }

}
