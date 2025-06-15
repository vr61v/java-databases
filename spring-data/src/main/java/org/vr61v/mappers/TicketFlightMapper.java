package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.TicketFlightDto;
import org.vr61v.entities.TicketFlight;

@Mapper(componentModel = "spring")
public interface TicketFlightMapper extends BaseMapper<TicketFlight, TicketFlightDto> {

    @Mapping(target = "ticketNo", source = "entity.id.ticket.ticketNo")
    @Mapping(target = "flightId", source = "entity.id.flight.flightId")
    TicketFlightDto toDto(TicketFlight entity);

}
