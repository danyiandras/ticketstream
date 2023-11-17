package com.example.ticketstream.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.ticketstream.TestTicketstreamApplication;
import com.example.ticketstream.model.Theater;

@SpringBootTest
@Import(TestTicketstreamApplication.class)
class TheaterServiceTest {

	@Autowired
	TheaterService theaterService;

	@Test
	void getAll() {
		List<Theater> movies = theaterService.getAll();
		assertEquals(4, movies.size());
	}

	@Test
	void getByName() {
		Optional<Theater> theater = theaterService.getByName("Theater One");
		assertTrue(theater.isPresent());
		assertEquals("Theater One", theater.get().getName());
		assertNotNull(theater.get().getSeats());
		assertEquals(20, theater.get().getSeats().size());
	}

	@Test
	void emptySeatsMap() {
		Optional<Theater> theaterOpt = theaterService.getByName("Theater Two");
		assertTrue(theaterOpt.isPresent());
		Theater theater = theaterOpt.orElseThrow();
		assertEquals("Theater Two", theater.getName());
		assertNotNull(theater.getSeats());
		assertEquals(20, theater.getSeats().size());
		
		theater.setSeats(null);
		assertNotNull(theater.getSeats());
		assertTrue(theater.getSeats().isEmpty());
		
		theaterService.save(theater);
		theaterOpt = theaterService.getByName("Theater Two");
		Theater theater2 = theaterOpt.get();
		assertNotNull(theater2.getSeats());
		assertTrue(theater2.getSeats().isEmpty());
	}

}
