package com.vr61v.entities.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Mapper<T> {

    T mapToEntity(ResultSet rs) throws SQLException, JsonProcessingException;

    List<String> mapToColumns(T entity) throws JsonProcessingException;

}
