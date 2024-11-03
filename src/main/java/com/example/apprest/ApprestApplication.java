package com.example.apprest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ApprestApplication {

	@RequestMapping("/")
	String home() {
		return "Haz tus peticiones";
	}

	public static void main(String[] args) {
		SpringApplication.run(ApprestApplication.class, args);
	}

}
