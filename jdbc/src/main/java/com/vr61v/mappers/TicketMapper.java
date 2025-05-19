package com.vr61v.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr61v.entities.ContactData;
import com.vr61v.entities.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of {@link Mapper} interface for {@link Ticket} entities.
 * Handles conversion between:
 * <ul>
 *   <li>Database records and {@link Ticket} objects</li>
 *   <li>{@link Ticket} objects and database column values</li>
 * </ul>
 *
 * <p>Specifically handles JSON serialization/deserialization of {@link ContactData} field
 * using Jackson's {@link ObjectMapper}.
 *
 * @see Mapper
 * @see Ticket
 * @see ContactData
 */
public class TicketMapper implements Mapper<Ticket> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * {@inheritDoc}
     * <p>Mapping details for Ticket:
     * <ol>
     *   <li>Reads next record from ResultSet (advances cursor)</li>
     *   <li>Deserializes contact_data from JSON to {@link ContactData} object</li>
     *   <li>Constructs new Ticket with all field values</li>
     * </ol>
     *
     * @return new Ticket instance or null if no more rows in ResultSet
     */
    @Override
    public Ticket mapToEntity(ResultSet result) throws SQLException, JsonProcessingException {
            ContactData contactData = objectMapper.readValue(
                    result.getString("contact_data"),
                    ContactData.class
            );

            return new Ticket(
                    result.getString("ticket_no"),
                    result.getString("book_ref"),
                    result.getString("passenger_id"),
                    result.getString("passenger_name"),
                    contactData
            );
    }

    /**
     * {@inheritDoc}
     * <p>Column value order for Ticket:
     * <ol>
     *   <li>ticket_no</li>
     *   <li>book_ref</li>
     *   <li>passenger_id</li>
     *   <li>passenger_name</li>
     *   <li>contact_data (as JSON string)</li>
     * </ol>
     */
    @Override
    public List<String> mapToColumns(Ticket entity) throws JsonProcessingException {
        return List.of(
                entity.getTicketNo(),
                entity.getBookRef(),
                entity.getPassengerId(),
                entity.getPassengerName(),
                objectMapper.writeValueAsString(entity.getContactData())
        );
    }
}
