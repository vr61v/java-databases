package org.vr61v.dtos;

import org.vr61v.embedded.ContactData;

public record TicketDto (
        String ticketNo,
        String bookRef,
        String passengerId,
        String passengerName,
        ContactData contactData
) { }
