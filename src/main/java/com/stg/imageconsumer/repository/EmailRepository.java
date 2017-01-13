package com.stg.imageconsumer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stg.imageconsumer.models.Email;

@Repository
public interface EmailRepository extends CrudRepository<Email, Long> {

}
