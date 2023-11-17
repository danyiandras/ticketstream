package com.example.ticketstream.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.ticketstream.model.Licence;

public interface LicenceRepository extends CrudRepository<Licence, UUID> {

	List<Licence> findByTheaterId(UUID theaterId);

}
