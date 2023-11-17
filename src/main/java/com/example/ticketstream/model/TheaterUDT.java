package com.example.ticketstream.model;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import jakarta.validation.constraints.Size;
import lombok.Data;

@UserDefinedType("THEATER")
@Data
public class TheaterUDT implements MappedUDT<Theater, TheaterUDT> {
	
	@Size(min=1, max=256)
	@Column("SOLD")
	private String name;

}
