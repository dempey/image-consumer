package com.stg.imageconsumer.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private EmailRepository emailRepository;

	@Override
	public Email save(Email email) {
		return emailRepository.save(email);
	}
	
	@Repository
	public interface EmailRepository extends CrudRepository<Email, Long> {

	}
}
