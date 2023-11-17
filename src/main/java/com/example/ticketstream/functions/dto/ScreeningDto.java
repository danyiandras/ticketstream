package com.example.ticketstream.functions.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import com.example.ticketstream.model.Screening;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScreeningDto implements EntityDto<Screening, ScreeningDto> {
	private static final long serialVersionUID = 1L;

	@NotNull
	LocalDateTime startTime;
	@NotNull
	LocalDateTime endTime;
	@NotNull
	UUID movieId;
	@Size(min=1, max=256)
	@NotNull
	String movieTitle;
	@NotNull
	UUID theaterId;
	@Size(min=1, max=256)
	@NotNull
	String theaterName;
	@NotNull
	UUID id;
	@NotNull
	Set<Integer> soldSeats;

//	public Screening convertToEntity(ScreeningService screeningService, ModelMapper mapper) {
//		return EntityDto.super.convertToEntity(screening -> 
//			screeningService.getScreening(screening.startTime, screening.endTime, screening.movieId, screening.theaterId, screening.id).orElseThrow(() -> 
//				new ResponseStatusException(HttpStatus.NOT_FOUND, "Screening id not found: "+this.getId())),
//			mapper);
//	}

	public static ScreeningDto convertToDto(Screening entity, ModelMapper mapper) {
	    return EntityDto.convertToDto(entity, ScreeningDto.class, mapper);
	}
}
