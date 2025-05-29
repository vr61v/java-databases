import com.vr61v.entities.Booking;
import com.vr61v.entities.Ticket;
import com.vr61v.entities.embedded.ContactData;
import com.vr61v.repositories.BookingRepository;
import com.vr61v.repositories.TicketRepository;
import com.vr61v.utils.RepositorySessionManager;
import org.junit.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class RepositoryTests {

    @Test
    public void crudOperationsBookingRepository() {
        // Setup initial data
        RepositorySessionManager sessionManager = new RepositorySessionManager();
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
        RepositorySessionManager sessionManager = new RepositorySessionManager();
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


        // When delete then should delete booking from DB
        // When findById should return empty Optional
        boolean deleted = ticketRepository.delete(found.get());
        assertThat(deleted).isTrue();
        assertThat(ticketRepository.findById(ticketId)).isEmpty();


        // Cleanup booking table
        assertThat(bookingRepository.delete(booking)).isTrue();
    }

}
