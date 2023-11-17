package com.example.ticketstream.functions.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class InTicketOrderDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	LocalDateTime startTime;
	@NotNull
	LocalDateTime endTime;
	@NotNull
	UUID screeningId;
	
	@NotEmpty
	Set<Integer> seatIds;

}
