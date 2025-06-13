package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.FlightDto;
import org.vr61v.entities.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper
        extends BaseMapper<Flight, FlightDto> { }