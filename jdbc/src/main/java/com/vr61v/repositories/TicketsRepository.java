package com.vr61v.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr61v.entities.Ticket;
import com.vr61v.entities.mappers.TicketMapper;
import com.vr61v.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TicketsRepository implements Repository<Ticket> {

    private final String table = "tickets";
    private final TicketMapper mapper = new TicketMapper();

    @Override
    public List<Ticket> findAll() {
        try (Connection connection = ConnectionManager.open()) {
            String query = String.format("SELECT * FROM %s", table);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
