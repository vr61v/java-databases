package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.types.FareCondition;

@Data
public class SeatDto {
    private String seatNo;
    private FareCondition fareCondition;
}
