package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AircraftDto {
    private String aircraftCode;
    private LocalizedString model;
    private Integer range;
}
