package com.example.ticketstream.functions;

import static com.example.ticketstream.TestConstants.searchEnd;
import static com.example.ticketstream.TestConstants.searchStart;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.ticketstream.TestTicketstreamApplication;
import com.example.ticketstream.functions.dto.InMovieTitle;
import com.example.ticketstream.functions.dto.InTheaterName;
import com.example.ticketstream.functions.dto.InTicketOrderDto;
import com.example.ticketstream.functions.dto.InTimeIntervallDto;
import com.example.ticketstream.functions.dto.InTimeIntervallMovieIdDto;
import com.example.ticketstream.functions.dto.InTimeIntervallMovieIdTheaterIdDto;
import com.example.ticketstream.functions.dto.MovieDto;
import com.example.ticketstream.functions.dto.ScreeningDto;
import com.example.ticketstream.functions.dto.SeatDto;
import com.example.ticketstream.functions.dto.TheaterDto;
import com.example.ticketstream.functions.dto.TicketDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Import(TestTicketstreamApplication.class)
class ApiTest {

    static {
        System.setProperty("spring.amqp.deserialization.trust.all", "true");
    }

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	void movieGetByTitle() {
		InMovieTitle message = InMovieTitle.builder().title("The Godfather").build();
	    MovieDto movie = (MovieDto) rabbitTemplate.convertSendAndReceive("movieGetByTitle.rpc", "rpc", message);
	    log.info("TEST >> movieGetByTitle: {}", movie);
	    assertEquals("The Godfather", movie.getTitle());
	}

	@Test
	void theaterGetByName() {
		InTheaterName message = InTheaterName.builder().name("Theater One").build();
	    TheaterDto theater = (TheaterDto) rabbitTemplate.convertSendAndReceive("theaterGetByName.rpc", "rpc", message);
	    log.info("TEST >> theaterGetByName: {}", theater);
	    assertEquals("Theater One", theater.getName());
	}

	@Test
	void screeningGetMoviesPlaying() {
		InTimeIntervallDto message = InTimeIntervallDto.builder().start(searchStart).end(searchEnd).build();
	    @SuppressWarnings("unchecked")
		List<ScreeningDto> screenings = (List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetMoviesPlaying.rpc", "rpc", message);
	    log.info("TEST >> screeningGetMoviesPlaying: {}", screenings);
	}

	@Test
	void screeningGetTheatersPlayingMovie() {
		InMovieTitle movieMessage = InMovieTitle.builder().title("The Godfather").build();
	    MovieDto movie = (MovieDto) rabbitTemplate.convertSendAndReceive("movieGetByTitle.rpc", "rpc", movieMessage);
		InTimeIntervallMovieIdDto message = InTimeIntervallMovieIdDto.builder().start(searchStart).end(searchEnd).movieId(movie.getId()).build();
	    @SuppressWarnings("unchecked")
		List<ScreeningDto> screenings = (List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetTheatersPlayingMovie.rpc", "rpc", message);
	    log.info("TEST >> screeningGetTheatersPlayingMovie: {}", screenings);
	}

	@Test
	void screeningGetScreeningsPlayingMovieInTheater() {
		InMovieTitle movieMessage = InMovieTitle.builder().title("The Godfather").build();
	    MovieDto movie = (MovieDto) rabbitTemplate.convertSendAndReceive("movieGetByTitle.rpc", "rpc", movieMessage);

	    InTheaterName theaterMessage = InTheaterName.builder().name("Theater One").build();
	    TheaterDto theater = (TheaterDto) rabbitTemplate.convertSendAndReceive("theaterGetByName.rpc", "rpc", theaterMessage);

	    InTimeIntervallMovieIdTheaterIdDto message = InTimeIntervallMovieIdTheaterIdDto.builder()
	    		.start(searchStart).end(searchEnd).movieId(movie.getId()).theaterId(theater.getId()).build();
	    @SuppressWarnings("unchecked")
		List<ScreeningDto> screenings = (List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetScreeningsPlayingMovieInTheater.rpc", "rpc", message);
	    log.info("TEST >> screeningGetScreeningsPlayingMovieInTheater: {}", screenings);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void screeningBuyTickets() {
		String movieIWantToSee = "The Godfather";
		String theaterIWantToGo = "Theater One";

	    InTimeIntervallDto screeningGetMoviesPlayingMessage = InTimeIntervallDto.builder().start(searchStart).end(searchEnd).build();
		List<ScreeningDto> screenings = 
				(List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetMoviesPlaying.rpc", "rpc", screeningGetMoviesPlayingMessage);
	    
	    var movieId = 
	    		screenings.stream().filter(s -> movieIWantToSee.equals(s.getMovieTitle())).findAny().orElseThrow().getMovieId();

		InTimeIntervallMovieIdDto screeningGetTheatersPlayingMovieMessage = 
				InTimeIntervallMovieIdDto.builder().start(searchStart).end(searchEnd).movieId(movieId).build();
		screenings = 
				(List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetTheatersPlayingMovie.rpc", "rpc", screeningGetTheatersPlayingMovieMessage);

	    var theaterId = 
	    		screenings.stream().filter(s -> theaterIWantToGo.equals(s.getTheaterName())).findAny().orElseThrow().getTheaterId();

	    InTimeIntervallMovieIdTheaterIdDto message = InTimeIntervallMovieIdTheaterIdDto.builder()
	    		.start(searchStart).end(searchEnd).movieId(movieId).theaterId(theaterId).build();
		screenings = (List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetScreeningsPlayingMovieInTheater.rpc", "rpc", message);
		
		ScreeningDto screeningIWantToGo = screenings.stream().findAny().orElseThrow();
		final Set<Integer> soldSeats = screeningIWantToGo.getSoldSeats();
		
	    InTheaterName theaterMessage = InTheaterName.builder().name("Theater One").build();
	    TheaterDto theater = (TheaterDto) rabbitTemplate.convertSendAndReceive("theaterGetByName.rpc", "rpc", theaterMessage);
		
	    Map<Integer, SeatDto> freeSeats = theater.getSeats().entrySet().stream()
	    	.filter(e -> !soldSeats.contains(e.getKey()))
	    	.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	    
	    SeatDto seatIWantToSit = freeSeats.get(19);
	    
	    InTicketOrderDto ticketOrder = InTicketOrderDto.builder()
	    		.startTime(screeningIWantToGo.getStartTime())
	    		.endTime(screeningIWantToGo.getEndTime())
	    		.screeningId(screeningIWantToGo.getId())
	    		.seatIds(Set.of(seatIWantToSit.getId()))
	    		.build();
	    List<TicketDto> myTickets = (List<TicketDto>) rabbitTemplate.convertSendAndReceive("screeningBuyTickets.rpc", "rpc", ticketOrder);
	    log.info("TEST >> screeningBuyTickets: {}", myTickets);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	void buyTickets() {
		String movieIWantToSee = "The Godfather";
		String theaterIWantToGo = "Theater One";

	    InTimeIntervallDto movieGetMoviesPlayingMessage = InTimeIntervallDto.builder().start(searchStart).end(searchEnd).build();
		List<MovieDto> movies = 
				(List<MovieDto>) rabbitTemplate.convertSendAndReceive("movieGetMoviesPlaying.rpc", "rpc", movieGetMoviesPlayingMessage);
	    
	    var movieId = 
	    		movies.stream().filter(m -> movieIWantToSee.equals(m.getTitle())).findAny().orElseThrow().getId();

		InTimeIntervallMovieIdDto theaterGetTheatersPlayingMovieMessage = 
				InTimeIntervallMovieIdDto.builder().start(searchStart).end(searchEnd).movieId(movieId).build();
		List<TheaterDto> theaters = (List<TheaterDto>) rabbitTemplate.convertSendAndReceive("theaterGetTheatersPlayingMovie.rpc", "rpc", theaterGetTheatersPlayingMovieMessage);

	    var theaterId = 
	    		theaters.stream().filter(t -> theaterIWantToGo.equals(t.getName())).findAny().orElseThrow().getId();

	    InTimeIntervallMovieIdTheaterIdDto message = InTimeIntervallMovieIdTheaterIdDto.builder()
	    		.start(searchStart).end(searchEnd).movieId(movieId).theaterId(theaterId).build();
		List<ScreeningDto> screenings = (List<ScreeningDto>) rabbitTemplate.convertSendAndReceive("screeningGetScreeningsPlayingMovieInTheater.rpc", "rpc", message);
		
		ScreeningDto screeningIWantToGo = screenings.stream().findAny().orElseThrow();
		final Set<Integer> soldSeats = screeningIWantToGo.getSoldSeats();
		
	    InTheaterName theaterMessage = InTheaterName.builder().name("Theater One").build();
	    TheaterDto theater = (TheaterDto) rabbitTemplate.convertSendAndReceive("theaterGetByName.rpc", "rpc", theaterMessage);
		
	    Map<Integer, SeatDto> freeSeats = theater.getSeats().entrySet().stream()
	    	.filter(e -> !soldSeats.contains(e.getKey()))
	    	.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	    
	    SeatDto seatIWantToSit = freeSeats.get(15);
	    
	    InTicketOrderDto ticketOrder = InTicketOrderDto.builder()
	    		.startTime(screeningIWantToGo.getStartTime())
	    		.endTime(screeningIWantToGo.getEndTime())
	    		.screeningId(screeningIWantToGo.getId())
	    		.seatIds(Set.of(seatIWantToSit.getId()))
	    		.build();
	    List<TicketDto> myTickets = (List<TicketDto>) rabbitTemplate.convertSendAndReceive("screeningBuyTickets.rpc", "rpc", ticketOrder);
	    log.info("TEST >> screeningBuyTickets: {}", myTickets);
	}
}
