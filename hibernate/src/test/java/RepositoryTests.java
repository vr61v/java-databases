import com.vr61v.entities.*;
import com.vr61v.entities.embedded.ContactData;
import com.vr61v.entities.embedded.LocalizedString;
import com.vr61v.entities.embedded.SeatID;
import com.vr61v.entities.types.FareCondition;
import com.vr61v.entities.types.FlightStatus;
import com.vr61v.repositories.*;
import com.vr61v.utils.RepositorySessionManager;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTests {

    private static final RepositorySessionManager sessionManager =
            new RepositorySessionManager();

    @Test
    public void crudOperationsBookingRepository() {
        // Setup initial data
        BookingRepository bookingRepository = new BookingRepository(sessionManager);

        String bookingId = "123456";
        Booking booking = Booking.builder()
                .bookRef(bookingId)
                .bookDate(OffsetDateTime.now())
                .totalAmount(50_000.00F)
                .build();


        // When save then should return saved booking from DB
        // When findById should return Optional with saved booking
        Booking saved = bookingRepository.save(booking);
        assertThat(saved).isEqualTo(booking);
        assertThat(bookingRepository.findById(bookingId)).isPresent();


        // When update then should update booking with new field
        Booking updated = Booking.builder()
                .bookRef(bookingId)
                .bookDate(booking.getBookDate())
                .totalAmount(10_000.00F)
                .build();

        bookingRepository.update(updated);


        // When findById then return updated totalAmount
        Optional<Booking> found = bookingRepository.findById(bookingId);
        assertThat(found).isPresent();
        assertThat(found.get().getTotalAmount()).isEqualTo(updated.getTotalAmount());


        // When delete then should delete booking from DB
        // When findById should return empty Optional
        boolean deleted = bookingRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(bookingRepository.findById(bookingId)).isEmpty();
    }

    @Test
    public void crudOperationsTicketRepository() {
        // setup initial data
        BookingRepository bookingRepository = new BookingRepository(sessionManager);
        TicketRepository ticketRepository = new TicketRepository(sessionManager);

        String bookingId = "123456";
        Booking booking = Booking.builder()
                .bookRef(bookingId)
                .bookDate(OffsetDateTime.now())
                .totalAmount(50_000.00F)
                .build();
        bookingRepository.save(booking);


        ContactData contacts = ContactData.builder()
                .email("email@gmail.com")
                .phone("+79999999999")
                .build();

        String ticketId = "1111111111111";
        Ticket ticket = Ticket.builder()
                .ticketNo(ticketId)
                .booking(booking)
                .passengerId("1234 123456")
                .passengerName("SOME NAME")
                .contactData(contacts)
                .build();


        // When save then should return saved ticket from DB
        // When findById should return Optional with saved ticket
        Ticket saved = ticketRepository.save(ticket);
        assertThat(saved).isEqualTo(ticket);
        assertThat(ticketRepository.findById(ticketId)).isPresent();


        // When update then should update ticket with new fields
        Ticket updated = Ticket.builder()
                .ticketNo(ticketId)
                .booking(ticket.getBooking())
                .passengerId("4321 654321")
                .passengerName("SOME UPDATE")
                .contactData(ticket.getContactData())
                .build();

        ticketRepository.update(updated);


        // When findById then return updated passengerId and passengerName
        Optional<Ticket> found = ticketRepository.findById(ticketId);
        assertThat(found).isPresent();
        assertThat(found.get().getPassengerId()).isEqualTo(updated.getPassengerId());
        assertThat(found.get().getPassengerName()).isEqualTo(updated.getPassengerName());


        // When delete then should delete ticket from DB
        // When findById should return empty Optional
        boolean deleted = ticketRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(ticketRepository.findById(ticketId)).isEmpty();


        // Cleanup booking table
        assertThat(bookingRepository.delete(booking)).isTrue();
    }

    @Test
    public void crudOperationsAircraftRepository() {
        // Setup initial data
        AircraftRepository aircraftRepository = new AircraftRepository(sessionManager);

        String aircraftId = "AIR";
        Aircraft aircraft = Aircraft.builder()
                .aircraftCode(aircraftId)
                .model(LocalizedString.builder().ru("самолет").en("aircraft").build())
                .range(1000)
                .build();

        // When save then should return saved aircraft from DB
        // When findById should return Optional with saved aircraft
        Aircraft saved = aircraftRepository.save(aircraft);
        assertThat(saved).isEqualTo(aircraft);
        assertThat(aircraftRepository.findById(aircraftId)).isPresent();


        // When update then should update aircraft with new fields
        Aircraft updated = Aircraft.builder()
                .aircraftCode(aircraftId)
                .model(LocalizedString.builder().ru("большой самолет").en("big aircraft").build())
                .range(5000)
                .build();

        aircraftRepository.update(updated);


        // When findById then return updated model and range
        Optional<Aircraft> found = aircraftRepository.findById(aircraftId);
        assertThat(found).isPresent();
        assertThat(found.get().getModel()).isEqualTo(updated.getModel());
        assertThat(found.get().getRange()).isEqualTo(updated.getRange());


        // When delete then should delete aircraft from DB
        // When findById should return empty Optional
        boolean deleted = aircraftRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(aircraftRepository.findById(aircraftId)).isEmpty();
    }

    @Test
    public void crudOperationsSeatRepository() {
        // Setup initial data
        AircraftRepository aircraftRepository = new AircraftRepository(sessionManager);
        SeatRepository seatRepository = new SeatRepository(sessionManager);

        String aircraftId = "AIR";
        Aircraft aircraft = Aircraft.builder()
                .aircraftCode(aircraftId)
                .model(LocalizedString.builder().ru("самолет").en("aircraft").build())
                .range(1000)
                .build();
        aircraftRepository.save(aircraft);


        SeatID seatId = SeatID.builder()
                .seatNo("AAAA")
                .aircraft(aircraft)
                .build();

        Seat seat = Seat.builder()
                .id(seatId)
                .fareConditions(FareCondition.COMFORT)
                .build();

        // When save then should return saved seat from DB
        // When findById should return Optional with saved seat
        Seat saved = seatRepository.save(seat);
        assertThat(saved).isEqualTo(seat);
        assertThat(seatRepository.findById(seatId)).isPresent();


        // When update then should update seat with new field
        Seat updated = Seat.builder()
                .id(seatId)
                .fareConditions(FareCondition.BUSINESS)
                .build();

        seatRepository.update(updated);


        // When findById then return updated fareConditions
        Optional<Seat> found = seatRepository.findById(seatId);
        assertThat(found).isPresent();
        assertThat(found.get().getFareConditions()).isEqualTo(updated.getFareConditions());


        // When delete then should delete seat from DB
        // When findById should return empty Optional
        boolean deleted = seatRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(seatRepository.findById(seatId)).isEmpty();


        // Cleanup booking table
        assertThat(aircraftRepository.delete(aircraft)).isTrue();
    }

    @Test
    public void crudOperationsAirportRepository() {
        // Setup initial data
        AirportRepository airportRepository = new AirportRepository(sessionManager);

        String airportCod = "COD";
        Airport airport = Airport.builder()
                .airportCode(airportCod)
                .airportName(LocalizedString.builder().ru("название").en("name").build())
                .city(LocalizedString.builder().ru("город").en("city").build())
                .timezone("Europe/Moscow")
                .build();

        // When save then should return saved airport from DB
        // When findById should return Optional with saved airport
        Airport saved = airportRepository.save(airport);
        assertThat(saved).isEqualTo(airport);
        assertThat(airportRepository.findById(airportCod)).isPresent();


        // When update then should update airport with new fields
        Airport updated = Airport.builder()
                .airportCode(airportCod)
                .airportName(LocalizedString.builder().ru("аэропорт").en("airport").build())
                .city(LocalizedString.builder().ru("русский аэропорт").en("russian airport").build())
                .timezone("Europe/Moscow")
                .build();

        airportRepository.update(updated);


        // When findById then return updated name and city
        Optional<Airport> found = airportRepository.findById(airportCod);
        assertThat(found).isPresent();
        assertThat(found.get().getAirportName()).isEqualTo(updated.getAirportName());
        assertThat(found.get().getCity()).isEqualTo(updated.getCity());


        // When delete then should delete airport from DB
        // When findById should return empty Optional
        boolean deleted = airportRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(airportRepository.findById(airportCod)).isEmpty();
    }

    @Test
    public void crudOperationsFlightRepository() {
        // Setup initial data
        AircraftRepository aircraftRepository = new AircraftRepository(sessionManager);
        AirportRepository airportRepository = new AirportRepository(sessionManager);
        FlightRepository flightRepository = new FlightRepository(sessionManager);

        String aircraftId = "AIR";
        Aircraft aircraft = Aircraft.builder()
                .aircraftCode(aircraftId)
                .model(LocalizedString.builder().ru("самолет").en("aircraft").build())
                .range(1000)
                .build();

        String airportCodDeparture = "COD";
        Airport airportDeparture = Airport.builder()
                .airportCode(airportCodDeparture)
                .airportName(LocalizedString.builder().ru("откуда").en("from").build())
                .city(LocalizedString.builder().ru("отправления").en("departure").build())
                .timezone("Europe/Moscow")
                .build();

        String airportCodArrival = "DOC";
        Airport airportArrival = Airport.builder()
                .airportCode(airportCodArrival)
                .airportName(LocalizedString.builder().ru("куда").en("to").build())
                .city(LocalizedString.builder().ru("назначения").en("arrival").build())
                .timezone("Europe/Moscow")
                .build();

        aircraftRepository.save(aircraft);
        airportRepository.save(airportDeparture);
        airportRepository.save(airportArrival);

        Flight flight = Flight.builder()
                .flightNo("SOMEFL")
                .aircraft(aircraft)
                .status(FlightStatus.SCHEDULED)
                .departureAirport(airportDeparture)
                .arrivalAirport(airportArrival)
                .scheduledDeparture(OffsetDateTime.now())
                .scheduledArrival(OffsetDateTime.now().plusHours(4))
                .build();


        // When save then should return saved flight from DB
        // When findById should return Optional with saved flight
        Flight saved = flightRepository.save(flight);
        Integer flightId = saved.getFlightId();
        assertThat(saved).isEqualTo(flight);
        assertThat(flightRepository.findById(flightId)).isPresent();

        // When update then should update flight with new fields
        Flight updated = Flight.builder()
                .flightId(flightId)
                .flightNo("SOMEFL")
                .aircraft(aircraft)
                .status(FlightStatus.DEPARTED)
                .departureAirport(airportDeparture)
                .arrivalAirport(airportArrival)
                .scheduledDeparture(flight.getScheduledDeparture())
                .scheduledArrival(flight.getScheduledArrival())
                .actualDeparture(flight.getScheduledDeparture())
                .build();

        flightRepository.update(updated);


        // When findById then return updated status and actualDeparture
        Optional<Flight> found = flightRepository.findById(flightId);
        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(updated.getStatus());
        assertThat(found.get().getActualDeparture()).isEqualTo(updated.getActualDeparture());


        // When delete then should delete aircraft from DB
        // When findById should return empty Optional
        boolean deleted = flightRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(flightRepository.findById(flightId)).isEmpty();

        aircraftRepository.delete(aircraft);
        airportRepository.delete(airportDeparture);
        airportRepository.delete(airportArrival);
    }
}
