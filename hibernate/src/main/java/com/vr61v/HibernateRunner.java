package com.vr61v;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.OffsetDateTime;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Bookings.class)
                .configure("hibernate.cfg.xml");

        try (SessionFactory factory = configuration.buildSessionFactory();
            Session session = factory.openSession()
        ) {
            Bookings booking = Bookings.builder()
                    .bookRef("SOMEBK")
                    .bookDate(OffsetDateTime.now())
                    .totalAmount(100.00F)
                    .build();
            session.beginTransaction();
            session.persist(booking);
            session.getTransaction().commit();
        }

    }
}
