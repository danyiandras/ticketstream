package com.example.ticketstream.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.ticketstream.model.Movie;
import com.example.ticketstream.repository.MovieRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Service
public class MovieService {

	private final MovieRepository movieRepository;
	
	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public List<Movie> getAll() {
		return movieRepository.findByPartitionKey(Movie.MOVIE_PARTITION_KEY);
	}

	public Optional<Movie> getById(@NotNull UUID id) {
		return movieRepository.findByPartitionKeyAndId(Movie.MOVIE_PARTITION_KEY, id);
	}

	public Optional<Movie> getByTitle(@Size(min=1) String title) {
		return movieRepository.findByPartitionKeyAndTitle(Movie.MOVIE_PARTITION_KEY, title);
	}

	public Movie save(Movie movie) {
		return movieRepository.save(movie);
	}

}
