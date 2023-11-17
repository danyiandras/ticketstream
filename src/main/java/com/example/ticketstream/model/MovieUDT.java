package com.example.ticketstream.model;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import jakarta.validation.constraints.Size;
import lombok.Data;

@UserDefinedType
@Data
public class MovieUDT implements MappedUDT<Movie, MovieUDT> {

	@Size(min=1, max=256)
	private String title;

}
