package com.example.ticketstream;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@Configuration
public class TicketStreamApplicationConfiguration {

	@Bean
    ModelMapper modelMapper() {
	    return new ModelMapper();
	}

}
