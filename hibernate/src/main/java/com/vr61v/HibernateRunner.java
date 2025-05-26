package com.vr61v;

import com.vr61v.entities.*;
import com.vr61v.entities.embedded.LocalizedString;
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
                .configure("hibernate.cfg.xml");

        try (SessionFactory factory = configuration.buildSessionFactory();
            Session session = factory.openSession()
        ) {
            session.beginTransaction();
            Airport airport = Airport.builder()
                    .airportCode("COD")
                    .airportName(LocalizedString.builder().ru("Аэропорт").en("Airport").build())
                    .city(LocalizedString.builder().ru("Город").en("City").build())
                    .timezone("Europe/Moscow")
                    .build();

            session.persist(airport);

            session.getTransaction().commit();
        }
    }
}
