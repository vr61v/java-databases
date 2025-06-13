package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vr61v.dtos.SeatDto;
import org.vr61v.entities.Seat;

@Mapper(componentModel = "spring")
public interface SeatMapper extends BaseMapper<Seat, SeatDto> {

    @Mapping(target = "aircraftCode", source = "entity.id.aircraft.aircraftCode")
    @Mapping(target = "seatNo", source = "entity.id.seatNo")
    SeatDto toDto(Seat entity);

}
