package com.example.ticketstream.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The Licence class represents the time interval a Theater is allowed to show a Movie.
 */
@Data
@EqualsAndHashCode(of = {"id"}) @ToString(includeFieldNames=false, of = {"theaterId", "movieId", "startDate", "endDate"})
@Table("LICENCES")
public class Licence {

	public Licence(UUID theaterId, UUID movieId, LocalDateTime startDate, LocalDateTime endDate) {
		this.theaterId = theaterId;
		this.movieId = movieId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Column("THEATER_ID")
	@PrimaryKeyColumn(type=PrimaryKeyType.PARTITIONED, ordinal = 0)
	private UUID theaterId;

	@Column("MOVIE_ID")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 1)
	private UUID movieId;

	@Column("START_TIME")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 2)
	private LocalDateTime startDate;
	
	@Column("END_TIME")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 3)
	private LocalDateTime endDate;

	@Column("LICENCE_ID")
	@PrimaryKeyColumn(type=PrimaryKeyType.CLUSTERED, ordinal = 4)
	private UUID id = Uuids.timeBased();

}
