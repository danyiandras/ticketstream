package com.example.ticketstream.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.example.ticketstream.TicketStreamApplicationConfigurationProperties;
import com.example.ticketstream.model.Screening;
import com.example.ticketstream.repository.ScreeningRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScreeningService {

	private final ScreeningRepository screeningRepository;
	int createTicketRetry;
	
	public ScreeningService(
			ScreeningRepository screeningRepository, 
			TicketStreamApplicationConfigurationProperties config
	) {
		this.screeningRepository = screeningRepository;
		this.createTicketRetry = config.getCreateTicketRetry();
	}

	public Optional<Screening> getScreeningByStartAndEndAndId(LocalDateTime start, LocalDateTime end, UUID screeningId) {
		log.info("getScreeningByStartAndEndAndId({}, {}, {})", start, end, screeningId);
		return screeningRepository.findByStartYearMonthAndStartTimeAndEndTimeAndId(
				Screening.getYearMonth(start), start, end, screeningId);
	}

	public List<Screening> getPlayingMovies(LocalDateTime start, LocalDateTime end) {
		return getForAllYearMonth(start, end, yearMonth -> screeningRepository.findByStartTimeBetweenAndEndTimeBetween(
				yearMonth, start, end));
	}

	public List<Screening> getAllTheatersPlayingTheMovie(LocalDateTime start, LocalDateTime end, UUID movieId) {
		return getForAllYearMonth(start, end, yearMonth -> screeningRepository.findByMovieIdAndByStartTimeBetweenAndEndTimeBetween(
				yearMonth, start, end, movieId));
	}

	public List<Screening> getAllScreeningsOfMovieInTheaterPlaying(
			LocalDateTime start, LocalDateTime end,
			UUID movieId, UUID theaterId
	) {
		return getForAllYearMonth(start, end, yearMonth -> screeningRepository
				.findByTheaterIdAndByStartTimeBetweenAndEndTimeBetween(yearMonth, start, end, theaterId).stream()
				.filter(screening -> screening.getMovieId().equals(movieId))
				.toList());
	}
	
	public List<Screening> getForAllYearMonth(LocalDateTime start, LocalDateTime end, Function<Integer, List<Screening>> function) {
		return start.toLocalDate().datesUntil(end.toLocalDate().plusDays(1))
			.map(date -> Screening.getYearMonth(date))
			.map(i -> function.apply(i))
			.distinct()
			.collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
	}

	public void save(Screening screening) {
		screeningRepository.save(screening);
	}
	
	public Screening createTickets(Screening screening, Set<Integer> seatIds) {
		Screening currentScreening = screening;
		Set<Integer> currentSoldSeats = new HashSet<>(currentScreening.getSoldSeats());
		for (int i=0; i<this.createTicketRetry; i++) {
			try {
				return createTickets(i, currentScreening, seatIds);
			} catch (OptimisticLockingFailureException e) {
				currentScreening.setSoldSeats(currentSoldSeats);
				log.info("OptimisticLockingFailureException "+e.getMessage());
				if (i == this.createTicketRetry-1) {
					throw e;
				} else {
					currentScreening = this.getScreeningByStartAndEndAndId(
						screening.getStartTime(), screening.getEndTime(), screening.getId()).orElseThrow();
					currentSoldSeats = new HashSet<>(currentScreening.getSoldSeats());
				}
			}
		}
		throw new IllegalStateException("Should never reach this line.");
	}

	private Screening createTickets(int retryCount, Screening screening, Set<Integer> seatIds) {
		log.info("createTickets - retry "+retryCount);
		
		screening.sellSeats(seatIds);

		return screening = screeningRepository.save(screening);
	}
	
}
