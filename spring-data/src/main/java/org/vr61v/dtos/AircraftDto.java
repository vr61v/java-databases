package org.vr61v.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.vr61v.embedded.LocalizedString;

@Data
public class AircraftDto {

    @Size(min = 3, max = 3)
    private String aircraftCode;

    @NotNull @Valid
    private LocalizedString model;

    @NotNull @Positive
    private Integer range;

}
