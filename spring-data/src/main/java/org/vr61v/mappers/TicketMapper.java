package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.TicketDto;
import org.vr61v.entities.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper extends BaseMapper<Ticket, TicketDto> { }
