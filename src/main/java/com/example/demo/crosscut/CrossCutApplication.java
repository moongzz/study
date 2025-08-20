package com.example.demo.crosscut;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CrossCutApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrossCutApplication.class, args);
    }
}
