package com.example.ticketstream.functions;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.ticketstream.functions.dto.InTicketOrderDto;
import com.example.ticketstream.functions.dto.InTimeIntervallDto;
import com.example.ticketstream.functions.dto.InTimeIntervallMovieIdDto;
import com.example.ticketstream.functions.dto.InTimeIntervallMovieIdTheaterIdDto;
import com.example.ticketstream.functions.dto.ScreeningDto;
import com.example.ticketstream.functions.dto.TicketDto;
import com.example.ticketstream.model.Screening;
import com.example.ticketstream.model.Theater;
import com.example.ticketstream.services.ScreeningService;
import com.example.ticketstream.services.TheaterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ScreeningApi {
	
	private final ScreeningService screeninigService;
	private final TheaterService theaterService;
	private final ModelMapper mapper;
	
	public ScreeningApi(ScreeningService screeninigService, TheaterService theaterService, ModelMapper mapper) {
		this.screeninigService = screeninigService;
		this.theaterService = theaterService;
		this.mapper = mapper;
	}

	@RabbitListener(queues = "screeningGetMoviesPlaying.rpc.requests")
	public List<ScreeningDto> screeningGetMoviesPlaying(InTimeIntervallDto timeIntervall) {
		log.info("screeningGetMoviesPlaying processing timeIntervall: {}", timeIntervall);
		var returnValue = screeninigService.getPlayingMovies(timeIntervall.getStart(), timeIntervall.getEnd()).stream()
				.map(screening -> ScreeningDto.convertToDto(screening, mapper) )
				.toList();
		log.info("screeningGetMoviesPlaying returning: {}", returnValue);

		return returnValue;
	}

	@RabbitListener(queues = "screeningGetTheatersPlayingMovie.rpc.requests")
	public List<ScreeningDto> screeningGetTheatersPlayingMovie(InTimeIntervallMovieIdDto timeIntervallMovieId) {
		log.info("screeningGetTheatersPlayingMovie processing timeIntervallMovieId: {}", timeIntervallMovieId);
		var returnValue = screeninigService.getAllTheatersPlayingTheMovie(
				timeIntervallMovieId.getStart(), timeIntervallMovieId.getEnd(), timeIntervallMovieId.getMovieId())
				.stream()
				.map(screening -> ScreeningDto.convertToDto(screening, mapper) )
				.toList();
		log.info("screeningGetTheatersPlayingMovie returning: {}", returnValue);

		return returnValue;
	}
	
	@RabbitListener(queues = "screeningGetScreeningsPlayingMovieInTheater.rpc.requests")
	public List<ScreeningDto> screeningGetScreeningsPlayingMovieInTheater(InTimeIntervallMovieIdTheaterIdDto timeIntervallMovieIdTheaterId) {
		log.info("screeningGetScreeningsPlayingMovieInTheater processing timeIntervallMovieIdTheaterId: {}", timeIntervallMovieIdTheaterId);
		var returnValue = screeninigService.getAllScreeningsOfMovieInTheaterPlaying(
				timeIntervallMovieIdTheaterId.getStart(), 
				timeIntervallMovieIdTheaterId.getEnd(),
				timeIntervallMovieIdTheaterId.getMovieId(), 
				timeIntervallMovieIdTheaterId.getTheaterId()
				).stream()
				.map(screening -> ScreeningDto.convertToDto(screening, mapper) )
				.toList();
		log.info("screeningGetScreeningsPlayingMovieInTheater returning: {}", returnValue);

		return returnValue;
	}


	@RabbitListener(queues = "screeningBuyTickets.rpc.requests")
	public List<TicketDto> screeningBuyTickets(InTicketOrderDto ticketOrderDto) {
		Screening screening = screeninigService.getScreeningByStartAndEndAndId(
				ticketOrderDto.getStartTime(), ticketOrderDto.getEndTime(), ticketOrderDto.getScreeningId())
				.orElseThrow();
		Theater theater = theaterService.getByName(screening.getTheater().getName()).orElseThrow();

		screeninigService.createTickets(screening, ticketOrderDto.getSeatIds());
		return ticketOrderDto.getSeatIds().stream()
				.<TicketDto>map(id -> TicketDto.builder()
						.seatId(id)
						.seatName(theater.getSeats().get(id).getName())
						.screeningStartTime(screening.getStartTime())
						.screeningEndTime(screening.getEndTime())
						.screeningId(screening.getId())
						.screeningMovieTitle(screening.getMovie().getTitle())
						.screeningTheaterName(screening.getTheater().getName())
						.build()
						)
				.toList();
	}


}
