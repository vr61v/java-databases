package org.vr61v.dtos;

import lombok.Data;

@Data
public class BoardingPassDto {
    private String ticketNo;
    private Integer flightId;
    private Integer boardingNo;
    private String seatNo;
}

