package com.vr61v;

import com.vr61v.entities.*;
import com.vr61v.entities.embedded.TicketFlightID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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

            Ticket ticket = session.find(Ticket.class, "0005432000284");
            Flight flight = session.find(Flight.class, 214868);
            TicketFlight ticketFlight = TicketFlight.builder()
                    .id(new TicketFlightID(ticket, flight))
                    .fareConditions("Business")
                    .amount(5000.00F)
                    .build();

            BoardingPass pass = BoardingPass.builder()
                    .id(new TicketFlightID(ticket, flight))
                    .boardingNo(1)
                    .seatNo(flight.getAircraft().getSeats().stream().iterator().next().getId().getSeatNo())
                    .build();

            session.persist(ticketFlight);
            session.persist(pass);

            session.getTransaction().commit();
        }
    }
}
