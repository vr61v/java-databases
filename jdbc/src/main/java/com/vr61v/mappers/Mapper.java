package com.vr61v.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Generic interface for mapping between database records and entity objects.
 * Provides bidirectional conversion capabilities:
 * <ul>
 *   <li>From database {@link ResultSet} to entity object (mapToEntity)</li>
 *   <li>From entity object to database column values (mapToColumns)</li>
 * </ul>
 *
 * @param <T> the type of entity this mapper handles
 *
 * @see ResultSet
 */
public interface Mapper<T> {

    /**
     * Converts a database record from {@link ResultSet} to an entity object.
     *
     * @param rs the ResultSet containing the database record (positioned at the desired row)
     * @return the mapped entity object
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if there's an error processing JSON data during mapping
     */
    T mapToEntity(ResultSet rs) throws SQLException, JsonProcessingException;

    /**
     * Converts an entity object to a list of database column values.
     * The order of values in the list should match the expected database column order.
     *
     * @param entity the entity object to convert
     * @return list of database column values in proper order
     * @throws JsonProcessingException if there's an error converting data to JSON format
     */
    List<String> mapToColumns(T entity) throws JsonProcessingException;

}
