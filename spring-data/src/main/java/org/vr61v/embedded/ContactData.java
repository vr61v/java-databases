package org.vr61v.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ContactData {

    @Pattern(regexp = "^\\+7\\d{10}$")
    @Size(min = 12, max = 12)
    private String phone;

    @Email
    private String email;

}
