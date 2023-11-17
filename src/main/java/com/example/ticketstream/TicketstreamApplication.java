package com.example.ticketstream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = { TicketStreamApplicationConfigurationProperties.class })
public class TicketstreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketstreamApplication.class, args);
	}

}
