package com.vr61v.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr61v.entities.Ticket;
import com.vr61v.entities.mappers.TicketMapper;
import com.vr61v.exceptions.RepositoryException;
import com.vr61v.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketsRepository implements Repository<Ticket> {

    private static final String table = "tickets";
    private static final TicketMapper mapper = new TicketMapper();

    private static final String ADD_QUERY = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, (to_json(?::json)));", table);
    private static final String FIND_BY_ID_QUERY = String.format("SELECT * FROM %s WHERE ticket_no = ?;", table);
    private static final String FIND_ALL_QUERY = String.format("SELECT * FROM %s;", table);
    private static final String FIND_ALL_BY_ID_QUERY = String.format("SELECT * FROM %s WHERE ticket_no IN ", table);
    private static final String FIND_PAGE_QUERY = String.format("SELECT * FROM %s ORDER BY ticket_no LIMIT ? OFFSET ?;", table);
    private static final String UPDATE_QUERY = String.format(
            """
            UPDATE %s
            SET
               book_ref = ?,
                passenger_id = ?,
                passenger_name = ?,
                contact_data = (to_json(?::json))
            WHERE ticket_no = ?;
            """,
            table
    );
    public static final String DELETE_QUERY = String.format("DELETE FROM %s WHERE ticket_no = ?;", table);

    @Override
    public boolean add(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(ADD_QUERY);

            int i = 0;
            for (String value : mapper.mapToColumns(ticket)) {
                statement.setString(++i, value);
            }

            int result = statement.executeUpdate();
            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean addAll(List<Ticket> t) {
        try (Connection connection = ConnectionManager.open()) {
            List<List<String>> valuesList = new ArrayList<>();
            for (Ticket ticket : t) {
                valuesList.add(mapper.mapToColumns(ticket));
            }

            StringBuilder query = new StringBuilder();
            query.append("BEGIN;")
                    .append(ADD_QUERY.repeat(valuesList.size()))
                    .append("END;");

            PreparedStatement statement = connection.prepareStatement(query.toString());

            int index = 0;
            for (List<String> values : valuesList) {
                for (String value : values) {
                    statement.setString(++index, value);
                }
            }

            return !statement.execute();
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public Ticket findById(String id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY);
            statement.setString(1, id);

            ResultSet result = statement.executeQuery();

            return mapper.mapToEntity(result);
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Ticket> findAll() {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (!result.isLast()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Ticket> findAllById(List<String> ids) {
        try (Connection connection = ConnectionManager.open()) {
            StringBuilder array = new StringBuilder("(");
            for (int i = 0; i < ids.size(); ++i) {
                if (i == ids.size() - 1) array.append("?);");
                else array.append("? ,");
            }

            StringBuilder query = new StringBuilder();
            query.append(FIND_ALL_BY_ID_QUERY)
                    .append(array);

            PreparedStatement statement = connection.prepareStatement(query.toString());

            int index = 0;
            for (String id : ids) {
                statement.setString(++index, id);
            }

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (!result.isLast()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Ticket> findPage(int page, int size) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(FIND_PAGE_QUERY);
            statement.setInt(1, size);
            statement.setInt(2, page * (size + 1));

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (!result.isLast()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        try (Connection connection = ConnectionManager.open()) {
            List<String> values = mapper.mapToColumns(ticket);

            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(values.size(), values.get(0));
            for (int i = 1; i < values.size(); ++i) {
                statement.setString(i, values.get(i));
            }

            int result = statement.executeUpdate();

            return result > 0;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean updateAll(List<Ticket> t) {
        try (Connection connection = ConnectionManager.open()) {
            List<List<String>> valuesList = new ArrayList<>();
            for (Ticket ticket : t) {
                valuesList.add(mapper.mapToColumns(ticket));
            }

            StringBuilder query = new StringBuilder();
            query.append("BEGIN;")
                    .append(UPDATE_QUERY.repeat(valuesList.size()))
                    .append("END;");

            PreparedStatement statement = connection.prepareStatement(query.toString());

            int index = 0;
            for (List<String> values : valuesList) {
                for (int i = 1; i < values.size(); ++i) {
                    statement.setString(++index, values.get(i));
                }
                statement.setString(++index, values.get(0));
            }

            return !statement.execute();
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) {
        try (Connection connection = ConnectionManager.open()) {
            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setString(1, id);

            int result = statement.executeUpdate();

            return result > 0;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public boolean deleteAll(List<String> ids) {
        try (Connection connection = ConnectionManager.open()) {
            StringBuilder query = new StringBuilder();
            query.append("BEGIN;")
                    .append(DELETE_QUERY.repeat(ids.size()))
                    .append("END;");

            PreparedStatement statement = connection.prepareStatement(query.toString());

            int index = 0;
            for (String id : ids) {
                statement.setString(++index, id);
            }

            return !statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
