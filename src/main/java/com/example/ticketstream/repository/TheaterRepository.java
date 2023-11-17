package com.example.ticketstream.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.ticketstream.model.Theater;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public interface TheaterRepository extends CrudRepository<Theater, UUID> {

	List<Theater> findByPartitionKey(byte theaterPartitionKey);

	Optional<Theater> findByPartitionKeyAndId(byte theaterPartitionKey, @NotNull UUID id);

	Optional<Theater> findByPartitionKeyAndName(byte partitionKey, @Size(min=1) String name);

}
