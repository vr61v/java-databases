import com.vr61v.entities.ContactData;
import com.vr61v.entities.Ticket;
import com.vr61v.exceptions.RepositoryException;
import com.vr61v.filters.TicketFilter;
import com.vr61v.repositories.TicketsRepository;
import com.vr61v.utils.ConnectionManager;
import com.vr61v.utils.RepositoryTestsConnectionManager;
import org.flywaydb.core.Flyway;
import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class TicketRepositoryTests {

    private static final ConnectionManager manager = new RepositoryTestsConnectionManager();
    private static final TicketsRepository repository = new TicketsRepository(manager);
    private static final List<List<String>> ticketsData = new ArrayList<>();
    private static final int TICKETS_DATA_SIZE = 10;

    // Need for tests where assert null from find ticket
    private static final String NOT_EXISTING_TICKET = "NOT_EXISTING";
    private static final String INVALID_BOOK_REF = "INVALID";

    private static Ticket generateTicket() {
        char[] ticketNo = new char[13];
        Random random = new Random();
        for (int j = 0; j < ticketNo.length; j++) {
            ticketNo[j] = (char) random.nextInt('0', '9');
        }

        return new Ticket(
                String.valueOf(ticketNo),
                "000000",
                random.nextInt(1000, 10_000) + " " + random.nextInt(100_000, 1_000_000),
                "GENERATED USER",
                new ContactData(
                        "+70000000000",
                        "generated@gmail.com"
                )
        );
    }

    private static List<Ticket> generateTickets(int count) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(generateTicket());
        }

        return tickets;
    }

    private void fillTable(List<List<String>> valuesList) {
        String CLEAR_QUERY = """
            TRUNCATE TABLE bookings.tickets;
            """;
        try (Connection connection = manager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CLEAR_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to clean test database %s", e.getMessage()));
        }

        String FILL_QUERY = """
            INSERT INTO bookings.tickets
            (ticket_no, book_ref, passenger_id, passenger_name, contact_data)
            VALUES (?, ?, ?, ?, (to_json(?::json)));
            """;
        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(FILL_QUERY)
        ) {
            for (List<String> ticket : valuesList) {
                for (int i = 0; i < ticket.size(); ++i) {
                    statement.setString(i + 1, ticket.get(i));
                }
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeClass
    public static void migrate() {
        String CLEAR_QUERY = """
            TRUNCATE TABLE bookings.tickets;
            """;
        try (Connection connection = manager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CLEAR_QUERY);
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to clean test database %s", e.getMessage()));
        }

        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:postgresql://localhost:5432/demo_test", "postgres", "postgres")
                .locations("classpath:db/migration")
                .load();

        flyway.migrate();

        for (int i = 0; i < TICKETS_DATA_SIZE; i++) {
            ticketsData.add(new ArrayList<>());
            ticketsData.get(i).add(i + "000000000000");
            ticketsData.get(i).add("000000");
            ticketsData.get(i).add("1234 567890");
            ticketsData.get(i).add("MIGRATED USER");
            ticketsData.get(i).add("{\"phone\":\"+70000000000\",\"email\":\"migrated@mail.com\"}");
        }
    }

    @Before
    public void setUp() {
        fillTable(ticketsData);
    }


    // Tests for add method
    @Test
    public void add_WhenValidTicket_ThenShouldReturnTrue() {
        Ticket ticket = generateTicket();
        assertTrue(repository.add(ticket));
    }

    @Test
    public void add_WhenTicketHasInvalidBookRef_ThenShouldThrowRepositoryException() {
        Ticket ticket = generateTicket();
        ticket.setBookRef(INVALID_BOOK_REF);
        assertThrows(RepositoryException.class, () -> repository.add(ticket));
    }

    @Test
    public void add_WhenTicketIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
    }


    // Tests for addAll method
    @Test
    public void addAll_WhenValidTicketsList_ThenShouldReturnTrue() {
        List<Ticket> tickets = generateTickets(5);
        assertTrue(repository.addAll(tickets));
    }

    @Test
    public void addAll_WhenListContainsTicketWithInvalidBookRef_ThenShouldThrowRepositoryException() {
        List<Ticket> tickets = generateTickets(5);
        tickets.get(tickets.size() - 1).setBookRef(INVALID_BOOK_REF);
        assertThrows(RepositoryException.class, () -> repository.addAll(tickets));
    }

    @Test
    public void addAll_WhenListIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.addAll(null));
    }

    @Test
    public void addAll_WhenListIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.addAll(new ArrayList<>()));
    }

    @Test
    public void addAll_WhenListContainsNull_ThenShouldThrowIllegalArgumentException() {
        List<Ticket> tickets = generateTickets(5);
        tickets.set(tickets.size() - 1, null);
        assertThrows(IllegalArgumentException.class, () -> repository.addAll(tickets));
    }


    // Tests for findById method
    @Test
    public void findById_WhenTicketExists_ThenShouldReturnTicket() {
        String id = "1000000000000";
        Ticket ticket = repository.findById(id).orElse(null);
        Assert.assertNotNull(ticket);
        Assert.assertEquals(id, ticket.getTicketNo());
    }

    @Test
    public void findById_WhenTicketDoesNotExist_ThenShouldReturnEmpty() {
        Ticket ticket = repository.findById(NOT_EXISTING_TICKET).orElse(null);
        Assert.assertNull(ticket);
    }

    @Test
    public void findById_WhenIdNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.findById(null).orElse(null));
    }

    @Test
    public void findById_WhenIdIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.findById("").orElse(null));
    }


    // Tests for findAll method
    @Test
    public void findAll_WhenNoFilter_ThenShouldReturnAllTickets() {
        List<Ticket> tickets = repository.findAll();
        Assert.assertNotNull(tickets);
        Assert.assertEquals(TICKETS_DATA_SIZE, tickets.size());
    }

    // Tests for findAll method with filter
    @Test
    public void findAll_WithValidFilterWithBookRef_ThenShouldReturnFilteredTickets() {
        TicketFilter filter = new TicketFilter("000000", null, null, null);
        List<Ticket> tickets = repository.findAll(filter);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(TICKETS_DATA_SIZE, tickets.size());
    }

    @Test
    public void findAll_WithValidFilterWithPassengerId_ThenShouldReturnFilteredTickets() {
        int expected = 3;
        String passengerId = "0000 000000";

        List<Ticket> tickets = generateTickets(5);
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < tickets.size(); ++i) {
            List<String> value = new ArrayList<>();
            value.add(tickets.get(i).getTicketNo());
            value.add(tickets.get(i).getBookRef());
            if (i < expected) value.add(passengerId);
            else value.add(tickets.get(i).getPassengerId());
            value.add(tickets.get(i).getPassengerName());
            value.add("{\"phone\":\"+70000000000\",\"email\":\"migrated@mail.com\"}");
            values.add(value);
        }
        fillTable(values);

        TicketFilter filter = new TicketFilter(null, passengerId, null, null);
        List<Ticket> actual = repository.findAll(filter);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void findAll_WithValidFilterWithPassengerName_ThenShouldReturnFilteredTickets() {
        int expected = 3;
        String passengerName = "EXPECTED NAME";

        List<Ticket> tickets = generateTickets(5);
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < tickets.size(); ++i) {
            List<String> value = new ArrayList<>();
            value.add(tickets.get(i).getTicketNo());
            value.add(tickets.get(i).getBookRef());
            value.add(tickets.get(i).getPassengerId());
            if (i < expected) value.add(passengerName);
            else value.add(tickets.get(i).getPassengerName());
            value.add("{\"phone\":\"+70000000000\",\"email\":\"migrated@mail.com\"}");
            values.add(value);
        }
        fillTable(values);

        TicketFilter filter = new TicketFilter(null, null, passengerName, null);
        List<Ticket> actual = repository.findAll(filter);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void findAll_WithValidFilterWithContactData_ThenShouldReturnFilteredTickets() {
        int expected = 3;
        String contactData = "{\"phone\":\"+79999999999\",\"email\":\"expected@mail.com\"}";

        List<Ticket> tickets = generateTickets(5);
        List<List<String>> values = new ArrayList<>();
        for (int i = 0; i < tickets.size(); ++i) {
            List<String> value = new ArrayList<>();
            value.add(tickets.get(i).getTicketNo());
            value.add(tickets.get(i).getBookRef());
            value.add(tickets.get(i).getPassengerId());
            value.add(tickets.get(i).getPassengerName());
            if (i < expected) value.add(contactData);
            else value.add("{\"phone\":\"+70000000000\",\"email\":\"migrated@mail.com\"}");
            values.add(value);
        }
        fillTable(values);

        TicketFilter filter = new TicketFilter(null, null, null, new ContactData("+79999999999", "expected@mail.com"));
        List<Ticket> actual = repository.findAll(filter);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(expected, actual.size());
    }

    @Test
    public void findAll_WhenEmptyFilter_ThenShouldThrowRepositoryException() {
        TicketFilter filter = new TicketFilter(null, null, null, null);
        assertThrows(RepositoryException.class, () -> repository.findAll(filter));
    }

    @Test
    public void findAll_WhenFilterIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.findAll(null));
    }


    // Tests for findAllById method
    @Test
    public void findAllById_WhenAllIdsExist_ThenShouldReturnAllRequestedTickets() {
        List<String> ids = List.of("1000000000000", "2000000000000", "3000000000000");
        List<Ticket> tickets = repository.findAllById(ids);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(ids.size(), tickets.size());
    }

    @Test
    public void findAllById_WhenSomeIdsDoNotExist_ThenShouldReturnOnlyExistingTickets() {
        List<String> ids = List.of("1000000000000", "2000000000000", NOT_EXISTING_TICKET);
        List<Ticket> tickets = repository.findAllById(ids);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(ids.size() - 1, tickets.size());
    }

    @Test
    public void findAllById_WhenIdsIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.findAllById(null));
    }

    @Test
    public void findAllById_WhenIdsIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.findAllById(List.of()));
    }

    @Test
    public void findAllById_WhenIdsContainsNull_ThenShouldThrowIllegalArgumentException() {
        List<String> ids = new ArrayList<>();
        ids.add("1000000000000");
        ids.add("2000000000000");
        ids.add(null);
        assertThrows(IllegalArgumentException.class, () -> repository.findAllById(ids));
    }

    @Test
    public void findAllById_WhenIdsContainsEmptyString_ThenShouldThrowIllegalArgumentException() {
        List<String> ids = new ArrayList<>();
        ids.add("1000000000000");
        ids.add("2000000000000");
        ids.add("");
        assertThrows(IllegalArgumentException.class, () -> repository.findAllById(ids));
    }


    // Tests for findPage method
    @Test
    public void findPage_WhenRequestFirstPage_ThenShouldReturnFirstPageResults() {
        int page = 0;
        int size = 2;
        List<Ticket> tickets = repository.findPage(page, size);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(size, tickets.size());
    }

    @Test
    public void findPage_WhenRequestSecondPage_ThenShouldReturnSecondPageResults() {
        int page = 1;
        int size = 2;
        List<Ticket> tickets = repository.findPage(page, size);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(size, tickets.size());
    }

    @Test
    public void findPage_WhenRequestLastPage_ThenShouldReturnLastPageResults() {
        int page = TICKETS_DATA_SIZE / 2 - 1;
        int size = 2;
        List<Ticket> tickets = repository.findPage(page, size);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(size, tickets.size());
    }

    @Test
    public void findPage_WhenPageSizeExceedsTotalCount_ThenShouldReturnAllAvailableTickets() {
        int page = 0;
        int size = TICKETS_DATA_SIZE + 1;
        List<Ticket> tickets = repository.findPage(page, size);
        Assert.assertNotNull(tickets);
        Assert.assertEquals(TICKETS_DATA_SIZE, tickets.size());
    }

    @Test
    public void findPage_WhenPageNumberIsNegative_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.findPage(-1, 1));
    }

    @Test
    public void findPage_WhenPageSizeIsNegative_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> repository.findPage(1, -1));
    }


    // Tests for update method
    @Test
    public void update_WhenTicketExists_ThenShouldReturnTrue() {
        Ticket ticket = generateTicket();
        ticket.setTicketNo("1000000000000");
        assertTrue(repository.update(ticket));
    }

    @Test
    public void update_WhenTicketDoesNotExist_ThenShouldReturnFalse() {
        Ticket ticket = generateTicket();
        ticket.setTicketNo("0");
        assertFalse(repository.update(ticket));
    }

    @Test
    public void update_WhenTicketHasInvalidBookRef_ThenShouldThrowRepositoryException() {
        Ticket ticket = generateTicket();
        ticket.setTicketNo("1000000000000");
        ticket.setBookRef("INVALID");
        assertThrows(RepositoryException.class, () -> repository.update(ticket));
    }

    @Test
    public void update_WhenTicketIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.update(null));
    }


    // Tests for updateAll method
    @Test
    public void updateAll_WhenAllTicketsExist_ThenShouldReturnTrue() {
        List<Ticket> tickets = generateTickets(5);
        for (int i = 0; i < tickets.size(); ++i) {
            tickets.get(i).setTicketNo((i + 1) + "000000000000");
        }
        assertTrue(repository.updateAll(tickets));
    }

    @Test
    public void updateAll_WhenSomeTicketsDoNotExist_ThenShouldReturnFalse() {
        List<Ticket> tickets = generateTickets(5);
        for (int i = 0; i < tickets.size(); ++i) {
            tickets.get(i).setTicketNo((i + 1) + "000000000000");
        }
        tickets.get(tickets.size() - 1).setTicketNo("0");
        assertFalse(repository.updateAll(tickets));
    }

    @Test
    public void updateAll_WhenListContainsTicketWithInvalidBookRef_ThenShouldThrowRepositoryException() {
        List<Ticket> tickets = generateTickets(5);
        for (int i = 0; i < tickets.size(); ++i) {
            tickets.get(i).setTicketNo((i+1) + "000000000000");
        }
        tickets.get(tickets.size() - 1).setBookRef("INVALID");
        assertThrows(RepositoryException.class, () -> repository.updateAll(tickets));
    }

    @Test
    public void updateAll_WhenListIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.updateAll(null));
    }

    @Test
    public void updateAll_WhenListIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.updateAll(List.of()));
    }

    @Test
    public void updateAll_WhenListContainsNull_ThenShouldThrowIllegalArgumentException() {
        List<Ticket> tickets = generateTickets(5);
        for (int i = 0; i < tickets.size(); ++i) {
            tickets.get(i).setTicketNo((i+1) + "000000000000");
        }
        tickets.set(tickets.size() - 1, null);
        assertThrows(IllegalArgumentException.class, () -> repository.updateAll(tickets));
    }


    // Tests for delete method
    @Test
    public void delete_WhenTicketExists_ThenShouldReturnTrue() {
        String id = "1000000000000";
        assertTrue(repository.delete(id));
    }

    @Test
    public void delete_WhenTicketDoesNotExist_ThenShouldReturnFalse() {
        String id = "0";
        assertFalse(repository.delete(id));
    }

    @Test
    public void delete_WhenTicketNoIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(null));
    }

    @Test
    public void delete_WhenTicketNoIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.delete(""));
    }


    // Tests for delete all method
    @Test
    public void deleteAll_WhenAllTicketsExist_ThenShouldReturnTrue() {
        List<String> ids = List.of("1000000000000", "2000000000000", "3000000000000");
        assertTrue(repository.deleteAll(ids));
    }

    @Test
    public void deleteAll_WhenSomeTicketsDoNotExist_ThenShouldReturnFalse() {
        List<String> ids = List.of("1000000000000", "2000000000000", NOT_EXISTING_TICKET);
        assertFalse(repository.deleteAll(ids));
    }

    @Test
    public void deleteAll_WhenIdsIsNull_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteAll(null));
    }

    @Test
    public void deleteAll_WhenIdsIsEmpty_ThenShouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> repository.deleteAll(List.of()));
    }

    @Test
    public void deleteAll_WhenIdsIsContainsNull_ThenShouldThrowIllegalArgumentException() {
        List<String> ids = new ArrayList<>();
        ids.add("1000000000000");
        ids.add("2000000000000");
        ids.add(null);
        assertThrows(IllegalArgumentException.class, () -> repository.deleteAll(ids));
    }

    @Test
    public void deleteAll_WhenIdsIsContainsEmptyString_ThenShouldThrowIllegalArgumentException() {
        List<String> ids = new ArrayList<>();
        ids.add("1000000000000");
        ids.add("2000000000000");
        ids.add("");
        assertThrows(IllegalArgumentException.class, () -> repository.deleteAll(ids));
    }
}
