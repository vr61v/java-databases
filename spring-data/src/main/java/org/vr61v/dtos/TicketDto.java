package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.embedded.ContactData;

@Data
public class TicketDto {
    private String ticketNo;
    private String bookRef;
    private String passengerId;
    private String passengerName;
    private ContactData contactDat;
}
