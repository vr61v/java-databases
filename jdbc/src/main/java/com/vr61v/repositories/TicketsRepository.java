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

/**
 * Implementation of {@link Repository} interface for {@link Ticket} entities.
 * Provides database operations for tickets using JDBC.
 * Uses {@link TicketMapper} for mapping between database records and entity objects.
 *
 * @see Repository
 * @see Ticket
 * @see TicketMapper
 */
public class TicketsRepository implements Repository<Ticket> {

    private static final String table = "tickets";
    private static final TicketMapper mapper = new TicketMapper();

    /**
     * SQL query for inserting a new ticket record into the database.
     * Inserts values for all columns: ticket_no, book_ref, passenger_id, passenger_name, contact_data.
     * The contact_data field is converted from JSON string to PostgreSQL json type.
     */
    private static final String ADD_QUERY = String.format("INSERT INTO %s VALUES (?, ?, ?, ?, (to_json(?::json)));", table);

    /**
     * SQL query for finding a ticket by its unique number.
     * Selects all columns from the tickets table where ticket_no matches the parameter.
     */
    private static final String FIND_BY_ID_QUERY = String.format("SELECT * FROM %s WHERE ticket_no = ?;", table);

    /**
     * SQL query for retrieving all tickets from the database.
     * Selects all columns from all records in the tickets table.
     */
    private static final String FIND_ALL_QUERY = String.format("SELECT * FROM %s;", table);

    /**
     * SQL query for finding multiple tickets by their numbers.
     * Selects all columns from the tickets table where ticket_no is in the specified list.
     * Note: The IN clause parameters need to be appended when building the query.
     */
    private static final String FIND_ALL_BY_ID_QUERY = String.format("SELECT * FROM %s WHERE ticket_no IN ", table);

    /**
     * SQL query for paginated retrieval of tickets.
     * Selects all columns from the tickets table with pagination support:
     * - LIMIT controls the page size (number of records per page)
     * - OFFSET skips the specified number of records
     * Results are ordered by ticket_no for consistent pagination.
     */
    private static final String FIND_PAGE_QUERY = String.format("SELECT * FROM %s ORDER BY ticket_no LIMIT ? OFFSET ?;", table);

    /**
     * SQL query for updating a ticket record.
     * Updates all fields of a ticket except the primary key (ticket_no):
     * - book_ref
     * - passenger_id
     * - passenger_name
     * - contact_data (converted from JSON string to PostgreSQL json type)
     * The WHERE clause ensures only the ticket with specified ticket_no is updated.
     */
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

    /**
     * SQL query for deleting a ticket by its number.
     * Deletes the record from the tickets table where ticket_no matches the parameter.
     */
    public static final String DELETE_QUERY = String.format("DELETE FROM %s WHERE ticket_no = ?;", table);

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation
     */
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

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation
     */
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
