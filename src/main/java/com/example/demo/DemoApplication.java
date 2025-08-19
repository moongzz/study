package com.example.demo;

import com.example.demo.di.src.BeanConfig;
import com.example.demo.di.src.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
	private final UserService userService;
	private final BeanConfig beanConfig;

	@Autowired
	public DemoApplication(UserService userService, BeanConfig beanConfig) {
		this.userService = userService;
		this.beanConfig = beanConfig;
	}

	public static void main(String[] args) {
		var context = SpringApplication.run(DemoApplication.class, args);
		DemoApplication app = context.getBean(DemoApplication.class);
		app.userService.createUser("method");
		app.beanConfig.createUser("annotation");
	}

}
