package com.example.apprest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
public class ApprestApplication {

	@RequestMapping("/")
	String home() {
		return "Ingresa tus datos gooo";
	}

	public static void main(String[] args) {
		SpringApplication.run(ApprestApplication.class, args);
	}

}
