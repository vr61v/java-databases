package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.SeatDto;
import org.vr61v.entities.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper
        extends BaseMapper<Seat, SeatDto> { }
