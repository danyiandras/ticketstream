package com.example.ticketstream.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.ticketstream.model.Movie;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public interface MovieRepository extends CrudRepository<Movie, UUID> {
	
	Optional<Movie> findByPartitionKeyAndId(byte moviePartitionKey, @NotNull UUID id);

	Optional<Movie> findByPartitionKeyAndTitle(byte partitionKey, @Size(min=1) String title);

	List<Movie> findByPartitionKey(byte theaterPartitionKey);

}
