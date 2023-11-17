package com.example.ticketstream.model;

import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The Movie class represents a movie with a unique title.
 */
@Data @NoArgsConstructor
@EqualsAndHashCode(of = {"id"}) @ToString(includeFieldNames=false, of = {"title"})
@Table("MOVIES")
public class Movie {
	
	public Movie(String title) {
		this.title = title;
	}
	
	public static final byte MOVIE_PARTITION_KEY = 1;

	@Column("PARTITION_KEY")
	@PrimaryKeyColumn(type=PrimaryKeyType.PARTITIONED, ordinal = 0)
	private byte partitionKey = MOVIE_PARTITION_KEY;

	@Size(min=1, max=256)
	@Column("TITLE")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 1)
	private String title;

	@Column("MOVIE_ID")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 2)
	private UUID id = Uuids.timeBased();
	
}
