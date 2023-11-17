package com.example.ticketstream.functions.dto;

import java.io.Serializable;
import java.util.UUID;
import java.util.function.Function;

import org.modelmapper.ModelMapper;

public interface EntityDto<EntityType, DtoType extends EntityDto<?, ?>> extends Serializable {

	public UUID getId();

	public default EntityType convertToEntity(Function<DtoType, EntityType> entityFinder, ModelMapper mapper) {
		return convertToEntity(entityFinder, mapper, true);
	}
	
	public default EntityType convertToEntity(Function<DtoType, EntityType> entityFinder, ModelMapper mapper, boolean readonly) {
		@SuppressWarnings("unchecked")
		EntityType entity = entityFinder.apply((DtoType) this);
		if (! readonly) {
			mapper.map(mapper, entity);
		}
		return entity;
	}

	public static <EntityType, DtoType> DtoType convertToDto(EntityType entity, Class<DtoType> dtoClass, ModelMapper mapper) {
	    return mapper.map(entity, dtoClass);
	}
	
}
