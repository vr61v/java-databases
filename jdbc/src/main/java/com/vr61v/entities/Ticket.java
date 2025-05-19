package com.vr61v.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticket {
    private String ticketNo;
    private String bookRef;
    private String passengerId;
    private String passengerName;
    private ContactData contactData;
}
