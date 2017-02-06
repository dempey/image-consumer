package com.stg.imageconsumer.domain.email;

import java.util.List;

public interface EmailService {

	Email save(Email email);
	
	List<Email> getAll();

}