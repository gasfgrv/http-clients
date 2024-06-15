package com.github.gasfgrv.clients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HttpClientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpClientsApplication.class, args);
	}

}
