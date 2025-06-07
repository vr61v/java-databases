package org.vr61v.dtos;

import java.time.OffsetDateTime;

public record BookingDto (
        String bookRef,
        OffsetDateTime bookDate,
        Float totalAmount
) { }
