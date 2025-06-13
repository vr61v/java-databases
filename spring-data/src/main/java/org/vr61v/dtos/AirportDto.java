package org.vr61v.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AirportDto {

    @Size(min = 3, max = 3)
    private String airportCode;

    @NotNull @Valid
    private LocalizedString airportName;

    @NotNull @Valid
    private LocalizedString city;

    @NotNull @NotBlank
    private String timezone;

}
