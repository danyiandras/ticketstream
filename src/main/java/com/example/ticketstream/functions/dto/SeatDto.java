package com.example.ticketstream.functions.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SeatDto implements Serializable {
	private static final long serialVersionUID = 1L;

	@NotNull
	int id;
	@Size(min=1, max=256)
	@NotNull
	String name;
}
