package ar.edu.utn.frc.tup.rodigym.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO: JAVADOC.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    private String name;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private Boolean status;
}

