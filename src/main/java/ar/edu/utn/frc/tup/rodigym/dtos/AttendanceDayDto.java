package ar.edu.utn.frc.tup.rodigym.dtos;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDayDto {
    private LocalDate date;
    private boolean success;
    private int checkins;
}
