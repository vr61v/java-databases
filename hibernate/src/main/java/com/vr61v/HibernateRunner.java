package com.vr61v;

import com.vr61v.entities.Booking;
import com.vr61v.repositories.BookingRepository;
import com.vr61v.utils.RepositorySessionManager;

import java.time.OffsetDateTime;

public class HibernateRunner {
    public static void main(String[] args) {
        RepositorySessionManager sessionManager = new RepositorySessionManager();
        BookingRepository bookingsRepository = new BookingRepository(sessionManager);

        String id = "123456";
        Booking booking1 = Booking.builder()
                .bookRef(id)
                .bookDate(OffsetDateTime.now())
                .totalAmount(50_000.00F)
                .build();

        Booking booking2 = Booking.builder()
                .bookRef(id)
                .bookDate(OffsetDateTime.now().plusDays(1))
                .totalAmount(10_000.00F)
                .build();

        System.out.println(bookingsRepository.save(booking1));
        System.out.println(bookingsRepository.update(booking2));
        System.out.println(bookingsRepository.findById(id));
        System.out.println(bookingsRepository.delete(booking1));
        System.out.println(bookingsRepository.findById(id));
    }
}

