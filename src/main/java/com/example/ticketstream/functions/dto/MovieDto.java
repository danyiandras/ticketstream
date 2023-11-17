package com.example.ticketstream.functions.dto;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.ticketstream.model.Movie;
import com.example.ticketstream.services.MovieService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class MovieDto implements EntityDto<Movie, MovieDto> {

	private static final long serialVersionUID = 1L;

	@NotNull
	UUID id;

	@NotNull
	@Size(min=1, max=256)
	String title;

	public Movie convertToEntity(MovieService movieService, ModelMapper mapper) {
		return EntityDto.super.convertToEntity(movie -> 
			movieService.getById(movie.id).orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie id not found: "+movie.getId())), 
			mapper);
	}

	public static MovieDto convertToDto(Movie entity, ModelMapper mapper) {
	    return EntityDto.convertToDto(entity, MovieDto.class, mapper);
	}
	
}
