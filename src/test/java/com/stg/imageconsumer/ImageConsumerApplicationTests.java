package com.stg.imageconsumer;

import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import static org.hamcrest.Matchers.is;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration(excludeName = { "org.springframework.integration.mail.ImapIdleChannelAdapter" })
@ComponentScan(basePackages = "com.stg.imageconsumer.domain")
public class ImageConsumerApplicationTests {
	
	@Autowired
	private DataSource dataSource;

	@Test
	public void contextLoads() throws SQLException {
		assertThat(dataSource.getConnection().getMetaData().getDatabaseProductName(), is("H2"));
	}

}
