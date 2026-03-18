package ar.edu.utn.frc.tup.rodigym.models;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing a check-in.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkin {
    private Long id;
    private Long memberId;
    private LocalDateTime checkinTime;
    private Boolean success;
    private String message;
}
