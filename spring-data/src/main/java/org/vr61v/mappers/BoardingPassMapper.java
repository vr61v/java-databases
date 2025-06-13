package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.BoardingPassDto;
import org.vr61v.entities.BoardingPass;

@Mapper(componentModel = "spring")
public interface BoardingPassMapper extends BaseMapper<BoardingPass, BoardingPassDto> {

    @Mapping(target = "ticketNo", source = "entity.id.ticket.ticketNo")
    @Mapping(target = "flightId", source = "entity.id.flight.flightId")
    BoardingPassDto toDto(BoardingPass entity);

}
