package com.vr61v.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vr61v.entities.Ticket;
import com.vr61v.exceptions.RepositoryException;
import com.vr61v.filters.Filter;
import com.vr61v.mappers.TicketMapper;
import com.vr61v.utils.ConnectionManager;
import com.vr61v.utils.RepositoryConnectionManager;
import com.vr61v.utils.RepositoryTestsConnectionManager;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JDBC implementation of {@link Repository} interface for {@link Ticket} entities.
 * <p>
 * This repository handles all database operations for Ticket entities including:
 * <ul>
 *   <li>CRUD operations</li>
 *   <li>Batch operations</li>
 *   <li>Filtered searches</li>
 *   <li>Pagination</li>
 * </ul>
 * <p>
 * The repository uses {@link TicketMapper} to convert between database records and
 * entity objects, and handles JSON serialization of contact data.
 * <p>
 * All database operations are performed using prepared statements to prevent SQL injection.
 * Any SQL or data processing exceptions are wrapped in {@link RepositoryException}.
 *
 * @see Repository
 * @see Ticket
 * @see TicketMapper
 * @see RepositoryException
 */
public class TicketsRepository implements Repository<Ticket> {

    private final ConnectionManager connectionManager;
    private static final TicketMapper mapper = new TicketMapper();

    /**
     * Constructs a new TicketsRepository with the specified connection manager.
     *
     * @param connectionManager the connection manager to use for database access
     * @throws IllegalArgumentException if connectionManager is null
     * @see RepositoryConnectionManager
     * @see RepositoryTestsConnectionManager
     */
    public TicketsRepository(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    /**
     * SQL query for inserting a new ticket record into the database.
     * Inserts values for all columns:
     * <ul>
     *   <li>ticket_no - the ticket number (primary key)</li>
     *   <li>book_ref - booking reference</li>
     *   <li>passenger_id - passenger identification</li>
     *   <li>passenger_name - passenger name</li>
     *   <li>contact_data - contact information in JSON format</li>
     * </ul>
     */
    private static final String ADD_QUERY = """
        INSERT INTO bookings.tickets
        (ticket_no, book_ref, passenger_id, passenger_name, contact_data)
        VALUES (?, ?, ?, ?, (to_json(?::json)));
    """;

    /**
     * SQL query for finding a ticket by its unique number.
     * Selects all columns from the tickets table where ticket_no matches.
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
     * Uses Postgres ANY operator to match against array of ticket numbers.
     */
    private static final String FIND_ALL_BY_ID_QUERY = """
        SELECT ticket_no, book_ref, passenger_id, passenger_name, contact_data
        FROM bookings.tickets
        WHERE ticket_no = ANY (?);
    """;

    /**
     * SQL query for paginated retrieval of tickets.
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
     * Updates all fields except the primary key (ticket_no).
     */
    private static final String UPDATE_QUERY = """
        UPDATE bookings.tickets
        SET book_ref = ?, passenger_id = ?, passenger_name = ?, contact_data = (to_json(?::json))
        WHERE ticket_no = ?;
    """;

    /**
     * SQL query for deleting a ticket by its number.
     */
    public static final String DELETE_QUERY = """
        DELETE FROM bookings.tickets
        WHERE ticket_no = ?;
    """;

    /**
     * Extracts database column values from a list of tickets.
     *
     * @param t list of tickets to convert
     * @return list of column value lists
     */
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
     *
     * @throws RepositoryException if database error occurs or ticket data is invalid
     * @throws IllegalArgumentException if ticket is null
     */
    @Override
    public boolean add(Ticket t) {
        if (t == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_QUERY)
        ) {
            List<String> values = mapper.mapToColumns(t);
            for (int i = 0; i < values.size(); ++i) {
                statement.setString(i + 1, values.get(i));
            }

            return statement.executeUpdate() == 1;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation uses batch processing for efficient insertion of multiple tickets.
     * The operation is atomic - either all tickets are added or none.
     *
     * @param t list of tickets to add, must not be null or empty
     * @return True if all tickets were successfully added, false if any insertion failed
     * @throws RepositoryException if there's a database error or ticket data is invalid
     * @throws IllegalArgumentException if the list is null, contains null or empty
     */
    @Override
    public boolean addAll(List<Ticket> t) {
        if (t == null || t.isEmpty() || t.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Ticket list cannot be null, contains null or empty");
        }

        List<List<String>> valuesList = extractValuesList(t);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_QUERY)
        ) {
            for (List<String> values : valuesList) {
                for (int i = 0; i < values.size(); ++i) {
                    statement.setString(i + 1, values.get(i));
                }
                statement.addBatch();
            }

            int[] result = statement.executeBatch();
            return result.length == valuesList.size() &&
                    Arrays.stream(result).allMatch(i -> i == 1);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ticket number to search for, must not be null or empty
     * @return Optional containing the found ticket or empty if not found
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if id is null or empty
     */
    @Override
    public Optional<Ticket> findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)
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
     *
     * @return List of all tickets, empty list if no tickets found
     * @throws RepositoryException if there's a database error
     */
    @Override
    public List<Ticket> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)
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

    /**
     * {@inheritDoc}
     * <p>
     * Builds a dynamic WHERE clause based on the filter parameters.
     * Only non-null filter values are included in the query.
     *
     * @param filter the filter criteria, must not be null
     * @return List of matching tickets, empty list if none found
     * @throws RepositoryException if there's a database error or invalid filter
     * @throws IllegalArgumentException if filter is null
     */
    @Override
    public List<Ticket> findAll(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("Filter cannot be null");
        }

        Map<String, Object> whereParameters = filter.toWhereParameters();
        List<String> keys = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : whereParameters.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }
        String where = keys.stream().collect(Collectors.joining(" AND ", " WHERE ", ";"));
        String query = FIND_ALL_QUERY + where;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)
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
     * <p>
     * Uses Postgres ANY operator for efficient lookup of multiple tickets.
     * The returned list may be smaller than the input list if some tickets weren't found.
     *
     * @param ids list of ticket numbers to search for, must not be null or empty
     * @return List of found tickets, empty list if none found
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if ids list is null, contains null or empty
     */
    @Override
    public List<Ticket> findAllById(List<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(str -> str == null || str.isEmpty())) {
            throw new IllegalArgumentException("IDs list cannot be null, contains null or empty");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_BY_ID_QUERY)
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
     *
     * @param page the page number (0-based)
     * @param size the number of tickets per page, must be positive
     * @return List of tickets for the page, empty list if page is empty
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if page is negative or size is not positive
     */
    @Override
    public List<Ticket> findPage(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page number cannot be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Page size must be positive");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_PAGE_QUERY)
        ) {
            statement.setInt(1, size);
            statement.setInt(2, size * page);

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
     * <p>
     * Updates all ticket fields except the primary key (ticket_no).
     * The ticket must exist in the database for the update to succeed.
     *
     * @param ticket the ticket with updated information, must not be null
     * @return True if the ticket was successfully updated, false if not found
     * @throws RepositoryException if there's a database error or ticket data is invalid
     * @throws IllegalArgumentException if ticket is null
     */
    @Override
    public boolean update(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket cannot be null");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            List<String> values = mapper.mapToColumns(ticket);
            statement.setString(values.size(), values.get(0));
            for (int i = 1; i < values.size(); ++i) {
                statement.setString(i, values.get(i));
            }

            return statement.executeUpdate() == 1;
        } catch (SQLException | JsonProcessingException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Performs batch update of multiple tickets. The operation is atomic - either all
     * tickets are updated or none.
     *
     * @param t list of tickets to update, must not be null or empty
     * @return True if all tickets were successfully updated, false if any update failed
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if tickets list is null, contains null or empty
     */
    @Override
    public boolean updateAll(List<Ticket> t) {
        if (t == null || t.isEmpty() || t.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Tickets list cannot be null, contains null or empty");
        }

        List<List<String>> valuesList = extractValuesList(t);
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)
        ) {
            for (List<String> values : valuesList) {
                statement.setString(values.size(), values.get(0));
                for (int i = 1; i < values.size(); ++i) {
                    statement.setString(i, values.get(i));
                }
                statement.addBatch();
            }

            int[] result = statement.executeBatch();
            return result.length == valuesList.size() &&
                    Arrays.stream(result).allMatch(i -> i == 1);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id the ticket number to delete, must not be null or empty
     * @return True if the ticket was successfully deleted, false if not found
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if id is null or empty
     */
    @Override
    public boolean delete(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Ticket ID cannot be null or empty");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            statement.setString(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Performs batch deletion of multiple tickets. The operation is atomic - either all
     * tickets are deleted or none.
     *
     * @param ids list of ticket numbers to delete, must not be null or empty
     * @return True if all tickets were successfully deleted, false if any deletion failed
     * @throws RepositoryException if there's a database error
     * @throws IllegalArgumentException if ids list is null, contains null or empty
     */
    @Override
    public boolean deleteAll(List<String> ids) {
        if (ids == null || ids.isEmpty() || ids.stream().anyMatch(i -> i == null || i.isEmpty())) {
            throw new IllegalArgumentException("IDs list cannot be null, contains null or empty");
        }

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)
        ) {
            for (String id : ids) {
                statement.setString(1, id);
                statement.addBatch();
            }

            int[] result = statement.executeBatch();
            return result.length == ids.size() &&
                    Arrays.stream(result).allMatch(i -> i == 1);
        } catch (SQLException e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
