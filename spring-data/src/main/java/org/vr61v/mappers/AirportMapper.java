package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.AirportDto;
import org.vr61v.entities.Airport;

@Mapper(componentModel = "spring")
public interface AirportMapper extends BaseMapper<Airport, AirportDto> { }
