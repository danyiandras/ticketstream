package com.example.ticketstream.functions.dto;

import java.util.Map;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.ticketstream.model.Theater;
import com.example.ticketstream.services.TheaterService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class TheaterDto implements EntityDto<Theater, TheaterDto> {
	private static final long serialVersionUID = 1L;

	public TheaterDto(UUID id, String name) {
		this.id = id;
		this.name = name;
	}
	
	@NotNull
	UUID id;
	@Size(min=1, max=256)
	@NotNull
	String name;

	private Map<Integer, SeatDto> seats;

	public Theater convertToEntity(TheaterService theaterService, ModelMapper mapper) {
		return EntityDto.super.convertToEntity(id -> 
			theaterService.getById(this.id).orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Theater id not found: "+this.getId())), 
			mapper);
	}

	public static TheaterDto convertToDto(Theater entity, ModelMapper mapper) {
	    return EntityDto.convertToDto(entity, TheaterDto.class, mapper);
	}

}
