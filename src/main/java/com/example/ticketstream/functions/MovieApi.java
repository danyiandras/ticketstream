package com.example.ticketstream.functions;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.ticketstream.functions.dto.InMovieTitle;
import com.example.ticketstream.functions.dto.InTimeIntervallDto;
import com.example.ticketstream.functions.dto.MovieDto;
import com.example.ticketstream.services.MovieService;
import com.example.ticketstream.services.ScreeningService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MovieApi {
	
	private final MovieService movieService;
	private final ScreeningService screeninigService;
	private final ModelMapper mapper;
	
	public MovieApi(MovieService movieService, ScreeningService screeninigService, ModelMapper mapper) {
		this.movieService = movieService;
		this.screeninigService = screeninigService;
		this.mapper = mapper;
	}

    @RabbitListener(queues = "movieGetByTitle.rpc.requests")
	public MovieDto movieGetByTitle(InMovieTitle movieTitle) {
		log.info("MovieGetByTitle processing title: {}", movieTitle.getTitle());
		var returnValue = MovieDto.convertToDto(movieService.getByTitle(movieTitle.getTitle()).orElseThrow(), mapper);
		log.info("MovieGetByTitle returning: {}", returnValue);
		return returnValue;
	}

	@RabbitListener(queues = "movieGetMoviesPlaying.rpc.requests")
	public List<MovieDto> movieGetMoviesPlaying(InTimeIntervallDto timeIntervall) {
		log.info("screeningGetMoviesPlaying processing timeIntervall: {}", timeIntervall);
		var returnValue = screeninigService.getPlayingMovies(timeIntervall.getStart(), timeIntervall.getEnd()).stream()
				.map(screening -> new MovieDto(screening.getMovieId(), screening.getMovie().getTitle()) )
				.distinct()
				.toList();
		log.info("screeningGetMoviesPlaying returning: {}", returnValue);

		return returnValue;
	}


}
