package org.vr61v.dtos;

import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AirportDto {
    private String airportCode;
    private LocalizedString airportName;
    private LocalizedString city;
    private String timezone;
}
