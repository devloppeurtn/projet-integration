package com.example.Getaway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GetawayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetawayServiceApplication.class, args);
	}

}
