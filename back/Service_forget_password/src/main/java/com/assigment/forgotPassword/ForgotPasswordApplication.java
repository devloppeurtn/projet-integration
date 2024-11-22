package com.assigment.forgotPassword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = "com.assigment.forgotPassword")
@EnableMongoRepositories
public class ForgotPasswordApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForgotPasswordApplication.class, args);
	}

}
