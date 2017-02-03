package com.stg.imageconsumer.local;

import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.service.EmailRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailRestServiceLocalImpl implements EmailRestService {

    @Autowired
    private EmailServiceLocalImpl.EmailRepository emailRepository;

    @Override
    public List<Email> getAll() {
        return null; //TODO make this call something
    }

}
