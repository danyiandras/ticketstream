package com.example.ticketstream.functions;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.ticketstream.functions.dto.InTheaterName;
import com.example.ticketstream.functions.dto.TheaterDto;
import com.example.ticketstream.services.TheaterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TheaterApi {
	
	private final TheaterService theaterService;
	private final ModelMapper mapper;
	
	public TheaterApi(TheaterService theaterService, ModelMapper mapper) {
		this.theaterService = theaterService;
		this.mapper = mapper;
	}

	@RabbitListener(queues = "theaterGetByName.rpc.requests")
	public TheaterDto theaterGetByName(InTheaterName theaterName) {
		log.info("TheaterGetByName processing name: {}", theaterName);
		var returnValue = TheaterDto.convertToDto(theaterService.getByName(theaterName.getName()).orElseThrow(), mapper);
		log.info("TheaterGetByName returning: {}", returnValue);
		return returnValue;
	}

}
