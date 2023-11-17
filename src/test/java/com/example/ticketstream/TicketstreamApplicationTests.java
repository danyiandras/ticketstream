package com.example.ticketstream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.RabbitMQContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Import(TestTicketstreamApplication.class)
class TicketstreamApplicationTests {

	@Autowired
	private CassandraContainer<?> cassandra;

	@Autowired
	private RabbitMQContainer rabbitMq;

	@Test
	void givenCassandraContainer_whenSpringContextIsBootstrapped_thenContainerIsRunningWithNoExceptions() {
	    assertThat(cassandra.isRunning()).isTrue();
	    assertThat(rabbitMq.isRunning()).isTrue();
	}
	
}
