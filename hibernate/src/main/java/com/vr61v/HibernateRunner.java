package com.vr61v;

import com.vr61v.entities.Booking;
import com.vr61v.entities.Ticket;
import com.vr61v.entities.embedded.ContactData;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.OffsetDateTime;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(Booking.class)
                .addAnnotatedClass(Ticket.class)
                .configure("hibernate.cfg.xml");

        try (SessionFactory factory = configuration.buildSessionFactory();
            Session session = factory.openSession()
        ) {
            session.beginTransaction();
            Booking booking = Booking.builder()
                    .bookRef("SOMEBK")
                    .bookDate(OffsetDateTime.now())
                    .totalAmount(100.00F)
                    .build();
            session.persist(booking);

            Ticket ticket = Ticket.builder()
                    .ticketNo("1111111111111")
                    .booking(booking)
                    .passengerId("1234 123456")
                    .passengerName("PASSENGER NAME")
                    .contactData(new ContactData("+70000000000", "my.email@google.com"))
                    .build();
            session.persist(ticket);
            session.getTransaction().commit();

        }
    }
}
