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
        System.out.println("Hibernate Runner...");
    }
}

