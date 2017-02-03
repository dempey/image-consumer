package com.stg.imageconsumer.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@Service
public class EmailServiceRDSImpl implements EmailService {
	
	private EmailRepository emailRepository;
	
	@Autowired
	public EmailServiceRDSImpl(EmailRepository emailRepository) {
		this.emailRepository = emailRepository;
	}

	@Override
	public Email save(Email email) {
		return emailRepository.save(email);
	}
	
	Long count() {
		return emailRepository.count();
	}
	
	@Repository
	public interface EmailRepository extends CrudRepository<Email, String> {

	}
}
