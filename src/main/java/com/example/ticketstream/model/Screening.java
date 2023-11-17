package com.example.ticketstream.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Version;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Frozen;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.datastax.oss.driver.api.core.uuid.Uuids;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The Screening class represents a show of a Movie in a Theater in a certain time interval.
 * Two Screenings in a Theater can not overlap in time.
 */
@Data
@EqualsAndHashCode(of = {"id"}) @ToString(includeFieldNames=false, of = {"theater", "movie", "startTime", "endTime"})
@Table
public class Screening {
	
	public Screening(
			LocalDateTime startTime, 
			LocalDateTime endTime, 
			UUID movieId,
			UUID theaterId, 
			TheaterUDT theater, 
			MovieUDT movie
		) {
			this.startYearMonth = getYearMonth(startTime);
			this.startTime = startTime;
			this.endTime = endTime;
			this.movieId = movieId;
			this.theaterId = theaterId;
			this.theater = theater;
			this.movie = movie;
		}

	public static int getYearMonth(LocalDateTime startTime) {
		return startTime.getYear()*100 + startTime.getMonthValue();
	}

	public static int getYearMonth(LocalDate startDate) {
		return startDate.getYear()*100 + startDate.getMonthValue();
	}

	@PrimaryKeyColumn(name="start_year_month", type=PrimaryKeyType.PARTITIONED, ordinal = 0)
	private int startYearMonth;

	@PrimaryKeyColumn(name="start_time", type=PrimaryKeyType.CLUSTERED, ordinal = 1)
	private LocalDateTime startTime;

	@PrimaryKeyColumn(name="end_time", type=PrimaryKeyType.CLUSTERED, ordinal = 2)
	private LocalDateTime endTime;

	@PrimaryKeyColumn(name="screening_id", type=PrimaryKeyType.CLUSTERED, ordinal = 3)
	private UUID id = Uuids.timeBased();
	
	@Indexed
	@Column("movie_id")
	private UUID movieId;

	@Indexed
	@Column("theater_id")
	private UUID theaterId;

	@Column("version")
	@Version
	int version;
	
	@Column("theater")
	private TheaterUDT theater; 
	
	@Column("movie")
	private MovieUDT movie; 
	
	@NotNull
	@Size(min=1, max=20)
	@AccessType(AccessType.Type.PROPERTY)
	@Column("sold_seats")
	@Frozen
	private Set<Integer> soldSeats = new HashSet<>(); 

	public void setSoldSeats(Set<Integer> soldSeats) {
		this.soldSeats = (soldSeats == null) ? new HashSet<>() : new HashSet<>(soldSeats);
	}

	public void sellSeats(Set<Integer> seatIds) {
		Set<Integer> alreadySold = new HashSet<>(seatIds);
		alreadySold.retainAll(this.soldSeats);
		if (! alreadySold.isEmpty()) {
			throw new IllegalArgumentException("Some seats are not available: "+alreadySold);
		}
		this.soldSeats.addAll(seatIds);
	}
	
	public boolean isSeatSold(int seatId) {
		return this.soldSeats.contains(seatId);
	}
}
