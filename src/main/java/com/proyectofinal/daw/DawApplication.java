package com.proyectofinal.daw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.proyectofinal.daw")
public class DawApplication {

	public static void main(String[] args) {
		SpringApplication.run(DawApplication.class, args);
	}

}
