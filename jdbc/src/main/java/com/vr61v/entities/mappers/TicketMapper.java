package com.vr61v.entities.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vr61v.entities.ContactData;
import com.vr61v.entities.Ticket;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TicketMapper implements Mapper<Ticket> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Ticket mapToEntity(ResultSet result) throws SQLException, JsonProcessingException {
        if (result.next()) {
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

        return null;
    }

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
