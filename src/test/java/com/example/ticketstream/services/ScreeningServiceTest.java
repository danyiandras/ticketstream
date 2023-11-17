package com.example.ticketstream.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.OptimisticLockingFailureException;

import com.example.ticketstream.TestConstants;
import com.example.ticketstream.TestTicketstreamApplication;
import com.example.ticketstream.model.Movie;
import com.example.ticketstream.model.Screening;
import com.example.ticketstream.model.Theater;

@SpringBootTest
@Import(TestTicketstreamApplication.class)
class ScreeningServiceTest {

	@Autowired
	ScreeningService screeningService;
	
	@Autowired
	MovieService movieService;
	
	@Autowired
	TheaterService theaterService;
	
	@Test
	void getScreeningByStartAndId() {
		List<Screening> screenings = screeningService.getPlayingMovies(TestConstants.searchStart, TestConstants.searchEnd);
		Optional<Screening> screeningOpt = screeningService.getScreeningByStartAndEndAndId(
				screenings.get(0).getStartTime(), 
				screenings.get(0).getEndTime(), 
				screenings.get(0).getId());
		assertTrue(screeningOpt.isPresent());
		assertEquals(screenings.get(0).getId(), screeningOpt.get().getId());
	}

	@Test
	void getAllPlayingMovies() {
		List<Screening> screenings = screeningService.getPlayingMovies(TestConstants.searchStart, TestConstants.searchEnd);
		assertEquals(8, screenings.size());
	}

	@Test
	void getAllTheatersPlayingTheMovie() {
		Optional<Movie> movie = movieService.getByTitle("The Godfather");
		List<Screening> screenings = 
				screeningService.getAllTheatersPlayingTheMovie(TestConstants.searchStart, TestConstants.searchEnd, movie.get().getId());
		assertEquals(2, screenings.size());
	}
	
	@Test
	void getAllScreeningsOfMovieInTheaterPlaying() {
		Optional<Movie> movie = movieService.getByTitle("The Godfather");
		Optional<Theater> theater = theaterService.getByName("Theater One");
		List<Screening> screenings = 
				screeningService.getAllScreeningsOfMovieInTheaterPlaying(
						TestConstants.searchStart, 
						TestConstants.searchEnd, 
						movie.get().getId(),
						theater.get().getId());
		assertEquals(1, screenings.size());
	}
	
	@Test
	void createTickets() {
		Optional<Movie> movie = movieService.getByTitle("The Godfather");
		Optional<Theater> theater = theaterService.getByName("Theater One");
		List<Screening> screenings = 
				screeningService.getAllScreeningsOfMovieInTheaterPlaying(
						TestConstants.searchStart, 
						TestConstants.searchEnd, 
						movie.get().getId(),
						theater.get().getId());
		assertEquals(1, screenings.size());
		Screening myScreening = screenings.get(0);
		int initialVersion = myScreening.getVersion();
		assertFalse(myScreening.isSeatSold(0));
		assertFalse(myScreening.isSeatSold(1));

		myScreening = screeningService.createTickets(myScreening, Set.of(0, 1));
		assertEquals(initialVersion+1, myScreening.getVersion());
		assertTrue(myScreening.isSeatSold(0));
		assertTrue(myScreening.isSeatSold(1));
		assertFalse(myScreening.isSeatSold(2));
		assertFalse(myScreening.isSeatSold(3));
	}
	
	@Test
	void createTickets_concurrent() {
		Optional<Movie> movie = movieService.getByTitle("The Godfather");
		Optional<Theater> theater = theaterService.getByName("Theater One");
		List<Screening> screenings = 
				screeningService.getAllScreeningsOfMovieInTheaterPlaying(
						TestConstants.searchStart, 
						TestConstants.searchEnd, 
						movie.get().getId(),
						theater.get().getId());
		assertEquals(1, screenings.size());

		Screening myScreening = screenings.get(0);
		Screening myScreeningCopy = new Screening(
			myScreening.getStartTime(),
			myScreening.getEndTime(),
			myScreening.getMovieId(),
			myScreening.getTheaterId(),
			myScreening.getTheater(),
			myScreening.getMovie()
		);
		myScreeningCopy.setVersion(myScreening.getVersion());
		myScreeningCopy.setId(myScreening.getId());

		int initialVersion = myScreening.getVersion();
		assertFalse(myScreening.isSeatSold(2));
		assertFalse(myScreening.isSeatSold(3));
		
		Screening modifiedScreening = screeningService.createTickets(myScreening, Set.of(2, 3));
		assertEquals(initialVersion+1, modifiedScreening.getVersion());
		assertTrue(modifiedScreening.isSeatSold(2));
		assertTrue(modifiedScreening.isSeatSold(3));
		
		int createTicketRetry = screeningService.createTicketRetry;
		screeningService.createTicketRetry = 1;
		assertThrows(OptimisticLockingFailureException.class, () -> screeningService.createTickets(myScreeningCopy, Set.of(4, 5)));
		screeningService.createTicketRetry = createTicketRetry;
		screeningService.createTickets(myScreeningCopy, Set.of(4, 5));

	}

}
