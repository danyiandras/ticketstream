package com.example.ticketstream.functions;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.ticketstream.functions.dto.InTheaterName;
import com.example.ticketstream.functions.dto.InTimeIntervallMovieIdDto;
import com.example.ticketstream.functions.dto.TheaterDto;
import com.example.ticketstream.services.ScreeningService;
import com.example.ticketstream.services.TheaterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TheaterApi {
	
	private final TheaterService theaterService;
	private final ScreeningService screeninigService;
	private final ModelMapper mapper;
	
	public TheaterApi(TheaterService theaterService, ScreeningService screeninigService, ModelMapper mapper) {
		this.theaterService = theaterService;
		this.screeninigService = screeninigService;
		this.mapper = mapper;
	}

	@RabbitListener(queues = "theaterGetByName.rpc.requests")
	public TheaterDto theaterGetByName(InTheaterName theaterName) {
		log.info("TheaterGetByName processing name: {}", theaterName);
		var returnValue = TheaterDto.convertToDto(theaterService.getByName(theaterName.getName()).orElseThrow(), mapper);
		log.info("TheaterGetByName returning: {}", returnValue);
		return returnValue;
	}

	@RabbitListener(queues = "theaterGetTheatersPlayingMovie.rpc.requests")
	public List<TheaterDto> theaterGetTheatersPlayingMovie(InTimeIntervallMovieIdDto timeIntervallMovieId) {
		log.info("screeningGetTheatersPlayingMovie processing timeIntervallMovieId: {}", timeIntervallMovieId);
		var returnValue = screeninigService.getAllTheatersPlayingTheMovie(
				timeIntervallMovieId.getStart(), timeIntervallMovieId.getEnd(), timeIntervallMovieId.getMovieId())
				.stream()
				.map(screening -> new TheaterDto(screening.getTheaterId(), screening.getTheater().getName()) )
				.distinct()
				.toList();
		log.info("screeningGetTheatersPlayingMovie returning: {}", returnValue);

		return returnValue;
	}
	
}
