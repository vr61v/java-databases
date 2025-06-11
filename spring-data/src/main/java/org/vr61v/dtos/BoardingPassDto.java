package org.vr61v.dtos;

import lombok.Data;

@Data
public class BoardingPassDto {
    private String ticketNo;
    private String flightNo;
    private Integer boardingNo;
    private String seatN;
}

