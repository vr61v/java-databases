package org.vr61v.mappers;

public interface BaseMapper<E, DTO> {

    DTO toDto(E entity);

    E toEntity(DTO dto);

}
