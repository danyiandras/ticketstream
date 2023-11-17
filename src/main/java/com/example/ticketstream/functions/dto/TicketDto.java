package com.example.ticketstream.functions.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	int seatId;
	@Size(min=1, max=256)
	@NotNull
	String seatName;

	@NotNull
	UUID screeningId;
	@Size(min=1, max=256)
	@NotNull
	String screeningMovieTitle;
	@Size(min=1, max=256)
	@NotNull
	String screeningTheaterName;
	@NotNull
	LocalDateTime screeningStartTime;
	@NotNull
	LocalDateTime screeningEndTime;

}
