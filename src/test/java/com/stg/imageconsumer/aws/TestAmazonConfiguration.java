package com.stg.imageconsumer.aws;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@TestConfiguration
@ComponentScan
@EnableJpaRepositories(considerNestedRepositories=true)
public class TestAmazonConfiguration {

}
