package ar.edu.utn.frc.tup.rodigym.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigResponseDto {
    private String key;
    private String value;
}
