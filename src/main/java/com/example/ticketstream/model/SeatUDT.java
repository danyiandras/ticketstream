package com.example.ticketstream.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@UserDefinedType("seat")
@Data @RequiredArgsConstructor
public class SeatUDT {
	
	public SeatUDT(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Column("seat_id")
	private int id;

	@Size(min=1, max=256)
	@Column("name")
	private String name;

}
