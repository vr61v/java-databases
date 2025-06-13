package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.AircraftDto;
import org.vr61v.entities.Aircraft;

@Mapper(componentModel = "spring")
public interface AircraftMapper extends BaseMapper<Aircraft, AircraftDto> { }
