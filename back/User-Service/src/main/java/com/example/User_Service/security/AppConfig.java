package com.example.User_Service.security;


import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    @LoadBalanced  // Important pour utiliser Eureka pour résoudre les noms
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
