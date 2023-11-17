package com.example.ticketstream.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.ticketstream.TestTicketstreamApplication;
import com.example.ticketstream.model.Movie;

@SpringBootTest
@Import(TestTicketstreamApplication.class)
class MovieServiceTest {

	@Autowired
	MovieService movieService;

	@Test
	void getAll() {
		List<Movie> movies = movieService.getAll();
		assertEquals(6, movies.size());
	}

	@Test
	void getByTitle() {
		Optional<Movie> movie = movieService.getByTitle("The Godfather");
		assertTrue(movie.isPresent());
		assertEquals("The Godfather", movie.get().getTitle());
	}

}
