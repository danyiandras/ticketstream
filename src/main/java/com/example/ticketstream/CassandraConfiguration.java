package com.example.ticketstream;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

@Configuration
public class CassandraConfiguration extends AbstractCassandraConfiguration {

	@Value("${TICKET_STREAM_CASSANDRA_CONTACTPOINT:localhost}")
	private String contactPoint;

	private String ticketStreamKeyspace = "ticketstream";

	@Override
	protected String getKeyspaceName() {
		return ticketStreamKeyspace;
	}

	@Override
	protected String getContactPoints() {
		return contactPoint;
	}

	@Override
	protected int getPort() {
		return 9042;
	}
	
	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.CREATE_IF_NOT_EXISTS;
	}

	@Override
	public String[] getEntityBasePackages() {
		return new String[] { "com.example.ticketstream.model" };
	}

	@Override
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(this.getKeyspaceName())
				.ifNotExists().with(KeyspaceOption.DURABLE_WRITES, true).withSimpleReplication());
	}


}