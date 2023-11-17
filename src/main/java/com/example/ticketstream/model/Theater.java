package com.example.ticketstream.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The Theater class represents a movie theater with only one auditorium. Every Theater has a unique name.
 */
@Data
@EqualsAndHashCode(of = {"id"}) @ToString(includeFieldNames=false, of = {"name"})
@Table("theaters")
public class Theater {
	
	public Theater(String name) {
		this.name = name;
	}

	public static final byte THEATER_PARTITION_KEY = 1;

	@Column("partition_key")
	@PrimaryKeyColumn(type=PrimaryKeyType.PARTITIONED, ordinal = 0)
	private byte partitionKey = THEATER_PARTITION_KEY;

	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 2)
	@Column("theater_id")
	private UUID id = Uuids.timeBased();
	
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 1)
	@Size(min=1, max=256)
	@Column("name")
	private String name;
	
	@Size(min=1, max=20)
	@NotNull
	@Column("seats")
	@AccessType(AccessType.Type.PROPERTY)
	@Frozen
	private Map<Integer, SeatUDT> seats = new HashMap<>();
	
	public void setSeats(HashMap<Integer, SeatUDT> seats) {
		this.seats = (seats == null) ? new HashMap<>() : new HashMap<>(seats);
	}
}
