package org.vr61v.mappers;

import org.mapstruct.Mapper;
import org.vr61v.dtos.BoardingPassDto;
import org.vr61v.entities.BoardingPass;

@Mapper(componentModel = "spring")
public interface BoardingPassMapper
        extends BaseMapper<BoardingPassDto, BoardingPass> { }
