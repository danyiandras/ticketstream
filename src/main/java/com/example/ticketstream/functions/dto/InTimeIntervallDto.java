package com.example.ticketstream.functions.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InTimeIntervallDto implements Serializable {
	private static final long serialVersionUID = 1L;

	LocalDateTime start;
	LocalDateTime end;
}
