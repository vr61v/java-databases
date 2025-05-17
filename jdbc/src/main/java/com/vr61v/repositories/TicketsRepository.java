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
    public boolean add(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            List<String> values = mapper.mapToColumns(ticket);
            String sql = String.format(
                    "INSERT INTO %s VALUES ('%s', '%s', '%s', '%s', '%s')",
                    table,
                    values.get(0), values.get(1), values.get(2), values.get(3), values.get(4));
            Statement statement = connection.createStatement();

            int result = statement.executeUpdate(sql);
            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

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

    @Override
    public Ticket findById(String id) {
        try (Connection connection = ConnectionManager.open()) {
            String query = String.format("SELECT * FROM %s WHERE ticket_no = '%s'", table, id);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);

            return mapper.mapToEntity(result);
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            List<String> values = mapper.mapToColumns(ticket);
            String sql = String.format(
                    """
                    UPDATE %s
                    SET
                        book_ref = '%s',
                        passenger_id = '%s',
                        passenger_name = '%s',
                        contact_data = '%s'
                    WHERE ticket_no = '%s'
                    """,
                    table,
                    values.get(1), values.get(2), values.get(3), values.get(4), values.get(0));
            Statement statement = connection.createStatement();

            int result = statement.executeUpdate(sql);
            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
