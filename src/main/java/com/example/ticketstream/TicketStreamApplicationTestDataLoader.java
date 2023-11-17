package com.example.ticketstream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.ticketstream.model.Licence;
import com.example.ticketstream.model.Movie;
import com.example.ticketstream.model.MovieUDT;
import com.example.ticketstream.model.Screening;
import com.example.ticketstream.model.SeatUDT;
import com.example.ticketstream.model.Theater;
import com.example.ticketstream.model.TheaterUDT;
import com.example.ticketstream.services.LicenceService;
import com.example.ticketstream.services.MovieService;
import com.example.ticketstream.services.ScreeningService;
import com.example.ticketstream.services.TheaterService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TicketStreamApplicationTestDataLoader implements ApplicationRunner {

	private final MovieService movieService;
	private final TheaterService theaterService;
	private final LicenceService licenceService;
	private final ScreeningService screeningService;
	private final ModelMapper modelMapper;
	
	public TicketStreamApplicationTestDataLoader(
			MovieService movieService, 
			TheaterService theaterService,
			LicenceService licenceService,
			ScreeningService screeningService,
			ModelMapper modelMapper
	){
		this.movieService = movieService;
		this.theaterService = theaterService;
		this.licenceService = licenceService;
		this.screeningService = screeningService;
		this.modelMapper = modelMapper;
	}
	

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (movieService.getAll().size() > 0) {
			log.info("Init test data skipped.");
			return;
		}
		
		log.info("Init Movies.");
		Movie theGodFather = movieService.save(new Movie("The Godfather"));
		Movie theDeerHunter = movieService.save(new Movie("The Deer Hunter"));
		Movie ragingBull = movieService.save(new Movie("Raging Bull"));
		Movie taxiDriver = movieService.save(new Movie("Taxi Driver"));
		Movie casino = movieService.save(new Movie("Casino"));
		Movie theIntern = movieService.save(new Movie("The Intern"));
		
		
		Map<UUID, Integer> movieStarts = Map.of(
				theGodFather.getId(), 16,
				theDeerHunter.getId(), 19,
				ragingBull.getId(), 21,
				taxiDriver.getId(), 16,
				casino.getId(), 19,
				theIntern.getId(), 21
				);

		Map<UUID, Movie> allMovies = movieService.getAll().stream()
			.collect(Collectors.toMap(Movie::getId, m -> m));
		
		log.info("Init Theaters.");
		Theater one = new Theater("Theater One");
		Theater two = new Theater("Theater Two");
		Theater three = new Theater("Theater Three");
		Theater four = new Theater("Theater Four");

		Map<UUID, Theater> allTheaters = List.of(one, two, three, four).stream()
				.collect(Collectors.toMap(Theater::getId, t -> t));
		log.info("Init Seats.");
		allTheaters.values().stream()
			.forEach(theater -> {
				IntStream.range(0, 20).forEach(i -> {
					theater.getSeats().put(i, new SeatUDT(i, "Seat %d".formatted(i)));
				});
			});

		allTheaters = allTheaters.values().stream()
				.map(theater -> theaterService.save(theater))
				.collect(Collectors.toMap(Theater::getId, t -> t));

		log.info("Init Licences.");
		Stream.of(one, two).forEach(theater -> {
			Stream.of(theGodFather, theDeerHunter, ragingBull).forEach(movie -> {
				licenceService.save(new Licence(theater.getId(), movie.getId(), LocalDateTime.now(), LocalDateTime.now().plusMonths(2)));
			});
			Stream.of(taxiDriver, casino, theIntern).forEach(movie -> {
				licenceService.save(new Licence(theater.getId(), movie.getId(), LocalDateTime.now().plusMonths(2), LocalDateTime.now().plusMonths(4)));
			});
		});
		Stream.of(three, four).forEach(theater -> {
			Stream.of(theGodFather, theDeerHunter, ragingBull).forEach(movie -> {
				licenceService.save(new Licence(theater.getId(), movie.getId(), LocalDateTime.now().plusMonths(2), LocalDateTime.now().plusMonths(4)));
			});
			Stream.of(taxiDriver, casino, theIntern).forEach(movie -> {
				licenceService.save(new Licence(theater.getId(), movie.getId(), LocalDateTime.now(), LocalDateTime.now().plusMonths(2)));
			});
		});

		log.info("Init Screenings.");
		theaterService.getAll().stream()
			.forEach(theater -> {
				licenceService.getByTheater(theater).stream().forEach(licence -> {
					licence.getStartDate().toLocalDate().datesUntil(licence.getEndDate().toLocalDate()).forEach(date -> {
						LocalDateTime movieStart = date.atTime(movieStarts.get(licence.getMovieId()), 0);
						TheaterUDT theaterUDT = modelMapper.map(theater, TheaterUDT.class);
						MovieUDT movieUDT = modelMapper.map(allMovies.get(licence.getMovieId()), MovieUDT.class);
						screeningService.save(
								new Screening(
										movieStart, 
										movieStart.plusHours(3),
										licence.getMovieId(), 
										theater.getId(),
										theaterUDT,
										movieUDT
										)
							);
					});
				});
			});

	}

}
