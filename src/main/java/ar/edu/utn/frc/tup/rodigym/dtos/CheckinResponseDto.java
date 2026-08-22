package ar.edu.utn.frc.tup.rodigym.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for sending a check-in response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckinResponseDto {

    private Long id;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("checkin_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkinTime;

    private Boolean success;
    private CheckinReason reason;

    private String message;
}
