package com.example.ticketstream.model;

import org.modelmapper.ModelMapper;

public interface MappedUDT<EntityType, MappedUdtType extends MappedUDT<?, ?>> {

	public static <EntityType, MappedUdtType> MappedUdtType convertToDto(
			EntityType entity, Class<MappedUdtType> mappedUDTClass, ModelMapper mapper) 
	{
	    return mapper.map(entity, mappedUDTClass);
	}
	
}
