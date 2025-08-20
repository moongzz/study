package com.example.demo.crosscut.src.controller;

import com.example.demo.crosscut.src.service.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("[Controller]");
        service.test();
        return "OK";
    }
}
