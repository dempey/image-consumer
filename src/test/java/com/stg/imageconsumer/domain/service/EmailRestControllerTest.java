package com.stg.imageconsumer.domain.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class EmailRestControllerTest {

    private EmailRestController emailController;

    @Before
    public void setUp() {
        emailController = new EmailRestController();
    }

    @Test
    public void getAllEmailsTest() {
        ResponseEntity emailData = emailController.getEmailData();
        assertEquals(emailData.getStatusCode(), HttpStatus.NOT_IMPLEMENTED);
    }

}
