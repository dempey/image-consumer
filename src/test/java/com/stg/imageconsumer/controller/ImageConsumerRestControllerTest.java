package com.stg.imageconsumer.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.ByteArrayInputStream;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.stg.imageconsumer.domain.attachment.Attachment;
import com.stg.imageconsumer.domain.attachment.AttachmentService;
import com.stg.imageconsumer.domain.attachment.KeyedFile;
import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={WebConfiguration.class})
@AutoConfigureMockMvc
public class ImageConsumerRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmailService emailService;
	
	@MockBean
	private AttachmentService attachmentService;

    //@Test
    public void testGetAllEmails() throws Exception {
    	Email email = new Email();
    	email.setId("test email");
    	email.setFrom("test@test.com");
    	email.setSubject("hello there");
    	email.setBody("I have a body".getBytes());
    	Attachment attachment = new Attachment("one.png", "one".getBytes());
    	attachment.setKey("keyone");
		email.addAttachment(attachment);
    	when(emailService.getAll()).thenReturn(Collections.singletonList(email));
        mockMvc.perform(get("/emails"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$[0].subject", is("hello there")))
	        .andExpect(jsonPath("$[0].attachments[0].key", is("keyone")))
	        .andExpect(jsonPath("$[0].attachments[0].url", is("http://localhost/attachments/keyone")));
//	        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());
    }

    @Test
    public void testGetS3() throws Exception {
    	byte[] bytes = "One".getBytes();
    	when(attachmentService.getFile(eq("one"))).thenReturn(new KeyedFile("one", new ByteArrayInputStream(bytes)));
    	mockMvc.perform(get("/attachments/one"))
    		.andExpect(status().isOk())
    		.andExpect(content().bytes(bytes));
//    		.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());
    }
}
