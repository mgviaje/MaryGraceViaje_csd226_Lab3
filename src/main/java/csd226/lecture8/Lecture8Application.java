package csd226.lecture8;

import csd226.lecture8.data.AccessToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.xml.crypto.Data;

@SpringBootApplication
public class Lecture8Application {

	public static void main(String[] args) {

		ConfigurableApplicationContext app = SpringApplication.run(Lecture8Application.class, args);
	}
	@Bean
	public AccessToken getAccessToken(){return new AccessToken();};

}
