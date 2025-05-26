package com.vr61v;

import com.vr61v.entities.*;
import com.vr61v.entities.embedded.LocalizedString;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.OffsetDateTime;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Booking.class)
                .addAnnotatedClass(Ticket.class)
                .addAnnotatedClass(Aircraft.class)
                .addAnnotatedClass(Seat.class)
                .addAnnotatedClass(Airport.class)
                .addAnnotatedClass(Flight.class)
                .configure("hibernate.cfg.xml");

        try (SessionFactory factory = configuration.buildSessionFactory();
            Session session = factory.openSession()
        ) {
            session.beginTransaction();
            Airport airport1 = Airport.builder()
                    .airportCode("COD")
                    .airportName(LocalizedString.builder().ru("Аэропорт").en("Airport").build())
                    .city(LocalizedString.builder().ru("Город").en("City").build())
                    .timezone("Europe/Moscow")
                    .build();

            Airport airport2 = Airport.builder()
                    .airportCode("DOC")
                    .airportName(LocalizedString.builder().ru("Аэропорт").en("Airport").build())
                    .city(LocalizedString.builder().ru("Город").en("City").build())
                    .timezone("Europe/Moscow")
                    .build();

            session.persist(airport1);
            session.persist(airport2);

            Aircraft aircraft = session.find(Aircraft.class, "773");
            OffsetDateTime time = OffsetDateTime.now();
            Flight flight = Flight.builder()
                    .flightNo("FLIGHT")
                    .departureAirport(airport1)
                    .arrivalAirport(airport2)
                    .status("On Time")
                    .aircraft(aircraft)
                    .scheduledDeparture(time)
                    .scheduledArrival(time.plusHours(2))
                    .actualDeparture(time.plusMinutes(30))
                    .actualArrival(time.plusHours(2).plusMinutes(30))
                    .build();

            session.persist(flight);

            session.getTransaction().commit();
        }
    }
}
