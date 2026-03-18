package ar.edu.utn.frc.tup.rodigym.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for API error responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorApi {
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
}
