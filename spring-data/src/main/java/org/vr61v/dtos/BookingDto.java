package org.vr61v.dtos;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BookingDto {
    private String bookRef;
    private OffsetDateTime bookDate;
    private Float totalAmount;
}
