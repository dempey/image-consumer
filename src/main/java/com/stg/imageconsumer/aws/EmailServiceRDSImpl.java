package com.stg.imageconsumer.aws;

import com.stg.imageconsumer.domain.email.Email;
import com.stg.imageconsumer.domain.email.EmailService;

public class EmailServiceRDSImpl implements EmailService {

	@Override
	public Email save(Email email) {
		return email;
	}

}
