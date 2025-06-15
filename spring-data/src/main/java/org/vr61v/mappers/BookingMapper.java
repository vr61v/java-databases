package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.BookingDto;
import org.vr61v.entities.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper extends BaseMapper<Booking, BookingDto> { }
