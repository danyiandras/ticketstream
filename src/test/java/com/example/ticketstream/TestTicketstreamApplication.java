package com.example.ticketstream;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestTicketstreamApplication {

	@Bean
	@ServiceConnection
	CassandraContainer<?> cassandraContainer() {
		CassandraContainer<?> cassandraContainer = new CassandraContainer<>(DockerImageName.parse("cassandra:latest"));
    	List<String> portBindings = new ArrayList<>();
    	portBindings.add("127.0.0.1:9042:9042"); // hostPort:containerPort
    	cassandraContainer.setPortBindings(portBindings);
		return cassandraContainer;
	}

	@Bean
	@ServiceConnection
	RabbitMQContainer rabbitContainer() {
		return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
	}

	public static void main(String[] args) {
		SpringApplication.from(TicketstreamApplication::main).with(TestTicketstreamApplication.class).run(args);
	}

}
