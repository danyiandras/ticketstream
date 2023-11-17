package com.example.ticketstream.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ticketstream.model.Licence;
import com.example.ticketstream.model.Theater;
import com.example.ticketstream.repository.LicenceRepository;

@Service
public class LicenceService {

	private final LicenceRepository licenceRepository;
	
	public LicenceService(LicenceRepository licenceRepository) {
		this.licenceRepository = licenceRepository;
	}

	public List<Licence> getByTheater(Theater theater) {
		return this.licenceRepository.findByTheaterId(theater.getId());
	}

	public Licence save(Licence licence) {
		return this.licenceRepository.save(licence);
	}

}
