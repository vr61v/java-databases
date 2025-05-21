package com.vr61v.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr61v.entities.Ticket;
import com.vr61v.exceptions.RepositoryException;
import com.vr61v.filters.Filter;
import com.vr61v.mappers.TicketMapper;
import com.vr61v.utils.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ConnectionManager connectionManager;

    private static final TicketMapper mapper = new TicketMapper();

    public TicketsRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * SQL query for inserting a new ticket record into the database.
     * Inserts values for all columns: ticket_no, book_ref, passenger_id, passenger_name, contact_data.
     * The contact_data field is converted from JSON string to Postgres json type.
     */
    private static final String ADD_QUERY = """
        INSERT INTO bookings.tickets
        (ticket_no, book_ref, passenger_id, passenger_name, contact_data)
        VALUES (?, ?, ?, ?, (to_json(?::json)));
    """;

    /**
     * SQL query for finding a ticket by its unique number.
     * Selects all columns from the tickets table where ticket_no matches the parameter.
     */
    private static final String FIND_BY_ID_QUERY = """
        SELECT ticket_no, book_ref, passenger_id, passenger_name, contact_data
        FROM bookings.tickets
        WHERE ticket_no = ?;
    """;

    /**
     * SQL query for finding all tickets from the database.
     * Selects all columns from all records in the tickets table.
     */
    private static final String FIND_ALL_QUERY = """
        SELECT ticket_no, book_ref, passenger_id, passenger_name, contact_data
        FROM bookings.tickets
    """;

    /**
     * SQL query for finding multiple tickets by their numbers.
     * Selects all columns from the tickets table where ticket_no is in the specified list.
     */
    private static final String FIND_ALL_BY_ID_QUERY = """
        SELECT ticket_no, book_ref, passenger_id, passenger_name, contact_data
        FROM bookings.tickets
        WHERE ticket_no = ANY (?);
    """;

    /**
     * SQL query for paginated retrieval of tickets.
     * Selects all columns from the tickets table with pagination support:
     * - LIMIT controls the page size (number of records per page)
     * - OFFSET skips the specified number of records
     * Results are ordered by ticket_no for consistent pagination.
     */
    private static final String FIND_PAGE_QUERY = """
        SELECT ticket_no, book_ref, passenger_id, passenger_name, contact_data
        FROM bookings.tickets
        ORDER BY ticket_no
        LIMIT ?
        OFFSET ?;
    """;

    /**
     * SQL query for updating a ticket record.
     * Updates all fields of a ticket except the primary key (ticket_no):
     * - book_ref
     * - passenger_id
     * - passenger_name
     * - contact_data (converted from JSON string to Postgres json type)
     * The WHERE clause ensures only the ticket with specified ticket_no is updated.
     */
    private static final String UPDATE_QUERY = """
        UPDATE bookings.tickets
        SET book_ref = ?, passenger_id = ?, passenger_name = ?, contact_data = (to_json(?::json))
        WHERE ticket_no = ?;
    """;

    /**
     * SQL query for deleting a ticket by its number.
     * Deletes the record from the tickets table where ticket_no matches the parameter.
     */
    public static final String DELETE_QUERY = """
        DELETE FROM bookings.tickets
        WHERE ticket_no = ?;
    """;

    private String buildTransactionQuery(String query, int count) {
        return "BEGIN;" + query.repeat(count) + "END;";
    }

    private List<List<String>> extractValuesList(List<Ticket> t) {
        List<List<String>> valuesList = new ArrayList<>();
        for (Ticket ticket : t) {
            try {
                valuesList.add(mapper.mapToColumns(ticket));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return valuesList;
    }

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
    @Override
    public boolean add(Ticket ticket) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(ADD_QUERY)
        ) {
            List<String> values = mapper.mapToColumns(ticket);
            for (int i = 0; i < values.size(); ++i) {
                statement.setString(i + 1, values.get(i));
            }

            return statement.executeUpdate() > 0;
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
        List<List<String>> valuesList = extractValuesList(t);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(buildTransactionQuery(ADD_QUERY, valuesList.size()))
        ) {
            int index = 0;
            for (List<String> values : valuesList) {
                for (String value : values) {
                    statement.setString(++index, value);
                }
            }

            return !statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation or JSON processing
     */
    @Override
    public Optional<Ticket> findById(String id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(FIND_BY_ID_QUERY)
        ) {
            statement.setString(1, id);

            ResultSet result = statement.executeQuery();

            return result.next() ?
                    Optional.ofNullable(mapper.mapToEntity(result)) :
                    Optional.empty();
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                    .prepareStatement(FIND_ALL_QUERY)
        ) {
            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
                tickets.add(mapper.mapToEntity(result));
            }

            return tickets;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public List<Ticket> findAll(Filter filter) {
        Map<String, Object> whereParameters = filter.toWhereParameters();
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : whereParameters.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        String query = FIND_ALL_QUERY +
                keys.stream().collect(
                        Collectors.joining(" AND ", " WHERE ", ";")
                );
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(query)
        ) {
            for (int i = 0; i < values.size(); ++i) {
                statement.setObject(i + 1, values.get(i));
            }

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                    .prepareStatement(FIND_ALL_BY_ID_QUERY)
        ) {
            statement.setArray(
                    1,
                    connection.createArrayOf("VARCHAR", ids.toArray())
            );

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                    .prepareStatement(FIND_PAGE_QUERY)
        ) {
            statement.setInt(1, size);
            statement.setInt(2, page * (size + 1));

            ResultSet result = statement.executeQuery();
            List<Ticket> tickets = new ArrayList<>();
            while (result.next()) {
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(UPDATE_QUERY)
        ) {
            List<String> values = mapper.mapToColumns(ticket);
            statement.setString(values.size(), values.get(0));
            for (int i = 1; i < values.size(); ++i) {
                statement.setString(i, values.get(i));
            }

            return statement.executeUpdate() > 0;
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
        List<List<String>> valuesList = extractValuesList(t);

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(buildTransactionQuery(UPDATE_QUERY, valuesList.size()))
        ) {
            int index = 0;
            for (List<String> values : valuesList) {
                for (int i = 1; i < values.size(); ++i) {
                    statement.setString(++index, values.get(i));
                }
                statement.setString(++index, values.get(0));
            }

            return !statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * @throws RepositoryException if there's an error during database operation
     */
    @Override
    public boolean delete(String id) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(DELETE_QUERY)
        ) {
            statement.setString(1, id);

            return statement.executeUpdate() > 0;
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
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection
                     .prepareStatement(buildTransactionQuery(DELETE_QUERY, ids.size()))
        ) {
            for (int i = 0; i < ids.size(); ++i) {
                statement.setString(i + 1, ids.get(i));
            }

            return !statement.execute();
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
