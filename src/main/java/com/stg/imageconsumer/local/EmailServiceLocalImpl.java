package com.stg.imageconsumer.local;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@Service
public class EmailServiceLocalImpl implements EmailService {
	
	private EmailRepository emailRepository;
	
	@Autowired
	public EmailServiceLocalImpl(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}


	@Override
	public Email save(Email email) {
		return emailRepository.save(email);
	}

	@Override
	public List<Email> getAll() {
		List<Email> allEmails = new ArrayList<>();
		emailRepository.findAll().forEach(allEmails::add);
		return allEmails;
	}
	
	Long count() {
		return emailRepository.count();
	}
	
	@Repository
	public interface EmailRepository extends CrudRepository<Email, Long> {

	}
}
