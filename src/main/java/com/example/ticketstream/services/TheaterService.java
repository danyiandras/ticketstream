package com.example.ticketstream.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.ticketstream.model.Theater;
import com.example.ticketstream.repository.TheaterRepository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Service
public class TheaterService {

	private final TheaterRepository theaterRepository;
	
	public TheaterService(TheaterRepository theaterRepository) {
		this.theaterRepository = theaterRepository;
	}

	public List<Theater> getAll() {
		return theaterRepository.findByPartitionKey(Theater.THEATER_PARTITION_KEY);
	}

	public Optional<Theater> getById(@NotNull UUID id) {
		return theaterRepository.findByPartitionKeyAndId(Theater.THEATER_PARTITION_KEY, id);
	}

	public Optional<Theater> getByName(@Size(min=1) String name) {
		return theaterRepository.findByPartitionKeyAndName(Theater.THEATER_PARTITION_KEY, name);
	}

	public Theater save(Theater theater) {
		return theaterRepository.save(theater);
	}

}
