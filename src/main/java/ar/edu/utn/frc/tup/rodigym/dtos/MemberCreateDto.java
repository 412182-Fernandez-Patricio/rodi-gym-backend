package ar.edu.utn.frc.tup.rodigym.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {
    @NotNull(message = "ID is required")
    private Long id;

    @NotEmpty(message = "Name is required and cannot be empty")
    private String name;

    @NotEmpty(message = "Last name is required and cannot be empty")
    @JsonProperty("last_name")
    private String lastName;

    @NotEmpty(message = "Phone number is required and cannot be empty")
    @JsonProperty("phone_number")
    private String phoneNumber;
}

