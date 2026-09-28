package ar.edu.utn.frc.tup.rodigym.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos para dar de alta un socio. El id es el DNI, que además es lo que se
 * teclea en el check-in.
 *
 * <p>El teléfono solo se exige no vacío: el formato argentino varía demasiado
 * (con o sin 0 y 15, con característica) para validarlo sin rechazar números
 * reales.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDto {

    @NotNull(message = "ID is required")
    @Min(value = 1_000_000, message = "ID must have 7 or 8 digits")
    @Max(value = 99_999_999, message = "ID must have 7 or 8 digits")
    private Long id;

    @NotBlank(message = "Name is required and cannot be empty")
    private String name;

    @NotBlank(message = "Last name is required and cannot be empty")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank(message = "Phone number is required and cannot be empty")
    @JsonProperty("phone_number")
    private String phoneNumber;
}
