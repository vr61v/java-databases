package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.TicketFlightDto;
import org.vr61v.entities.TicketFlight;

@Mapper(componentModel = "spring")
public interface TicketFlightMapper
        extends BaseMapper<TicketFlightDto, TicketFlight> { }
