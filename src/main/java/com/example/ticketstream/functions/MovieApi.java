package com.example.ticketstream.functions;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.ticketstream.functions.dto.InMovieTitle;
import com.example.ticketstream.functions.dto.MovieDto;
import com.example.ticketstream.services.MovieService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MovieApi {
	
	private final MovieService movieService;
	private final ModelMapper mapper;
	
	public MovieApi(MovieService movieService, ModelMapper mapper) {
		this.movieService = movieService;
		this.mapper = mapper;
	}

    @RabbitListener(queues = "movieGetByTitle.rpc.requests")
	public MovieDto movieGetByTitle(InMovieTitle movieTitle) {
		log.info("MovieGetByTitle processing title: {}", movieTitle.getTitle());
		var returnValue = MovieDto.convertToDto(movieService.getByTitle(movieTitle.getTitle()).orElseThrow(), mapper);
		log.info("MovieGetByTitle returning: {}", returnValue);
		return returnValue;
	}

}
