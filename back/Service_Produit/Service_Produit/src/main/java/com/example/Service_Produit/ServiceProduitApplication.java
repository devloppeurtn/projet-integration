package com.example.Service_Produit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.example.Service_Produit")
public class ServiceProduitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceProduitApplication.class, args);
	}

}
