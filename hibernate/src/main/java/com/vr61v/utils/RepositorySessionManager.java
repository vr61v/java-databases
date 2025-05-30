package com.vr61v.utils;

import com.vr61v.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class RepositorySessionManager implements SessionManager {

    private final SessionFactory sessionFactory;

    public RepositorySessionManager() {
        try {
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

            this.sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Session getSession() {
        return sessionFactory.openSession();
    }

}
