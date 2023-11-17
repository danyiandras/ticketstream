package com.example.ticketstream;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Primary;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ConfigurationProperties(prefix = "ticketstream")
@ConfigurationPropertiesScan
@Getter @Setter @ToString
@Primary
public class TicketStreamApplicationConfigurationProperties {

	@Min(1)
	private int createTicketRetry = 1;
	
}
