package com.example.ticketstream.functions.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class InTimeIntervallMovieIdDto implements Serializable {
	private static final long serialVersionUID = 1L;

	LocalDateTime start;
	LocalDateTime end;
	UUID movieId;
}
