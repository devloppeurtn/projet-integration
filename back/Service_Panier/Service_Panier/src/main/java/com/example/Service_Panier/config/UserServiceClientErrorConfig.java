package com.example.Service_Panier.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class UserServiceClientErrorConfig {

        @Bean
        public ErrorDecoder errorDecoder() {
            return new ErrorDecoder.Default();
        }
    }