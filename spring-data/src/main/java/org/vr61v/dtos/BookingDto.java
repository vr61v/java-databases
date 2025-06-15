package org.vr61v.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BookingDto {

    @Size(min = 6, max = 6)
    private String bookRef;

    @NotNull
    private OffsetDateTime bookDate;

    @NotNull @Positive
    private Float totalAmount;

}
