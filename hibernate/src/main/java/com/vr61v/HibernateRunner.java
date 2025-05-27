package com.vr61v;

import com.vr61v.entities.Booking;
import com.vr61v.repositories.BookingsRepository;
import com.vr61v.utils.RepositorySessionManager;

import java.time.OffsetDateTime;
import java.util.Optional;

public class HibernateRunner {
    public static void main(String[] args) {
        RepositorySessionManager sessionManager = new RepositorySessionManager();
        BookingsRepository bookingsRepository = new BookingsRepository(sessionManager);

        Booking booking1 = Booking.builder()
                .bookRef("123456")
                .bookDate(OffsetDateTime.now())
                .totalAmount(50_000.00F)
                .build();

        Booking booking2 = Booking.builder()
                .bookRef("123456")
                .bookDate(OffsetDateTime.now().plusDays(1))
                .totalAmount(10_000.00F)
                .build();

        bookingsRepository.save(booking1);
        bookingsRepository.update(booking2);
        Optional<Booking> found1 = bookingsRepository.findById(booking2.getBookRef());
        bookingsRepository.delete(booking2);
        Optional<Booking> found2 = bookingsRepository.findById(booking2.getBookRef());
        System.out.println(found1.get());
        System.out.println(found2.isPresent());
    }
}

