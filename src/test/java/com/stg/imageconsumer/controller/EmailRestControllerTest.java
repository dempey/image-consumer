package com.stg.imageconsumer.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

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
public class EmailRestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmailService emailService;
	
	@MockBean
	private AttachmentService attachmentService;

    @Test
    public void getAllEmailsTest() throws Exception {
    	Email email = new Email();
    	email.setId("test email");
    	email.setFrom("test@test.com");
    	email.setSubject("hello there");
    	email.setBody("I have a body");
    	Attachment attachment = new Attachment("one.png", "one".getBytes());
    	attachment.setKey("key");
		email.addAttachment(attachment);
    	when(emailService.getAll()).thenReturn(Collections.singletonList(email));
    	when(attachmentService.getFile(eq("key"))).thenReturn(new KeyedFile("key", new ByteArrayInputStream("one".getBytes())));
        mockMvc.perform(get("/emails"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$[0].subject", is("hello there")))
	        .andExpect(jsonPath("$[0].attachments[0].src", is("data:image/png;base64,b25l")));
//	        .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print());
    }

}
