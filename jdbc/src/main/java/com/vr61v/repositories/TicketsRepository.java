package com.vr61v.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr61v.entities.Ticket;
import com.vr61v.entities.mappers.TicketMapper;
import com.vr61v.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketsRepository implements Repository<Ticket> {

    private final String table = "tickets";
    private final TicketMapper mapper = new TicketMapper();

    @Override
    public boolean add(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("INSERT INTO %s VALUES (?, ?, ?, ?, (to_json(?::json)))", table)
            );

            int i = 0;
            for (String value : mapper.mapToColumns(ticket)) {
                statement.setString(++i, value);
            }

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Ticket findById(String id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s WHERE ticket_no = ?", table)
            );

            statement.setString(1, id);

            ResultSet result = statement.executeQuery();

            return mapper.mapToEntity(result);
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s", table)
            );

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (!result.isLast()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findPage(int page, int size) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("SELECT * FROM %s ORDER BY ticket_no LIMIT ? OFFSET ?", table)
            );
            statement.setInt(1, size);
            statement.setInt(2, page * (size + 1));

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (!result.isLast()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            List<String> values = mapper.mapToColumns(ticket);
            PreparedStatement statement = connection.prepareStatement(
                    String.format(
                            """
                            UPDATE %s
                            SET
                                book_ref = ?,
                                passenger_id = ?,
                                passenger_name = ?,
                                contact_data = (to_json(?::json))
                            WHERE ticket_no = ?
                            """,
                            table
                    )
            );

            for (int i = 1; i < values.size(); ++i) {
                statement.setString(i, values.get(i));
            }
            statement.setString(values.size(), values.get(0));

            int result = statement.executeUpdate();

            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(
                    String.format("DELETE FROM %s WHERE ticket_no = ?", table)
            );

            statement.setString(1, id);

            int result = statement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
