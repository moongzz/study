package com.example.demo.di.src;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserService2 {
    @Bean
    public UserService userService() {
        return new UserService();
    }
}

