package com.vr61v;

import com.vr61v.entities.*;
import com.vr61v.entities.embedded.LocalizedString;
import com.vr61v.entities.embedded.SeatID;
import com.vr61v.entities.embedded.TicketFlightID;
import com.vr61v.entities.types.FareCondition;
import com.vr61v.entities.types.FlightStatus;
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
                .addAnnotatedClass(TicketFlight.class)
                .addAnnotatedClass(BoardingPass.class)
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
                    .status(FlightStatus.ARRIVED)
                    .aircraft(aircraft)
                    .scheduledDeparture(time)
                    .scheduledArrival(time.plusHours(2))
                    .actualDeparture(time.plusMinutes(30))
                    .actualArrival(time.plusHours(2).plusMinutes(30))
                    .build();

            session.persist(flight);

            Seat seat = Seat.builder()
                    .fareConditions(FareCondition.COMFORT)
                    .id(SeatID.builder().aircraft(aircraft).seatNo("1Z").build()).build();

            session.persist(seat);

            Ticket ticket = session.find(Ticket.class, "0005432001522");
            TicketFlight ticketFlight = TicketFlight.builder()
                    .id(TicketFlightID.builder().flight(flight).ticket(ticket).build())
                    .fareConditions(FareCondition.COMFORT)
                    .amount(5000.00F)
                    .build();
            session.persist(ticketFlight);

            session.getTransaction().commit();
        }
    }
}

